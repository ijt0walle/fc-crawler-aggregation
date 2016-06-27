package fc.cralwer.properity;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import fc.crawler.qunyou.ResourceTest;

public class Config {
	public static Logger logger = Logger.getLogger(ResourceTest.class.getName());// 写控制台

	static private Properties ins;

	public static Properties getPropery() {
		if (ins == null) {
			synchronized (Config.class) {
				if (ins == null) {
					Properties p = new Properties();
					ClassLoader classLoader = Config.class.getClassLoader();
					try {
						p.load(classLoader.getResourceAsStream("redis.properties"));
						ins = p;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ins;
	}
}
