package com.pab.jmxmonitor.input;

public class JmxId {
	private String url;
	private String service;
	private String user;
	private String password;
	
	public JmxId(String url, String user, String password, String service) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.service = service;
	}
	
	public String toString() {
		return getUrl() + ((getUser() == null) ? "" : ("[" + getUser()+ "/"+getPassword() + "]"));
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof JmxId))
			return false;
		JmxId oj = (JmxId) o;
		return getUrl().equals(oj.getUrl()) 
			&& checkedEqual(getUser(), oj.getUser()) && checkedEqual(getPassword(), oj.getPassword())
			&& checkedEqual(service, oj.service);
	}
	
	public int hashCode() {
		return 67 * getUrl().hashCode() + 31 * checkedHashcode(getUser()) + checkedHashcode(getPassword()); 
	}
	
	private int checkedHashcode(Object s1) {
		return s1 == null ? 30 : s1.hashCode();
	}
	private boolean checkedEqual(Object s1, Object s2) {
		if (s1 == null && s2 != null)
			return false;
		if (s1 == null)
			return true;
		return s1.equals(s2);
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}
	
}