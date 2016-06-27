package fc.crawler.qunyou;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fc.crawler.base.SingleHelper;

public class QunYouHelper extends SingleHelper {
	private static final Logger logger = LoggerFactory.getLogger(QunYouHelper.class);
	public static final String BASE_URL = "http://v2.qun.hk/v1/phonebook/members?keyword=";
	
	protected String qunId = null;
	protected int page = 1;

	public QunYouHelper(QunYouCrawler fc) {
		super(fc);
		page = 1;
	}

	public void urlBuidler() {
		url = BASE_URL;
		if (page > 0) {
			url += "&page=" + page;
		}
		if (qunId != null && qunId.length() > 0) {
			url += "&id=" + qunId;
		}
	}

	public void setQunId(String str) {
		qunId = str;
	}

	public void setHeader() {
		header.add(new BasicHeader("Host", "v2.qun.hk"));
		header.add(new BasicHeader("Connection", "keep-alive"));
		header.add(new BasicHeader("Accept", "*/*"));
		header.add(new BasicHeader("Authorization", "Bearer zN2fpYIf-YfABN7d"));
		header.add(new BasicHeader("Cache-Control", "no-cache"));
		header.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36"));
		header.add(new BasicHeader("Referer", "http://v2.qun.hk/"));
		header.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		header.add(new BasicHeader("Accept-Language", "en-us,en;q=0.8"));
		header.add(new BasicHeader("Cookie",cookie));
	}

	@Override
	public void beforeProcess() {
		setHeader();
		urlBuidler();
		initRequest(url, header);
	}

	@Override
	public void afterProcess() {
		if (data != null) {

			JsonParser jp = new JsonParser();
			JsonObject jo = (JsonObject) jp.parse(data);
			List<QunYouData> l = parseResult(jo);
			save(l);
			data = null;

		}
	}

	public List<QunYouData> parseResult(JsonObject data) {
		List<QunYouData> l = new LinkedList<QunYouData>();
		Gson gson = new GsonBuilder().create();
		JsonArray jarr = data.get("list").getAsJsonArray();
		for (int i = 0; i < jarr.size(); i++) {
			JsonObject jo = jarr.get(i).getAsJsonObject();
			QunYouData qyd = gson.fromJson(jo, QunYouData.class);
			l.add(qyd);
		}

		page = data.get("next").getAsInt();
		return l;
	}

	public void save(List<QunYouData> l) {
		File f = new File("qunyou.txt");
		Iterator<QunYouData> it = l.iterator();
		while (it.hasNext()) {
			QunYouData qyd = it.next();
			if (filter(qyd.getCard().getPhone())) {
				StringBuffer sb = new StringBuffer();
				sb.append(qyd.getCard().getRealname() + "\t");
				sb.append(qyd.getCard().getPosition() + "\t");
				sb.append(qyd.getCard().getCompany() + "\t");
				sb.append(qyd.getCard().getPhone() + "\t");
				sb.append(qyd.getCard().getNickname() + "\t");
				sb.append(qyd.getCredit() + "\t");
				sb.append(qyd.getInvite_count() + "\t");
				sb.append(qyd.getShare_count() + "\t");
				sb.append(qyd.getJoin_at() + "\t");
				sb.append(qyd.getCard().getAvatar() + "\r\n");
				try {
					FileUtils.write(f, sb.toString(), "UTF-8", true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public boolean filter(String str) {
		boolean ret = false;
		logger.debug("str:" + str);
		if (str != null && str.length() > 0) {
			ret = str.contains("*");
		}
		return !ret;
	}

	public void batchProcess() {
		while (page > 0) {
			logger.debug("========page:+" + page + "=========");
			doTask();
		}
	}
	
	
	
}
