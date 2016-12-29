package id.co.babe.filter.model;

public class Komen {
	public static final String SPAM = "0";
	public static final String NORMAL = "1";
	
	
	public String content;
	public String label;
	
	public Komen(String content, String label) {
		this.content = content;
		this.label = label;
	}
	
	

}
