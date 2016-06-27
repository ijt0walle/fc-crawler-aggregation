package fc.crawler.qunyou;

public class QunYouCard {
	private String user_id;
	private String card_id;
	private String realname;
	private String nickname;
	private String company;
	private String position;
	private int certified;
	private String phone;
	private String avatar;	

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getCertified() {
		return certified;
	}

	public void setCertified(int certified) {
		this.certified = certified;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "QunYouCard [user_id=" + user_id + ", card_id=" + card_id + ", realname=" + realname + ", nickname="
				+ nickname + ", company=" + company + ", position=" + position + ", certified=" + certified + ", phone="
				+ phone + ", avatar=" + avatar + "]";
	}

}
