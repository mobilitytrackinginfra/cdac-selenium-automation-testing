package com.filehandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Demonstrates writing files using:
 * - FileWriter (basic character writing)
 * - BufferedWriter (efficient buffered writing)
 * - OutputStreamWriter (with encoding support)
 */
public class FileWriterDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    FILE WRITER DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. Basic FileWriter usage
        demonstrateBasicFileWriter();

        // 2. FileWriter with append mode
        demonstrateFileWriterAppend();

        // 3. BufferedWriter for efficient writing
        demonstrateBufferedWriter();

        // 4. Writing with specific encoding
        demonstrateEncodedWriter();

        // 5. Writing different data types
        demonstrateWritingDataTypes();

        // 6. Writing formatted content
        demonstrateFormattedWriting();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Basic FileWriter - writing strings and characters
     */
    private static void demonstrateBasicFileWriter() {
        System.out.println("1. BASIC FILEWRITER");
        System.out.println("-------------------");
        
        String filePath = DEMO_DIR + "/basic_write.txt";
        
        // Method 1: Using try-finally (old way)
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            
            // Write a string
            writer.write("Hello, File Handling!\n");
            
            // Write a character
            writer.write('J');
            writer.write('a');
            writer.write('v');
            writer.write('a');
            writer.write('\n');
            
            // Write portion of a string
            String text = "This is a demonstration of FileWriter";
            writer.write(text, 0, 20); // Write first 20 characters
            writer.write('\n');
            
            // Write char array
            char[] chars = {'H', 'e', 'l', 'l', 'o'};
            writer.write(chars);
            writer.write('\n');
            
            // Write portion of char array
            writer.write(chars, 0, 3); // Write "Hel"
            
            System.out.println("   File written: " + filePath);
            System.out.println("   Content written successfully");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    System.out.println("   Writer closed in finally block");
                }
            } catch (IOException e) {
                System.out.println("   Error closing writer: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * FileWriter in append mode
     */
    private static void demonstrateFileWriterAppend() {
        System.out.println("2. FILEWRITER - APPEND MODE");
        System.out.println("---------------------------");
        
        String filePath = DEMO_DIR + "/append_demo.txt";
        
        // First write - create file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("First line - Original content\n");
            System.out.println("   Initial content written");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Second write - append mode (true)
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("Second line - Appended content\n");
            writer.write("Third line - More appended content\n");
            System.out.println("   Content appended successfully");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read and display file contents
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * BufferedWriter for efficient writing
     */
    private static void demonstrateBufferedWriter() {
        System.out.println("3. BUFFEREDWRITER - EFFICIENT WRITING");
        System.out.println("--------------------------------------");
        
        String filePath = DEMO_DIR + "/buffered_write.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            // Write strings
            writer.write("Line 1: BufferedWriter is efficient!");
            writer.newLine(); // Platform-independent newline
            
            writer.write("Line 2: It buffers data before writing to disk.");
            writer.newLine();
            
            writer.write("Line 3: This reduces I/O operations.");
            writer.newLine();
            
            // Flush buffer manually if needed
            writer.flush();
            System.out.println("   Buffer flushed manually");
            
            // Write more content
            for (int i = 4; i <= 10; i++) {
                writer.write("Line " + i + ": Auto-generated line");
                writer.newLine();
            }
            
            System.out.println("   File written: " + filePath);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // BufferedWriter with custom buffer size
        String filePath2 = DEMO_DIR + "/buffered_custom.txt";
        int bufferSize = 8192; // 8KB buffer
        
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath2), bufferSize)) {
            
            writer.write("Custom buffer size: " + bufferSize + " bytes");
            writer.newLine();
            writer.write("Larger buffer = fewer disk writes = better performance");
            
            System.out.println("   Custom buffer file written: " + filePath2);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Writing with specific character encoding
     */
    private static void demonstrateEncodedWriter() {
        System.out.println("4. OUTPUTSTREAMWRITER - WITH ENCODING");
        System.out.println("--------------------------------------");
        
        String filePath = DEMO_DIR + "/encoded_utf8.txt";
        
        // Write UTF-8 encoded file
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(filePath), 
                    StandardCharsets.UTF_8))) {
            
            writer.write("UTF-8 Encoded File");
            writer.newLine();
            writer.write("Special characters: äöü ñ 你好 🎉");
            writer.newLine();
            writer.write("Japanese: こんにちは");
            writer.newLine();
            writer.write("Greek: Γειά σου");
            writer.newLine();
            writer.write("Arabic: مرحبا");
            
            System.out.println("   UTF-8 file written: " + filePath);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Write with different encoding (UTF-16)
        String filePath2 = DEMO_DIR + "/encoded_utf16.txt";
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(filePath2), 
                    StandardCharsets.UTF_16))) {
            
            writer.write("UTF-16 Encoded File");
            writer.newLine();
            writer.write("This file uses UTF-16 encoding");
            
            System.out.println("   UTF-16 file written: " + filePath2);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Writing different data types
     */
    private static void demonstrateWritingDataTypes() {
        System.out.println("5. WRITING DIFFERENT DATA TYPES");
        System.out.println("--------------------------------");
        
        String filePath = DEMO_DIR + "/data_types.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            // Writing various data types (converted to String)
            int intValue = 42;
            double doubleValue = 3.14159;
            boolean boolValue = true;
            char charValue = 'X';
            long longValue = 9876543210L;
            
            writer.write("Integer: " + intValue);
            writer.newLine();
            
            writer.write("Double: " + doubleValue);
            writer.newLine();
            
            writer.write("Boolean: " + boolValue);
            writer.newLine();
            
            writer.write("Character: " + charValue);
            writer.newLine();
            
            writer.write("Long: " + longValue);
            writer.newLine();
            
            // Writing an array
            int[] array = {1, 2, 3, 4, 5};
            writer.write("Array: " + java.util.Arrays.toString(array));
            writer.newLine();
            
            // Writing a date
            writer.write("Date: " + new java.util.Date());
            writer.newLine();
            
            System.out.println("   Data types written to: " + filePath);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Writing formatted content
     */
    private static void demonstrateFormattedWriting() {
        System.out.println("6. FORMATTED WRITING");
        System.out.println("--------------------");
        
        String filePath = DEMO_DIR + "/formatted.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            // Create a formatted table
            writer.write("+------------+----------+--------+");
            writer.newLine();
            writer.write("| Name       | Age      | Score  |");
            writer.newLine();
            writer.write("+------------+----------+--------+");
            writer.newLine();
            
            // Using String.format for formatting
            String[][] data = {
                {"Alice", "25", "95.5"},
                {"Bob", "30", "87.3"},
                {"Charlie", "22", "92.8"}
            };
            
            for (String[] row : data) {
                String formatted = String.format("| %-10s | %-8s | %-6s |", 
                    row[0], row[1], row[2]);
                writer.write(formatted);
                writer.newLine();
            }
            
            writer.write("+------------+----------+--------+");
            writer.newLine();
            
            // Writing with tabs
            writer.newLine();
            writer.write("Tab-separated values:");
            writer.newLine();
            writer.write("Name\tAge\tScore");
            writer.newLine();
            for (String[] row : data) {
                writer.write(row[0] + "\t" + row[1] + "\t" + row[2]);
                writer.newLine();
            }
            
            System.out.println("   Formatted file written: " + filePath);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Helper method to display file contents
     */
    private static void displayFileContents(String filePath) {
        System.out.println("   --- File Contents ---");
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("   " + line);
            }
        } catch (IOException e) {
            System.out.println("   Error reading file: " + e.getMessage());
        }
        System.out.println("   ---------------------");
    }

    /**
     * Cleanup demo files
     */
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

