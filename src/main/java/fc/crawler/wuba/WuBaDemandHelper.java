package fc.crawler.wuba;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.redis.RedisService;
import fc.crawler.base.FCrawler;

public class WuBaDemandHelper extends WuBaDiscHelper {
	private static final Logger logger = LoggerFactory.getLogger(WuBaDemandHelper.class);
	public static final String REDIS_WUBA_DEMAND = "wuba:demandlist";
	public static final String REDIS_WUBA_DEMAND_FAIL = "wuba:demandlist:fail";

	private String baseUrl = "";

	public WuBaDemandHelper(FCrawler fc) {
		super(fc);
	}

	@Override
	public void beforeProcess() {
		setHeader();
		initRequest(url, header);
	}

	@Override
	public void afterProcess() {
		if (data != null) {
			demandListParser(data);
		}
	}

	public void demandListParser(String data) {
		Document d = Jsoup.parse(data);
		Elements list_item = d.select("#infolist > table > tbody");
		if (list_item != null && list_item.size() == 1) {
			Element e = list_item.get(0);
			Elements ee = e.getElementsByAttribute("href");
			if (ee != null) {
				Iterator<Element> ite = ee.iterator();
				while (ite.hasNext()) {
					Element eee = ite.next();
					String endUrl = eee.attr("href");
					if (endUrl != null && endUrl.length() > 0&&endUrl.indexOf(".shtml")>0) {
						saveListItem(endUrl);
					}
				}
			}
		} else {
			RedisService.getIns().sadd(WuBaDiscHelper.WUBA_REDIS_LIST_PAGE_FAIL, url);
		}
	}

	public void saveListItem(String url) {
		RedisService.getIns().sadd(WuBaDemandHelper.REDIS_WUBA_DEMAND, url);
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			String url = RedisService.getIns().spop(WuBaDiscHelper.WUBA_REDIS_LIST_PAGE);

			if (url != null && url.length() > 0) {
				logger.info(url);
				WuBaDemandHelper gjlh = new WuBaDemandHelper(new FCrawler());
				gjlh.setUrl(url);
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
