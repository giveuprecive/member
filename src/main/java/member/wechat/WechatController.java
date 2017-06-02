package member.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import member.utils.SignUtil;

import member.service.MsgHandle;

@RestController
public class WechatController {
	public Logger logger = Logger.getLogger(getClass());

	@Autowired
	private MsgHandle msgHandle;

	@RequestMapping(value = "/wechat/sevice", method = RequestMethod.POST)
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("接收到微信请求。。。");
		long start = System.currentTimeMillis();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		String reply = "";
		try {
			// 这里一定要加上utf-8,保证读出来的不是乱码，后期则无需转码。
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line;
			StringBuffer result = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				// System.out.println("line while：" + line);
				result.append(line);

			}
			logger.info("接收到微信请求【" + result);
			reply = msgHandle.MsgProcess(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		writer.append(reply);
		writer.flush();
		// writer.print("success");
		writer.close();
		writer = null;
		// 处理请求时间
		long end = System.currentTimeMillis();
		logger.debug("本次请求处理结束。。。耗时" + (end - start) + "毫秒");
	}

	@RequestMapping(value = "/wechat/sevice", method = RequestMethod.GET)
	public void sign(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("开始调用doGet方法处理请求。。。");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			logger.debug("验证服务器地址的有效性成功！doget");
			// 首次提交验证申请，需要原样返回echostr
			writer.append(echostr);
		} else {
			logger.warn("验证服务器地址的有效性失败！");
			writer.append("消息并非来自微信！");

		}
		writer.flush();
		writer.flush();
		writer.close();
		writer = null;
	}

}
