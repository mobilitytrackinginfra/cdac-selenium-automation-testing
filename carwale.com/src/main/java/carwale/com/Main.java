package carwale.com;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
	private static WebDriver dr;
	private static WebDriverWait wt;
	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to("https://www.carwale.com/find-car/results/?budget=200000%2C20000000");
		Thread.sleep(5000);
		dr = driver;
		wt = new WebDriverWait(driver, Duration.ofSeconds(10));

		for(int j=1;j<=14;j++) {
			int size = j==1?17:16;
			for(int i=1;i<=size;i++) {
								
				// Click the parent <a> so navigation always triggers
				WebElement carLink = getElement("(//ul[@data-section-id='model-list-section']//h3)["+i+"]");
				String name = getCarNameFromLink(carLink);
				System.out.println(name);
				clickElementSafe(getElement("(//ul[@data-section-id='model-list-section']//h3)["+i+"]"));
				if (!waitForDetailPage()) {
					System.out.println("Detail page did not load for: " + name + ", skipping.");
					if (dr.findElements(By.xpath("//ul[@data-section-id='model-list-section']")).isEmpty()) {
						driver.navigate().back();
						waitForListPage();
					}
					continue;
				}
				if(isElementExist("//div[text()='View More Variants']")) {
					clickElementSafe(getElement("//div[text()='View More Variants']"));
				}
				int totalVariants = getVariantRowsCount();
				for(int k=1;k<=totalVariants;k++) {
					continue;
//					if(k!=1) {
//						clickElement("//div[text()='View More Variants']");
//					}
//					clickElement("//th[contains(text(),'Variants')]/ancestor::table//tbody/tr["+k+"]//div[@title]");
////					Thread.sleep(1000);
//					String title = getElementText("//h1");
//					System.out.println("Entered Car Variant "+k+"\t"+title);
//					int specDivsCount = getElements("//h3[text()='Specifications']/../ul/div").size();
//					for(int l=1;l<=specDivsCount;l++) {
//						if(l>2) {
//							getElement("//h3[text()='Specifications']/../ul/div["+l+"]").click();
//						}
//						int rows = getElements("//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid]").size();
//						for(int m=1;m<=rows;m++) {
//							String key = getElementText("(//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid])["+m+"]/div[1]");
//							String value = getElementText("(//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid])["+m+"]/div[2]");
//							System.out.println(key+":"+value);
//
//						}
//					}
//					driver.navigate().back();
////					Thread.sleep(1000);
				}
				driver.navigate().back();
				if(j!=1) {
					clickElementSafe(getElement("//a[text()='"+j+"']"));
					Thread.sleep(1000);
				}
			}
			if(j<14) {
				clickElementSafe(getElement("//a[text()='"+(j+1)+"']"));
				Thread.sleep(1000);
			}
		}

		Thread.sleep(5000);
		driver.quit();
	}
	
	public static boolean isElementExist(String xpath) {
		try {
			dr.findElement(By.xpath(xpath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static WebElement getElement(String xpath) {
		return wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	private static final String VARIANTS_TABLE_XPATH =
		"//th[contains(.,'Variants')]/ancestor::table//tbody/tr | //th[contains(.,'Variants')]/ancestor::table//tbody/div";

	/** Gets car name from link: h3 if present, otherwise the link's own text. */
	public static String getCarNameFromLink(WebElement carLink) {
		try {
			WebElement h3 = carLink.findElement(By.xpath(".//h3"));
			String t = h3.getText();
			if (t != null && !t.isBlank()) return t.trim();
		} catch (Exception ignored) { }
		String t = carLink.getText();
		return (t != null && !t.isBlank()) ? t.trim() : "Unknown";
	}

	/** Waits for car detail page to load (Variants section or "View More Variants"). Returns true if loaded in time. */
	public static boolean waitForDetailPage() {
		WebDriverWait detailWait = new WebDriverWait(dr, Duration.ofSeconds(20));
		try {
			detailWait.until(d -> {
				if (dr.findElements(By.xpath("//th[contains(.,'Variants')]")).size() > 0) return true;
				if (dr.findElements(By.xpath("//div[text()='View More Variants']")).size() > 0) return true;
				return false;
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** Waits for list page to be ready after navigate.back(). */
	public static void waitForListPage() {
		WebDriverWait backWait = new WebDriverWait(dr, Duration.ofSeconds(15));
		backWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@data-section-id='model-list-section']")));
	}

	/** Waits for variants table to be present (not clickable) and returns row count. */
	public static int getVariantRowsCount() {
		WebDriverWait variantWait = new WebDriverWait(dr, Duration.ofSeconds(10));
		variantWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[contains(.,'Variants')]/ancestor::table//tbody")));
		return dr.findElements(By.xpath(VARIANTS_TABLE_XPATH)).size();
	}

	/** Clicks an element after scrolling it into view; uses Actions or JS click if normal click is intercepted. */
	public static void clickElementSafe(WebElement element) {
		// Scroll element into view (center of viewport) so it's not covered or off-screen
		((JavascriptExecutor) dr).executeScript(
			"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", element);
		try {
			Thread.sleep(300); // brief pause after scroll for layout
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		try {
			element.click();
		} catch (org.openqa.selenium.ElementClickInterceptedException e) {
			try {
				new Actions(dr).moveToElement(element).click().perform();
			} catch (Exception e2) {
				((JavascriptExecutor) dr).executeScript("arguments[0].click();", element);
			}
		}
	}
	
	public static String getElementText(String xpath) {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		while(true) {
			try {return dr.findElement(By.xpath(xpath)).getText();} catch (Exception e) {}
		}
	}
	
	public static void clickElement(String xpath) throws InterruptedException {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		while(true) {
			try {dr.findElement(By.xpath(xpath)).click();break;} catch (Exception e) {e.printStackTrace();Thread.sleep(3000);}
		}
	}
	
	public static List<WebElement> getElements(String xpath) {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		return dr.findElements(By.xpath(xpath));
	}
}
