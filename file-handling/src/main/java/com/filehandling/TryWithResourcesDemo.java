package com.filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Demonstrates try-with-resources statement:
 * - Automatic resource management
 * - Multiple resources
 * - Custom AutoCloseable resources
 * - Suppressed exceptions
 * - Comparing with traditional try-finally
 */
public class TryWithResourcesDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    TRY-WITH-RESOURCES DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();
        createSampleFile();

        // 1. Traditional try-finally vs try-with-resources
        demonstrateComparison();

        // 2. Single resource
        demonstrateSingleResource();

        // 3. Multiple resources
        demonstrateMultipleResources();

        // 4. Custom AutoCloseable
        demonstrateCustomAutoCloseable();

        // 5. Suppressed exceptions
        demonstrateSuppressedExceptions();

        // 6. Effectively final variables (Java 9+)
        demonstrateEffectivelyFinal();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    private static void createSampleFile() {
        try {
            Files.writeString(Paths.get(DEMO_DIR, "sample.txt"), 
                "Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
        } catch (IOException e) {
            System.out.println("Error creating sample file: " + e.getMessage());
        }
    }

    /**
     * Comparing traditional try-finally with try-with-resources
     */
    private static void demonstrateComparison() {
        System.out.println("1. TRADITIONAL vs TRY-WITH-RESOURCES");
        System.out.println("-------------------------------------");
        
        String filePath = DEMO_DIR + "/sample.txt";
        
        // Traditional way (before Java 7)
        System.out.println("   TRADITIONAL TRY-FINALLY:");
        System.out.println("   (Verbose and error-prone)");
        System.out.println();
        System.out.println("   FileReader reader = null;");
        System.out.println("   try {");
        System.out.println("       reader = new FileReader(file);");
        System.out.println("       // use reader");
        System.out.println("   } catch (IOException e) {");
        System.out.println("       // handle exception");
        System.out.println("   } finally {");
        System.out.println("       if (reader != null) {");
        System.out.println("           try {");
        System.out.println("               reader.close();");
        System.out.println("           } catch (IOException e) {");
        System.out.println("               // handle close exception");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        
        // Actual traditional code
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
            char[] buffer = new char[100];
            int read = reader.read(buffer);
            System.out.println("\n   Read " + read + " chars using traditional method");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    System.out.println("   Closed reader manually in finally block");
                } catch (IOException e) {
                    System.out.println("   Error closing: " + e.getMessage());
                }
            }
        }
        
        // Modern way (Java 7+)
        System.out.println("\n   TRY-WITH-RESOURCES (Java 7+):");
        System.out.println("   (Clean and safe)");
        System.out.println();
        System.out.println("   try (FileReader reader = new FileReader(file)) {");
        System.out.println("       // use reader");
        System.out.println("   } catch (IOException e) {");
        System.out.println("       // handle exception");
        System.out.println("   }");
        System.out.println("   // reader is automatically closed!");
        
        // Actual try-with-resources code
        try (FileReader autoReader = new FileReader(filePath)) {
            char[] buffer = new char[100];
            int read = autoReader.read(buffer);
            System.out.println("\n   Read " + read + " chars using try-with-resources");
            System.out.println("   Reader will be automatically closed");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Single resource example
     */
    private static void demonstrateSingleResource() {
        System.out.println("2. SINGLE RESOURCE");
        System.out.println("------------------");
        
        String filePath = DEMO_DIR + "/sample.txt";
        
        // BufferedReader
        System.out.println("   Reading with BufferedReader:");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("   > " + line);
            }
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // FileInputStream
        System.out.println("\n   Reading bytes with FileInputStream:");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[10];
            int bytesRead = fis.read(buffer);
            System.out.println("   Read " + bytesRead + " bytes: " + new String(buffer));
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Writing
        String outputPath = DEMO_DIR + "/output.txt";
        System.out.println("\n   Writing with BufferedWriter:");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("Written using try-with-resources");
            bw.newLine();
            bw.write("Auto-closed after this block");
            System.out.println("   Written to: " + outputPath);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Multiple resources in one try statement
     */
    private static void demonstrateMultipleResources() {
        System.out.println("3. MULTIPLE RESOURCES");
        System.out.println("---------------------");
        
        String inputPath = DEMO_DIR + "/sample.txt";
        String outputPath = DEMO_DIR + "/copy.txt";
        
        // Copy file using multiple resources
        System.out.println("   Copying file using multiple resources:");
        try (InputStream in = new FileInputStream(inputPath);
             OutputStream out = new FileOutputStream(outputPath)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            
            System.out.println("   Copied " + totalBytes + " bytes");
            System.out.println("   Both streams will be closed automatically");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Multiple readers
        String path1 = DEMO_DIR + "/sample.txt";
        String path2 = DEMO_DIR + "/copy.txt";
        
        System.out.println("\n   Reading from two files simultaneously:");
        try (BufferedReader reader1 = new BufferedReader(new FileReader(path1));
             BufferedReader reader2 = new BufferedReader(new FileReader(path2))) {
            
            String line1 = reader1.readLine();
            String line2 = reader2.readLine();
            
            System.out.println("   File 1 first line: " + line1);
            System.out.println("   File 2 first line: " + line2);
            System.out.println("   Files are identical: " + line1.equals(line2));
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Note about closing order
        System.out.println("\n   NOTE: Resources are closed in REVERSE order");
        System.out.println("   If opened: A, B, C");
        System.out.println("   Closed in: C, B, A order");
        
        System.out.println();
    }

    /**
     * Custom AutoCloseable resources
     */
    private static void demonstrateCustomAutoCloseable() {
        System.out.println("4. CUSTOM AUTOCLOSEABLE RESOURCES");
        System.out.println("----------------------------------");
        
        // Using custom resource
        System.out.println("   Using custom DatabaseConnection:");
        try (DatabaseConnection conn = new DatabaseConnection("mydb")) {
            conn.query("SELECT * FROM users");
            conn.query("SELECT * FROM orders");
        }
        
        System.out.println("\n   Using custom FileProcessor:");
        try (FileProcessor processor = new FileProcessor(DEMO_DIR + "/sample.txt")) {
            processor.process();
        } catch (Exception e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Closeable vs AutoCloseable
        System.out.println("\n   Closeable vs AutoCloseable:");
        System.out.println("   - AutoCloseable: close() throws Exception");
        System.out.println("   - Closeable: close() throws IOException (more specific)");
        System.out.println("   - Closeable extends AutoCloseable");
        System.out.println("   - Use Closeable for I/O resources");
        System.out.println("   - Use AutoCloseable for other resources");
        
        System.out.println();
    }

    /**
     * Suppressed exceptions handling
     */
    private static void demonstrateSuppressedExceptions() {
        System.out.println("5. SUPPRESSED EXCEPTIONS");
        System.out.println("------------------------");
        
        System.out.println("   When exception occurs in try AND close():");
        System.out.println("   - Main exception is thrown");
        System.out.println("   - Close exception is 'suppressed'");
        System.out.println("   - Can retrieve with getSuppressed()");
        
        try (ProblematicResource resource = new ProblematicResource()) {
            resource.doWork(); // This throws exception
        } catch (Exception e) {
            System.out.println("\n   Caught main exception: " + e.getMessage());
            
            Throwable[] suppressed = e.getSuppressed();
            System.out.println("   Number of suppressed exceptions: " + suppressed.length);
            
            for (Throwable t : suppressed) {
                System.out.println("   Suppressed: " + t.getMessage());
            }
        }
        
        System.out.println();
    }

    /**
     * Effectively final variables (Java 9+)
     */
    private static void demonstrateEffectivelyFinal() {
        System.out.println("6. EFFECTIVELY FINAL VARIABLES (Java 9+)");
        System.out.println("-----------------------------------------");
        
        System.out.println("   Java 9 allows using effectively final variables");
        System.out.println("   in try-with-resources:");
        System.out.println();
        System.out.println("   // Java 7-8 style:");
        System.out.println("   try (BufferedReader br = existingReader) {...}");
        System.out.println();
        System.out.println("   // Java 9+ style:");
        System.out.println("   BufferedReader br = new BufferedReader(...);");
        System.out.println("   try (br) {  // Can use existing variable directly");
        System.out.println("       // use br");
        System.out.println("   }");
        
        // Demonstrate with actual code (Java 9+ syntax shown conceptually)
        String filePath = DEMO_DIR + "/sample.txt";
        
        // For Java 8 compatibility, we use standard try-with-resources
        // In Java 9+, you could write: try (existingReader) { ... }
        try (BufferedReader existingReader = new BufferedReader(new FileReader(filePath))) {
            String line = existingReader.readLine();
            System.out.println("\n   Read line: " + line);
            System.out.println("   (In Java 9+, can use pre-declared effectively final variables)");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Multiple resources (Java 8 compatible syntax)
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(DEMO_DIR + "/output2.txt"))) {
            String line = reader.readLine();
            writer.write("Copied: " + line);
            System.out.println("   Demonstrated multiple resources");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
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

// ==================== Custom AutoCloseable Classes ====================

/**
 * Custom AutoCloseable - simulates a database connection
 */
class DatabaseConnection implements AutoCloseable {
    private String dbName;
    private boolean isOpen;
    
    public DatabaseConnection(String dbName) {
        this.dbName = dbName;
        this.isOpen = true;
        System.out.println("   -> Opened connection to: " + dbName);
    }
    
    public void query(String sql) {
        if (!isOpen) {
            throw new IllegalStateException("Connection is closed");
        }
        System.out.println("   -> Executing: " + sql);
    }
    
    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            System.out.println("   -> Closed connection to: " + dbName);
        }
    }
}

/**
 * Custom Closeable - file processor
 */
class FileProcessor implements Closeable {
    private String filePath;
    private boolean processing;
    
    public FileProcessor(String filePath) throws IOException {
        this.filePath = filePath;
        this.processing = true;
        System.out.println("   -> Started processing: " + filePath);
    }
    
    public void process() {
        System.out.println("   -> Processing file...");
        System.out.println("   -> Processing complete!");
    }
    
    @Override
    public void close() throws IOException {
        if (processing) {
            processing = false;
            System.out.println("   -> Closed file processor");
        }
    }
}

/**
 * Resource that throws exception on close - for suppressed exception demo
 */
class ProblematicResource implements AutoCloseable {
    public ProblematicResource() {
        System.out.println("   -> Created problematic resource");
    }
    
    public void doWork() throws Exception {
        System.out.println("   -> Doing work... then throwing exception!");
        throw new Exception("Exception from doWork()");
    }
    
    @Override
    public void close() throws Exception {
        System.out.println("   -> Closing... and throwing another exception!");
        throw new Exception("Exception from close()");
    }
}

