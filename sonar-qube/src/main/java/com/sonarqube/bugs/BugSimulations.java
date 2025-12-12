package com.sonarqube.bugs;

import java.io.*;
import java.util.*;
import java.sql.*;

/**
 * This class demonstrates various BUG patterns that SonarQube can detect.
 * These are issues that represent clearly wrong code or code that will cause unexpected behavior.
 */
public class BugSimulations {

    // ================== NULL POINTER DEREFERENCE ==================
    
    // Bug: Null pointer dereference - using object without null check
    public void nullPointerDereference(String input) {
        String result = input.toUpperCase(); // Potential NPE if input is null
        System.out.println(result);
    }

    // Bug: Guaranteed null pointer dereference
    public void guaranteedNullPointer() {
        String str = null;
        int length = str.length(); // NPE guaranteed
        System.out.println(length);
    }

    // Bug: Null check after dereference (too late)
    public void nullCheckAfterDereference(Object obj) {
        int hash = obj.hashCode();
        if (obj != null) { // Useless - already dereferenced above
            System.out.println(hash);
        }
    }

    // ================== RESOURCE LEAKS ==================
    
    // Bug: Resource leak - stream not closed
    public void resourceLeak(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        int data = fis.read();
        System.out.println(data);
        // fis is never closed - resource leak!
    }

    // Bug: Resource leak in exception case
    public void resourceLeakOnException(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine(); // If this throws, reader is never closed
        reader.close();
        System.out.println(line);
    }

    // Bug: Connection leak
    public void connectionLeak(String url) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1");
        // Resources not closed!
    }

    // ================== COMPARISON ISSUES ==================
    
    // Bug: Comparing strings with == instead of equals()
    public boolean stringComparisonWithOperator(String a, String b) {
        return a == b; // Should use equals()
    }

    // Bug: Comparing boxed types with ==
    public boolean boxedTypeComparison(Integer a, Integer b) {
        return a == b; // Should use equals() for Integer comparison
    }

    // Bug: Incompatible comparison
    public void incompatibleComparison() {
        Integer num = 100;
        String str = "100";
        boolean result = num.equals(str); // Always false - incompatible types
        System.out.println(result);
    }

    // ================== ARRAY/COLLECTION ISSUES ==================
    
    // Bug: Array index out of bounds
    public void arrayIndexOutOfBounds() {
        int[] arr = new int[5];
        for (int i = 0; i <= 5; i++) { // Bug: i <= 5 should be i < 5
            arr[i] = i;
        }
    }

    // Bug: Empty array accessed
    public void emptyArrayAccess() {
        int[] arr = new int[0];
        int first = arr[0]; // ArrayIndexOutOfBoundsException
        System.out.println(first);
    }

    // Bug: Collection modified during iteration
    public void concurrentModification(List<String> list) {
        for (String item : list) {
            if (item.isEmpty()) {
                list.remove(item); // ConcurrentModificationException
            }
        }
    }

    // ================== MATHEMATICAL ISSUES ==================
    
    // Bug: Division by zero
    public int divisionByZero(int a) {
        int divisor = 0;
        return a / divisor; // ArithmeticException
    }

    // Bug: Integer overflow
    public long integerOverflow(int a, int b) {
        return a * b; // Potential overflow before cast to long
    }

    // Bug: Loss of precision
    public void lossOfPrecision() {
        long bigValue = Long.MAX_VALUE;
        int smallValue = (int) bigValue; // Loss of precision
        System.out.println(smallValue);
    }

    // ================== SYNCHRONIZATION ISSUES ==================
    
    private int counter = 0;
    
    // Bug: Non-synchronized access to shared variable
    public void incrementCounter() {
        counter++; // Not thread-safe
    }

    // Bug: Double-checked locking without volatile
    private Object instance;
    
    public Object getInstance() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = new Object(); // Broken double-checked locking
                }
            }
        }
        return instance;
    }

    // Bug: Synchronizing on mutable field
    private Object lock = new Object();
    
    public void synchronizeOnMutableField() {
        synchronized (lock) { // lock can be reassigned
            // critical section
        }
        lock = new Object(); // Reassigning lock object
    }

    // ================== CONTROL FLOW ISSUES ==================
    
    // Bug: Infinite loop
    public void infiniteLoop() {
        int i = 0;
        while (i < 10) {
            System.out.println(i);
            // i is never incremented - infinite loop
        }
    }

    // Bug: Unreachable code after return
    public int unreachableCode() {
        return 42;
        // System.out.println("Unreachable"); // This would be unreachable
    }

    // Bug: Dead store - value never used
    public void deadStore() {
        int x = 10;
        x = 20; // Previous value never used
        System.out.println(x);
    }

    // Bug: Condition always true/false
    public void constantCondition() {
        int x = 10;
        if (x > 5) { // Always true
            System.out.println("Always executes");
        }
    }

    // ================== EQUALS/HASHCODE ISSUES ==================
    
    // Bug: equals() without hashCode()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return true;
    }
    // Missing hashCode() override!

    // ================== EXCEPTION HANDLING ISSUES ==================
    
    // Bug: Catching Throwable
    public void catchThrowable() {
        try {
            riskyOperation();
        } catch (Throwable t) { // Should not catch Throwable
            System.out.println("Error");
        }
    }

    // Bug: Ignoring InterruptedException
    public void ignoreInterruptedException() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignoring interruption - bug!
        }
    }

    // Bug: Re-throwing without cause
    public void rethrowWithoutCause() throws Exception {
        try {
            riskyOperation();
        } catch (Exception e) {
            throw new Exception("Error occurred"); // Original cause lost
        }
    }

    private void riskyOperation() throws Exception {
        throw new Exception("Risky");
    }

    // ================== SERIALIZATION ISSUES ==================
    
    // Bug: Serializable class without serialVersionUID
    static class SerializableWithoutUID implements Serializable {
        private String data;
        // Missing: private static final long serialVersionUID = 1L;
    }

    // Bug: Non-transient non-serializable field
    static class BadSerialization implements Serializable {
        private static final long serialVersionUID = 1L;
        private Thread thread; // Thread is not serializable
    }
}

