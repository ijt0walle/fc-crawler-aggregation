package fc.crawler.ganji;

import java.util.ListIterator;

import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.redis.RedisService;
import fc.crawler.base.FCrawler;
import fc.crawler.base.SingleHelper;

public class GanJiDiscHelper extends SingleHelper {
	private static final Logger logger = LoggerFactory.getLogger(GanJiDiscHelper.class);
	public static final String GANJI_REDIS_CAT = "ganji:catlist";
	public static final String GANJI_REDIS_CAT_FAIL = "ganji:catlist:fail";
	public static final String BASE_URL = "http://wap.ganji.com/";
	public static final String END_URL = "/fang6/?ac=sgeo";
	private String city = "";
	public String COOKIE = "ganji_uuid=4763292922855597437014; ganji_xuuid=0210296e-2cae-44a7-9288-c06e7ffab06a.1451971284829; citydomain=bj; mobversionbeta=2.0; __utmganji_v20110909=0xe06cc76ed0437d691db4ddb1dd1f616; cityDomain=bj; lg=1; __utma=32156897.1774320139.1464925777.1465373575.1465375490.5; __utmz=32156897.1464925777.1.1.utmcsr=bj.ganji.com|utmccn=(referral)|utmcmd=referral|utmcct=/fang6/2150553937x.htm; crawler_uuid=146581169837736413958999; GANJISESSID=d85c3ae77e2e623503b8d2df1d8b24aa; _gj_txz=MTQ2NTgxMjMxMTocbTyKVsmeLVNgbuHognW8Bi1KuA==; open_new_fang_path=1; ganji_temp=on";

	public GanJiDiscHelper(FCrawler fc) {
		super(fc);
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public void urlBuidler() {
		url = BASE_URL + city + END_URL;
		logger.debug("current url is :" + url);
	}

	@Override
	public void beforeProcess() {
		urlBuidler();
		setHeader();
		initRequest(url, header);
	}

	@Override
	public void afterProcess() {
		if (data != null) {
			logger.info(data);
			catListParser(data);
		}
	}

	public void setHeader() {

		header.add(new BasicHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
		header.add(new BasicHeader("Accept-Encoding", "gzip, deflate, sdch"));
		header.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
		header.add(new BasicHeader("Connection", "keep-alive"));
		header.add(new BasicHeader("Cookie", COOKIE));
		header.add(new BasicHeader("User-Agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"));

		header.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));

	}

	public void catListParser(String data) {
		Document d = Jsoup.parse(data);
		Elements es = d.getElementsByClass("filter-list");
		if (es != null && es.size() > 0) {
			Element filter = es.get(0);
			Elements e = filter.getElementsByAttribute("href");
			ListIterator<Element> it = e.listIterator();
			while (it.hasNext()) {
				Element ee = it.next();
				if (!ee.text().equals("不限")) {
					String tempUrl = BASE_URL + ee.attr("href");
					logger.info(tempUrl);
					RedisService.getIns().sadd(GANJI_REDIS_CAT, tempUrl);
				}
			}
		}
	}

}
