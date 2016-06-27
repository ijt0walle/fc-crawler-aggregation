package fc.juhe;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestYanZhengMa {
	private static final Logger logger = LoggerFactory.getLogger(TestYanZhengMa.class);
	@Test
	public void testJuHe() {
		try {
			String ret= YanZhengMa.post("1011", new File("c:/showphone.gif"));
			logger.info(ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestWubaYZM() {
		YanZhengMa.wubaYZM("http://image.58.com/showphone.aspx?t=v55&v=017889D4E98037DA078BD6E2A689E9BC4");
	}
}
