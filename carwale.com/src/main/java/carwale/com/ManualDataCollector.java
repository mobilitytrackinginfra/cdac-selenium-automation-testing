package carwale.com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import modals.Car;
import modals.Section;
import modals.Variant;

public class ManualDataCollector {

	private static WebDriver dr;
	private static WebDriverWait wt;
	private static final String VARIANTS_TABLE_XPATH =
			"//th[contains(.,'Variants')]/ancestor::table//tbody/tr | //th[contains(.,'Variants')]/ancestor::table//tbody/div";

	private static final String PATH = "D:\\cars\\";
	
	
	public static void main1(String[] args) throws Exception {
		printCar(loadCar(PATH+"Hyundai_Creta_1773038760068.ser"));
	}	
	
	public static void main(String[] args) throws Exception {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to("https://www.carwale.com/find-car/results/?budget=200000%2C20000000");
		dr = driver;
		wt = new WebDriverWait(driver, Duration.ofSeconds(15));
		
		while(true) {


			Car car = new Car();
			int totalVariants = getVariantRowsCount();
			car.setName(getElementText("//h1[@data-lang-id='car_overview_heading']"));
			List<Variant> variants = new ArrayList<>(totalVariants);
			car.setVariants(variants);
			
			for(int k=1;k<=totalVariants;k++) {
				
				
				((JavascriptExecutor) dr).executeScript(
						"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement("//div[contains(text(),'Contact')]"));
				Thread.sleep(300);
				
				if(isElementExist("//div[text()='View More Variants']")) {
					((JavascriptExecutor) dr).executeScript(
							"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement("//div[text()='View More Variants']"));
					Thread.sleep(300);
					getElement("//div[text()='View More Variants']").click();
				}
				
				
				
				clickElement("(//th[contains(text(),'Variants')]/ancestor::table//tbody//tr)["+k+"]//div[@title]/a");
				//	Thread.sleep(1000);
				Variant variant = new Variant();
				variants.add(variant);
				String title = getElementText("//h1");
				variant.setName(title);
				String price = getElementText("//div[contains(text(),'Rs.')]");
				variant.setPrice(price);
				System.out.println("Entered Car Variant "+k+"\t"+title);
				Thread.sleep(2000);
				
				((JavascriptExecutor) dr).executeScript(
						"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement("//h2[@data-skin='title' and contains(text(),'Other')]"));
				Thread.sleep(300);
				
				int specDivsCount = getElements("//h3[text()='Specifications']/../ul/div").size();
				
				List<Section> sections = new ArrayList<>();
				variant.setSections(sections);
				//Specifications
				for(int l=1;l<=specDivsCount;l++) {
					Section sec = new Section();
					sections.add(sec);
					if(l>2) {
						((JavascriptExecutor) dr).executeScript(
								"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement("//h3[text()='Specifications']/../ul/div["+l+"]"));
						Thread.sleep(300);
						String secName = getElementText("//h3[text()='Specifications']/../ul/div["+l+"]");
						sec.setSectionName(secName);
//						System.out.println("****Section "+secName);
						getElement("//h3[text()='Specifications']/../ul/div["+l+"]").click();
					} else {
						String secName = getElementText("//h3[text()='Specifications']/../ul/div["+l+"]");
						sec.setSectionName(secName);
//						System.out.println("****Section "+secName);
					}
					int rows = getElements("//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid]").size();
					Map<String, String> map = new LinkedHashMap<>();
					sec.setSpecs(map);
					for(int m=1;m<=rows;m++) {
						String key = getElementText("(//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid])["+m+"]/div[1]");
						String value = getElementText("(//h3[text()='Specifications']/../ul/div["+l+"]//div[@data-testid])["+m+"]/div[2]");
//						System.out.println(key+":"+value);
						map.put(key, value);
					}
				}
				
				
				//Features
				int features = getElements("//div[@data-index=\"1\"]//ul/div/li").size();
				for(int i=1;i<=features;i++) {
					Section sec = new Section();
					sections.add(sec);

					String secName = getElementText("(//div[@data-index=\"1\"]//ul/div/li)["+i+"]");
					sec.setSectionName(secName);
//					System.out.println("****Section "+secName);
					clickElement("(//div[@data-index=\"1\"]//ul/div/li)["+i+"]");
					int rows = getElements("(//div[@data-index=\"1\"]//ul/div/li)["+i+"]//li").size();
					Map<String, String> map = new LinkedHashMap<String, String>();
					sec.setSpecs(map);
					for(int j=1;j<=rows;j++) {
						String key = getElementText("((//div[@data-index=\"1\"]//ul/div/li)["+i+"]//li["+j+"]//div[@data-testid]/div)[1]");
						String val = getElementText("((//div[@data-index=\"1\"]//ul/div/li)["+i+"]//li["+j+"]//div[@data-testid]/div)[2]");
//						System.out.println(key+":"+val);
						map.put(key, val);
					}
				}

				printCar(car);
				driver.navigate().back();			
				Thread.sleep(1000);
			}	
			storeCar(car);

		}


		//	Thread.sleep(1000);
	}

	/**
	 * Serializes the car object to a file so it can be deserialized later with {@link #loadCar(String)}.
	 * @param car the car to store (must not be null)
	 * @param filePath path for the serialized file (e.g. "car_data.ser" or "saved/car.ser")
	 */
	public static void storeCar(Car car, String filePath) throws IOException {
		if (car == null) throw new IllegalArgumentException("car must not be null");
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
			oos.writeObject(car);
		}
	}

	/**
	 * Serializes the car to a default file name based on car name and timestamp.
	 */
	public static void storeCar(Car car) throws IOException {
		String safeName = car.getName() != null ? car.getName().replaceAll("[^a-zA-Z0-9]", "_") : "car";
		String filePath = safeName + "_" + System.currentTimeMillis() + ".ser";
		storeCar(car, PATH+filePath);
	}

	/**
	 * Deserializes a Car object from a file previously written by {@link #storeCar(Car, String)}.
	 * @param filePath path to the serialized file
	 * @return the restored Car object
	 */
	public static Car loadCar(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
			return (Car) ois.readObject();
		}
	}
	
	public static void printCar(Car car) {
		if (car == null) return;
		System.out.println("========== CAR: " + (car.getName() != null ? car.getName() : "(no name)") + " ==========");
		List<Variant> variants = car.getVariants();
		if (variants == null) return;
		for (int v = 0; v < variants.size(); v++) {
			Variant variant = variants.get(v);
			System.out.println("  --- Variant " + (v + 1) + ": " + (variant.getName() != null ? variant.getName() : "(no name)") + " ---");
			System.out.println("Price "+variant.getPrice());
			List<Section> sections = variant.getSections();
			if (sections == null) continue;
			for (Section sec : sections) {
				System.out.println("    ** Section: " + (sec.getSectionName() != null ? sec.getSectionName() : "(no name)") + " **");
				Map<String, String> specs = sec.getSpecs();
				if (specs != null) {
					for (Map.Entry<String, String> e : specs.entrySet()) {
						System.out.println("*      " + e.getKey() + " :$ " + (e.getValue() != null ? e.getValue() : ""));
					}
				}
			}
		}
		System.out.println("========================================");
	}
	
	
	public static WebElement getElement(String xpath) {
		return wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	public static boolean isElementExist(String xpath) {
		try {
			dr.findElement(By.xpath(xpath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** Waits for variants table to be present (not clickable) and returns row count. */
	public static int getVariantRowsCount() {
		WebDriverWait variantWait = new WebDriverWait(dr, Duration.ofSeconds(10));
		variantWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[contains(.,'Variants')]/ancestor::table//tbody")));
		return dr.findElements(By.xpath(VARIANTS_TABLE_XPATH)).size();
	}

	public static String getElementText(String xpath) {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		while(true) {
			try {return dr.findElement(By.xpath(xpath)).getText();} catch (Exception e) {}
		}
	}

	public static List<WebElement> getElements(String xpath) {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		return dr.findElements(By.xpath(xpath));
	}

	public static void clickElement(String xpath) throws InterruptedException {
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		((JavascriptExecutor) dr).executeScript(
				"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement(xpath));
		Thread.sleep(300);
		while(true) {
			try {dr.findElement(By.xpath(xpath)).click();break;} catch (Exception e) {e.printStackTrace();Thread.sleep(3000);}
		}
	}

}
