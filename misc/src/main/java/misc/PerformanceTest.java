package misc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class PerformanceTest {

	public static void main(String[] args) {
//		loginPerformance();
		parallelLogin();
	}
	
	public static void parallelLogin() {
		List<Runnable> tasks = new ArrayList<>();
		for(int i=0;i<10;i++) {
			Runnable task1 = () -> {
				login();
			};
			tasks.add(task1);
		}

		for(Runnable t: tasks) {
			new Thread(t).start();
		}
	}
	
	public static void loginPerformance() {
		login();
	}
	
	public static void login() {
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().window().maximize();
		driver.get("http://localhost/crm");
		
		By locatorUsername = By.id("field-userName");
		WebElement txtUsername = driver.findElement(locatorUsername);
		txtUsername.sendKeys("admin");

		By locatorPassword = By.name("password");
		WebElement txtPassword = driver.findElement(locatorPassword);
		txtPassword.sendKeys("admin");

		long start = System.currentTimeMillis();
		By locatorLogin = By.tagName("button");
		WebElement btnLogin = driver.findElement(locatorLogin);
		btnLogin.click();
		driver.findElement(By.cssSelector(".fa-building"));
		long end = System.currentTimeMillis();
		
		System.out.println("Login Operation Time in Milliseconds:"+(end-start));
		
		driver.quit();
	}
}
