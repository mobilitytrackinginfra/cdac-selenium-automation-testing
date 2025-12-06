package parallel.db;

import org.testng.annotations.Test;

public class DBTest2 {

	@Test
	public void m1() {
		System.out.println("DBTest2 Method m1:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	@Test
	public void m2() {
		System.out.println("DBTest2 Method m2:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	@Test
	public void m3() {
		System.out.println("DBTest2 Method m3:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	@Test
	public void m4() {
		System.out.println("DBTest2 Method m4:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	public void sleep() {
		try {Thread.sleep(5000);} catch (InterruptedException e) {}
	}
}
