package com.filehandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Demonstrates appending to files using different methods:
 * - FileWriter with append mode
 * - BufferedWriter with append mode
 * - PrintWriter with append mode
 * - FileOutputStream with append mode
 * - Files.write with StandardOpenOption.APPEND
 * - RandomAccessFile
 */
public class FileAppendDemo {

    private static final String DEMO_DIR = "demo_files";
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    FILE APPEND DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. FileWriter append mode
        demonstrateFileWriterAppend();

        // 2. BufferedWriter append mode
        demonstrateBufferedWriterAppend();

        // 3. PrintWriter append mode
        demonstratePrintWriterAppend();

        // 4. FileOutputStream append mode
        demonstrateFileOutputStreamAppend();

        // 5. NIO Files.write with APPEND option
        demonstrateNIOAppend();

        // 6. RandomAccessFile for appending
        demonstrateRandomAccessAppend();

        // 7. Practical example: Log file
        demonstrateLogFileAppend();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Appending using FileWriter
     */
    private static void demonstrateFileWriterAppend() {
        System.out.println("1. FILEWRITER APPEND MODE");
        System.out.println("-------------------------");
        
        String filePath = DEMO_DIR + "/filewriter_append.txt";
        
        // First write - create file (append = false, default)
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Initial content - Line 1\n");
            writer.write("Initial content - Line 2\n");
            System.out.println("   Initial content written");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Second write - APPEND mode (append = true)
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("Appended content - Line 3\n");
            writer.write("Appended content - Line 4\n");
            System.out.println("   Content appended");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Third write - more appending
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("More appended content - Line 5\n");
            System.out.println("   More content appended");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Appending using BufferedWriter
     */
    private static void demonstrateBufferedWriterAppend() {
        System.out.println("2. BUFFEREDWRITER APPEND MODE");
        System.out.println("-----------------------------");
        
        String filePath = DEMO_DIR + "/bufferedwriter_append.txt";
        
        // Create initial file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("=== File Created ===");
            writer.newLine();
            writer.write("Timestamp: " + LocalDateTime.now().format(formatter));
            writer.newLine();
            System.out.println("   Initial file created");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Append multiple times
        for (int i = 1; i <= 3; i++) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath, true))) {
                writer.write("Appended entry #" + i);
                writer.newLine();
                System.out.println("   Appended entry #" + i);
                
                // Small delay to show different timestamps
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                System.out.println("   Error: " + e.getMessage());
            }
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Appending using PrintWriter
     */
    private static void demonstratePrintWriterAppend() {
        System.out.println("3. PRINTWRITER APPEND MODE");
        System.out.println("--------------------------");
        
        String filePath = DEMO_DIR + "/printwriter_append.txt";
        
        // Create initial file
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Header: PrintWriter Append Demo");
            writer.println("================================");
            System.out.println("   Header created");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Append using PrintWriter with FileWriter in append mode
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(filePath, true))) {
            writer.printf("Entry 1: %d items at $%.2f each%n", 5, 9.99);
            writer.printf("Entry 2: %d items at $%.2f each%n", 10, 19.99);
            System.out.println("   Entries appended");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Append footer
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(filePath, true))) {
            writer.println("================================");
            writer.println("End of entries");
            System.out.println("   Footer appended");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Appending using FileOutputStream
     */
    private static void demonstrateFileOutputStreamAppend() {
        System.out.println("4. FILEOUTPUTSTREAM APPEND MODE");
        System.out.println("--------------------------------");
        
        String filePath = DEMO_DIR + "/outputstream_append.txt";
        
        // Create initial file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            String initial = "FileOutputStream Initial Content\n";
            fos.write(initial.getBytes());
            System.out.println("   Initial content written");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Append using FileOutputStream with append = true
        try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
            String appended = "Appended via FileOutputStream\n";
            fos.write(appended.getBytes());
            
            // Append byte by byte
            byte[] moreBytes = "More bytes appended\n".getBytes();
            for (byte b : moreBytes) {
                fos.write(b);
            }
            
            System.out.println("   Content appended");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Appending using NIO Files.write
     */
    private static void demonstrateNIOAppend() {
        System.out.println("5. NIO Files.write WITH APPEND");
        System.out.println("-------------------------------");
        
        String filePath = DEMO_DIR + "/nio_append.txt";
        
        try {
            // Create initial file
            Files.write(Paths.get(filePath), 
                "NIO Initial Content\n".getBytes(),
                StandardOpenOption.CREATE);
            System.out.println("   Initial content written");
            
            // Append content
            Files.write(Paths.get(filePath), 
                "First append via NIO\n".getBytes(),
                StandardOpenOption.APPEND);
            System.out.println("   First append done");
            
            // Append more content
            Files.write(Paths.get(filePath), 
                "Second append via NIO\n".getBytes(),
                StandardOpenOption.APPEND);
            System.out.println("   Second append done");
            
            // Append list of lines
            java.util.List<String> lines = java.util.Arrays.asList(
                "Line from list 1",
                "Line from list 2",
                "Line from list 3"
            );
            Files.write(Paths.get(filePath), lines,
                StandardOpenOption.APPEND);
            System.out.println("   List of lines appended");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Appending using RandomAccessFile
     */
    private static void demonstrateRandomAccessAppend() {
        System.out.println("6. RANDOMACCESSFILE APPEND");
        System.out.println("--------------------------");
        
        String filePath = DEMO_DIR + "/randomaccess_append.txt";
        
        // Create initial file
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.writeBytes("Initial content\n");
            raf.writeBytes("Second line\n");
            System.out.println("   Initial content written");
            System.out.println("   File length: " + raf.length() + " bytes");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Append by seeking to end
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            // Move to end of file
            raf.seek(raf.length());
            
            raf.writeBytes("Appended via seek to end\n");
            raf.writeBytes("More appended content\n");
            
            System.out.println("   Content appended at end");
            System.out.println("   New file length: " + raf.length() + " bytes");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(filePath);
        System.out.println();
    }

    /**
     * Practical example: Appending to a log file
     */
    private static void demonstrateLogFileAppend() {
        System.out.println("7. PRACTICAL EXAMPLE: LOG FILE");
        System.out.println("-------------------------------");
        
        String logFile = DEMO_DIR + "/application.log";
        
        // Initialize log file with header
        try (PrintWriter writer = new PrintWriter(logFile)) {
            writer.println("========================================");
            writer.println("     APPLICATION LOG");
            writer.println("     Started: " + LocalDateTime.now().format(formatter));
            writer.println("========================================");
            System.out.println("   Log file initialized");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Simulate logging events
        logEvent(logFile, "INFO", "Application started");
        logEvent(logFile, "DEBUG", "Loading configuration");
        logEvent(logFile, "INFO", "Database connection established");
        logEvent(logFile, "WARN", "Memory usage above 80%");
        logEvent(logFile, "ERROR", "Failed to connect to external service");
        logEvent(logFile, "INFO", "Retry successful");
        logEvent(logFile, "INFO", "Processing complete");
        
        // Add footer
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(logFile, true))) {
            writer.println("========================================");
            writer.println("     Log ended: " + LocalDateTime.now().format(formatter));
            writer.println("========================================");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        displayFileContents(logFile);
        System.out.println();
    }

    /**
     * Helper method to log events
     */
    private static void logEvent(String logFile, String level, String message) {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(logFile, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.printf("[%s] [%-5s] %s%n", timestamp, level, message);
            System.out.println("   Logged: [" + level + "] " + message);
            
            // Small delay for timestamp variation
            Thread.sleep(50);
        } catch (IOException | InterruptedException e) {
            System.out.println("   Error logging: " + e.getMessage());
        }
    }

    private static void displayFileContents(String filePath) {
        System.out.println("   --- File Contents ---");
        try (java.util.Scanner scanner = new java.util.Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                System.out.println("   " + scanner.nextLine());
            }
        } catch (IOException e) {
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

