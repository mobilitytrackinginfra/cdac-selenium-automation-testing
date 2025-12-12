package com.sonarqube.codesmells;

import java.util.*;
import java.io.*;

/**
 * This class demonstrates various CODE SMELL patterns that SonarQube can detect.
 * Code smells are maintainability issues that don't necessarily cause bugs but make
 * the code harder to understand, modify, and maintain.
 */
public class CodeSmellSimulations {

    // ================== UNUSED CODE ==================
    
    // Code Smell: Unused private field
    private String unusedField = "never used";
    
    // Code Smell: Unused private method
    private void unusedMethod() {
        System.out.println("This method is never called");
    }

    // Code Smell: Unused local variable
    public void unusedLocalVariable() {
        int x = 10; // Never used
        int y = 20;
        System.out.println(y);
    }

    // Code Smell: Unused parameter
    public void unusedParameter(String usedParam, String unusedParam) {
        System.out.println(usedParam);
        // unusedParam is never used
    }

    // Code Smell: Unused import (would be at top of file)
    // import java.awt.*; // If not used

    // ================== EMPTY BLOCKS ==================
    
    // Code Smell: Empty catch block
    public void emptyCatchBlock() {
        try {
            riskyOperation();
        } catch (Exception e) {
            // Empty - swallowing exception silently
        }
    }

    // Code Smell: Empty if statement
    public void emptyIfStatement(boolean condition) {
        if (condition) {
            // Empty block
        }
    }

    // Code Smell: Empty finally block
    public void emptyFinallyBlock() {
        try {
            riskyOperation();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Empty finally
        }
    }

    // Code Smell: Empty synchronized block
    public void emptySynchronizedBlock() {
        synchronized (this) {
            // Empty synchronized block
        }
    }

    // ================== COMPLEXITY ISSUES ==================
    
    // Code Smell: Cognitive complexity too high
    public String highCognitiveComplexity(int a, int b, int c, String type) {
        String result = "";
        if (type.equals("A")) {
            if (a > 0) {
                if (b > 0) {
                    if (c > 0) {
                        result = "All positive";
                    } else {
                        if (a > b) {
                            result = "A greater";
                        } else {
                            result = "B greater";
                        }
                    }
                } else {
                    result = "B not positive";
                }
            } else {
                result = "A not positive";
            }
        } else if (type.equals("B")) {
            if (a < 0) {
                if (b < 0) {
                    result = "Both negative";
                } else {
                    result = "Mixed";
                }
            } else {
                result = "A not negative";
            }
        } else {
            result = "Unknown type";
        }
        return result;
    }

    // Code Smell: Too many if-else branches
    public String tooManyBranches(int code) {
        if (code == 1) {
            return "One";
        } else if (code == 2) {
            return "Two";
        } else if (code == 3) {
            return "Three";
        } else if (code == 4) {
            return "Four";
        } else if (code == 5) {
            return "Five";
        } else if (code == 6) {
            return "Six";
        } else if (code == 7) {
            return "Seven";
        } else if (code == 8) {
            return "Eight";
        } else if (code == 9) {
            return "Nine";
        } else if (code == 10) {
            return "Ten";
        } else {
            return "Other";
        }
    }

    // ================== MAGIC NUMBERS/STRINGS ==================
    
    // Code Smell: Magic numbers
    public double calculateArea(double radius) {
        return 3.14159 * radius * radius; // Should use Math.PI or named constant
    }

    // Code Smell: Magic strings
    public boolean isAdmin(String role) {
        return role.equals("ADMIN"); // Should be a constant
    }

    // Code Smell: Multiple magic numbers
    public int calculatePrice(int quantity) {
        if (quantity > 100) {
            return quantity * 10; // Magic numbers
        } else if (quantity > 50) {
            return quantity * 12;
        } else {
            return quantity * 15;
        }
    }

    // ================== NAMING CONVENTIONS ==================
    
    // Code Smell: Non-descriptive variable names
    public int process(int a, int b, int c) {
        int x = a + b;
        int y = x * c;
        return y;
    }

    // Code Smell: Constant not in UPPER_CASE
    public static final String adminRole = "ADMIN"; // Should be ADMIN_ROLE

    // Code Smell: Class name doesn't follow conventions
    // class myClass {} // Should be MyClass

    // Code Smell: Method name starts with uppercase
    public void ProcessData() { // Should be processData
        System.out.println("Processing");
    }

    // Code Smell: Boolean method without is/has/can prefix
    public boolean admin(String role) { // Should be isAdmin
        return role.equals("ADMIN");
    }

    // ================== CODE DUPLICATION ==================
    
    // Code Smell: Duplicated code blocks
    public void processUser(String name, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age");
        }
        System.out.println("Processing user: " + name + ", " + age);
    }

    public void processEmployee(String name, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age");
        }
        System.out.println("Processing employee: " + name + ", " + age);
    }

    public void processCustomer(String name, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age");
        }
        System.out.println("Processing customer: " + name + ", " + age);
    }

    // ================== COMMENTED OUT CODE ==================
    
    // Code Smell: Commented out code
    public void methodWithCommentedCode() {
        int x = 10;
        // int y = 20;
        // int z = x + y;
        // System.out.println(z);
        System.out.println(x);
    }

    // ================== TOO MANY PARAMETERS ==================
    
    // Code Smell: Method with too many parameters
    public void createUser(String firstName, String lastName, String email, 
                          String phone, String address, String city, 
                          String state, String zipCode, String country,
                          Date birthDate, String department) {
        // Too many parameters - should use a builder pattern or DTO
    }

    // ================== LONG METHOD ==================
    
    // Code Smell: Method is too long (should be split)
    public void longMethod(List<String> items) {
        // Initialization
        int count = 0;
        String result = "";
        List<String> processed = new ArrayList<>();
        
        // First phase
        for (String item : items) {
            if (item != null) {
                count++;
            }
        }
        
        // Second phase
        for (String item : items) {
            if (item != null && !item.isEmpty()) {
                result += item + ",";
            }
        }
        
        // Third phase
        for (String item : items) {
            if (item != null && item.length() > 3) {
                processed.add(item.toUpperCase());
            }
        }
        
        // Fourth phase
        for (String item : processed) {
            System.out.println(item);
        }
        
        // Fifth phase
        if (count > 0) {
            System.out.println("Count: " + count);
            System.out.println("Result: " + result);
        }
        
        // More processing that could be in separate methods
        Map<String, Integer> counts = new HashMap<>();
        for (String item : items) {
            if (item != null) {
                counts.merge(item, 1, Integer::sum);
            }
        }
        
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // ================== RETURN STATEMENTS ==================
    
    // Code Smell: Multiple return statements
    public String multipleReturns(int value) {
        if (value < 0) {
            return "negative";
        }
        if (value == 0) {
            return "zero";
        }
        if (value < 10) {
            return "small";
        }
        if (value < 100) {
            return "medium";
        }
        return "large";
    }

    // Code Smell: Unnecessary return in void method
    public void unnecessaryReturn(boolean condition) {
        if (condition) {
            System.out.println("True");
            return; // Unnecessary
        }
        System.out.println("False");
        return; // Unnecessary
    }

    // ================== STRING ISSUES ==================
    
    // Code Smell: String concatenation in loop
    public String concatenateInLoop(List<String> items) {
        String result = "";
        for (String item : items) {
            result = result + item + ", "; // Should use StringBuilder
        }
        return result;
    }

    // Code Smell: Unnecessary toString()
    public void unnecessaryToString(String str) {
        System.out.println(str.toString()); // Unnecessary
    }

    // ================== BOOLEAN EXPRESSIONS ==================
    
    // Code Smell: Redundant boolean literal
    public boolean redundantBoolean(boolean condition) {
        if (condition == true) { // Should be: if (condition)
            return true;
        }
        return false; // Whole method should be: return condition;
    }

    // Code Smell: Negated condition
    public void negatedCondition(boolean a, boolean b) {
        if (!(a && b)) { // Hard to read
            System.out.println("Not both true");
        }
    }

    // Helper method
    private void riskyOperation() throws Exception {
        throw new Exception("Risky");
    }
}

