package com.filehandling;

import java.io.File;
import java.io.IOException;

/**
 * Demonstrates basic file operations in Java:
 * - Creating files and directories
 * - Checking file existence and properties
 * - Getting file information
 * - Deleting files and directories
 */
public class BasicFileOperations {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    BASIC FILE OPERATIONS IN JAVA");
        System.out.println("========================================\n");

        // 1. Creating a File object (doesn't create actual file)
        File file = new File("demo_files/sample.txt");
        System.out.println("1. FILE OBJECT CREATION");
        System.out.println("   File object created for: " + file.getPath());
        System.out.println("   Absolute path: " + file.getAbsolutePath());
        System.out.println();

        // 2. Creating parent directories
        System.out.println("2. CREATING DIRECTORIES");
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            System.out.println("   Directory created: " + dirCreated);
            System.out.println("   Directory path: " + parentDir.getAbsolutePath());
        } else {
            System.out.println("   Directory already exists");
        }
        System.out.println();

        // 3. Creating a new file
        System.out.println("3. CREATING A NEW FILE");
        try {
            if (file.createNewFile()) {
                System.out.println("   File created successfully: " + file.getName());
            } else {
                System.out.println("   File already exists: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("   Error creating file: " + e.getMessage());
        }
        System.out.println();

        // 4. Checking file existence
        System.out.println("4. CHECKING FILE EXISTENCE");
        System.out.println("   File exists: " + file.exists());
        System.out.println("   Is a file: " + file.isFile());
        System.out.println("   Is a directory: " + file.isDirectory());
        System.out.println();

        // 5. Getting file information
        System.out.println("5. FILE INFORMATION");
        System.out.println("   File name: " + file.getName());
        System.out.println("   Parent directory: " + file.getParent());
        System.out.println("   Absolute path: " + file.getAbsolutePath());
        System.out.println("   File size (bytes): " + file.length());
        System.out.println("   Last modified: " + new java.util.Date(file.lastModified()));
        System.out.println("   Can read: " + file.canRead());
        System.out.println("   Can write: " + file.canWrite());
        System.out.println("   Can execute: " + file.canExecute());
        System.out.println("   Is hidden: " + file.isHidden());
        System.out.println();

        // 6. Creating multiple directories
        System.out.println("6. CREATING NESTED DIRECTORIES");
        File nestedDirs = new File("demo_files/level1/level2/level3");
        if (nestedDirs.mkdirs()) {
            System.out.println("   Nested directories created: " + nestedDirs.getPath());
        } else {
            System.out.println("   Directories already exist or couldn't be created");
        }
        System.out.println();

        // 7. Renaming a file
        System.out.println("7. RENAMING A FILE");
        File renamedFile = new File("demo_files/renamed_sample.txt");
        if (file.renameTo(renamedFile)) {
            System.out.println("   File renamed from '" + file.getName() + "' to '" + renamedFile.getName() + "'");
            // Update reference for further operations
            file = renamedFile;
        } else {
            System.out.println("   Failed to rename file");
        }
        System.out.println();

        // 8. Getting free space information
        System.out.println("8. DISK SPACE INFORMATION");
        System.out.println("   Total space: " + formatBytes(file.getTotalSpace()));
        System.out.println("   Free space: " + formatBytes(file.getFreeSpace()));
        System.out.println("   Usable space: " + formatBytes(file.getUsableSpace()));
        System.out.println();

        // 9. Listing files in a directory
        System.out.println("9. LISTING DIRECTORY CONTENTS");
        File dir = new File("demo_files");
        String[] fileList = dir.list();
        if (fileList != null) {
            System.out.println("   Contents of 'demo_files':");
            for (String fileName : fileList) {
                File f = new File(dir, fileName);
                String type = f.isDirectory() ? "[DIR] " : "[FILE]";
                System.out.println("   " + type + " " + fileName);
            }
        }
        System.out.println();

        // 10. Listing files with a filter
        System.out.println("10. LISTING FILES WITH FILTER (.txt files only)");
        File[] txtFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (txtFiles != null && txtFiles.length > 0) {
            for (File txtFile : txtFiles) {
                System.out.println("    " + txtFile.getName());
            }
        } else {
            System.out.println("    No .txt files found");
        }
        System.out.println();

        // 11. Deleting files and directories
        System.out.println("11. DELETING FILES AND DIRECTORIES");
        
        // Delete the renamed file
        if (file.delete()) {
            System.out.println("    Deleted: " + file.getName());
        }
        
        // Delete nested directories (must be empty to delete)
        deleteDirectory(new File("demo_files/level1"));
        System.out.println("    Deleted nested directories");
        
        // Delete parent directory (if empty)
        if (parentDir != null && parentDir.delete()) {
            System.out.println("    Deleted: " + parentDir.getName());
        }
        System.out.println();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Recursively deletes a directory and all its contents
     */
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteDirectory(f);
                    } else {
                        f.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    /**
     * Formats bytes into human-readable format
     */
    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }
}

