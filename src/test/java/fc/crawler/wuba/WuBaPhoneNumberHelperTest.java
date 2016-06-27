package fc.crawler.wuba;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class WuBaPhoneNumberHelperTest {

	@Test
	public void testBatchRun() {
		WuBaPhoneNumberHelper wpnh = new WuBaPhoneNumberHelper(new FCrawler());	
		wpnh.batchRun();
	}
	@Test
	public void testDoTask() {
		
		WuBaPhoneNumberHelper wpnh = new WuBaPhoneNumberHelper(new FCrawler());		
//		wpnh.setUrl("http://bj.58.com/shangpu/25891634718789x.shtml?psid=185950435192137914742041062&entinfo=25891634718789_0");
		wpnh.setUrl("http://bj.58.com/shangpu/26075453704109x.shtml?psid=150303588192137941836428309&entinfo=26075453704109_0");
		wpnh.doTask();
	}
}
