package member.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import member.dao.PendingDao;
import member.dao.RecordDao;
import member.dao.UserDao;
import member.entity.Pending;
import member.entity.Record;
import member.entity.User;
import member.utils.Contanst;
import member.utils.DateUtils;

@Component
public class MsgHandle {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PendingDao pendingDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RecordDao recordDao;

	public String MsgProcess(String reqXml) throws IOException, DocumentException {
		logger.debug("进入消息处理。。。。");
		logger.debug("reqXml:" + reqXml);
		String reply = reply(reqXml);
		return reply;

	}

	/**
	 * 预处理微信请求xml
	 * 
	 * @param reqStr
	 *            微信post过来的xml
	 * @return
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 */
	private String reply(String reqStr) throws DocumentException, UnsupportedEncodingException {

		Document document = DocumentHelper.parseText(reqStr);
		Element root = document.getRootElement();
		// 取得微信消息中的字段
		Element ToUserNameE = root.element("ToUserName");
		Element ContentE = root.element("Content");
		Element FromUserNameE = root.element("FromUserName");
		Element MsgTypeE = root.element("MsgType");
		Element EventE = root.element("Event");
		String content = "success";

		String ToUserName = ToUserNameE.getText();
		String FromUserName = FromUserNameE.getText();
		String MsgType = MsgTypeE.getText();
		// String CreateTimeFrom = CreateTimeE.getText();
		// vps在不同市区，设置为上海时间为默认时间
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		// 消息自带时间需要乘1000

		Calendar calendar = Calendar.getInstance();
		long timestmp = calendar.getTimeInMillis();
		String CreateTime = String.valueOf(timestmp);
		if (Contanst.VOICE.equals(MsgType)) {
			Element RecognitionE = root.element("Recognition");
			content = RecognitionE.getText();

			logger.debug("发送消息为语音类型，语音识别为：" + content);
			content = dealer(content, FromUserName);
		} else if (Contanst.TEXT.equals(MsgType)) {
			content = ContentE.getText();
			content = dealer(content, FromUserName);

		} else if (Contanst.EVENT.equals(MsgType)) {
			String subscribe = EventE.getText();
			if (Contanst.WSUBSCRIBE.equals(subscribe)) {
				content = Contanst.SUBSCRIBE;
			}
		}

		logger.debug("Element,ToUserName:" + ToUserName + "Content  real:" + content);
		String reply = "<xml><ToUserName>" + FromUserName
				+ "</ToUserName><FromUserName><![CDATA[gh_0111995ecad4]]></FromUserName><CreateTime>" + CreateTime
				+ "</CreateTime><MsgType><![CDATA[text]]></MsgType><Content>" + content + "</Content></xml>";

		return reply;
	}

	/**
	 * 处理微信过来的关键字处理
	 * 
	 * @param content
	 *            微信过来的原文字
	 * @param FromUserName
	 *            用户的wechatId
	 * @return
	 */
	public String dealer(String content, String FromUserName) {
		// 处理余额和次数查询
		if (Contanst.YUE.equals(content)
				|| (content != null && content.length() > 1 & content.indexOf(Contanst.YUE) != -1)) {
			List<User> p = userDao.findByWechatid(FromUserName);
			if (p != null && p.size() > 0) {
				return Contanst.CURRENTYUE + p.get(0).getPhone() + Contanst.YUAN;
			} else {
				return Contanst.NOREGISTED;
			}

		} else if (Contanst.COUNT.equals(content)
				|| (content != null && content.length() > 1 & content.indexOf(Contanst.COUNT) != -1)) {
			List<User> p = userDao.findByWechatid(FromUserName);
			if (p != null && p.size() > 0) {
				String count = StringUtils.isEmpty(p.get(0).getQq())?"0":p.get(0).getQq();
				return Contanst.CURRENTCOUNT + count + Contanst.CI;
			} else {
				return Contanst.NOREGISTED;
			}

		}else if (content != null && content.length() > 1 & content.indexOf(Contanst.REGIST) != -1) {
			// 处理注册请求
			// 格式要求 姓名 手机号
			Pending p = new Pending();
			p.setWechatid(FromUserName);
			if (content.split(" ").length < 2) {
				return Contanst.FORMATTERWRONG;
			}
			p.setName(content.split(" ")[1]);
			p.setMobile(content.split(" ")[2]);
			p.setPhone(Contanst.INITMONEY);
			if (pendingDao.findByWechatid(FromUserName).size() > 0
					|| pendingDao.findByMobile(content.split(" ")[2]).size() > 0) {
				return Contanst.REGISTEDNOPASS;
			} else if (userDao.findByWechatid(FromUserName).size() > 0
					|| userDao.findByMobile(content.split(" ")[2]).size() > 0) {
				return Contanst.REGISTED;
			}
			p = pendingDao.save(p);
			if (p.getMobile() != null) {
				return Contanst.REGISTSUCESS;
			} else {
				return Contanst.REGISTFAIL;
			}
		} else if (content != null && content.length() > 1
				& (content.indexOf(Contanst.RECORD) != -1 || content.indexOf(Contanst.COST) != -1)) {
			// 处理交易记录
			List<User> users = userDao.findByWechatid(FromUserName);
			if (users != null && users.size() > 0) {
				 Sort sort = new Sort(Sort.Direction.DESC, "id");
				 Pageable pageable = new PageRequest(0, 10, sort);
				List<Record> records = recordDao.findByMobile(users.get(0).getMobile(),pageable);
				
				StringBuilder b = new StringBuilder();
				b.append("您的手机号码：").append(users.get(0).getMobile()).append("。为您查询最近10条交易记录：").append("\n");
				Iterator<Record> it = records.iterator();
				while (it.hasNext()) {
					Record r = it.next();
					b.append("时间：[").append(DateUtils.timeSwitch(r.getSendtime())).append("]")
							.append(StringUtils.isEmpty(r.getPhone()) ? "" : "本次金额[" + r.getPhone() + "]")
							.append(StringUtils.isEmpty(r.getQq()) ? "" : "本次次数[" + r.getQq() + "]")
							.append(StringUtils.isEmpty(r.getType()) ? "" : "付款方式[" + r.getType() + "]")
							.append(StringUtils.isEmpty(r.getRemark()) ? "" : "备注信息[" + r.getRemark() + "]")
							.append("\n");
				}
				return b.toString();
			} else {
				return Contanst.NOREGISTED;
			}
		}
		return Contanst.UNRECONGISE;
	}

}
