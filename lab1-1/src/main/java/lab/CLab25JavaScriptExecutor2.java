package lab;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScriptKey;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CLab25JavaScriptExecutor2 {
    public static void main(String[] args) throws Exception {
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--remote-allow-origins=*"); // sometimes needed with newer chrome-driver combos
        WebDriver driver = new ChromeDriver(opts);

        driver.get("http://localhost/samples/jsexecutor.php");
        Thread.sleep(Duration.ofSeconds(5));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        
        ScriptKey getTextKey = js.pin("return arguments[0].innerText;");
        
        Set<ScriptKey> pinned = js.getPinnedScripts();
        System.out.println("Pinned scripts count: " + pinned.size());
        for(ScriptKey key : pinned) {
        	System.out.println("Script Identifier "+key.getIdentifier());
        }
        
        WebElement itemsUl = driver.findElement(By.id("items"));
        List<WebElement> lis = itemsUl.findElements(By.tagName("li"));
        WebElement firstLi = lis.get(1);

        Object firstText = js.executeScript(getTextKey, firstLi);
        System.out.println("First <li> text (pinned): " + firstText);
        
        js.unpin(getTextKey);
        
        try {
        	js.executeScript(getTextKey, firstLi);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Thread.sleep(Duration.ofSeconds(5));
        driver.quit();
        
    }
}
