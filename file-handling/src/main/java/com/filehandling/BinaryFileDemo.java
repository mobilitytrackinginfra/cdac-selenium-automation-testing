package com.filehandling;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Demonstrates binary file operations:
 * - FileInputStream/FileOutputStream for raw bytes
 * - BufferedInputStream/BufferedOutputStream for efficiency
 * - DataInputStream/DataOutputStream for primitive types
 * - Reading and writing binary data
 * - Copying files using streams
 */
public class BinaryFileDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    BINARY FILE OPERATIONS DEMO");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. FileOutputStream - writing raw bytes
        demonstrateFileOutputStream();

        // 2. FileInputStream - reading raw bytes
        demonstrateFileInputStream();

        // 3. Buffered streams for efficiency
        demonstrateBufferedStreams();

        // 4. DataOutputStream/DataInputStream for primitives
        demonstrateDataStreams();

        // 5. File copy using streams
        demonstrateFileCopy();

        // 6. Reading/Writing byte arrays
        demonstrateByteArrayOperations();

        // 7. Working with image-like binary data
        demonstrateBinaryDataHandling();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Writing raw bytes using FileOutputStream
     */
    private static void demonstrateFileOutputStream() {
        System.out.println("1. FILEOUTPUTSTREAM - WRITING BYTES");
        System.out.println("------------------------------------");
        
        String filePath = DEMO_DIR + "/bytes_output.bin";
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            
            // Write single byte
            fos.write(65);  // ASCII for 'A'
            fos.write(66);  // ASCII for 'B'
            fos.write(67);  // ASCII for 'C'
            
            // Write byte array
            byte[] bytes = {68, 69, 70, 71, 72};  // D, E, F, G, H
            fos.write(bytes);
            
            // Write portion of byte array
            byte[] moreBytes = {73, 74, 75, 76, 77, 78};  // I, J, K, L, M, N
            fos.write(moreBytes, 0, 3);  // Write only I, J, K
            
            // Write string as bytes
            String text = "\nHello Binary!";
            fos.write(text.getBytes());
            
            // Flush to ensure all data is written
            fos.flush();
            
            System.out.println("   Bytes written to: " + filePath);
            System.out.println("   File size: " + new File(filePath).length() + " bytes");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading raw bytes using FileInputStream
     */
    private static void demonstrateFileInputStream() {
        System.out.println("2. FILEINPUTSTREAM - READING BYTES");
        System.out.println("-----------------------------------");
        
        String filePath = DEMO_DIR + "/bytes_output.bin";
        
        // Method 1: Read byte by byte
        System.out.println("   Reading byte by byte:");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int byteData;
            int count = 0;
            System.out.print("   ");
            
            while ((byteData = fis.read()) != -1 && count < 10) {
                System.out.print(byteData + "(" + (char)byteData + ") ");
                count++;
            }
            System.out.println("...");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 2: Read into byte array
        System.out.println("\n   Reading into byte array:");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[10];
            int bytesRead;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                System.out.print("   Read " + bytesRead + " bytes: ");
                for (int i = 0; i < bytesRead; i++) {
                    System.out.print((char)buffer[i]);
                }
                System.out.println();
            }
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Method 3: Using available() and skip()
        System.out.println("\n   Using available() and skip():");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println("   Available bytes: " + fis.available());
            
            // Skip first 5 bytes
            long skipped = fis.skip(5);
            System.out.println("   Skipped " + skipped + " bytes");
            
            // Read next 5 bytes
            byte[] buffer = new byte[5];
            int read = fis.read(buffer);
            System.out.print("   Next " + read + " bytes: ");
            for (byte b : buffer) {
                System.out.print((char)b);
            }
            System.out.println();
            
            System.out.println("   Remaining bytes: " + fis.available());
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Buffered streams for efficient I/O
     */
    private static void demonstrateBufferedStreams() {
        System.out.println("3. BUFFERED STREAMS - EFFICIENT I/O");
        System.out.println("------------------------------------");
        
        String filePath = DEMO_DIR + "/buffered_binary.bin";
        int dataSize = 100000;  // 100KB of data
        
        // Write with BufferedOutputStream
        System.out.println("   Writing " + dataSize + " bytes with BufferedOutputStream:");
        long startTime = System.nanoTime();
        
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(filePath), 8192)) {  // 8KB buffer
            
            for (int i = 0; i < dataSize; i++) {
                bos.write(i % 256);
            }
            bos.flush();
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        long writeTime = System.nanoTime() - startTime;
        System.out.println("   Write time: " + (writeTime / 1_000_000.0) + " ms");
        
        // Read with BufferedInputStream
        System.out.println("\n   Reading with BufferedInputStream:");
        startTime = System.nanoTime();
        int totalBytesRead = 0;
        
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(filePath), 8192)) {  // 8KB buffer
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                totalBytesRead += bytesRead;
            }
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        long readTime = System.nanoTime() - startTime;
        System.out.println("   Total bytes read: " + totalBytesRead);
        System.out.println("   Read time: " + (readTime / 1_000_000.0) + " ms");
        
        // Compare with unbuffered (much slower for large files)
        System.out.println("\n   Performance comparison:");
        System.out.println("   Buffered I/O is typically 10-100x faster than unbuffered");
        System.out.println("   for large files due to reduced system calls");
        System.out.println();
    }

    /**
     * DataOutputStream/DataInputStream for primitive types
     */
    private static void demonstrateDataStreams() {
        System.out.println("4. DATA STREAMS - PRIMITIVE TYPES");
        System.out.println("----------------------------------");
        
        String filePath = DEMO_DIR + "/data_primitives.bin";
        
        // Write primitives
        System.out.println("   Writing primitive types:");
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {
            
            // Write various primitives
            dos.writeBoolean(true);
            dos.writeByte(127);
            dos.writeShort(32767);
            dos.writeInt(2147483647);
            dos.writeLong(9223372036854775807L);
            dos.writeFloat(3.14159f);
            dos.writeDouble(2.718281828459045);
            dos.writeChar('J');
            dos.writeUTF("Hello DataStream!");  // UTF-8 string
            
            System.out.println("   boolean: true");
            System.out.println("   byte: 127");
            System.out.println("   short: 32767");
            System.out.println("   int: 2147483647");
            System.out.println("   long: 9223372036854775807");
            System.out.println("   float: 3.14159");
            System.out.println("   double: 2.718281828459045");
            System.out.println("   char: 'J'");
            System.out.println("   UTF String: \"Hello DataStream!\"");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read primitives (must read in same order)
        System.out.println("\n   Reading primitive types:");
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {
            
            boolean boolVal = dis.readBoolean();
            byte byteVal = dis.readByte();
            short shortVal = dis.readShort();
            int intVal = dis.readInt();
            long longVal = dis.readLong();
            float floatVal = dis.readFloat();
            double doubleVal = dis.readDouble();
            char charVal = dis.readChar();
            String strVal = dis.readUTF();
            
            System.out.println("   boolean: " + boolVal);
            System.out.println("   byte: " + byteVal);
            System.out.println("   short: " + shortVal);
            System.out.println("   int: " + intVal);
            System.out.println("   long: " + longVal);
            System.out.println("   float: " + floatVal);
            System.out.println("   double: " + doubleVal);
            System.out.println("   char: '" + charVal + "'");
            System.out.println("   UTF String: \"" + strVal + "\"");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println("\n   File size: " + new File(filePath).length() + " bytes");
        System.out.println();
    }

    /**
     * File copy using streams
     */
    private static void demonstrateFileCopy() {
        System.out.println("5. FILE COPY USING STREAMS");
        System.out.println("--------------------------");
        
        String sourceFile = DEMO_DIR + "/source_file.bin";
        String destFile = DEMO_DIR + "/copied_file.bin";
        
        // Create source file
        try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
            for (int i = 0; i < 1000; i++) {
                fos.write(("Line " + i + " - Sample content for copying\n").getBytes());
            }
            System.out.println("   Source file created: " + new File(sourceFile).length() + " bytes");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Copy file
        long startTime = System.nanoTime();
        long bytesCopied = 0;
        
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {
            
            byte[] buffer = new byte[8192];  // 8KB buffer
            int bytesRead;
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                bytesCopied += bytesRead;
            }
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        long copyTime = System.nanoTime() - startTime;
        System.out.println("   File copied successfully!");
        System.out.println("   Bytes copied: " + bytesCopied);
        System.out.println("   Time taken: " + (copyTime / 1_000_000.0) + " ms");
        System.out.println("   Source size: " + new File(sourceFile).length());
        System.out.println("   Dest size: " + new File(destFile).length());
        System.out.println();
    }

    /**
     * Reading/Writing byte arrays
     */
    private static void demonstrateByteArrayOperations() {
        System.out.println("6. BYTE ARRAY OPERATIONS");
        System.out.println("------------------------");
        
        String filePath = DEMO_DIR + "/byte_array.bin";
        
        // Write entire byte array at once
        byte[] dataToWrite = new byte[256];
        for (int i = 0; i < 256; i++) {
            dataToWrite[i] = (byte) i;
        }
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(dataToWrite);
            System.out.println("   Wrote 256 bytes (0-255) to file");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read entire file into byte array
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int fileSize = fis.available();
            byte[] dataRead = new byte[fileSize];
            
            int totalRead = fis.read(dataRead);
            
            System.out.println("   Read " + totalRead + " bytes from file");
            System.out.print("   First 16 bytes: ");
            for (int i = 0; i < 16; i++) {
                System.out.printf("%02X ", dataRead[i] & 0xFF);
            }
            System.out.println();
            
            System.out.print("   Last 16 bytes: ");
            for (int i = 240; i < 256; i++) {
                System.out.printf("%02X ", dataRead[i] & 0xFF);
            }
            System.out.println();
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Working with binary data (simulating image-like data)
     */
    private static void demonstrateBinaryDataHandling() {
        System.out.println("7. BINARY DATA HANDLING");
        System.out.println("-----------------------");
        
        String filePath = DEMO_DIR + "/image_sim.bin";
        
        // Simulate writing a simple "image" with header
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))) {
            
            // Write header
            dos.writeBytes("IMG");  // Magic number (3 bytes)
            dos.writeByte(1);       // Version
            dos.writeInt(100);      // Width
            dos.writeInt(100);      // Height
            dos.writeByte(3);       // Channels (RGB)
            
            // Write pixel data (simulated)
            int width = 100;
            int height = 100;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Gradient pattern
                    dos.writeByte(x * 255 / width);      // R
                    dos.writeByte(y * 255 / height);    // G
                    dos.writeByte((x + y) * 255 / (width + height));  // B
                }
            }
            
            System.out.println("   Simulated image file created");
            System.out.println("   File size: " + new File(filePath).length() + " bytes");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read and verify the "image"
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {
            
            // Read header
            byte[] magic = new byte[3];
            dis.read(magic);
            int version = dis.readByte();
            int width = dis.readInt();
            int height = dis.readInt();
            int channels = dis.readByte();
            
            System.out.println("\n   Reading image header:");
            System.out.println("   Magic: " + new String(magic));
            System.out.println("   Version: " + version);
            System.out.println("   Dimensions: " + width + "x" + height);
            System.out.println("   Channels: " + channels);
            
            // Read first pixel
            int r = dis.readUnsignedByte();
            int g = dis.readUnsignedByte();
            int b = dis.readUnsignedByte();
            System.out.println("   First pixel RGB: (" + r + ", " + g + ", " + b + ")");
            
            // Skip to approximate center
            int pixelsToSkip = (50 * width + 50) * 3 - 3;  // Already read 3 bytes
            dis.skipBytes(pixelsToSkip);
            
            r = dis.readUnsignedByte();
            g = dis.readUnsignedByte();
            b = dis.readUnsignedByte();
            System.out.println("   Center pixel RGB: (" + r + ", " + g + ", " + b + ")");
            
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

