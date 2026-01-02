package com.filehandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Demonstrates reading files using:
 * - FileReader (character-by-character)
 * - BufferedReader (line-by-line, efficient)
 * - LineNumberReader (with line numbers)
 */
public class FileReaderDemo {

    private static final String FILE_PATH = "demo_files/reader_demo.txt";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    FILE READER DEMONSTRATION");
        System.out.println("========================================\n");

        // Create sample file for demonstration
        createSampleFile();

        // 1. Reading using FileReader (character by character)
        demonstrateFileReader();

        // 2. Reading using FileReader with char array
        demonstrateFileReaderWithArray();

        // 3. Reading using BufferedReader (line by line)
        demonstrateBufferedReader();

        // 4. Reading using BufferedReader with Stream API
        demonstrateBufferedReaderStream();

        // 5. Reading using LineNumberReader
        demonstrateLineNumberReader();

        // 6. Reading specific number of characters
        demonstrateReadingSpecificChars();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    private static void createSampleFile() {
        System.out.println("CREATING SAMPLE FILE FOR DEMONSTRATION");
        System.out.println("---------------------------------------");
        
        File dir = new File("demo_files");
        dir.mkdirs();
        
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write("Line 1: Welcome to File Handling in Java!\n");
            writer.write("Line 2: This file demonstrates FileReader.\n");
            writer.write("Line 3: Java provides multiple ways to read files.\n");
            writer.write("Line 4: FileReader reads character by character.\n");
            writer.write("Line 5: BufferedReader is more efficient.\n");
            writer.write("Line 6: Always close your resources!\n");
            writer.write("Line 7: Or use try-with-resources.\n");
            writer.write("Line 8: Happy Coding!\n");
            System.out.println("Sample file created: " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error creating sample file: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading file character by character using FileReader
     */
    private static void demonstrateFileReader() {
        System.out.println("1. FILEREADER - Character by Character");
        System.out.println("---------------------------------------");
        
        FileReader reader = null;
        try {
            reader = new FileReader(FILE_PATH);
            System.out.println("Reading first 50 characters:");
            System.out.print("   \"");
            
            int charData;
            int count = 0;
            while ((charData = reader.read()) != -1 && count < 50) {
                System.out.print((char) charData);
                count++;
            }
            System.out.println("...\"");
            System.out.println("   (Read " + count + " characters)");
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    System.out.println("   FileReader closed manually in finally block");
                }
            } catch (IOException e) {
                System.out.println("Error closing reader: " + e.getMessage());
            }
        }
        System.out.println();
    }

    /**
     * Reading file using char array for better performance
     */
    private static void demonstrateFileReaderWithArray() {
        System.out.println("2. FILEREADER - Using Char Array Buffer");
        System.out.println("----------------------------------------");
        
        try (FileReader reader = new FileReader(FILE_PATH)) {
            char[] buffer = new char[100];
            int charsRead;
            StringBuilder content = new StringBuilder();
            
            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
            
            System.out.println("   Total characters read: " + content.length());
            System.out.println("   First 100 chars: " + 
                content.substring(0, Math.min(100, content.length())) + "...");
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading file line by line using BufferedReader
     */
    private static void demonstrateBufferedReader() {
        System.out.println("3. BUFFEREDREADER - Line by Line");
        System.out.println("---------------------------------");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 1;
            
            System.out.println("   File contents:");
            while ((line = reader.readLine()) != null) {
                System.out.println("   [" + lineNumber + "] " + line);
                lineNumber++;
            }
            System.out.println("   Total lines read: " + (lineNumber - 1));
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading file using BufferedReader with Java 8 Stream API
     */
    private static void demonstrateBufferedReaderStream() {
        System.out.println("4. BUFFEREDREADER - Using Stream API (Java 8+)");
        System.out.println("----------------------------------------------");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            System.out.println("   Lines containing 'Java':");
            reader.lines()
                  .filter(line -> line.contains("Java"))
                  .forEach(line -> System.out.println("   -> " + line));
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Another stream example - counting words
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            long wordCount = reader.lines()
                                   .flatMap(line -> java.util.Arrays.stream(line.split("\\s+")))
                                   .count();
            System.out.println("   Total word count: " + wordCount);
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading file with line numbers using LineNumberReader
     */
    private static void demonstrateLineNumberReader() {
        System.out.println("5. LINENUMBERREADER - Built-in Line Numbers");
        System.out.println("--------------------------------------------");
        
        try (LineNumberReader reader = new LineNumberReader(new FileReader(FILE_PATH))) {
            // Set starting line number (default is 0)
            reader.setLineNumber(0);
            
            String line;
            System.out.println("   Reading with automatic line numbering:");
            while ((line = reader.readLine()) != null) {
                System.out.println("   Line " + reader.getLineNumber() + ": " + line);
            }
            
            System.out.println("   Final line count: " + reader.getLineNumber());
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading specific number of characters
     */
    private static void demonstrateReadingSpecificChars() {
        System.out.println("6. READING SPECIFIC NUMBER OF CHARACTERS");
        System.out.println("-----------------------------------------");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            char[] buffer = new char[20];
            
            // Read first 20 characters
            int charsRead = reader.read(buffer, 0, 20);
            System.out.println("   First 20 chars: \"" + new String(buffer, 0, charsRead) + "\"");
            
            // Skip some characters
            long skipped = reader.skip(30);
            System.out.println("   Skipped " + skipped + " characters");
            
            // Read next 20 characters
            charsRead = reader.read(buffer, 0, 20);
            if (charsRead > 0) {
                System.out.println("   Next 20 chars: \"" + new String(buffer, 0, charsRead) + "\"");
            }
            
            // Check if ready to read
            System.out.println("   Ready to read more: " + reader.ready());
            
            // Mark and reset functionality
            if (reader.markSupported()) {
                System.out.println("   Mark/Reset supported: Yes");
                reader.mark(100); // Mark current position
                reader.readLine(); // Read a line
                reader.reset(); // Go back to marked position
                System.out.println("   Successfully demonstrated mark/reset");
            }
            
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void cleanup() {
        System.out.println("CLEANUP");
        System.out.println("-------");
        File file = new File(FILE_PATH);
        File dir = new File("demo_files");
        
        if (file.delete()) {
            System.out.println("Deleted: " + FILE_PATH);
        }
        if (dir.delete()) {
            System.out.println("Deleted: demo_files directory");
        }
        System.out.println();
    }
}

