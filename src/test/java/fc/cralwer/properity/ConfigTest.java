package fc.cralwer.properity;

import java.util.logging.Logger;

import org.junit.Test;

public class ConfigTest {
	public static Logger logger = Logger.getLogger(ConfigTest.class.getName());// 写控制台

	@Test
	public void testPropery() {

		logger.info(Config.getPropery().getProperty("redis.host"));
	}

}
