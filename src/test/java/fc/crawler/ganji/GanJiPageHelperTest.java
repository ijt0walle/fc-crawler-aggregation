package fc.crawler.ganji;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class GanJiPageHelperTest {

	@Test
	public void testBatchRun() {
		GanJiListHelper gjlh  = new GanJiListHelper(new FCrawler());
		gjlh.batchRun();
	}

}
