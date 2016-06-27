package fc.cralwer.redis;

import java.util.logging.Logger;

import org.junit.Test;

public class RedisServiceTest {
	public static Logger logger = Logger.getLogger(RedisServiceTest.class.getName());//写控制台
	@Test
	public void testGetIns() {
		RedisService.getIns();		
	}
	@Test
	public void testRedisPop(){
		boolean flag = true;
		while(flag){
			String item = RedisService.getIns().spop("wuba:phoneNumber_temp");
			if(item.startsWith("http")){
				RedisService.getIns().sadd("wuba:demandlist_bak", item);
			}else{
				RedisService.getIns().sadd("wuba:phoneNumber_temp1", item);
			}			
		}
		
	}

}
