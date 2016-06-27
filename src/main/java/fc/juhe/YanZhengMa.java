package fc.juhe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YanZhengMa {

	private static final Logger logger = LoggerFactory.getLogger(YanZhengMa.class);
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "31a61aca9f454c440e400395e7b6312c";

	public static String wubaYZM(String url) {
		URL u;
		String ret = null;
		File tempf = getTempFile();
		try {
			u = new URL(url);
			FileUtils.copyURLToFile(u, tempf);
			String str = post("1011", tempf);
			logger.debug(" juhe return data :  "+str);
			ret = getPhoneNumber(str);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			tempf.delete();
		}
		return ret;
	}

	private static File getTempFile() {
		Date date = new Date();
		Random r = new Random(100);
		String fileName = date.getTime() + "" + r.nextLong() + ".gif";
		File f = new File(fileName);
		return f;
	}

	private static String getPhoneNumber(String str) {
		String ret = null;
		JsonParser jp = new JsonParser();
		JsonObject je = jp.parse(str).getAsJsonObject();
		int i = je.get("error_code").getAsInt();
		String result = je.get("result").getAsString();
		if (i == 0) {
			ret = result;
		}
		return ret;
	}

	public static String post(String type, File file) throws Exception {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
			HttpPost httppost = new HttpPost("http://op.juhe.cn/vercode/index");
			StringBody keyBody = new StringBody(APPKEY, ContentType.TEXT_PLAIN);
			StringBody typeBody = new StringBody(type, ContentType.TEXT_PLAIN);
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addBinaryBody("image", file, ContentType.create("image/jpeg"), file.getName())
					.addPart("key", keyBody).addPart("codeType", typeBody).build();
			httppost.setEntity(reqEntity);
			httppost.setConfig(config);
			response = httpClient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = ConvertStreamToString(resEntity.getContent(), "UTF-8");
			}
			EntityUtils.consume(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
			httpClient.close();
		}
		logger.info(result);
		return result;
	}

	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
					out.writeBytes(urlencode(params));
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	private static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static String ConvertStreamToString(InputStream is, String charset) throws Exception {
		StringBuilder sb = new StringBuilder();
		try (InputStreamReader inputStreamReader = new InputStreamReader(is, charset)) {
			try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\r\n");
				}
			}
		}
		return sb.toString();
	}

}
