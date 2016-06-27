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

public class GanJiListHelper extends GanJiDiscHelper {
	private static final Logger logger = LoggerFactory.getLogger(GanJiListHelper.class);
	public static final String GANJI_REDIS_DEMAND = "ganji:demandlist";
	public static final String GANJI_REDIS_PAGELIST = "ganji:pagelist";
	public static final String GANJI_REDIS_PAGELIST_FAIL = "ganji:pagelist:fail";
	private String baseUrl = "";
	private static final String DEMAND_BASE_URL = "http://wap.ganji.com/";
	public GanJiListHelper(FCrawler fc) {
		super(fc);
	}
	@Override
	public void urlBuidler() {
		url = baseUrl;
	}


	@Override
	public void afterProcess() {
		if (data != null) {
			pageNumberParser(data);
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
		} 
	}

	public void pageNumberParser(String data) {
		Document d = Jsoup.parse(data);
		Elements list_item = d.getElementsByClass("page-wpbg");
		if (list_item != null && list_item.size() > 0) {
			Element e = list_item.get(0);
			String str = e.nextSibling().outerHtml();
			setListTask(str);
		}else{			
			RedisService.getIns().sadd(GanJiDiscHelper.GANJI_REDIS_CAT_FAIL, url);
		}
	}

	public void setListTask(String str) {
		int end = parse(str);
		for (int i = 1; i <= end; i++) {
			String url = baseUrl + "o" + i;
			RedisService.getIns().sadd(GANJI_REDIS_PAGELIST, url);
		}
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int parse(String str) {
		int ret = 0;
		int index = str.indexOf("/");
		if (index > 0) {
			ret = Integer.parseInt(str.substring(index + 1).trim());
		}
		return ret;
	}

	public void saveListItem(String url) {
		RedisService.getIns().sadd(GANJI_REDIS_PAGELIST, url);
	}

	public void prepareListCrawler() {
		this.doTask();
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			String cat = RedisService.getIns().spop(GanJiDiscHelper.GANJI_REDIS_CAT);
			if (cat != null && cat.length() > 0) {
				logger.info(cat);
				GanJiListHelper gjlh = new GanJiListHelper(new FCrawler());
				gjlh.setBaseUrl(cat);
				gjlh.prepareListCrawler();
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
