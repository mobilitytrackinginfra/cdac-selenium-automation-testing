package test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;

public class TestXSS {

	@BeforeClass
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 80; // adjust if needed
        RestAssured.basePath = "/api-demo/api/xss_api.php";
    }

    /**
     * Test: Vulnerable mode should reflect raw script.
     * This demonstrates a FINDING (we expect to see &lt;script&gt;).
     */
    @Test
    public void reflectedXss_VulnerableMode_ShouldContainRawScript() {
        String payload = "<script>alert('XSS')</script>";

        given()
            .queryParam("type", "reflect")
            .queryParam("mode", "vulnerable")
            .contentType(ContentType.JSON)
            .body("{\"input\":\"" + payload.replace("\"", "\\\"") + "\"}")
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("output", containsString("<script>alert('XSS')</script>"))
            .body("xss_possible", equalTo(true));
    }

    /**
     * Test: Secure mode should NOT contain raw <script> and should escape it.
     */
    @Test
    public void reflectedXss_SecureMode_ShouldEscapeScript() {
        String payload = "<script>alert('XSS')</script>";

        given()
            .queryParam("type", "reflect")
            .queryParam("mode", "secure")
            .contentType(ContentType.JSON)
            .body("{\"input\":\"" + payload.replace("\"", "\\\"") + "\"}")
        .when()
            .post()
        .then()
            .statusCode(200)
            .body("output", not(containsString("<script>")))
            .body("output", containsString("&lt;script&gt;"))
            .body("xss_possible", equalTo(false));
    }

    /**
     * Test (intentionally wrong expectation) – it assumes secure behavior in vulnerable mode.
     * This will FAIL and you can show it in lecture as: "Test says secure, but app is still vulnerable".
     */
    @Test
    public void reflectedXss_VulnerableMode_ShouldNOTAllowScript_BUT_IT_DOES() {
        String payload = "<script>alert('XSS')</script>";

        given()
            .queryParam("type", "reflect")
            .queryParam("mode", "vulnerable")
            .contentType(ContentType.JSON)
            .body("{\"input\":\"" + payload.replace("\"", "\\\"") + "\"}")
        .when()
            .post()
        .then()
            // Expectation of security (401 or encoded) – but actual is 200 + raw script.
            .statusCode(200)
            .body("output", not(containsString("<script>"))); // → This will fail
    }
}
