package fc.crawler.wuba;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class WuBaDiscHelperTest {

	@Test
	public void wuBaDiscHelperTest() {
		WuBaDiscHelper wbdh = new WuBaDiscHelper(new FCrawler());
		wbdh.setCity("bj");
		wbdh.doTask();
	}
}
