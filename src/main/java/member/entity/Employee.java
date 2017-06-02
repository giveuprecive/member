package member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "name", nullable = true, length = 30)
	private String name;

	@Column(name = "mobile", nullable = true, length = 20)
	private String mobile;

	@Column(name = "phone", nullable = true, length = 20)
	private String phone;

	@Column(name = "address", nullable = true, length = 200)
	private String address;

	@Column(name = "wechatid", nullable = true, length = 100)
	private String wechatid;

	@Column(name = "qq", nullable = true, length = 20)
	private String qq;

	@Column(name = "remark", nullable = true, length = 2000)
	private String remark;

	@Column(name = "email", nullable = true, length = 50)
	private String email;

	@Column(name = "birthday", nullable = true, length = 50)
	private String birthday;

	@Column(name = "sendtime", nullable = true, length = 50)
	private String sendtime;

	@Column(name = "word", nullable = true, length = 80)
	private String word;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		StringBuilder userString = new StringBuilder().append("地址是【").append(this.address).append("】").append("名字是[")
				.append(this.name);

		return userString.toString();

	}
}
