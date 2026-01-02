package com.filehandling;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Demonstrates directory operations in Java:
 * - Creating directories
 * - Listing directory contents
 * - Filtering files
 * - Recursive directory operations
 * - Directory size calculation
 */
public class DirectoryOperationsDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    DIRECTORY OPERATIONS DEMO");
        System.out.println("========================================\n");

        // 1. Creating directories
        demonstrateCreateDirectories();

        // 2. Listing directory contents
        demonstrateListContents();

        // 3. Filtering files
        demonstrateFiltering();

        // 4. Directory information
        demonstrateDirectoryInfo();

        // 5. Recursive operations
        demonstrateRecursiveOperations();

        // 6. Temporary directories
        demonstrateTempDirectories();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Creating directories
     */
    private static void demonstrateCreateDirectories() {
        System.out.println("1. CREATING DIRECTORIES");
        System.out.println("-----------------------");
        
        // mkdir() - creates single directory
        File singleDir = new File(DEMO_DIR);
        if (singleDir.mkdir()) {
            System.out.println("   Created: " + singleDir.getPath());
        } else if (singleDir.exists()) {
            System.out.println("   Already exists: " + singleDir.getPath());
        }
        
        // mkdirs() - creates parent directories if needed
        File nestedDirs = new File(DEMO_DIR + "/level1/level2/level3");
        if (nestedDirs.mkdirs()) {
            System.out.println("   Created nested: " + nestedDirs.getPath());
        }
        
        // Create multiple directories
        String[] dirs = {"documents", "images", "music", "videos"};
        for (String dir : dirs) {
            File d = new File(DEMO_DIR + "/" + dir);
            if (d.mkdir()) {
                System.out.println("   Created: " + d.getName());
            }
        }
        
        // Create sample files
        createSampleFiles();
        
        System.out.println();
    }

    private static void createSampleFiles() {
        String[][] files = {
            {"documents/report.txt", "Report content"},
            {"documents/notes.txt", "Notes content"},
            {"documents/summary.doc", "Summary document"},
            {"images/photo1.jpg", "image data"},
            {"images/photo2.png", "image data"},
            {"music/song1.mp3", "audio data"},
            {"videos/video1.mp4", "video data"},
            {"readme.txt", "README content"},
            {"config.xml", "<config></config>"},
            {"data.json", "{\"key\": \"value\"}"}
        };
        
        for (String[] file : files) {
            try {
                Path path = Paths.get(DEMO_DIR, file[0]);
                Files.createDirectories(path.getParent());
                Files.writeString(path, file[1]);
            } catch (IOException e) {
                // Ignore
            }
        }
        System.out.println("   Created sample files for demonstration");
    }

    /**
     * Listing directory contents
     */
    private static void demonstrateListContents() {
        System.out.println("2. LISTING DIRECTORY CONTENTS");
        System.out.println("-----------------------------");
        
        File dir = new File(DEMO_DIR);
        
        // list() - returns String array of file names
        System.out.println("   Method 1: list() - Returns names only");
        String[] names = dir.list();
        if (names != null) {
            System.out.println("   Contents of " + dir.getName() + ":");
            for (String name : names) {
                System.out.println("   - " + name);
            }
        }
        
        // listFiles() - returns File array
        System.out.println("\n   Method 2: listFiles() - Returns File objects");
        File[] files = dir.listFiles();
        if (files != null) {
            System.out.println("   Contents with details:");
            for (File file : files) {
                String type = file.isDirectory() ? "[DIR] " : "[FILE]";
                String size = file.isFile() ? " (" + file.length() + " bytes)" : "";
                System.out.println("   " + type + " " + file.getName() + size);
            }
        }
        
        // Sorted listing
        System.out.println("\n   Sorted by name:");
        if (files != null) {
            Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            for (File file : files) {
                System.out.println("   - " + file.getName());
            }
        }
        
        System.out.println();
    }

    /**
     * Filtering files
     */
    private static void demonstrateFiltering() {
        System.out.println("3. FILTERING FILES");
        System.out.println("------------------");
        
        File dir = new File(DEMO_DIR);
        
        // FilenameFilter - filter by name
        System.out.println("   FilenameFilter: .txt files only");
        FilenameFilter txtFilter = (d, name) -> name.endsWith(".txt");
        String[] txtFiles = dir.list(txtFilter);
        if (txtFiles != null) {
            for (String name : txtFiles) {
                System.out.println("   - " + name);
            }
        }
        
        // FileFilter - filter by File properties
        System.out.println("\n   FileFilter: directories only");
        FileFilter dirFilter = File::isDirectory;
        File[] directories = dir.listFiles(dirFilter);
        if (directories != null) {
            for (File d : directories) {
                System.out.println("   - " + d.getName() + "/");
            }
        }
        
        // FileFilter - files only
        System.out.println("\n   FileFilter: files only");
        File[] filesOnly = dir.listFiles(File::isFile);
        if (filesOnly != null) {
            for (File f : filesOnly) {
                System.out.println("   - " + f.getName());
            }
        }
        
        // Complex filter
        System.out.println("\n   Complex filter: files > 10 bytes");
        FileFilter sizeFilter = f -> f.isFile() && f.length() > 10;
        File[] largeFiles = dir.listFiles(sizeFilter);
        if (largeFiles != null) {
            for (File f : largeFiles) {
                System.out.println("   - " + f.getName() + " (" + f.length() + " bytes)");
            }
        }
        
        // Filter in subdirectory
        System.out.println("\n   Filtering in 'documents' subdirectory:");
        File docsDir = new File(DEMO_DIR + "/documents");
        File[] docs = docsDir.listFiles();
        if (docs != null) {
            for (File doc : docs) {
                System.out.println("   - " + doc.getName());
            }
        }
        
        System.out.println();
    }

    /**
     * Directory information
     */
    private static void demonstrateDirectoryInfo() {
        System.out.println("4. DIRECTORY INFORMATION");
        System.out.println("------------------------");
        
        File dir = new File(DEMO_DIR);
        
        System.out.println("   Directory: " + dir.getAbsolutePath());
        System.out.println("   Exists: " + dir.exists());
        System.out.println("   Is directory: " + dir.isDirectory());
        System.out.println("   Can read: " + dir.canRead());
        System.out.println("   Can write: " + dir.canWrite());
        System.out.println("   Can execute: " + dir.canExecute());
        System.out.println("   Is hidden: " + dir.isHidden());
        System.out.println("   Last modified: " + new java.util.Date(dir.lastModified()));
        
        // Count contents
        File[] contents = dir.listFiles();
        int fileCount = 0, dirCount = 0;
        if (contents != null) {
            for (File f : contents) {
                if (f.isFile()) fileCount++;
                else if (f.isDirectory()) dirCount++;
            }
        }
        System.out.println("   Direct files: " + fileCount);
        System.out.println("   Direct subdirs: " + dirCount);
        
        // Disk space (for the partition containing this directory)
        System.out.println("\n   Disk space information:");
        System.out.println("   Total space: " + formatSize(dir.getTotalSpace()));
        System.out.println("   Free space: " + formatSize(dir.getFreeSpace()));
        System.out.println("   Usable space: " + formatSize(dir.getUsableSpace()));
        
        System.out.println();
    }

    /**
     * Recursive operations
     */
    private static void demonstrateRecursiveOperations() {
        System.out.println("5. RECURSIVE OPERATIONS");
        System.out.println("-----------------------");
        
        File dir = new File(DEMO_DIR);
        
        // Recursive listing
        System.out.println("   Recursive listing (tree view):");
        printDirectoryTree(dir, "   ");
        
        // Count all files recursively
        int[] counts = countFilesRecursively(dir);
        System.out.println("\n   Recursive counts:");
        System.out.println("   Total files: " + counts[0]);
        System.out.println("   Total directories: " + counts[1]);
        
        // Calculate total size
        long totalSize = calculateDirectorySize(dir);
        System.out.println("   Total size: " + formatSize(totalSize));
        
        // Find all files of a type recursively
        System.out.println("\n   All .txt files (recursive):");
        findFilesRecursively(dir, ".txt");
        
        System.out.println();
    }

    private static void printDirectoryTree(File dir, String indent) {
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) return -1;
                if (!f1.isDirectory() && f2.isDirectory()) return 1;
                return f1.getName().compareToIgnoreCase(f2.getName());
            });
            
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println(indent + "├── " + file.getName() + "/");
                    printDirectoryTree(file, indent + "│   ");
                } else {
                    System.out.println(indent + "├── " + file.getName());
                }
            }
        }
    }

    private static int[] countFilesRecursively(File dir) {
        int[] counts = {0, 0}; // [files, directories]
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    counts[0]++;
                } else if (file.isDirectory()) {
                    counts[1]++;
                    int[] subCounts = countFilesRecursively(file);
                    counts[0] += subCounts[0];
                    counts[1] += subCounts[1];
                }
            }
        }
        return counts;
    }

    private static long calculateDirectorySize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += calculateDirectorySize(file);
                }
            }
        }
        return size;
    }

    private static void findFilesRecursively(File dir, String extension) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(extension)) {
                    System.out.println("   - " + file.getPath());
                } else if (file.isDirectory()) {
                    findFilesRecursively(file, extension);
                }
            }
        }
    }

    /**
     * Temporary directories
     */
    private static void demonstrateTempDirectories() {
        System.out.println("6. TEMPORARY DIRECTORIES");
        System.out.println("------------------------");
        
        // Get system temp directory
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.println("   System temp directory: " + tempDir);
        
        // Create temp directory using NIO
        try {
            Path tempPath = Files.createTempDirectory("javaDemo_");
            System.out.println("   Created temp dir: " + tempPath);
            
            // Create a temp file in that directory
            Path tempFile = Files.createTempFile(tempPath, "temp_", ".txt");
            System.out.println("   Created temp file: " + tempFile.getFileName());
            
            // Clean up
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(tempPath);
            System.out.println("   Cleaned up temp directory");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Using File.createTempFile
        try {
            File tempFile = File.createTempFile("prefix_", "_suffix.tmp");
            System.out.println("\n   Created temp file (File API): " + tempFile.getName());
            System.out.println("   Location: " + tempFile.getParent());
            tempFile.deleteOnExit(); // Scheduled for deletion on JVM exit
            System.out.println("   Scheduled for deletion on JVM exit");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private static void cleanup() {
        System.out.println("CLEANUP");
        System.out.println("-------");
        deleteDirectory(new File(DEMO_DIR));
        System.out.println("Deleted demo directory and contents");
        System.out.println();
    }

    private static void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }
}

