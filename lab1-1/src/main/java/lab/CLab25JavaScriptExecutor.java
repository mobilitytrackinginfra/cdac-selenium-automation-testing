package lab;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CLab25JavaScriptExecutor {
    public static void main(String[] args) throws Exception {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--remote-allow-origins=*"); // sometimes needed with newer chrome-driver combos
        WebDriver driver = new ChromeDriver(opts);

        driver.get("http://localhost/samples/jsexecutor.php");
        Thread.sleep(Duration.ofSeconds(5));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // -----------------------
        // 1) Simple sync executeScript: read title, URL, return text
        // -----------------------
        Object title = js.executeScript("return document.title;");
        System.out.println("Title: " + title);

        Object url = js.executeScript("return window.location.href;");
        System.out.println("URL: " + url);

        // -----------------------
        // 2) Set input value (direct DOM), pass element as argument
        // -----------------------
        WebElement input = driver.findElement(By.id("textInput"));
        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                input, "set-from-js");
        System.out.println("Input value after set: " + input.getAttribute("value"));

        
        Thread.sleep(Duration.ofSeconds(5));
        driver.quit();
        
    }
}
