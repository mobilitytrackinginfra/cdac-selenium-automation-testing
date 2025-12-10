package test;

import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestJSONHandling {

	@BeforeSuite
	public void init() {
		RestAssured.baseURI  = "http://localhost";
		RestAssured.port     = 80;
		RestAssured.basePath = "/api-demo/api";
	}
	
	/**
	 * Helper method to get response from /books API
	 */
	private Response getBooksResponse() {
		return RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.get("/books");
	}
	
	/**
	 * $.field → Value at root level
	 * Example: $.meta, $.data
	 */
	@Test
	public void testRootLevelAccess() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== ROOT LEVEL ACCESS ($.field) ===");
		
		// Access root level fields
		Object data = jsonPath.get("data");
		Object meta = jsonPath.get("meta");
		
		System.out.println("$.data: " + data);
		System.out.println("$.meta: " + meta);
		
		// Direct root level value
		int total = jsonPath.getInt("meta.total");
		System.out.println("Total books (meta.total): " + total);
	}
	
	/**
	 * $.parent.child → Nested object access
	 * Example: $.meta.page, $.meta.totalPages
	 */
	@Test
	public void testNestedObjectAccess() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== NESTED OBJECT ACCESS ($.parent.child) ===");
		
		// Access nested objects - meta.page, meta.perPage, etc.
		int page = jsonPath.getInt("meta.page");
		int perPage = jsonPath.getInt("meta.perPage");
		int totalPages = jsonPath.getInt("meta.totalPages");
		int total = jsonPath.getInt("meta.total");
		
		System.out.println("meta.page: " + page);
		System.out.println("meta.perPage: " + perPage);
		System.out.println("meta.totalPages: " + totalPages);
		System.out.println("meta.total: " + total);
		
		// Nested access within array element
		String firstBookAuthor = jsonPath.getString("data[0].author");
		System.out.println("data[0].author: " + firstBookAuthor);
	}
	
	/**
	 * $[index] → Array element access by index
	 * Example: data[0], data[1]
	 */
	@Test
	public void testArrayIndexAccess() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== ARRAY INDEX ACCESS ($[index]) ===");
		
		// Access first element of data array
		Map<String, Object> firstBook = jsonPath.getMap("data[0]");
		System.out.println("data[0] (first book): " + firstBook);
		
		// Access second element
		Map<String, Object> secondBook = jsonPath.getMap("data[1]");
		System.out.println("data[1] (second book): " + secondBook);
		
		// Access specific fields from indexed elements
		String firstTitle = jsonPath.getString("data[0].title");
		String secondTitle = jsonPath.getString("data[1].title");
		int firstPrice = jsonPath.getInt("data[0].price");
		int secondPrice = jsonPath.getInt("data[1].price");
		
		System.out.println("data[0].title: " + firstTitle);
		System.out.println("data[1].title: " + secondTitle);
		System.out.println("data[0].price: " + firstPrice);
		System.out.println("data[1].price: " + secondPrice);
		
		// Access last element using negative index (Groovy GPath)
		Map<String, Object> lastBook = jsonPath.getMap("data[-1]");
		System.out.println("data[-1] (last book): " + lastBook);
	}
	
	/**
	 * $..field → Deep scan - all occurrences of field anywhere in JSON
	 * Example: $..title, $..id, $..price
	 */
	@Test
	public void testDeepScanAllFields() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== DEEP SCAN ($..field) ===");
		
		// Get all titles from anywhere in JSON
		List<String> allTitles = jsonPath.getList("data.title");
		System.out.println("All titles (data.title): " + allTitles);
		// Get all IDs
		List<Integer> allIds = jsonPath.getList("data.id");
		System.out.println("All IDs (data.id): " + allIds);
		
		// Get all authors
		List<String> allAuthors = jsonPath.getList("data.author");
		System.out.println("All authors (data.author): " + allAuthors);
		
		// Get all prices
		List<Integer> allPrices = jsonPath.getList("data.price");
		System.out.println("All prices (data.price): " + allPrices);
		
		List<Object> allStocks = jsonPath.getList("data.stock");
		System.out.println("All stocks (data.stock): " + allStocks);
	}
	
	/**
	 * $[?(@.field > value)] → Filter expressions
	 * REST Assured uses Groovy's findAll/find methods
	 * Example: Find books where price > 500
	 */
	@Test
	public void testFilterExpressions() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== FILTER EXPRESSIONS ($[?(@.field > value)]) ===");
		
		// Filter: Get books where price > 500
		List<Map<String, Object>> expensiveBooks = jsonPath.getList("data.findAll { it.price > 500 }");
		System.out.println("Books with price > 500: " + expensiveBooks);
		
		// Filter: Get books where price > 100
		List<Map<String, Object>> booksOver100 = jsonPath.getList("data.findAll { it.price > 100 }");
		System.out.println("Books with price > 100: " + booksOver100);
		
		// Filter: Get books where stock < 10
		List<Map<String, Object>> lowStockBooks = jsonPath.getList("data.findAll { it.stock < 10 }");
		System.out.println("Books with stock < 10: " + lowStockBooks);
		
		// Filter: Find first book with specific author
		Map<String, Object> martinBook = jsonPath.getMap("data.find { it.author.contains('Martin') }");
		System.out.println("Book by Martin: " + martinBook);
		
		// Filter: Get titles of expensive books only
		List<String> expensiveTitles = jsonPath.getList("data.findAll { it.price > 500 }.title");
		System.out.println("Titles of expensive books (price > 500): " + expensiveTitles);
		
		// Filter: Get books where title contains 'Java'
		List<Map<String, Object>> javaBooks = jsonPath.getList("data.findAll { it.title.contains('Java') }");
		System.out.println("Books with 'Java' in title: " + javaBooks);
	}
	
	/**
	 * Additional useful JSONPath operations
	 */
	@Test
	public void testAdvancedJSONPathOperations() {
		Response res = getBooksResponse();
		JsonPath jsonPath = res.jsonPath();
		
		System.out.println("=== ADVANCED JSONPATH OPERATIONS ===");
		
		// Count elements in array
		int bookCount = jsonPath.getList("data").size();
		System.out.println("Number of books: " + bookCount);
		
		// Sum of all prices
		List<Integer> prices = jsonPath.getList("data.price");
		int totalPrice = prices.stream().mapToInt(Integer::intValue).sum();
		System.out.println("Total of all prices: " + totalPrice);
		
		// Min and Max price
		int minPrice = jsonPath.getInt("data.min { it.price }.price");
		int maxPrice = jsonPath.getInt("data.max { it.price }.price");
		System.out.println("Min price: " + minPrice);
		System.out.println("Max price: " + maxPrice);
		
		// Get specific fields as list of maps
		List<Map<String, Object>> titleAndPrice = jsonPath.getList("data.collect { [title: it.title, price: it.price] }");
		System.out.println("Title and Price only: " + titleAndPrice);
		
		// Check if any book matches condition
		boolean hasExpensiveBook = jsonPath.getBoolean("data.any { it.price > 500 }");
		System.out.println("Has book with price > 500: " + hasExpensiveBook);
		
		// Check if all books match condition
		boolean allInStock = jsonPath.getBoolean("data.every { it.stock > 0 }");
		System.out.println("All books in stock: " + allInStock);
	}
}
