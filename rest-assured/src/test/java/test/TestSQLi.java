package test;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;

public class TestSQLi {

	@BeforeSuite
	public void init() {
		RestAssured.baseURI  = "http://localhost";
		RestAssured.port     = 80;
		RestAssured.basePath = "/api-demo/api/sql_test.php";
	}
	
	@Test
    public void normalLoginShouldSucceed_VulnerableMode() {
        String body = "{ \"username\": \"admin\", \"password\": \"admin123\" }";

        RestAssured.given()
            .queryParam("mode", "vulnerable")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("login", equalTo(true))
            .body("message", equalTo("Login successful"))
            .body("row_count", greaterThanOrEqualTo(1));
    }

	
	/**
     * This test demonstrates a BROKEN SQL injection payload.
     * 
     * Injection in USERNAME: ' OR '1'='1
     * Generated SQL: SELECT * FROM users WHERE username = '' OR '1'='1' AND password = 'anything'
     * 
     * Due to SQL operator precedence (AND before OR):
     * = username = '' OR ('1'='1' AND password = 'anything')
     * = FALSE OR (TRUE AND FALSE) = FALSE
     * 
     * Result: 0 rows returned, login FAILS → Test PASSES (expects 401)
     */
    @Test
    public void sqlInjectionInUsername_FailsDueToANDPrecedence() {
        // This injection doesn't work because AND has higher precedence than OR
        String body = "{ \"username\": \"' OR '1'='1\", \"password\": \"anything\" }";

        RestAssured.given()
            .queryParam("mode", "vulnerable")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()
        .then()
            // Injection fails due to operator precedence, so we get 401
            .statusCode(401)
            .body("login", equalTo(false));
    }
	
    
    
    /**
     * This test PROVES the API is vulnerable.
     * SQL Injection in PASSWORD field bypasses authentication.
     * 
     * Generated SQL: SELECT * FROM users WHERE username = 'anything' AND password = '' OR '1'='1'
     * Due to operator precedence: (username='anything' AND password='') OR '1'='1'
     * = FALSE OR TRUE = TRUE → Returns ALL rows!
     */
    @Test
    public void sqlInjectionLoginShouldSucceed_InVulnerableMode() {
        // Inject in PASSWORD field - this works because OR '1'='1' is evaluated last
        String body = "{ \"username\": \"anything\", \"password\": \"' OR '1'='1\" }";

        RestAssured.given()
            .queryParam("mode", "vulnerable")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()  // Fixed: removed "/sql_test.php" as basePath already includes it
        .then()
            .statusCode(200)
            .body("login", equalTo(true))
            .body("row_count", greaterThanOrEqualTo(1))
            .body("constructed_sql", containsString("OR '1'='1"));
    }
	
    
    
    
    
    @Test
    public void normalLoginShouldSucceed_SecureMode() {
        String body = "{ \"username\": \"admin\", \"password\": \"admin123\" }";

        RestAssured.given()
            .queryParam("mode", "secure")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("login", equalTo(true))
            .body("message", equalTo("Login successful"))
            .body("row_count", greaterThanOrEqualTo(1));
    }

    /**
     * In secure mode, SQL injection should NOT work.
     * This test should PASS when mode=secure.
     */
    @Test
    public void sqlInjectionShouldNotLogin_SecureMode() {
        String body = "{ \"username\": \"' OR '1'='1\", \"password\": \"anything\" }";

        RestAssured.given()
            .queryParam("mode", "secure")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()
        .then()
            .statusCode(401)
            .body("login", equalTo(false))
            .body("row_count", equalTo(0));
    }

    /**
     * This test EXPECTS injection to succeed.
     * It will FAIL in secure mode (which is what we want to show).
     */
    @Test
    public void sqlInjectionLoginShouldSucceed_BUT_IT_WONT_SECURE() {
        String body = "{ \"username\": \"' OR '1'='1\", \"password\": \"anything\" }";

        RestAssured.given()
            .queryParam("mode", "secure")
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post()
        .then()
            // We (intentionally) expect vulnerable behavior here.
            // But secure mode returns 401 + login=false → TEST FAILS.
            .statusCode(200)
            .body("login", equalTo(true));
    }
}
