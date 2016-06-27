package fc.crawler.ganji;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class GanJiPhoneNumberHelperTest {

	@Test
	public void testBatchRun() {
		GanJiPhoneNumberHelper gjlh  = new GanJiPhoneNumberHelper(new FCrawler());
		gjlh.batchRun();
	}

}
