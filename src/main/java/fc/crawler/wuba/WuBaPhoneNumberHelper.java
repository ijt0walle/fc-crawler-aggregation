package fc.crawler.wuba;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.redis.RedisService;
import fc.crawler.base.FCrawler;

public class WuBaPhoneNumberHelper extends WuBaDemandHelper {
	private static final Logger logger = LoggerFactory.getLogger(WuBaPhoneNumberHelper.class);
	public static final String REDIS_WUBA_PHONENUMBER = "wuba:phoneNumber";
	public static final String REDIS_WUBA_PHONENUMBER_IMAGE = "wuba:phoneNumber:image";
	public static final String REDIS_WUBA_PHONENUMBER_OVERDUE = "wuba:phoneNumber:overdue";
	public static final String REDIS_WUBA_PHONENUMBER_ERROR = "wuba:phoneNumber:error";
//	public static final String REG = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	public static final String REG = "^(1[0-9])\\d{9}$";

	public WuBaPhoneNumberHelper(FCrawler fc) {
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

		Elements image = d.select("#t_phone > script");
		if (image != null && image.size() > 0) {
			Element e = image.get(0);
			String str = parsePhone(e.html().trim());
			saveImagePhone(str);
		} else {
			Elements phone = d.select("#t_phone");
			if (phone != null && phone.size() > 0) {
				Element e = phone.get(0);
				String str = e.text().trim();

				if (str != null && str.length() > 0) {
					if (str.contains("您查看的信息已过期")) {
						saveOverDue();
					} else if (isMobileNO(str)) {
						savePhoneNumber(str);
					} else {
						saveError();
					}
				}
			}
		}
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile(REG);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	private String parsePhone(String str) {
		String ret = "";
		if (str != null && str.length() > 5) {
			String[] arr = str.split("'");
			if (arr != null && arr.length == 3) {
				ret = arr[1];
			}
		}
		return ret;
	}

	private void saveImagePhone(String str) {
		RedisService.getIns().sadd(REDIS_WUBA_PHONENUMBER_IMAGE, str);
	}

	private void savePhoneNumber(String str) {
		RedisService.getIns().sadd(REDIS_WUBA_PHONENUMBER, str);
	}

	private void saveOverDue() {
		RedisService.getIns().sadd(REDIS_WUBA_PHONENUMBER_OVERDUE, url);
	}

	private void saveError() {
		RedisService.getIns().sadd(REDIS_WUBA_PHONENUMBER_ERROR, url);
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			String cat = RedisService.getIns().spop(WuBaDemandHelper.REDIS_WUBA_DEMAND);
			if (cat != null && cat.length() > 0) {
				logger.info(cat);
				WuBaPhoneNumberHelper gjlh = new WuBaPhoneNumberHelper(new FCrawler());
				gjlh.setUrl(cat);
				gjlh.doTask();
			} else {
				flag = false;
			}
		}
	}

}
