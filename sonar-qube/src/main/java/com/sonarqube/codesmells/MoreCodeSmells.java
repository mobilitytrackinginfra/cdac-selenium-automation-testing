package com.sonarqube.codesmells;

import java.util.*;
import java.io.*;

/**
 * Additional code smell patterns that SonarQube can detect.
 */
public class MoreCodeSmells {

    // ================== CLASS DESIGN ISSUES ==================
    
    // Code Smell: Utility class with public constructor
    public static class UtilityClass {
        // Should have private constructor
        public static String helper() {
            return "help";
        }
    }

    // Code Smell: Empty class
    public static class EmptyClass {
        // Class with no members
    }

    // Code Smell: Class with only static members (should be utility class)
    public static class ShouldBeUtility {
        public static int VALUE = 10;
        public static void process() {
            System.out.println(VALUE);
        }
        // Has implicit public constructor but only static members
    }

    // Code Smell: Too many fields
    public static class TooManyFields {
        private String field1;
        private String field2;
        private String field3;
        private String field4;
        private String field5;
        private String field6;
        private String field7;
        private String field8;
        private String field9;
        private String field10;
        private String field11;
        private String field12;
        private String field13;
        private String field14;
        private String field15;
        private int field16;
        private int field17;
        private int field18;
        private int field19;
        private int field20;
    }

    // ================== INHERITANCE ISSUES ==================
    
    // Code Smell: Calling overridable method in constructor
    public static class BadConstructor {
        public BadConstructor() {
            initialize(); // Calling overridable method in constructor
        }
        
        public void initialize() {
            System.out.println("Initializing");
        }
    }

    // Code Smell: Deep inheritance hierarchy
    static class Level1 {}
    static class Level2 extends Level1 {}
    static class Level3 extends Level2 {}
    static class Level4 extends Level3 {}
    static class Level5 extends Level4 {}
    static class Level6 extends Level5 {} // Too deep!

    // ================== EXCEPTION HANDLING SMELLS ==================
    
    // Code Smell: Generic exception caught
    public void catchGenericException() {
        try {
            riskyOperation();
        } catch (Exception e) { // Too generic
            e.printStackTrace();
        }
    }

    // Code Smell: Generic exception thrown
    public void throwGenericException() throws Exception { // Too generic
        throw new Exception("Error");
    }

    // Code Smell: Exception used for flow control
    public boolean exceptionForFlowControl(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false; // Using exception for normal flow
        }
    }

    // Code Smell: Logging and rethrowing
    public void logAndRethrow() throws Exception {
        try {
            riskyOperation();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage()); // Logging
            throw e; // And rethrowing - choose one
        }
    }

    // Code Smell: Returning in finally
    public int returnInFinally() {
        try {
            return 1;
        } finally {
            return 2; // Overrides the try block return
        }
    }

    // ================== SWITCH STATEMENT SMELLS ==================
    
    // Code Smell: Switch without default
    public String switchWithoutDefault(int code) {
        switch (code) {
            case 1:
                return "One";
            case 2:
                return "Two";
            // Missing default case
        }
        return "";
    }

    // Code Smell: Switch with fallthrough
    public void switchWithFallthrough(int code) {
        switch (code) {
            case 1:
                System.out.println("One");
                // Missing break - fallthrough
            case 2:
                System.out.println("Two");
                break;
            default:
                System.out.println("Other");
        }
    }

    // Code Smell: Too many case clauses
    public String tooManyCases(int code) {
        switch (code) {
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            case 5: return "E";
            case 6: return "F";
            case 7: return "G";
            case 8: return "H";
            case 9: return "I";
            case 10: return "J";
            case 11: return "K";
            case 12: return "L";
            case 13: return "M";
            case 14: return "N";
            case 15: return "O";
            default: return "Other";
        }
    }

    // ================== LOOP SMELLS ==================
    
    // Code Smell: For loop can be foreach
    public void forLoopCouldBeForeach(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)); // Could use foreach
        }
    }

    // Code Smell: Loop counter modified inside loop
    public void modifyLoopCounter() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            i++; // Modifying loop counter
        }
    }

    // Code Smell: Nested loops too deep
    public void deeplyNestedLoops(int[][][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                for (int k = 0; k < data[i][j].length; k++) {
                    for (int l = 0; l < 10; l++) { // Too deep
                        System.out.println(data[i][j][k] + l);
                    }
                }
            }
        }
    }

    // ================== FIELD/VARIABLE SMELLS ==================
    
    // Code Smell: Public non-final field
    public String publicField = "exposed"; // Should be private with getter

    // Code Smell: Mutable static field
    public static List<String> mutableStatic = new ArrayList<>(); // Mutable static

    // Code Smell: Collection field not initialized
    private List<String> uninitializedCollection; // Should be initialized
    
    public void addItem(String item) {
        if (uninitializedCollection != null) {
            uninitializedCollection.add(item);
        }
    }

    // Code Smell: Declaring multiple variables on same line
    public void multipleDeclarations() {
        int a = 1, b = 2, c = 3; // Hard to read
        System.out.println(a + b + c);
    }

    // ================== METHOD SMELLS ==================
    
    // Code Smell: Method returns null
    public String returnsNull(boolean condition) {
        if (condition) {
            return "value";
        }
        return null; // Should return Optional or throw exception
    }

    // Code Smell: Method does nothing
    public void doesNothing() {
        // Empty method body
    }

    // Code Smell: Method with boolean parameter (suggests method should be split)
    public void methodWithBooleanParam(String data, boolean flag) {
        if (flag) {
            // Do one thing
            System.out.println("Processing: " + data);
        } else {
            // Do another thing
            System.out.println("Skipping: " + data);
        }
    }

    // ================== COMPARISON SMELLS ==================
    
    // Code Smell: Yoda condition
    public boolean yodaCondition(int x) {
        return 5 == x; // Should be x == 5
    }

    // Code Smell: Null check before instanceof
    public boolean redundantNullCheck(Object obj) {
        if (obj != null && obj instanceof String) { // null check redundant
            return true;
        }
        return false;
    }

    // Code Smell: Comparing with boolean literal
    public void compareWithBoolean(boolean flag) {
        if (flag == false) { // Should be !flag
            System.out.println("False");
        }
    }

    // ================== MISCELLANEOUS SMELLS ==================
    
    // Code Smell: System.out.println in production code
    public void usesSystemOut() {
        System.out.println("Debug message"); // Should use proper logging
    }

    // Code Smell: printStackTrace
    public void usesPrintStackTrace() {
        try {
            riskyOperation();
        } catch (Exception e) {
            e.printStackTrace(); // Should use proper logging
        }
    }

    // Code Smell: Thread.sleep in non-test code
    public void usesThreadSleep() throws InterruptedException {
        Thread.sleep(1000); // Usually a code smell
    }

    // Code Smell: Using deprecated API
    public Date deprecatedUsage() {
        return new Date(2023, 1, 1); // Deprecated constructor
    }

    // Code Smell: TODO/FIXME comment
    public void todoComment() {
        // TODO: implement this properly
        // FIXME: this is broken
        System.out.println("Incomplete");
    }

    // Code Smell: Hardcoded IP address
    public void hardcodedIp() {
        String server = "192.168.1.100"; // Hardcoded IP
        System.out.println("Connecting to " + server);
    }

    // Code Smell: Hardcoded file path
    public void hardcodedPath() {
        String path = "C:\\Users\\admin\\data.txt"; // Hardcoded path
        System.out.println("Reading from " + path);
    }

    private void riskyOperation() throws Exception {
        throw new Exception("Error");
    }
}

