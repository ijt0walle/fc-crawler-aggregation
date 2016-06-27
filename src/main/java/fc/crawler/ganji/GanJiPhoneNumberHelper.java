package fc.crawler.ganji;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.redis.RedisService;
import fc.crawler.base.FCrawler;

public class GanJiPhoneNumberHelper extends GanJiDiscHelper {
	private static final Logger logger = LoggerFactory.getLogger(GanJiPhoneNumberHelper.class);
	public static final String REDIS_PHONENUMBER = "ganji:phoneNumber";

	public GanJiPhoneNumberHelper(FCrawler fc) {
		super(fc);
	}

	@Override
	public void afterProcess() {
		if (data != null) {
			phoneNumberParser(data);
		}
	}

	@Override
	public void beforeProcess() {
		setHeader();
		initRequest(url, header);
	}

	public void phoneNumberParser(String data) {
		Document d = Jsoup.parse(data);
		Elements list_item = d.getElementsByClass("phone-contact");
		if (list_item != null && list_item.size() > 0) {
			Iterator<Element> it = list_item.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				Elements ee = e.getElementsByAttribute("gjalog");
				if (ee != null) {
					Iterator<Element> ite = ee.iterator();
					while (ite.hasNext()) {
						Element eee = ite.next();
						String phoneString = eee.attr("href").trim();
						if (phoneString != null && phoneString.length() > 0) {
							String temp = extractPhoneNumber(phoneString);
							logger.info(temp);
							saveListItem(temp);
						}
					}
				}
			}
		}
	}

	private String extractPhoneNumber(String str) {
		String ret = null;
		if (str != null && str.length() > 0) {
			int index = str.indexOf(":");
			if (index > 1) {
				ret = str.substring(index + 1).trim();
			}
		}
		return ret;
	}

	public int parse(String str) {
		int ret = 0;
		int index = str.indexOf("/");
		if (index > 0) {
			ret = Integer.parseInt(str.substring(index + 1).trim());
		}
		return ret;
	}

	public void saveListItem(String hpn) {
		RedisService.getIns().sadd(REDIS_PHONENUMBER, hpn);
	}

	public void prepareListCrawler() {
		this.doTask();
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			String cat = RedisService.getIns().spop(GanJiListHelper.GANJI_REDIS_DEMAND);
			if (cat != null && cat.length() > 0) {
				logger.info(cat);
				GanJiPhoneNumberHelper gjlh = new GanJiPhoneNumberHelper(new FCrawler());
				gjlh.setUrl(cat);
				gjlh.doTask();
			} else {
				flag = false;
			}
		}
	}

}
