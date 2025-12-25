package com.sonarqube.bugs;

import java.util.List;

/**
 * Additional bug patterns that SonarQube can detect.
 */
public class MoreBugs {

    // ================== STRING ISSUES ==================
    
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

    // ================== COLLECTION ISSUES ==================
    
    // Bug: Checking size before isEmpty
    public boolean checkEmpty(List<String> list) {
        return list.size() == 0; // Should use isEmpty()
    }

    // ================== METHOD ISSUES ==================
    
    // Bug: Ignoring method return value
    public void ignoreReturnValue(String str) {
        str.trim(); // Return value ignored
        str.toLowerCase(); // Return value ignored
        System.out.println(str);
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

}

