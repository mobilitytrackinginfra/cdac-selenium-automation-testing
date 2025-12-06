package parallel.ui;

import org.testng.annotations.Test;

public class UITest1 {

	@Test
	public void m1() {
		System.out.println("UITest1 Method m1:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	@Test
	public void m2() {
		System.out.println("UITest1 Method m2:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}

	@Test
	public void m3() {
		System.out.println("UITest1 Method m3:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	@Test
	public void m4() {
		System.out.println("UITest1 Method m4:"+Thread.currentThread().getName()+"\t"+this);
		sleep();
	}
	
	public void sleep() {
		try {Thread.sleep(5000);} catch (InterruptedException e) {}
	}
}
