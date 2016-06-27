package fc.crawler.wuba;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class WuBaDemandHelperTest {

	@Test
	public void testBatchRun() {
		WuBaDemandHelper wbdh = new WuBaDemandHelper(new FCrawler());
		wbdh.batchRun();
	}

}
