package fc.crawler.qunyou;

public class QunYouData {
	private String member_id;
	private String phonebook_id;
	private String user_id;
	private long join_at;
	private int credit;
	private String card_id;
	private int share_count;
	private int invite_count;
	private QunYouCard card;
	
	public String getMember_id() {
		return member_id;
	}

	public String getPhonebook_id() {
		return phonebook_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public long getJoin_at() {
		return join_at;
	}

	public int getCredit() {
		return credit;
	}

	public String getCard_id() {
		return card_id;
	}

	public int getShare_count() {
		return share_count;
	}

	public int getInvite_count() {
		return invite_count;
	}

	public QunYouCard getCard() {
		return card;
	}	
}
