package test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.Book;
import model.BookResponse;

public class TestResponseSpecifictions {

	@BeforeSuite
	public void init() {
		RestAssured.baseURI  = "http://localhost";
		RestAssured.port     = 80;
		RestAssured.basePath = "/api-demo/api";
	}

	@Test
	public void t1() {
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON);
		
		Response res = req.get("/books");
		System.out.println("StatusCode:"+res.statusCode());
		System.out.println("StatusLine:"+res.statusLine());
		System.out.println("SessionId:"+res.sessionId());
		System.out.println("Body:"+res.body().asPrettyString());
		BookResponse books = res.body().as(BookResponse.class);
		for(Book bk : books.getData()) {
			System.out.println(bk.getId()+","+bk.getTitle());			
		}
		List<String> ids = res.jsonPath().getList("data.id");
		System.out.println(ids);
		
		String name = res.jsonPath().getString("data[0].title");
		System.out.println(name);
		Object title = res.path("data[1].title");
		System.out.println(title);
		
		String contentType = res.header("Content-Type");
		System.out.println("Header Content Type:\t"+contentType);
		
		System.out.println("Execution Time in MS: "+res.time());
	}

	
	
	@Test
	public void t2() {
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON);
		
		Response respone = req.get("/books");
		ValidatableResponse res = respone.then();
		res.statusCode(200);
		res.statusLine("HTTP/1.1 200 OK");
		res.body(containsString("data"));
		res.statusLine(containsString("200"));
		res.body("meta.page",equalTo(1));
		res.header("Content-Type", containsString("json"));
	}
	
	
	
	@Test
	public void t3() {
		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON);
		
		Response respone = req.get("/books");
		ValidatableResponse res = respone.then();
		res.statusCode(200);
		res.statusLine("HTTP/1.1 200 OK");
		res.body(containsString("data"));
		res.statusLine(containsString("200"));
		res.body("meta.page",equalTo(1));
		res.header("Content-Type", containsString("json"));
	}
	
	
}
