package fc.crawler.ganji;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class GanJiDemandHelperTest {

	@Test
	public void testBatchRun() {
		GanJiDemandHelper gjlh  = new GanJiDemandHelper(new FCrawler());
		gjlh.batchRun();
	}

}
