package fc.cralwer.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fc.cralwer.properity.Config;
import redis.clients.jedis.Jedis;

public class RedisService {
	private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

	private RedisService() {

	}

	private static Jedis ins;

	static public Jedis getIns() {
		if (ins == null) {
			synchronized (RedisService.class) {
				if (ins == null) {
					String host = Config.getPropery().getProperty("redis.host");
					String port = Config.getPropery().getProperty("redis.port");
					String pass = Config.getPropery().getProperty("redis.pass");
					ins = new Jedis(host, Integer.parseInt(port));
					if (pass != null&&pass.length()>0) {
						ins.auth(pass);
					};
					logger.info("jedis init============");
				}
			}
		}
		return ins;
	}
}
