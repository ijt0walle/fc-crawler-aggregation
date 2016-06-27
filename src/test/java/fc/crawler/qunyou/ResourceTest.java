package fc.crawler.qunyou;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Test;

public class ResourceTest {
	public static Logger logger = Logger.getLogger(ResourceTest.class.getName());//写控制台

	@Test
	public void getRedisPro() {
		 Properties p=new Properties();
		 ClassLoader classLoader = getClass().getClassLoader();  
		 try {
			p.load(classLoader.getResourceAsStream("redis.properties"));
			logger.info(p.getProperty("redis.expire"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
