package fc.crawler.qunyou;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QunYouHelperSimple extends QunYouHelper{
	public static Logger logger = Logger.getLogger(QunYouHelperSimple.class.getName());//写控制台
	public QunYouHelperSimple(QunYouCrawler fc) {
		super(fc);
	}
	public void setHeader(){
		logger.log(Level.INFO, "----set header----");
	}
	
}
