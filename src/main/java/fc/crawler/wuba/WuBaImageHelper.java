package fc.crawler.wuba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.redis.RedisService;
import fc.juhe.YanZhengMa;

public class WuBaImageHelper {
	private static final Logger logger = LoggerFactory.getLogger(WuBaDiscHelper.class);

	public static final String WUBA_REDIS_IMG_JUHE_FAIL = "wuba:img:juhefail";
	public static final String WUBA_REDIS_IMG_PARSE_FAIL = "wuba:img:parsefail";
	public static final String WUBA_REDIS_IMG_REC = "wuba:img:rec";

	public void singleRec() {
		String str = RedisService.getIns().spop(WuBaPhoneNumberHelper.REDIS_WUBA_PHONENUMBER_IMAGE);
		if (str != null) {
			String retPhone = YanZhengMa.wubaYZM(str);
			if (retPhone != null) {
				if (WuBaPhoneNumberHelper.isMobileNO(retPhone)) {
					logger.debug(retPhone);
					RedisService.getIns().sadd(WUBA_REDIS_IMG_REC, retPhone);
				} else {
					RedisService.getIns().sadd(WUBA_REDIS_IMG_PARSE_FAIL, str);
				}
			} else {
				RedisService.getIns().sadd(WUBA_REDIS_IMG_JUHE_FAIL, str);
			}
		}
	}

	public void batchRun() {
		boolean flag = true;
		while (flag) {
			singleRec();
			if(RedisService.getIns().scard("uBaPhoneNumberHelper.REDIS_WUBA_PHONENUMBER_IMAGE")==0){
				flag=false;
			}
		}
	}

}
