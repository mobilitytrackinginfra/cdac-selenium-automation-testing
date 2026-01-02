package com.filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Demonstrates Java NIO (New I/O) file operations:
 * - Path and Paths class
 * - Files utility class
 * - Reading/Writing with NIO
 * - File attributes
 * - Directory operations with NIO
 * - FileChannel for efficient I/O
 * - Walking file trees
 */
public class NIOFileDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    JAVA NIO FILE DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        try {
            Files.createDirectories(Paths.get(DEMO_DIR));
        } catch (IOException e) {
            System.out.println("Error creating demo directory: " + e.getMessage());
        }

        // 1. Path and Paths basics
        demonstratePathBasics();

        // 2. Files utility class
        demonstrateFilesClass();

        // 3. Reading files with NIO
        demonstrateNIOReading();

        // 4. Writing files with NIO
        demonstrateNIOWriting();

        // 5. File attributes
        demonstrateFileAttributes();

        // 6. Directory operations
        demonstrateDirectoryOperations();

        // 7. FileChannel
        demonstrateFileChannel();

        // 8. Walking file trees
        demonstrateFileTreeWalking();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Path and Paths class basics
     */
    private static void demonstratePathBasics() {
        System.out.println("1. PATH AND PATHS BASICS");
        System.out.println("------------------------");
        
        // Creating Path objects
        Path path1 = Paths.get("demo_files/sample.txt");
        Path path2 = Paths.get("demo_files", "subdir", "file.txt");
        Path path3 = Path.of("demo_files/another.txt");  // Java 11+
        
        System.out.println("   Path creation:");
        System.out.println("   path1: " + path1);
        System.out.println("   path2: " + path2);
        System.out.println("   path3: " + path3);
        
        // Path components
        System.out.println("\n   Path components for: " + path2);
        System.out.println("   File name: " + path2.getFileName());
        System.out.println("   Parent: " + path2.getParent());
        System.out.println("   Root: " + path2.getRoot());
        System.out.println("   Name count: " + path2.getNameCount());
        for (int i = 0; i < path2.getNameCount(); i++) {
            System.out.println("   Name[" + i + "]: " + path2.getName(i));
        }
        
        // Path operations
        System.out.println("\n   Path operations:");
        Path absolute = path1.toAbsolutePath();
        System.out.println("   Absolute: " + absolute);
        
        Path normalized = Paths.get("demo_files/../demo_files/./sample.txt").normalize();
        System.out.println("   Normalized: " + normalized);
        
        // Resolving paths
        Path base = Paths.get("demo_files");
        Path resolved = base.resolve("subdir/file.txt");
        System.out.println("   Resolved: " + resolved);
        
        // Relativizing paths
        Path path4 = Paths.get("demo_files/subdir");
        Path path5 = Paths.get("demo_files/other/file.txt");
        Path relative = path4.relativize(path5);
        System.out.println("   Relative path from " + path4 + " to " + path5 + ": " + relative);
        
        // Converting between Path and File
        File file = path1.toFile();
        Path fromFile = file.toPath();
        System.out.println("\n   Path -> File -> Path: " + fromFile);
        
        System.out.println();
    }

    /**
     * Files utility class methods
     */
    private static void demonstrateFilesClass() {
        System.out.println("2. FILES UTILITY CLASS");
        System.out.println("----------------------");
        
        Path testFile = Paths.get(DEMO_DIR, "test_file.txt");
        Path testDir = Paths.get(DEMO_DIR, "test_subdir");
        
        try {
            // Create file
            if (!Files.exists(testFile)) {
                Files.createFile(testFile);
                System.out.println("   Created file: " + testFile);
            }
            
            // Create directory
            if (!Files.exists(testDir)) {
                Files.createDirectory(testDir);
                System.out.println("   Created directory: " + testDir);
            }
            
            // Create nested directories
            Path nested = Paths.get(DEMO_DIR, "a", "b", "c");
            Files.createDirectories(nested);
            System.out.println("   Created nested directories: " + nested);
            
            // Check file properties
            System.out.println("\n   File properties for: " + testFile);
            System.out.println("   Exists: " + Files.exists(testFile));
            System.out.println("   Is regular file: " + Files.isRegularFile(testFile));
            System.out.println("   Is directory: " + Files.isDirectory(testFile));
            System.out.println("   Is readable: " + Files.isReadable(testFile));
            System.out.println("   Is writable: " + Files.isWritable(testFile));
            System.out.println("   Is executable: " + Files.isExecutable(testFile));
            System.out.println("   Is hidden: " + Files.isHidden(testFile));
            
            // Copy file
            Path copyDest = Paths.get(DEMO_DIR, "test_file_copy.txt");
            Files.copy(testFile, copyDest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("\n   Copied file to: " + copyDest);
            
            // Move/rename file
            Path moveDest = Paths.get(DEMO_DIR, "test_file_renamed.txt");
            Files.move(copyDest, moveDest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("   Moved/renamed file to: " + moveDest);
            
            // Delete file
            Files.deleteIfExists(moveDest);
            System.out.println("   Deleted: " + moveDest);
            
            // Get file size
            Files.writeString(testFile, "Hello NIO!");
            System.out.println("\n   File size: " + Files.size(testFile) + " bytes");
            
            // Get content type
            String contentType = Files.probeContentType(testFile);
            System.out.println("   Content type: " + contentType);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading files with NIO
     */
    private static void demonstrateNIOReading() {
        System.out.println("3. READING FILES WITH NIO");
        System.out.println("-------------------------");
        
        Path filePath = Paths.get(DEMO_DIR, "read_demo.txt");
        
        // Create sample file
        try {
            List<String> lines = List.of(
                "Line 1: Hello NIO!",
                "Line 2: Java NIO makes file handling easier.",
                "Line 3: Files class provides convenient methods.",
                "Line 4: Path interface represents file paths.",
                "Line 5: End of demo file."
            );
            Files.write(filePath, lines);
            System.out.println("   Created sample file for reading demos\n");
        } catch (IOException e) {
            System.out.println("   Error creating file: " + e.getMessage());
            return;
        }
        
        // Method 1: Read all bytes
        System.out.println("   Method 1: readAllBytes()");
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            System.out.println("   Read " + bytes.length + " bytes");
            System.out.println("   First 50 chars: " + new String(bytes, 0, Math.min(50, bytes.length)) + "...");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 2: Read all lines
        System.out.println("\n   Method 2: readAllLines()");
        try {
            List<String> lines = Files.readAllLines(filePath);
            System.out.println("   Read " + lines.size() + " lines");
            lines.forEach(line -> System.out.println("   > " + line));
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 3: Read as string (Java 11+)
        System.out.println("\n   Method 3: readString() [Java 11+]");
        try {
            String content = Files.readString(filePath);
            System.out.println("   Content length: " + content.length() + " chars");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 4: lines() stream (lazy reading)
        System.out.println("\n   Method 4: lines() stream (lazy)");
        try (Stream<String> lineStream = Files.lines(filePath)) {
            long count = lineStream.filter(line -> line.contains("NIO")).count();
            System.out.println("   Lines containing 'NIO': " + count);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 5: BufferedReader via Files.newBufferedReader
        System.out.println("\n   Method 5: newBufferedReader()");
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            System.out.println("   First line: " + firstLine);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Writing files with NIO
     */
    private static void demonstrateNIOWriting() {
        System.out.println("4. WRITING FILES WITH NIO");
        System.out.println("-------------------------");
        
        // Method 1: write bytes
        System.out.println("   Method 1: write(byte[])");
        Path file1 = Paths.get(DEMO_DIR, "write_bytes.txt");
        try {
            byte[] data = "Hello from NIO write!".getBytes(StandardCharsets.UTF_8);
            Files.write(file1, data);
            System.out.println("   Written to: " + file1);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 2: write lines
        System.out.println("\n   Method 2: write(Iterable<String>)");
        Path file2 = Paths.get(DEMO_DIR, "write_lines.txt");
        try {
            List<String> lines = List.of("Line 1", "Line 2", "Line 3");
            Files.write(file2, lines);
            System.out.println("   Written " + lines.size() + " lines to: " + file2);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 3: writeString (Java 11+)
        System.out.println("\n   Method 3: writeString() [Java 11+]");
        Path file3 = Paths.get(DEMO_DIR, "write_string.txt");
        try {
            Files.writeString(file3, "Direct string writing with NIO!\nSecond line.");
            System.out.println("   Written string to: " + file3);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 4: Append mode
        System.out.println("\n   Method 4: write() with APPEND option");
        try {
            Files.write(file2, List.of("Line 4 (appended)", "Line 5 (appended)"),
                StandardOpenOption.APPEND);
            System.out.println("   Appended 2 lines to: " + file2);
            
            // Verify
            List<String> allLines = Files.readAllLines(file2);
            System.out.println("   Total lines now: " + allLines.size());
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 5: BufferedWriter via Files.newBufferedWriter
        System.out.println("\n   Method 5: newBufferedWriter()");
        Path file4 = Paths.get(DEMO_DIR, "buffered_write.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file4, StandardCharsets.UTF_8)) {
            writer.write("Written with BufferedWriter");
            writer.newLine();
            writer.write("Second line with BufferedWriter");
            System.out.println("   Written with BufferedWriter to: " + file4);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 6: Create and write with options
        System.out.println("\n   Method 6: write() with multiple options");
        Path file5 = Paths.get(DEMO_DIR, "options_write.txt");
        try {
            Files.write(file5, "Content with options".getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
            System.out.println("   Written with options to: " + file5);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * File attributes
     */
    private static void demonstrateFileAttributes() {
        System.out.println("5. FILE ATTRIBUTES");
        System.out.println("------------------");
        
        Path filePath = Paths.get(DEMO_DIR, "attributes_demo.txt");
        
        try {
            Files.writeString(filePath, "File for attribute demonstration");
            
            // Basic attributes
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            
            System.out.println("   Basic attributes for: " + filePath.getFileName());
            System.out.println("   Creation time: " + attrs.creationTime());
            System.out.println("   Last modified: " + attrs.lastModifiedTime());
            System.out.println("   Last access: " + attrs.lastAccessTime());
            System.out.println("   Size: " + attrs.size() + " bytes");
            System.out.println("   Is regular file: " + attrs.isRegularFile());
            System.out.println("   Is directory: " + attrs.isDirectory());
            System.out.println("   Is symbolic link: " + attrs.isSymbolicLink());
            System.out.println("   Is other: " + attrs.isOther());
            System.out.println("   File key: " + attrs.fileKey());
            
            // Modify attributes
            System.out.println("\n   Modifying last modified time:");
            FileTime newTime = FileTime.fromMillis(System.currentTimeMillis() - 86400000);
            Files.setLastModifiedTime(filePath, newTime);
            System.out.println("   Set last modified to 1 day ago: " + newTime);
            
            // Read modified attribute
            System.out.println("   Verified: " + Files.getLastModifiedTime(filePath));
            
            // Owner (platform dependent)
            System.out.println("\n   File owner: " + Files.getOwner(filePath));
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Directory operations with NIO
     */
    private static void demonstrateDirectoryOperations() {
        System.out.println("6. DIRECTORY OPERATIONS");
        System.out.println("-----------------------");
        
        Path dirPath = Paths.get(DEMO_DIR);
        
        // Create some files for demonstration
        try {
            Files.writeString(Paths.get(DEMO_DIR, "file1.txt"), "content1");
            Files.writeString(Paths.get(DEMO_DIR, "file2.txt"), "content2");
            Files.writeString(Paths.get(DEMO_DIR, "data.csv"), "a,b,c");
            Files.writeString(Paths.get(DEMO_DIR, "config.xml"), "<root/>");
        } catch (IOException e) {
            System.out.println("   Error creating files: " + e.getMessage());
        }
        
        // Method 1: DirectoryStream (lazy iteration)
        System.out.println("   Method 1: DirectoryStream");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            System.out.println("   All entries in " + dirPath + ":");
            for (Path entry : stream) {
                String type = Files.isDirectory(entry) ? "[DIR]" : "[FILE]";
                System.out.println("   " + type + " " + entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 2: DirectoryStream with glob filter
        System.out.println("\n   Method 2: DirectoryStream with glob filter (*.txt)");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.txt")) {
            for (Path entry : stream) {
                System.out.println("   " + entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 3: Files.list() stream
        System.out.println("\n   Method 3: Files.list() stream");
        try (Stream<Path> stream = Files.list(dirPath)) {
            long count = stream.filter(Files::isRegularFile).count();
            System.out.println("   Regular files count: " + count);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 4: Filter by predicate
        System.out.println("\n   Method 4: DirectoryStream with filter predicate");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, 
                path -> Files.size(path) > 5)) {
            System.out.println("   Files larger than 5 bytes:");
            for (Path entry : stream) {
                System.out.println("   " + entry.getFileName() + " (" + Files.size(entry) + " bytes)");
            }
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * FileChannel for efficient I/O
     */
    private static void demonstrateFileChannel() {
        System.out.println("7. FILECHANNEL");
        System.out.println("--------------");
        
        Path filePath = Paths.get(DEMO_DIR, "channel_demo.txt");
        
        // Write with FileChannel
        System.out.println("   Writing with FileChannel:");
        try (FileChannel channel = FileChannel.open(filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            
            String data = "Hello from FileChannel! This is efficient binary I/O.";
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
            
            int bytesWritten = channel.write(buffer);
            System.out.println("   Bytes written: " + bytesWritten);
            System.out.println("   Channel position: " + channel.position());
            System.out.println("   Channel size: " + channel.size());
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read with FileChannel
        System.out.println("\n   Reading with FileChannel:");
        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            
            ByteBuffer buffer = ByteBuffer.allocate(64);
            int bytesRead = channel.read(buffer);
            
            buffer.flip();  // Prepare for reading from buffer
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            
            System.out.println("   Bytes read: " + bytesRead);
            System.out.println("   Content: " + new String(bytes));
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Random access with FileChannel
        System.out.println("\n   Random access with FileChannel:");
        try (FileChannel channel = FileChannel.open(filePath, 
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            
            // Read from specific position
            ByteBuffer buffer = ByteBuffer.allocate(10);
            channel.read(buffer, 6);  // Read starting at position 6
            buffer.flip();
            System.out.println("   Read at pos 6: " + StandardCharsets.UTF_8.decode(buffer));
            
            // Write at specific position
            buffer = ByteBuffer.wrap("INSERTED".getBytes());
            channel.write(buffer, 30);
            System.out.println("   Wrote 'INSERTED' at position 30");
            
            // Truncate file
            channel.truncate(50);
            System.out.println("   Truncated file to 50 bytes");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Memory-mapped file (for large files)
        System.out.println("\n   Note: FileChannel also supports:");
        System.out.println("   - Memory-mapped files (map() method)");
        System.out.println("   - File locking (lock() method)");
        System.out.println("   - Transfer to/from other channels");
        
        System.out.println();
    }

    /**
     * Walking file trees
     */
    private static void demonstrateFileTreeWalking() {
        System.out.println("8. WALKING FILE TREES");
        System.out.println("---------------------");
        
        Path startDir = Paths.get(DEMO_DIR);
        
        // Create nested structure for demo
        try {
            Files.createDirectories(Paths.get(DEMO_DIR, "subdir1"));
            Files.createDirectories(Paths.get(DEMO_DIR, "subdir2/nested"));
            Files.writeString(Paths.get(DEMO_DIR, "subdir1/file1.txt"), "content");
            Files.writeString(Paths.get(DEMO_DIR, "subdir2/file2.txt"), "content");
            Files.writeString(Paths.get(DEMO_DIR, "subdir2/nested/file3.txt"), "content");
        } catch (IOException e) {
            System.out.println("   Error creating structure: " + e.getMessage());
        }
        
        // Method 1: Files.walk() stream
        System.out.println("   Method 1: Files.walk() stream");
        try (Stream<Path> walk = Files.walk(startDir)) {
            walk.forEach(path -> {
                String indent = "   " + "  ".repeat(startDir.relativize(path).getNameCount());
                String type = Files.isDirectory(path) ? "[D]" : "[F]";
                System.out.println(indent + type + " " + path.getFileName());
            });
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 2: Files.walk() with depth limit
        System.out.println("\n   Method 2: Files.walk() with maxDepth=1");
        try (Stream<Path> walk = Files.walk(startDir, 1)) {
            walk.forEach(path -> System.out.println("   " + path.getFileName()));
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 3: Files.find() with filter
        System.out.println("\n   Method 3: Files.find() - all .txt files");
        try (Stream<Path> found = Files.find(startDir, Integer.MAX_VALUE,
                (path, attrs) -> path.toString().endsWith(".txt") && attrs.isRegularFile())) {
            found.forEach(path -> System.out.println("   Found: " + startDir.relativize(path)));
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 4: FileVisitor for complex operations
        System.out.println("\n   Method 4: FileVisitor pattern");
        try {
            Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    System.out.println("   Entering: " + dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("   Visiting: " + file.getFileName() + 
                        " (" + attrs.size() + " bytes)");
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    System.out.println("   Leaving: " + dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    private static void cleanup() {
        System.out.println("CLEANUP");
        System.out.println("-------");
        
        Path dir = Paths.get(DEMO_DIR);
        try {
            // Walk in reverse order to delete files first, then directories
            Files.walk(dir)
                .sorted((a, b) -> -a.compareTo(b))  // Reverse order
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        System.out.println("Deleted: " + path.getFileName());
                    } catch (IOException e) {
                        System.out.println("Failed to delete: " + path);
                    }
                });
        } catch (IOException e) {
            System.out.println("Cleanup error: " + e.getMessage());
        }
        System.out.println();
    }
}

