package lab;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lab6Cookies {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/suitecrm/public/");
		
		Cookie cookie1 = new Cookie("test", "selenium");
		driver.manage().addCookie(cookie1);
		
		Cookie newCookie = driver.manage().getCookieNamed("test");
		System.out.println("****New Cookie "+newCookie);
		
		Set<Cookie> cookies = driver.manage().getCookies();
		System.out.println("****All Cookies");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}
		
		System.out.println("****Deleted Cookie");
		driver.manage().deleteCookie(cookie1);

		cookies = driver.manage().getCookies();
		System.out.println("****All Cookies After Delete");
		for(Cookie cookie : cookies) {
			System.out.println(cookie);			
		}

		driver.manage().deleteCookieNamed("XSRF-TOKEN");
		System.out.println("****Deleted Cookie");
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
