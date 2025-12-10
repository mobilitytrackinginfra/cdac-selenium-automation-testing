package test;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Book;

public class TestRequestMethods {

	@BeforeSuite
	public void init() {
		RestAssured.baseURI  = "http://localhost";
		RestAssured.port     = 80;
		RestAssured.basePath = "/api-demo/api";
	}
	
	
	//POST + GET
	@Test
	public void t1() {
		Book bk = new Book("Automation Testing", "Swapnil", 900, 20);
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(bk);
				
		Response res = req.post("/books");
		System.out.println(res.asPrettyString());

		res = given().header("Authorization", "Bearer test-token")
		.get("/books");
		System.out.println(res.body().asPrettyString());
	}
	
	
	
	//PUT
	@Test
	public void t2() {
		Book bk = new Book("Automation Testing(Updated)", "Swapnil", 450, 50);
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(bk)
				.pathParam("bookId", 7);
				
		Response res = req.put("/books/{bookId}");
		System.out.println(res.asPrettyString());

		res = given().header("Authorization", "Bearer test-token")
		.get("/books");
		System.out.println(res.body().asPrettyString());
	}
	
	
	
	//PATCH
	@Test
	public void t3() {
		Book bk = new Book(null, null, null, 500);
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(bk)
				.pathParam("bookId", 7);
				
		Response res = req.patch("/books/{bookId}");
		System.out.println(res.asPrettyString());

		res = given().header("Authorization", "Bearer test-token")
		.get("/books");
		System.out.println(res.body().asPrettyString());
	}
	
	
	
	//DELETE
		@Test
		public void t4() {
			RequestSpecification req = RestAssured.given()
					.header("Authorization", "Bearer test-token")
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.pathParam("bookId", 7);
					
			Response res = req.delete("/books/{bookId}");
			System.out.println(res.asPrettyString());

			res = given().header("Authorization", "Bearer test-token")
			.get("/books");
			System.out.println(res.body().asPrettyString());
		}
	
}
