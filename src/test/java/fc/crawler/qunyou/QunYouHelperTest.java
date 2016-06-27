package fc.crawler.qunyou;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class QunYouHelperTest {

	@Test
	public void testSimpleDoTask() {
		QunYouCrawler qyc = new QunYouCrawler();
		QunYouHelperSimple qyh = new QunYouHelperSimple(qyc);
		qyh.setQunId("729d356ed3d7");
		qyh.setCookie("auth.token=zN2fpYIf-YfABN7d; " + "auth.user_id=23y34ecoEzDoN7eiHDsWQD36iypw11Ui6sAjAt; "
				+ "auth.expires_in=1467788473; auth.platform=3; "
				+ "Hm_lvt_6b7b8615af4a1f51df2500d655268be1=1465194401,1465349965; "
				+ "Hm_lpvt_6b7b8615af4a1f51df2500d655268be1=1465356224; "
				+ "SERVERID=fdbe7b01fb22c2cedbf5697ba444c972|1465356206|1465349947");
		qyh.doTask();
	}

	@Test
	public void testSimpleBatchProcess() {
		QunYouCrawler qyc = new QunYouCrawler();
		QunYouHelperSimple qyh = new QunYouHelperSimple(qyc);
		qyh.setQunId("9d0c9f6bb9fa");
		qyh.batchProcess();
	}

	@Test
	public void testDoTask() {
		QunYouCrawler qyc = new QunYouCrawler();
		QunYouHelper qyh = new QunYouHelper(qyc);
		qyh.setQunId("9d0c9f6bb9fa");
		qyh.setCookie("auth.token=zN2fpYIf-YfABN7d; " + "auth.user_id=23y34ecoEzDoN7eiHDsWQD36iypw11Ui6sAjAt; "
				+ "auth.expires_in=1467788473; auth.platform=3; "
				+ "Hm_lvt_6b7b8615af4a1f51df2500d655268be1=1465194401,1465349965; "
				+ "Hm_lpvt_6b7b8615af4a1f51df2500d655268be1=1465356224; "
				+ "SERVERID=fdbe7b01fb22c2cedbf5697ba444c972|1465356206|1465349947");
		qyh.doTask();
	}

	@Test
	public void testBatchProcess() {

		List<String> l = new LinkedList<String>();
//		l.add("9d0c9f6bb9fa");
//		l.add("729d356ed3d7");
//		l.add("609c13ba90e7");
//		l.add("a4196b9b3b18");
//		l.add("8149fa82055c");
//		l.add("7be518baf75b");
//		l.add("6b83fd513a68");
//		l.add("392a22449a3c");
//		l.add("e116e882bbd6");
//		l.add("4183011a44be");
//		l.add("2739551da6bb");
		
		l.add("e65fe39d14cc");
		l.add("17fd1964d795");
		l.add("559dca240725");
		l.add("d82e2a74d9af");	

		
		Iterator<String> it = l.iterator();
		while (it.hasNext()) {
			String id = it.next();
			QunYouCrawler qyc = new QunYouCrawler();
			QunYouHelper qyh = new QunYouHelper(qyc);
			qyh.setQunId(id);
			qyh.setCookie("auth.token=zN2fpYIf-YfABN7d; " + "auth.user_id=23y34ecoEzDoN7eiHDsWQD36iypw11Ui6sAjAt; "
					+ "auth.expires_in=1467788473; auth.platform=3; "
					+ "Hm_lvt_6b7b8615af4a1f51df2500d655268be1=1465194401,1465349965; "
					+ "Hm_lpvt_6b7b8615af4a1f51df2500d655268be1=1465356224; "
					+ "SERVERID=fdbe7b01fb22c2cedbf5697ba444c972|1465356206|1465349947");
			qyh.batchProcess();
		}

	}
}
