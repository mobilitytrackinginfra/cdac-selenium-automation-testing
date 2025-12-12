package com.sonarqube.bugs;

import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;

/**
 * Additional bug patterns that SonarQube can detect.
 */
public class MoreBugs {

    // ================== STRING ISSUES ==================
    
    // Bug: String.replace used with regex intent
    public String incorrectReplace(String input) {
        return input.replace(".", "-"); // . is literal, not regex
    }

    // Bug: Inefficient string concatenation in loop
    public String inefficientConcatenation(List<String> items) {
        String result = "";
        for (String item : items) {
            result = result + item; // Should use StringBuilder
        }
        return result;
    }

    // Bug: Using == for string literal comparison
    public boolean literalStringComparison(String input) {
        return input == "test"; // Should use equals()
    }

    // ================== DATE/TIME ISSUES ==================
    
    // Bug: SimpleDateFormat not thread-safe when shared
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public String formatDate(Date date) {
        return DATE_FORMAT.format(date); // Not thread-safe
    }

    // Bug: Calendar.getInstance() vs new GregorianCalendar()
    public void calendarIssue() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, 13, 1); // Invalid month (should be 0-11)
    }

    // ================== REGULAR EXPRESSION ISSUES ==================
    
    // Bug: Regex denial of service (ReDoS)
    public boolean vulnerableRegex(String input) {
        return input.matches("(a+)+$"); // Catastrophic backtracking
    }

    // Bug: Invalid regex pattern
    public void invalidRegex() {
        String pattern = "["; // Invalid regex
        "test".matches(pattern);
    }

    // ================== CLONING ISSUES ==================
    
    // Bug: clone() without implementing Cloneable
    public Object badClone() throws CloneNotSupportedException {
        return super.clone(); // Class doesn't implement Cloneable
    }

    // ================== REFLECTION ISSUES ==================
    
    // Bug: Reflection used to access private fields
    public void reflectionAccess(Object obj) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField("secret");
        field.setAccessible(true); // Security risk
        Object value = field.get(obj);
        System.out.println(value);
    }

    // ================== FINALIZATION ISSUES ==================
    
    // Bug: Relying on finalize()
    @Override
    protected void finalize() throws Throwable {
        // Deprecated and unreliable
        System.out.println("Finalizing");
        super.finalize();
    }

    // ================== NUMERIC ISSUES ==================
    
    // Bug: Float/double equality comparison
    public boolean floatEquality(float a, float b) {
        return a == b; // Unreliable for floating point
    }

    // Bug: Using float for currency
    public float calculateTotal(float price, int quantity) {
        return price * quantity; // Should use BigDecimal for money
    }

    // Bug: BigDecimal created from double
    public BigDecimal badBigDecimal() {
        return new BigDecimal(0.1); // Imprecise - should use String constructor
    }

    // Bug: Random number in wrong range
    public int randomInRange(int max) {
        Random random = new Random();
        return random.nextInt() % max; // Can be negative!
    }

    // ================== COLLECTION ISSUES ==================
    
    // Bug: Checking size before isEmpty
    public boolean checkEmpty(List<String> list) {
        return list.size() == 0; // Should use isEmpty()
    }

    // Bug: Using raw types
    public void rawTypes() {
        List list = new ArrayList(); // Raw type - no generics
        list.add("string");
        list.add(123);
    }

    // Bug: Returning null instead of empty collection
    public List<String> getItems(boolean hasItems) {
        if (hasItems) {
            return Arrays.asList("item1", "item2");
        }
        return null; // Should return empty collection
    }

    // Bug: Using contains() with different type
    public boolean containsWrongType(Set<Integer> set, String value) {
        return set.contains(value); // Always false - wrong type
    }

    // ================== METHOD ISSUES ==================
    
    // Bug: Ignoring method return value
    public void ignoreReturnValue(String str) {
        str.trim(); // Return value ignored
        str.toLowerCase(); // Return value ignored
        System.out.println(str);
    }

    // Bug: Optional.get() without isPresent() check
    public String unsafeOptional(Optional<String> opt) {
        return opt.get(); // NoSuchElementException if empty
    }

    // Bug: hashCode() on array
    public int arrayHashCode(int[] arr) {
        return arr.hashCode(); // Returns identity hash, not content hash
    }

    // Bug: toString() on array
    public String arrayToString(int[] arr) {
        return arr.toString(); // Returns something like "[I@hashcode"
    }

    // ================== I/O ISSUES ==================
    
    // Bug: File.delete() return value ignored
    public void deleteFile(String path) {
        File file = new File(path);
        file.delete(); // Return value ignored - may fail silently
    }

    // Bug: mkdirs() return value ignored
    public void createDirectory(String path) {
        File dir = new File(path);
        dir.mkdirs(); // May fail silently
    }

    // ================== LOOP ISSUES ==================
    
    // Bug: Off-by-one error
    public void offByOne(int[] arr) {
        for (int i = 1; i <= arr.length; i++) { // Should start at 0, use <
            System.out.println(arr[i]);
        }
    }

    // Bug: Same expression on both sides of operator
    public boolean redundantComparison(int x) {
        return x == x; // Always true (unless NaN for floats)
    }

    // Bug: Assignment in conditional
    public void assignmentInCondition(int x) {
        int y;
        if ((y = x) > 0) { // Confusing - was == intended?
            System.out.println(y);
        }
    }

    // ================== MISCELLANEOUS BUGS ==================
    
    // Bug: System.exit() called
    public void callSystemExit() {
        System.exit(1); // Should not be called in library code
    }

    // Bug: Runtime.getRuntime().exec() without checking
    public void executeCommand(String cmd) throws IOException {
        Runtime.getRuntime().exec(cmd); // Return value/errors ignored
    }

    // Bug: Thread.run() instead of Thread.start()
    public void wrongThreadStart() {
        Thread thread = new Thread(() -> System.out.println("Running"));
        thread.run(); // Should call start() to run in new thread
    }

    // Bug: Waiting on wrong object
    public void waitOnWrongObject() throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();
        synchronized (lock1) {
            lock2.wait(); // Waiting on lock2 while holding lock1
        }
    }
}

