package carwale.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	
	
	public static void mainxc(String[] args) throws Exception {
		Set<String> set = new LinkedHashSet<>();
		Set<String> values = new LinkedHashSet<>();
		
		File dir = new File(PATH);
		if (!dir.isDirectory()) {
			System.err.println("PATH is not a directory: " + PATH);
			return;
		}
		File[] files = dir.listFiles((d, name) -> name != null && name.endsWith(".ser"));
		if (files == null || files.length == 0) {
			System.out.println("No .ser files found in " + PATH);
			return;
		}
		List<Car> cars = new ArrayList<>();
		for (File f : files) {
			try {
				Car car = loadCar(f.getAbsolutePath());
				cars.add(car);
//				System.out.println("Loaded: " + f.getName() + " -> " + (car.getName() != null ? car.getName() : "(no name)"));
			} catch (Exception e) {
				System.err.println("Failed to load " + f.getName() + ": " + e.getMessage());
			}
		}
		System.out.println("Total cars deserialized: " + cars.size());
		int var = 0;
		for (Car car : cars) {
			var = var + car.getVariants().size();
			System.out.println(car.getName());
			for(Variant v : car.getVariants()) {
				for(Section s : v.getSections()) {
					set.addAll(s.getSpecs().keySet());
					for(String key : s.getSpecs().keySet()) {
						values.add(s.getSpecs().get(key));
					}
				}
			}
		}
		System.out.println("****************************");
		Iterator<String> itr = values.iterator();
		for(String key : set) {
			System.out.println(key+":"+itr.next());
		}
		
		System.out.println("Total Variants "+var);
	}

	/**
	 * Reads all Car objects from PATH (deserialized .ser files), then writes a CSV file
	 * with columns: Car Name, Variant Name, Price, and every spec key (one column per key).
	 * One row per variant; spec values are merged from all sections of that variant.
	 */
	public static void main(String[] args) throws Exception {
		// 1. Load all cars from PATH (same as main)
		File dir = new File(PATH);
		if (!dir.isDirectory()) {
			System.err.println("PATH is not a directory: " + PATH);
			return;
		}
		File[] files = dir.listFiles((d, name) -> name != null && name.endsWith(".ser"));
		if (files == null || files.length == 0) {
			System.out.println("No .ser files found in " + PATH);
			return;
		}
		List<Car> cars = new ArrayList<>();
		for (File f : files) {
			try {
				Car car = loadCar(f.getAbsolutePath());
				cars.add(car);
			} catch (Exception e) {
				System.err.println("Failed to load " + f.getName() + ": " + e.getMessage());
			}
		}
		if (cars.isEmpty()) {
			System.out.println("No cars loaded. Skipping CSV export.");
			return;
		}
		// 2. Collect all unique spec keys (column headers)
		Set<String> allSpecKeys = new TreeSet<>();
		for (Car car : cars) {
			if (car.getVariants() == null) continue;
			for (Variant v : car.getVariants()) {
				if (v.getSections() == null) continue;
				for (Section s : v.getSections()) {
					if (s.getSpecs() != null) allSpecKeys.addAll(s.getSpecs().keySet());
				}
			}
		}
		List<String> specColumns = new ArrayList<>(allSpecKeys);
		// 3. Write CSV
		String csvPath = PATH + "cars_export.csv";
		try (PrintWriter out = new PrintWriter(new FileWriter(csvPath))) {
			// Header: Car Name, Variant Name, Price, Luxury, then all spec keys
			out.print(escapeCsv("Car Name"));
			out.print(",");
			out.print(escapeCsv("Variant Name"));
			out.print(",");
			out.print(escapeCsv("Price"));
			out.print(",");
			out.print(escapeCsv("Luxury"));
			for (String key : specColumns) {
				out.print(",");
				out.print(escapeCsv(key));
			}
			out.println();
			// Rows: one per variant
			for (Car car : cars) {
				String carName = car.getName() != null ? car.getName() : "";
				if (car.getVariants() == null) continue;
				for (Variant v : car.getVariants()) {
					String variantName = v.getName() != null ? v.getName() : "";
					String price = v.getPrice() != null ? v.getPrice() : "";
					// Merge all specs from all sections of this variant
					Map<String, String> rowSpecs = new LinkedHashMap<>();
					if (v.getSections() != null) {
						for (Section s : v.getSections()) {
							if (s.getSpecs() != null) rowSpecs.putAll(s.getSpecs());
						}
					}
					out.print(escapeCsv(carName));
					out.print(",");
					out.print(escapeCsv(variantName));
					out.print(",");
					out.print(escapeCsv(price));
					out.print(",");
					out.print(escapeCsv(isLuxury(carName, price) ? "Yes" : "No"));
					for (String key : specColumns) {
						out.print(",");
						out.print(escapeCsv(rowSpecs.getOrDefault(key, "")));
					}
					out.println();
				}
			}
		}
		System.out.println("CSV written to " + csvPath + " (" + cars.size() + " cars).");
	}

	/** Returns true if the car is considered luxury (by brand or price). */
	private static boolean isLuxury(String carName, String price) {
		if (carName == null) carName = "";
		if (price == null) price = "";
		String name = carName.trim();
		String p = price.trim();
		// Luxury brands (car name typically starts with brand)
		String[] luxuryBrands = {
			"Aston Martin", "Audi", "BMW", "Mercedes-Benz", "Mercedes", "Jaguar", "Lexus",
			"Land Rover", "Rolls-Royce", "Bentley", "Porsche", "Volvo", "Mini", "Genesis",
			"Maserati", "Lamborghini", "Ferrari", "McLaren", "Bugatti", "Maybach"
		};
		for (String brand : luxuryBrands) {
			if (name.startsWith(brand)) return true;
		}
		// Price in Crore = luxury
		if (p.contains("Crore")) return true;
		// Price >= 40 Lakh (e.g. "Rs. 44.45 Lakh" or "Rs. 44.45 Lakh\nonwards")
		if (p.contains("Lakh")) {
			java.util.regex.Pattern pat = java.util.regex.Pattern.compile("([0-9]+(?:\\.[0-9]+)?)\\s*Lakh");
			java.util.regex.Matcher m = pat.matcher(p);
			if (m.find()) {
				try {
					double lakh = Double.parseDouble(m.group(1));
					if (lakh >= 40) return true;
				} catch (NumberFormatException ignored) { }
			}
		}
		return false;
	}

	private static String escapeCsv(String value) {
		if (value == null) return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}

	public static void main2(String[] args) throws Exception {
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
				String price = getElementText("//span[contains(text(),'Rs.')]");
				variant.setPrice(price);
				System.out.println("Entered Car Variant "+k+"\t"+title);
				Thread.sleep(2000);
				
				((JavascriptExecutor) dr).executeScript(
						"arguments[0].scrollIntoView({behavior:'instant',block:'center',inline:'center'});", getElement("//h2[@data-skin='title' and contains(text(),'Other') or contains(text(),'Alternatives')]"));
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

//				printCar(car);
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
