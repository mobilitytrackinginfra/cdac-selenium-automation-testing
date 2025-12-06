package lab;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CLab26ScriptLoadTimeout {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		System.out.println("Before Set: "+driver.manage().timeouts().getScriptTimeout().getSeconds());
		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
		System.out.println("After Set: "+driver.manage().timeouts().getScriptTimeout().getSeconds());
		driver.get("http://localhost/samples/timeout-test.php?mode=fast");
		
		
        System.out.println("Running async script that resolves after 10s with a 5s scriptTimeout...");
        ((JavascriptExecutor) driver).executeAsyncScript(
            "const done = arguments[arguments.length - 1];" +
            "window.TimeoutDemo.resolveAfter(10000).then(() => done('resolved'));"
        );
        System.out.println("Unexpected: async script finished within 5s.");
		
		
		Thread.sleep(Duration.ofSeconds(3));
		driver.quit();

	}
}
