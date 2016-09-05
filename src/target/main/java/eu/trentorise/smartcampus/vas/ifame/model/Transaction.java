package eu.trentorise.smartcampus.vas.ifame.model;

public class Transaction {

	private Long timemillis;
	private String value;

	public Transaction() {
	}

	public Long getTimemillis() {
		return timemillis;
	}

	public void setTimemillis(Long timemillis) {
		this.timemillis = timemillis;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}