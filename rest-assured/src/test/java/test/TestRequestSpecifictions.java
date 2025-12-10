package test;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestRequestSpecifictions {

	@BeforeSuite
	public void init() {
		RestAssured.baseURI  = "http://localhost";
		RestAssured.port     = 80;
		RestAssured.basePath = "/api-demo/api";
	}

	@Test
	public void t1() {
		given()
		.log().all()
		.header("Authorization", "Bearer test-token")
		.when()
		.get("/books")
		.then()
		.log().all()
		.statusCode(200);
	}


	@Test
	public void t2() {
		RequestSpecification req = RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/api-demo/api")
				.port(80)
				.header("Authorization", "Bearer test-token")
				.pathParam("bookId", 1);
		Response res = req.get("/books/{bookId}");
		System.out.println(res.asPrettyString());
	}

	@Test
	public void t3() {

		Map<String, Object> params = new HashMap<>();
		params.put("page", "1");
		params.put("perPage", 2);
		params.put("author", "Robert");

		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.queryParams(params);

		Response res = req.get("/books");
		System.out.println(res.asPrettyString());
	}


	@Test
	public void t4() {

		Headers headers = new Headers(
				new Header("Content-Type", "application/json"),
				new Header("Accept", "application/json")
				);

		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.headers(headers)
				.cookie("sessionId", "abc123xyz");
		
		Response res = req.get("/books");
		System.out.println(res.body().asPrettyString());
	}
	
	
	@Test
	public void t5() {

		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON);
		
		Response res = req.get("/books");
		System.out.println(res.body().asPrettyString());
	}
	
	
	@Test
	public void t6() {

		RequestSpecification req = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.multiPart("file", new File("D:\\CDAC\\Assignments.txt"));
		
		Response res = req.post("/upload");
		System.out.println(res.body().asPrettyString());
	}

	
	@Test
	public void t7() {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		RequestSpecification spec = builder.addHeader("Authorization", "Bearer test-token")
			.setContentType(ContentType.JSON)
			.setAccept(ContentType.JSON)
			.build();
		
		
		Response res = given().spec(spec).get("/books");
		System.out.println(res.body().asPrettyString());
		
		res = given().spec(spec).get("/books");
		System.out.println(res.body().asPrettyString());
		
	}
	
	
}
