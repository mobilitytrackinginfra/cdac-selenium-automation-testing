package model;

public class Book {
	private String title;
	private String author;
	private Integer price;
	private Integer stock;
	private String createdAt;
	private Integer id;
	
	// Default constructor required for Jackson deserialization
	public Book() {
		
	}
	
	public Book(String title, String author, Integer price, Integer stock) {
		this.title = title;
		this.author = author;
		this.price = price;
		this.stock = stock;
	}

	
	public String getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	
}
