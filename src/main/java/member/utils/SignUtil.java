package member.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 消息验证，用于首次提交验证申请。此后所有消息验证仍然通过对签名的效验判断此条消息的真实性。效验方式与首次提交验证申请一致。
 * 
 * @author zhaokunyang
 * @date 2015.06.09
 */
public class SignUtil {
	/**
	 * 验证签名，默认为true
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] arr = new String[] { Contanst.TOKEN, timestamp, nonce };
		String content;
		String result = "";
		Arrays.sort(arr);
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			buffer.append(arr[i]);
		}
		content = buffer.toString();
		if (!StringUtils.isEmpty(content)) {
			result = DigestUtils.sha1Hex(content);
		}
		if (!result.equals(signature)) {
			return false;
		}
		return true;

	}

	/**
	 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。
	 * access_token的存储至少要保留512个字符空间
	 * 。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String getAccess_token() throws Exception {
		URI uri = new URIBuilder().setScheme("https")
				.setHost("api.weixin.qq.com").setPath("/cgi-bin/token")
				.setParameter("grant_type", Contanst.GRANT_TYPE)
				.setParameter("appid", Contanst.APPID)
				.setParameter("secret", Contanst.SECRET).build();

		HttpPost httppost = new HttpPost(uri);

		StringEntity myEntity = new StringEntity("getAccess_token",
				ContentType.create("text/plain", "UTF-8"));
		httppost.setEntity(myEntity);
		String json = sendPost(uri, httppost);
		JSONObject jsonObject = JSONObject.parseObject(json);
		System.out.println("获取access_token返回字符串："+json);
		String access_token = "";
		try {
			// 获取到的凭证
			access_token = jsonObject.getString("access_token");
			// 凭证有效时间，单位：秒
			// String expires_in = jsonObject.getString("expires_in");
		} catch (JSONException e) {
			String errmsg = jsonObject.getString("errmsg");
			String errcode = jsonObject.getString("errcode");
			access_token = "错误代码：" + errcode + "错误原因：" + errmsg;
			e.printStackTrace();
		} finally {

		}

		return access_token;
	}

	/**
	 * 发送一个get请求，返回CloseableHttpResponse
	 * 
	 * @param uri
	 * @param httpget
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	public static CloseableHttpResponse sendGet(URI uri, HttpGet httpget)
			throws URISyntaxException, Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();

		// 设置url
		uri = new URIBuilder().setScheme("http").setHost("138.128.215.38")
				.setPath("/daily/CheckSignature")
				.setParameter("timestamp", "httpclient")
				.setParameter("nonce", "GoogleSearch")
				.setParameter("echostr", "f").build();

		httpget = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpget, context);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream inputstream = entity.getContent();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] tmp = new byte[2048];
			int l;
			while ((l = inputstream.read(tmp)) != -1) {
				os.write(tmp, 0, l);
			}
			String str = os.toString();
			System.out.println(str);
		}
		return response;
	}

	/**
	 * 发送一个post请求，返回entity.getContent()
	 * 
	 * @param uri
	 * @param httppost
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(URI uri, HttpPost httppost) throws Exception {
		//

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		String content = null;

		if (entity != null) {
			InputStream inputstream = entity.getContent();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] tmp = new byte[2048];
			int l;
			while ((l = inputstream.read(tmp)) != -1) {
				os.write(tmp, 0, l);
			}
			content = os.toString();
		}
		return content;
	}

	public static void main(String[] args) throws Exception {
		String access_token = getAccess_token();
		System.out.println("access_token:" + access_token);
	}

}
