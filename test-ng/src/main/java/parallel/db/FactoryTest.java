package parallel.db;

import org.testng.annotations.Factory;

import ui.UITest1;


public class FactoryTest {

	@Factory
	public Object[] testCases() {
		return new Object[] {
				new db.DBTest1(),
				new UITest1()
	        };
	}
}
