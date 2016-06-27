package fc.crawler.ganji;

import org.junit.Test;

import fc.crawler.base.FCrawler;

public class GanjiDistTest {
	@Test
	public void testGanjiDist() {
		GanJiDiscHelper gdh = new GanJiDiscHelper(new FCrawler());
		gdh.setCity("bj");
		gdh.doTask();
	}
}
