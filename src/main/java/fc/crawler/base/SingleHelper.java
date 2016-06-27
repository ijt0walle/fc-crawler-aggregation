package fc.crawler.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class SingleHelper implements Helper {
	protected FCrawler fcr;
	protected  String data;
	protected String url = "";
	protected List<Header> header = new LinkedList<Header>();
	protected String cookie =""; 
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie){
		this.cookie= cookie;
	}
	public SingleHelper(FCrawler fc) {
		fcr = fc;
	}

	private HttpUriRequest request;

	public HttpUriRequest getRequest() {
		return request;
	}
	public void urlBuidler(){
		
	}
	public void setRequest(HttpUriRequest request) {
		this.request = request;
	}
	public void initRequest(String url, List<Header> header) {
		HttpUriRequest  hur = new HttpGet(url);
		if(header!=null){
			Iterator<Header> it = header.iterator();
			while(it.hasNext()){
				hur.setHeader(it.next());
			}
		}				
		this.request = hur;
	}
	@Override
	public void process(){
		data = fcr.getData(request);
	}

	@Override
	public void beforeProcess() {
		
	}

	@Override
	public void afterProcess() {
		
	}
	public String getData(){
		return data;
	}
	
	public void doTask(){
		beforeProcess();
		process();
		afterProcess();
	}
	public void setUrl(String _url) {
		url = _url;
	}
	
	public String getUrl(){
		return url;
	}
}
