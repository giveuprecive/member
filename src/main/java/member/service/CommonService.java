package member.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import member.dao.PendingDao;
import member.dao.RecordDao;
import member.dao.UserDao;
import member.entity.Pending;
import member.entity.Record;
import member.entity.User;
import member.utils.Contanst;

@Component
public class CommonService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private RecordDao recordDao;

	@Autowired
	private PendingDao PendingDao;

	private final String NOTENAFU = "{success:\"0\",money:\"余额不足！\"}";

	private final String FAIL = "{success:\"0\",money:\"充值失败！\"}";

	private final String NOUSER = "{success:\"0\",money:\"用户不存在！\"}";

	private final String SUCCESS = "{success:1,money:%s}";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 通用事务控制器
	 * 超市、洗浴充值和消费  
	 * 
	 * @param paras
	 * @return
	 */
	@Transactional
	public String operate(Map<String, String> paras) {
		// 确定用户是否已经存在
		List<User> userIn = userDao.findByMobile(paras.get(Contanst.MOBILE));
		if (userIn.size() == 0) {
			logger.warn("User {} is not exists!", userIn.toString());
			return NOUSER;
		}
		saveRecord(paras);
		//TODO，有bug
		if (StringUtils.isNotEmpty(paras.get(Contanst.PHONE))) {
			logger.info("parameter phone is empty! please check it!");
			return operateMarket(paras);
		} else if (StringUtils.isNotEmpty(paras.get(Contanst.QQ))) {
			return operateBath(paras);
		}
		return FAIL;
	}

	/**
	 * 超市充值消费操作
	 * 
	 * @param paras
	 * @return
	 */
	private String operateMarket(Map<String, String> paras) {
		User user = userDao.findByMobile(paras.get(Contanst.MOBILE)).get(0);
		String money = StringUtils.isEmpty(user.getPhone())?"0":user.getPhone();

		// 计算余额
		if (money != null && money.length() > 0) {
			int total;
			int moneyNew;
			try {
				total = Integer.parseInt(money); // 原余额
				moneyNew = Integer.parseInt(paras.get(Contanst.PHONE)); // 新加金额
			} catch (NumberFormatException e) {
				logger.warn("when parameter phone values {} parseInt its fail!", paras.get(Contanst.PHONE));
				return FAIL;
			}
			if ("1".equals(paras.get(Contanst.OPERATE))) { // 充值
				total = total + moneyNew;
			} else { // 消费
				total = total - moneyNew;
				if (total < 0) {
					logger.warn("cost fail ! not enough,it costs {},but current is {}", moneyNew, total);
					return NOTENAFU;
				}
			}
			user.setPhone(String.valueOf(total));
			userDao.save(user);

		}
		return String.format(SUCCESS, user.getPhone());
	}

	/**
	 * 洗浴消费充值操作
	 * 
	 * @param paras
	 * @return
	 */

	private String operateBath(Map<String, String> paras) {

		// 充值
		User user = userDao.findByMobile(paras.get(Contanst.MOBILE)).get(0);
		String money = StringUtils.isEmpty(user.getQq())?"0":user.getQq();

		// 计算余额
		if (money != null && money.length() > 0) {
			int total;
			int moneyNew;
			try {
				total = Integer.parseInt(money); // 原余额
				moneyNew = Integer.parseInt(paras.get(Contanst.QQ)); // 新加金额
			} catch (NumberFormatException e) {
				return FAIL;
			}
			if ("1".equals(paras.get(Contanst.OPERATE))) { // 充值
				total = total + moneyNew;
			} else { // 消费
				total = total - moneyNew;
				if (total < 0) {
					return NOTENAFU;
				}
			}
			user.setQq(String.valueOf(total));
			userDao.save(user);

		}
		return String.format(SUCCESS, user.getQq());
	}

	/**
	 * 用于提前保存消费记录。
	 * 
	 * @param paras
	 */
	private void saveRecord(Map<String, String> paras) {
		Record record = new Record();
		record.setMobile(paras.get(Contanst.MOBILE));
		record.setQq(paras.get(Contanst.QQ));
		record.setSendtime(String.valueOf(System.currentTimeMillis()));
		record.setName(userDao.findByMobile(paras.get(Contanst.MOBILE)).get(0).getName());
		record.setType(paras.get("resource"));
		record.setPhone(paras.get(Contanst.PHONE));
		record.setRemark(paras.get("desc"));
		recordDao.save(record);
	}

	@Transactional
	public String regular(Map<String, String> paras) throws Exception {

		List<Pending> pendingList = PendingDao.findByMobile(paras.get(Contanst.MOBILE));
		Pending p = pendingList.get(0);
		User user = new User();
		user.setName(p.getName());
		user.setMobile(p.getMobile());
		user.setWechatid(p.getWechatid());
		user.setPhone(p.getPhone());
		PendingDao.delete(p);
		user.setSendtime(String.valueOf(System.currentTimeMillis()));
		userDao.save(user);
		return user.getMobile();
	}
}
