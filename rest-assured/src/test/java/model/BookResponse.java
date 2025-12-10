package model;

import java.util.List;

public class BookResponse {
	private List<Book> data;
	private Meta meta;
	
	public List<Book> getData() {
		return data;
	}
	public void setData(List<Book> data) {
		this.data = data;
	}
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	
	
}
