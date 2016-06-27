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

public class GanJiDemandHelper extends GanJiListHelper {
	private static final Logger logger = LoggerFactory.getLogger(GanJiDemandHelper.class);

	private String baseUrl = "";
	private static final String DEMAND_BASE_URL = "http://wap.ganji.com/";

	public GanJiDemandHelper(FCrawler fc) {
		super(fc);
	}

	@Override
	public void urlBuidler() {
		url = baseUrl;
	}

	@Override
	public void afterProcess() {
		if (data != null) {
			demandListParser(data);
		}
	}

	public void demandListParser(String data) {
		Document d = Jsoup.parse(data);
		Elements list_item = d.getElementsByClass("list-item");
		if (list_item != null && list_item.size() > 0) {
			Iterator<Element> it = list_item.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				Elements ee = e.getElementsByAttribute("href");
				if (ee != null) {
					Iterator<Element> ite = ee.iterator();
					while (ite.hasNext()) {
						Element eee = ite.next();
						String endUrl = eee.attr("href");
						if (endUrl != null && endUrl.length() > 0 && endUrl.indexOf("?") > 0) {
							String temp = DEMAND_BASE_URL + endUrl;
							logger.info(temp);
							saveListItem(temp);
						}
					}
				}
			}
		} else {

			RedisService.getIns().sadd(GanJiListHelper.GANJI_REDIS_PAGELIST_FAIL, url);
		}
	}

	public void saveListItem(String url) {
		RedisService.getIns().sadd(GanJiListHelper.GANJI_REDIS_DEMAND, url);
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			String cat = RedisService.getIns().spop(GanJiListHelper.GANJI_REDIS_PAGELIST);
			if (cat != null && cat.length() > 0) {
				logger.info(cat);
				GanJiDemandHelper gjlh = new GanJiDemandHelper(new FCrawler());
				gjlh.setBaseUrl(cat);
				gjlh.doTask();
			} else {
				flag = false;
			}
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
