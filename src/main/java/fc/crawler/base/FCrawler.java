package fc.crawler.base;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import fc.crawler.http.HttpClientUtil;

public class FCrawler {
	public void urlBulider() {

	}

	public String getData(HttpUriRequest request) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String ret = null;
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(request);
			HttpEntity e = response.getEntity();
			InputStream is = e.getContent();
			ret = IOUtils.toString(is, "UTF-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public void getDataTest(String url) {
		 HttpClientUtil.get(url);
	}

}
