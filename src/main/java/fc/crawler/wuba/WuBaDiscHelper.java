package fc.crawler.wuba;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

public class WuBaDiscHelper extends SingleHelper {
	private static final Logger logger = LoggerFactory.getLogger(WuBaDiscHelper.class);
	public static final String WUBA_REDIS_CAT = "wuba:catlist";
	public static final String WUBA_REDIS_LIST_PAGE = "wuba:listpage";
	public static final String WUBA_REDIS_LIST_PAGE_FAIL = "wuba:listpage:fail";
	public static final String BASE_URL = "http://wap.58.com/";
	public static final String END_URL = "/shangpucz/?pagesize=10&filter=0&waptype=local";
	private String city = "";
	private final int MAX_PAGE = 70;
	public String COOKIE = "f=n; f=n; id58=05dvOVYplBsysXuVdieEAg==; als=0; _vz=viz_561f421b68498; bj58_id58s=\"WjVTLSszMzVpYWw2NjU0OQ==\";"
			+ " cookieuid=2754fbc0-b2b8-4166-a02c-f477673c1291; myfeet_tooltip=end; bangbigtip2=1; quanmyy=forfirst; "
			+ "Hm_lvt_3bb04d7a4ca3846dcc66a99c3e861511=1464766726,1464775908,1464852950; __"
			+ "autma=253535702.1834619887.1464852950.1464852950.1464852950.1; "
			+ "Hm_lvt_4d4cdf6bc3c5cb0d6306c928369fe42f=1464766708,1464852971; m58comvp=t14v115.159.229.14; nonLaunch=1; "
			+ "ipcity=bj%7C%u5317%u4EAC%7C0; cookieuid1=c5/npldfZO1fnj9BCHh/Ag==; 58app_hide=1; __utma=253535702.1515010898.1464853021.1464853021.1465869920.2;"
			+ " __utmz=253535702.1465869920.2.2.utmcsr=bj.58.com|utmccn=(referral)|utmcmd=referral|utmcct=/shangpuzushou/; bj=2016614103420; "
			+ "sessionid=45f09795-4b8d-4579-8c95-491a6564ad23; mcity=bj; mcityName=%E5%8C%97%E4%BA%AC; "
			+ "nearCity=%5B%7B%22city%22%3A%22bj%22%2C%22cityName%22%3A%22%E5%8C%97%E4%BA%AC%22%7D%5D; "
			+ "58home=bj; ABTESTCOOKIEVALUE=8; JSESSIONID=C831E1D1C2F6AA9037315986DD32A4EF; "
			+ "final_history=26242984483761%2C26110430164175%2C26351552320825%2C26295954599599%2C26329546889258; "
			+ "city=bj; from=\"\"; 58tj_uuid=87b786b6-e278-426c-a417-9d7154bac384; new_session=0; new_uv=13; "
			+ "utm_source=market; spm=b-31580022738699-pe-f-851.sogoupz_m_biaoti; "
			+ "init_refer=http%253A%252F%252Fm.sogou.com%252Fweb%252FsearchList.jsp%253F%2526sosojump%253D1%2526pid%253Dsogou-misc-77c493ec14246d74%2"
			+ "526keyword%253D58; m_refrom=m58; bj58_new_session=0; bj58_init_refer=\"http://tools.wap.58.com/bj/\"; bj58_new_uv=11; device=wap; f=n";

	public List<String> categoryList = new ArrayList<String>();

	public WuBaDiscHelper(FCrawler fc) {
		super(fc);
		categoryList.add("shengyizr");
		categoryList.add("shangpucz");
		categoryList.add("shangpucs");
		categoryList.add("shangpuqz");
		categoryList.add("shangpuqg");

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
			saveTaskItem();
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
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36"));

	}

	public void catListParser(String data) {
		Document d = Jsoup.parse(data);
		Elements es = d.getElementsByAttributeValueEnding("href", "?pagesize=10&waptype=local");
		// Elements es = d.getElementsByClass("sub-list");
		if (es != null && es.size() > 0) {
			Iterator<Element> it = es.iterator();
			while (it.hasNext()) {
				Element eee = it.next();
				String str = eee.attr("href");
				if (!(str.indexOf("/" + city + "/") > 0)) {
					String disc = parseCat(str);
					saveListItem(disc);
				}
			}
		}
	}

	public String parseCat(String str) {
		String ret = null;
		String[] arr = str.split("/");
		if (arr != null && arr.length > 3) {
			ret = arr[3];
		}
		return ret;
	}

	public void saveListItem(String hpn) {
		RedisService.getIns().sadd(WUBA_REDIS_CAT, hpn);
	}

	private void saveTaskItem() {
		Set<String> set = RedisService.getIns().smembers(WUBA_REDIS_CAT);
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String dis = it.next();
			for (int i = 0; i < categoryList.size(); i++) {
				buildTaskIUrl(dis, categoryList.get(i));
			}
		}

	}

	private void buildTaskIUrl(String dis, String category) {
		StringBuffer sb = new StringBuffer();
		String str = sb.append("http://").append(city).append(".58.com/").append(dis).append("/").append(category)
				.append("/pn").toString();
		for (int i = 0; i < MAX_PAGE; i++) {
			RedisService.getIns().sadd(WUBA_REDIS_LIST_PAGE, str + i);
		}
	}

}
