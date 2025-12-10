package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestFileHandling {

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
				.multiPart("file", new File("D:\\CDAC\\TAF\\DemoTestData.csv"));
		
		Response res = req.post("/upload");
		System.out.println(res.body().asPrettyString());
	}
	
	
	@Test
	public void t2() throws IOException {
		// Download file and save to disk
		String filename = "DemoTestData.csv";
		String savePath = "D:/CDAC/downloads/" + filename;
		
		Response res = RestAssured.given()
				.header("Authorization", "Bearer test-token")
				.when()
				.get("/files/" + filename);
		
		System.out.println("Status Code: " + res.statusCode());
		
		if (res.statusCode() == 200) {
			// Save file to disk
			byte[] fileContent = res.asByteArray();
			
			File outputFile = new File(savePath);
			outputFile.getParentFile().mkdirs();  // Create directories if not exist
			
			try (FileOutputStream fos = new FileOutputStream(outputFile)) {
				fos.write(fileContent);
			}
			
			System.out.println("File saved to: " + savePath);
			System.out.println("File size: " + outputFile.length() + " bytes");
			
			Assert.assertTrue(outputFile.exists(), "File should be saved");
		}
	}
	
}
