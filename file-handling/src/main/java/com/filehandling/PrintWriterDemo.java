package com.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Demonstrates PrintWriter for file writing:
 * - Basic PrintWriter usage
 * - Formatted output with printf/format
 * - Auto-flush mode
 * - Different constructors
 * - Error handling with checkError()
 */
public class PrintWriterDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    PRINTWRITER DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. Basic PrintWriter usage
        demonstrateBasicPrintWriter();

        // 2. Formatted output with printf/format
        demonstrateFormattedOutput();

        // 3. PrintWriter with auto-flush
        demonstrateAutoFlush();

        // 4. Different constructors
        demonstrateDifferentConstructors();

        // 5. Writing various data types
        demonstrateDataTypes();

        // 6. Error handling
        demonstrateErrorHandling();

        // 7. Creating formatted reports
        demonstrateReportGeneration();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Basic PrintWriter usage - print, println methods
     */
    private static void demonstrateBasicPrintWriter() {
        System.out.println("1. BASIC PRINTWRITER");
        System.out.println("--------------------");
        
        String filePath = DEMO_DIR + "/basic_print.txt";
        
        try (PrintWriter writer = new PrintWriter(filePath)) {
            
            // print - no newline
            writer.print("Hello ");
            writer.print("World");
            
            // println - with newline
            writer.println("!");
            writer.println("This is line 2");
            writer.println("This is line 3");
            
            // println with empty string (blank line)
            writer.println();
            writer.println("After blank line");
            
            System.out.println("   File written: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Formatted output using printf and format methods
     */
    private static void demonstrateFormattedOutput() {
        System.out.println("2. FORMATTED OUTPUT (printf/format)");
        System.out.println("------------------------------------");
        
        String filePath = DEMO_DIR + "/formatted_print.txt";
        
        try (PrintWriter writer = new PrintWriter(filePath)) {
            
            // printf - formatted printing
            writer.printf("Integer: %d%n", 42);
            writer.printf("Float: %.2f%n", 3.14159);
            writer.printf("String: %s%n", "Hello");
            writer.printf("Padded number: %05d%n", 7);
            writer.printf("Left-aligned: %-10s|%n", "Java");
            writer.printf("Right-aligned: %10s|%n", "Java");
            
            // format - same as printf
            writer.format("%n--- Using format() ---%n");
            writer.format("Hex: %x%n", 255);
            writer.format("Octal: %o%n", 64);
            writer.format("Scientific: %e%n", 12345.6789);
            writer.format("Uppercase Hex: %X%n", 255);
            
            // Multiple values in one format
            writer.printf("%n--- Multiple Values ---%n");
            writer.printf("Name: %s, Age: %d, Score: %.1f%n", "Alice", 25, 95.5);
            writer.printf("Name: %s, Age: %d, Score: %.1f%n", "Bob", 30, 87.3);
            
            // Width and precision
            writer.printf("%n--- Width and Precision ---%n");
            writer.printf("|%10.2f|%n", 123.456);    // width 10, 2 decimals
            writer.printf("|%-10.2f|%n", 123.456);   // left-aligned
            writer.printf("|%+10.2f|%n", 123.456);   // show sign
            writer.printf("|%,d|%n", 1234567);       // thousands separator
            
            System.out.println("   File written: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * PrintWriter with auto-flush enabled
     */
    private static void demonstrateAutoFlush() {
        System.out.println("3. PRINTWRITER WITH AUTO-FLUSH");
        System.out.println("-------------------------------");
        
        String filePath = DEMO_DIR + "/autoflush.txt";
        
        try {
            // Auto-flush enabled (second parameter = true)
            // Note: auto-flush only works with println, printf, format methods
            PrintWriter writer = new PrintWriter(
                new FileOutputStream(filePath), true);
            
            writer.println("Line 1 - auto-flushed immediately");
            System.out.println("   Line 1 written and flushed");
            
            writer.println("Line 2 - also auto-flushed");
            System.out.println("   Line 2 written and flushed");
            
            // print() does NOT trigger auto-flush
            writer.print("Line 3 - not auto-flushed until...");
            writer.println(" ...now!"); // This triggers flush
            System.out.println("   Line 3 written and flushed");
            
            writer.close();
            System.out.println("   File written: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Different constructors for PrintWriter
     */
    private static void demonstrateDifferentConstructors() {
        System.out.println("4. DIFFERENT CONSTRUCTORS");
        System.out.println("-------------------------");
        
        // Constructor 1: PrintWriter(String fileName)
        String file1 = DEMO_DIR + "/constructor1.txt";
        try (PrintWriter pw = new PrintWriter(file1)) {
            pw.println("Created with filename string");
            System.out.println("   Constructor 1: PrintWriter(String) - " + file1);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Constructor 2: PrintWriter(File file)
        String file2 = DEMO_DIR + "/constructor2.txt";
        try (PrintWriter pw = new PrintWriter(new File(file2))) {
            pw.println("Created with File object");
            System.out.println("   Constructor 2: PrintWriter(File) - " + file2);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Constructor 3: PrintWriter(OutputStream out)
        String file3 = DEMO_DIR + "/constructor3.txt";
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file3))) {
            pw.println("Created with FileOutputStream");
            System.out.println("   Constructor 3: PrintWriter(OutputStream) - " + file3);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Constructor 4: PrintWriter(Writer out)
        String file4 = DEMO_DIR + "/constructor4.txt";
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file4)))) {
            pw.println("Created with BufferedWriter wrapper");
            System.out.println("   Constructor 4: PrintWriter(Writer) - " + file4);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Constructor 5: With encoding
        String file5 = DEMO_DIR + "/constructor5.txt";
        try (PrintWriter pw = new PrintWriter(file5, StandardCharsets.UTF_8.name())) {
            pw.println("Created with UTF-8 encoding");
            pw.println("Special chars: äöü ñ 你好");
            System.out.println("   Constructor 5: PrintWriter(String, charset) - " + file5);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Constructor 6: With OutputStreamWriter for encoding control
        String file6 = DEMO_DIR + "/constructor6.txt";
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream(file6), StandardCharsets.UTF_8))) {
            pw.println("Created with OutputStreamWriter + encoding");
            System.out.println("   Constructor 6: PrintWriter(OutputStreamWriter) - " + file6);
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Writing various data types
     */
    private static void demonstrateDataTypes() {
        System.out.println("5. WRITING VARIOUS DATA TYPES");
        System.out.println("-----------------------------");
        
        String filePath = DEMO_DIR + "/data_types.txt";
        
        try (PrintWriter writer = new PrintWriter(filePath)) {
            
            // Primitives
            writer.println("--- Primitive Types ---");
            writer.println(true);           // boolean
            writer.println('A');            // char
            writer.println(42);             // int
            writer.println(3.14159);        // double
            writer.println(100L);           // long
            writer.println(3.14f);          // float
            
            // Arrays (as objects)
            writer.println("\n--- Arrays ---");
            int[] intArray = {1, 2, 3, 4, 5};
            writer.println(java.util.Arrays.toString(intArray));
            
            String[] strArray = {"Hello", "World"};
            writer.println(java.util.Arrays.toString(strArray));
            
            // Objects
            writer.println("\n--- Objects ---");
            writer.println(new java.util.Date());
            writer.println(java.time.LocalDateTime.now());
            
            // Null handling
            writer.println("\n--- Null Handling ---");
            String nullStr = null;
            writer.println(nullStr);  // prints "null"
            
            // Character array
            writer.println("\n--- Char Array ---");
            char[] charArray = {'J', 'a', 'v', 'a'};
            writer.println(charArray);  // prints "Java"
            
            System.out.println("   File written: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Error handling with checkError()
     */
    private static void demonstrateErrorHandling() {
        System.out.println("6. ERROR HANDLING WITH checkError()");
        System.out.println("------------------------------------");
        
        String filePath = DEMO_DIR + "/error_check.txt";
        
        try (PrintWriter writer = new PrintWriter(filePath)) {
            
            writer.println("Line 1");
            
            // Check if any error occurred
            if (writer.checkError()) {
                System.out.println("   Error occurred during writing!");
            } else {
                System.out.println("   No errors so far");
            }
            
            writer.println("Line 2");
            writer.flush();
            
            if (writer.checkError()) {
                System.out.println("   Error occurred during writing!");
            } else {
                System.out.println("   Writing completed without errors");
            }
            
            System.out.println("   File written: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Demonstrate error scenario (writing to closed writer)
        System.out.println("\n   Testing error detection:");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filePath);
            writer.println("Test line");
            writer.close();
            
            // Writing after close (will set error flag)
            writer.println("This won't work");
            
            if (writer.checkError()) {
                System.out.println("   Error detected: Writer was closed");
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Creating formatted reports
     */
    private static void demonstrateReportGeneration() {
        System.out.println("7. REPORT GENERATION");
        System.out.println("--------------------");
        
        String filePath = DEMO_DIR + "/sales_report.txt";
        
        try (PrintWriter writer = new PrintWriter(filePath)) {
            
            // Report header
            writer.println("╔══════════════════════════════════════════════════╗");
            writer.println("║            MONTHLY SALES REPORT                  ║");
            writer.println("║            January 2026                          ║");
            writer.println("╠══════════════════════════════════════════════════╣");
            
            // Column headers
            writer.printf("║ %-15s │ %8s │ %10s │ %8s ║%n", 
                "Product", "Qty", "Price", "Total");
            writer.println("╠═════════════════╪══════════╪════════════╪══════════╣");
            
            // Data
            String[][] salesData = {
                {"Laptop", "15", "999.99"},
                {"Mouse", "150", "29.99"},
                {"Keyboard", "80", "79.99"},
                {"Monitor", "25", "299.99"},
                {"Headphones", "200", "49.99"}
            };
            
            double grandTotal = 0;
            for (String[] row : salesData) {
                int qty = Integer.parseInt(row[1]);
                double price = Double.parseDouble(row[2]);
                double total = qty * price;
                grandTotal += total;
                
                writer.printf("║ %-15s │ %8d │ $%9.2f │ $%8.2f ║%n", 
                    row[0], qty, price, total);
            }
            
            // Footer
            writer.println("╠═════════════════╧══════════╧════════════╪══════════╣");
            writer.printf("║                             GRAND TOTAL │ $%8.2f ║%n", grandTotal);
            writer.println("╚═══════════════════════════════════════════════════╝");
            
            // Summary
            writer.println("\n--- Summary ---");
            writer.printf("Total Products: %d%n", salesData.length);
            writer.printf("Grand Total: $%,.2f%n", grandTotal);
            writer.printf("Report Generated: %s%n", java.time.LocalDateTime.now());
            
            System.out.println("   Report generated: " + filePath);
            
        } catch (FileNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    private static void displayFileContents(String filePath) {
        System.out.println("   --- File Contents ---");
        try (java.util.Scanner scanner = new java.util.Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                System.out.println("   " + scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("   Error reading file: " + e.getMessage());
        }
        System.out.println("   ---------------------");
    }

    private static void cleanup() {
        System.out.println("CLEANUP");
        System.out.println("-------");
        
        File dir = new File(DEMO_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.delete()) {
                        System.out.println("Deleted: " + file.getName());
                    }
                }
            }
            if (dir.delete()) {
                System.out.println("Deleted: " + DEMO_DIR + " directory");
            }
        }
        System.out.println();
    }
}

