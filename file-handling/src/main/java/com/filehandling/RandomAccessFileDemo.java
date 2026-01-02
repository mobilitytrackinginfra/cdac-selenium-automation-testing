package com.filehandling;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Demonstrates RandomAccessFile operations:
 * - Reading and writing at any position
 * - Seeking within a file
 * - Reading/writing primitive types
 * - Modifying existing file content
 * - Creating a simple database-like structure
 */
public class RandomAccessFileDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    RANDOMACCESSFILE DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. Basic RandomAccessFile operations
        demonstrateBasicOperations();

        // 2. Seek and read at positions
        demonstrateSeekOperations();

        // 3. Modifying existing content
        demonstrateModification();

        // 4. Reading/writing primitives
        demonstratePrimitiveTypes();

        // 5. Simple record-based file
        demonstrateRecordBasedFile();

        // 6. Inserting data (challenging with RAF)
        demonstrateInsertionChallenge();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Basic RandomAccessFile operations
     */
    private static void demonstrateBasicOperations() {
        System.out.println("1. BASIC RANDOMACCESSFILE OPERATIONS");
        System.out.println("-------------------------------------");
        
        String filePath = DEMO_DIR + "/random_basic.dat";
        
        // Writing to file ("rw" mode - read/write)
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            // Write some bytes
            raf.writeBytes("Hello, RandomAccessFile!");
            System.out.println("   Written: 'Hello, RandomAccessFile!'");
            System.out.println("   Current position: " + raf.getFilePointer());
            System.out.println("   File length: " + raf.length());
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Reading from file ("r" mode - read only)
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            
            System.out.println("\n   Reading file:");
            int ch;
            StringBuilder content = new StringBuilder();
            while ((ch = raf.read()) != -1) {
                content.append((char) ch);
            }
            System.out.println("   Content: '" + content + "'");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // File modes explanation
        System.out.println("\n   File Modes:");
        System.out.println("   'r'   - Read only");
        System.out.println("   'rw'  - Read and write");
        System.out.println("   'rws' - Read/write with sync to storage (data + metadata)");
        System.out.println("   'rwd' - Read/write with sync to storage (data only)");
        System.out.println();
    }

    /**
     * Seek operations - moving to different positions
     */
    private static void demonstrateSeekOperations() {
        System.out.println("2. SEEK OPERATIONS");
        System.out.println("------------------");
        
        String filePath = DEMO_DIR + "/seek_demo.dat";
        
        // Create file with content
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.writeBytes("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            System.out.println("   Created file with alphabet (26 bytes)");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Seek to different positions
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            
            // Read from beginning
            byte[] buffer = new byte[3];
            raf.read(buffer);
            System.out.println("   First 3 chars: " + new String(buffer));
            System.out.println("   Position after read: " + raf.getFilePointer());
            
            // Seek to position 10
            raf.seek(10);
            System.out.println("\n   Seeked to position 10");
            raf.read(buffer);
            System.out.println("   3 chars at pos 10: " + new String(buffer));
            
            // Seek to end
            raf.seek(raf.length() - 3);
            System.out.println("\n   Seeked to last 3 positions");
            raf.read(buffer);
            System.out.println("   Last 3 chars: " + new String(buffer));
            
            // Seek to middle
            long middle = raf.length() / 2;
            raf.seek(middle);
            System.out.println("\n   Seeked to middle (position " + middle + ")");
            System.out.println("   Char at middle: " + (char) raf.read());
            
            // Using skipBytes
            raf.seek(0);
            int skipped = raf.skipBytes(5);
            System.out.println("\n   Skipped " + skipped + " bytes from start");
            System.out.println("   Current char: " + (char) raf.read());
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Modifying existing content in place
     */
    private static void demonstrateModification() {
        System.out.println("3. MODIFYING EXISTING CONTENT");
        System.out.println("-----------------------------");
        
        String filePath = DEMO_DIR + "/modify_demo.dat";
        
        // Create initial file
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.writeBytes("Hello World! This is a test file.");
            System.out.println("   Original: 'Hello World! This is a test file.'");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Modify content at specific position
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            // Replace "World" with "Java!"
            raf.seek(6);  // Position of "World"
            raf.writeBytes("Java!");  // Same length
            System.out.println("   Modified 'World' to 'Java!' at position 6");
            
            // Read modified content
            raf.seek(0);
            byte[] content = new byte[(int) raf.length()];
            raf.read(content);
            System.out.println("   Modified: '" + new String(content) + "'");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Overwriting with different length (overwrites subsequent data)
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            raf.seek(13);  // Position of "This"
            raf.writeBytes("FILE MODIFIED!");
            System.out.println("\n   Overwrote from position 13");
            
            raf.seek(0);
            byte[] content = new byte[(int) raf.length()];
            raf.read(content);
            System.out.println("   Result: '" + new String(content) + "'");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Truncating file
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            System.out.println("\n   File length before truncate: " + raf.length());
            raf.setLength(20);  // Truncate to 20 bytes
            System.out.println("   File length after truncate: " + raf.length());
            
            raf.seek(0);
            byte[] content = new byte[(int) raf.length()];
            raf.read(content);
            System.out.println("   Truncated content: '" + new String(content) + "'");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading and writing primitive types
     */
    private static void demonstratePrimitiveTypes() {
        System.out.println("4. READING/WRITING PRIMITIVE TYPES");
        System.out.println("-----------------------------------");
        
        String filePath = DEMO_DIR + "/primitives.dat";
        
        // Write primitives
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            System.out.println("   Writing primitives:");
            
            raf.writeBoolean(true);    // 1 byte
            System.out.println("   - boolean: true (1 byte) at pos 0");
            
            raf.writeByte(127);        // 1 byte
            System.out.println("   - byte: 127 (1 byte) at pos " + (raf.getFilePointer() - 1));
            
            raf.writeShort(32767);     // 2 bytes
            System.out.println("   - short: 32767 (2 bytes) at pos " + (raf.getFilePointer() - 2));
            
            raf.writeChar('J');        // 2 bytes
            System.out.println("   - char: 'J' (2 bytes) at pos " + (raf.getFilePointer() - 2));
            
            raf.writeInt(2147483647);  // 4 bytes
            System.out.println("   - int: 2147483647 (4 bytes) at pos " + (raf.getFilePointer() - 4));
            
            raf.writeLong(9223372036854775807L);  // 8 bytes
            System.out.println("   - long: max value (8 bytes) at pos " + (raf.getFilePointer() - 8));
            
            raf.writeFloat(3.14159f);  // 4 bytes
            System.out.println("   - float: 3.14159 (4 bytes) at pos " + (raf.getFilePointer() - 4));
            
            raf.writeDouble(2.718281828);  // 8 bytes
            System.out.println("   - double: 2.718... (8 bytes) at pos " + (raf.getFilePointer() - 8));
            
            raf.writeUTF("Hello");     // 2 bytes length + string bytes
            System.out.println("   - UTF String: 'Hello'");
            
            System.out.println("\n   Total file size: " + raf.length() + " bytes");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read primitives
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            
            System.out.println("\n   Reading primitives:");
            System.out.println("   - boolean: " + raf.readBoolean());
            System.out.println("   - byte: " + raf.readByte());
            System.out.println("   - short: " + raf.readShort());
            System.out.println("   - char: '" + raf.readChar() + "'");
            System.out.println("   - int: " + raf.readInt());
            System.out.println("   - long: " + raf.readLong());
            System.out.println("   - float: " + raf.readFloat());
            System.out.println("   - double: " + raf.readDouble());
            System.out.println("   - UTF: '" + raf.readUTF() + "'");
            
            // Random access - read int directly
            System.out.println("\n   Random access - reading int at position 6:");
            raf.seek(6);  // Position of int
            System.out.println("   - int at pos 6: " + raf.readInt());
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Simple record-based file (fixed-length records)
     */
    private static void demonstrateRecordBasedFile() {
        System.out.println("5. RECORD-BASED FILE (SIMPLE DATABASE)");
        System.out.println("---------------------------------------");
        
        String filePath = DEMO_DIR + "/employees.dat";
        
        // Record structure:
        // - ID: 4 bytes (int)
        // - Name: 20 bytes (fixed length string, padded)
        // - Salary: 8 bytes (double)
        // Total: 32 bytes per record
        
        final int RECORD_SIZE = 32;
        final int NAME_SIZE = 20;
        
        // Write records
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            System.out.println("   Writing employee records:");
            
            // Record 1
            writeRecord(raf, 1, "Alice Johnson", 75000.00);
            
            // Record 2
            writeRecord(raf, 2, "Bob Smith", 82000.00);
            
            // Record 3
            writeRecord(raf, 3, "Charlie Brown", 68000.00);
            
            // Record 4
            writeRecord(raf, 4, "Diana Prince", 95000.00);
            
            System.out.println("   Total records: 4");
            System.out.println("   File size: " + raf.length() + " bytes");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read all records
        System.out.println("\n   Reading all records:");
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            
            int numRecords = (int) (raf.length() / RECORD_SIZE);
            for (int i = 0; i < numRecords; i++) {
                readAndPrintRecord(raf, i, RECORD_SIZE, NAME_SIZE);
            }
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Direct access to specific record
        System.out.println("\n   Direct access to record #3:");
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            
            // Calculate position: record_index * record_size
            int recordIndex = 2;  // 0-based, so record #3 is index 2
            raf.seek(recordIndex * RECORD_SIZE);
            
            readAndPrintRecord(raf, recordIndex, RECORD_SIZE, NAME_SIZE);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Update a specific record
        System.out.println("\n   Updating record #2 (Bob's salary):");
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            // Seek to Bob's salary field
            int recordIndex = 1;
            int salaryOffset = 4 + NAME_SIZE;  // After ID and Name
            raf.seek(recordIndex * RECORD_SIZE + salaryOffset);
            
            // Update salary
            raf.writeDouble(90000.00);
            System.out.println("   Bob's salary updated to 90000.00");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Verify update
        System.out.println("\n   Verifying update - Record #2:");
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            readAndPrintRecord(raf, 1, RECORD_SIZE, NAME_SIZE);
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void writeRecord(RandomAccessFile raf, int id, String name, double salary) 
            throws IOException {
        raf.writeInt(id);
        
        // Pad or truncate name to fixed length
        byte[] nameBytes = new byte[20];
        byte[] originalName = name.getBytes();
        System.arraycopy(originalName, 0, nameBytes, 0, 
            Math.min(originalName.length, nameBytes.length));
        raf.write(nameBytes);
        
        raf.writeDouble(salary);
        
        System.out.println("   - Written: ID=" + id + ", Name=" + name + ", Salary=$" + salary);
    }

    private static void readAndPrintRecord(RandomAccessFile raf, int index, 
            int recordSize, int nameSize) throws IOException {
        raf.seek(index * recordSize);
        
        int id = raf.readInt();
        byte[] nameBytes = new byte[nameSize];
        raf.read(nameBytes);
        String name = new String(nameBytes).trim();
        double salary = raf.readDouble();
        
        System.out.printf("   [%d] ID: %d, Name: %-15s, Salary: $%,.2f%n", 
            index + 1, id, name, salary);
    }

    /**
     * The challenge of inserting data with RandomAccessFile
     */
    private static void demonstrateInsertionChallenge() {
        System.out.println("6. INSERTION CHALLENGE");
        System.out.println("----------------------");
        
        String filePath = DEMO_DIR + "/insert_demo.dat";
        
        // Create file
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.writeBytes("Line 1: First line\n");
            raf.writeBytes("Line 2: Second line\n");
            raf.writeBytes("Line 3: Third line\n");
            
            System.out.println("   Original file content:");
            raf.seek(0);
            byte[] content = new byte[(int) raf.length()];
            raf.read(content);
            System.out.println("   " + new String(content).replace("\n", "\n   "));
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Insert data between lines (requires shifting)
        System.out.println("\n   Inserting 'NEW LINE' after Line 1:");
        System.out.println("   (This requires reading remaining content and rewriting)");
        
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            
            String insertText = "NEW LINE: Inserted!\n";
            int insertPos = 19;  // After "Line 1: First line\n"
            
            // Read remaining content
            raf.seek(insertPos);
            byte[] remaining = new byte[(int) (raf.length() - insertPos)];
            raf.read(remaining);
            
            // Write new content
            raf.seek(insertPos);
            raf.writeBytes(insertText);
            
            // Write back remaining content
            raf.write(remaining);
            
            // Display result
            raf.seek(0);
            byte[] content = new byte[(int) raf.length()];
            raf.read(content);
            System.out.println("\n   Modified file content:");
            System.out.println("   " + new String(content).replace("\n", "\n   "));
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println("   Note: RandomAccessFile is best for fixed-length records,");
        System.out.println("   not for inserting variable-length data in the middle.");
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

