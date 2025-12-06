package dataprovider;

import org.testng.annotations.DataProvider;

public class DPClass {

	@DataProvider(name = "dp2")
	public String[] getData() {
		return new String[]{"madam", "sir", "abc", "racecar"};
	}
}
