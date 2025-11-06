package lab;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ALab6Cookies {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/crm");
		
		Cookie cookie1 = new Cookie("module1", "selenium");
		driver.manage().addCookie(cookie1);

		Cookie cookie2 = new Cookie("module2", "java");
		driver.manage().addCookie(cookie2);

		Cookie cookie3 = new Cookie("module3", "api");
		driver.manage().addCookie(cookie3);

		Cookie cookie4 = new Cookie("module4", "database");
		driver.manage().addCookie(cookie4);

		Cookie newCookie = driver.manage().getCookieNamed("module1");
		System.out.println("****New Cookie "+newCookie);
		
		Set<Cookie> cookies = driver.manage().getCookies();
		System.out.println("****All Cookies");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}
		
		System.out.println("****Deleted Cookie module1");
		driver.manage().deleteCookie(cookie1);

		cookies = driver.manage().getCookies();
		System.out.println("****All Cookies After Delete");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}

		driver.manage().deleteCookieNamed("module2");
		System.out.println("****Deleted Cookie by Name module2");
		cookies = driver.manage().getCookies();
		System.out.println("****All Cookies After Delete");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}
		
		driver.manage().deleteAllCookies();
		System.out.println("****Deleted All Cookies");
		
		cookies = driver.manage().getCookies();
		System.out.println("****All Cookies After Delete All");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}
		
		
		Thread.sleep(Duration.ofSeconds(5));
		driver.quit();

	}
}
