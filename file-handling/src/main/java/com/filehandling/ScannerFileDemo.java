package com.filehandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Demonstrates reading files using Scanner class:
 * - Reading line by line
 * - Reading word by word
 * - Reading with custom delimiters
 * - Reading different data types
 * - Pattern matching with Scanner
 */
public class ScannerFileDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SCANNER FILE READING DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory and files
        new File(DEMO_DIR).mkdirs();
        createDemoFiles();

        // 1. Reading line by line
        demonstrateLineByLine();

        // 2. Reading word by word
        demonstrateWordByWord();

        // 3. Reading with custom delimiters
        demonstrateCustomDelimiters();

        // 4. Reading different data types
        demonstrateDataTypes();

        // 5. Pattern matching with Scanner
        demonstratePatternMatching();

        // 6. Reading CSV-like data
        demonstrateCSVReading();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    private static void createDemoFiles() {
        System.out.println("CREATING DEMO FILES");
        System.out.println("-------------------");
        
        // Create text file
        try (FileWriter writer = new FileWriter(DEMO_DIR + "/scanner_demo.txt")) {
            writer.write("Hello World from Scanner!\n");
            writer.write("This is line two.\n");
            writer.write("Scanner makes reading files easy.\n");
            writer.write("Java File Handling is powerful.\n");
            System.out.println("   Created: scanner_demo.txt");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Create file with different data types
        try (FileWriter writer = new FileWriter(DEMO_DIR + "/data_types.txt")) {
            writer.write("42\n");          // int
            writer.write("3.14159\n");     // double
            writer.write("true\n");        // boolean
            writer.write("Hello\n");       // string
            writer.write("100 200 300\n"); // multiple ints
            writer.write("1.1 2.2 3.3\n"); // multiple doubles
            System.out.println("   Created: data_types.txt");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Create CSV-like file
        try (FileWriter writer = new FileWriter(DEMO_DIR + "/students.csv")) {
            writer.write("Name,Age,Grade,Score\n");
            writer.write("Alice,20,A,95.5\n");
            writer.write("Bob,22,B,87.3\n");
            writer.write("Charlie,21,A,92.8\n");
            writer.write("Diana,23,B,88.0\n");
            writer.write("Eve,20,A,98.2\n");
            System.out.println("   Created: students.csv");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Create file with custom delimiters
        try (FileWriter writer = new FileWriter(DEMO_DIR + "/custom_delim.txt")) {
            writer.write("apple|banana|cherry|date\n");
            writer.write("red|yellow|red|brown\n");
            writer.write("1|2|3|4\n");
            System.out.println("   Created: custom_delim.txt");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        System.out.println();
    }

    /**
     * Reading file line by line using Scanner
     */
    private static void demonstrateLineByLine() {
        System.out.println("1. READING LINE BY LINE");
        System.out.println("-----------------------");
        
        File file = new File(DEMO_DIR + "/scanner_demo.txt");
        
        try (Scanner scanner = new Scanner(file)) {
            int lineNum = 1;
            System.out.println("   File contents:");
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("   " + lineNum + ": " + line);
                lineNum++;
            }
            
            System.out.println("   Total lines: " + (lineNum - 1));
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading file word by word (token by token)
     */
    private static void demonstrateWordByWord() {
        System.out.println("2. READING WORD BY WORD");
        System.out.println("-----------------------");
        
        File file = new File(DEMO_DIR + "/scanner_demo.txt");
        
        try (Scanner scanner = new Scanner(file)) {
            int wordCount = 0;
            System.out.println("   Words in file:");
            System.out.print("   ");
            
            while (scanner.hasNext()) {
                String word = scanner.next();
                System.out.print("[" + word + "] ");
                wordCount++;
                
                // Print newline every 5 words for readability
                if (wordCount % 5 == 0) {
                    System.out.print("\n   ");
                }
            }
            
            System.out.println("\n   Total words: " + wordCount);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading with custom delimiters
     */
    private static void demonstrateCustomDelimiters() {
        System.out.println("3. READING WITH CUSTOM DELIMITERS");
        System.out.println("----------------------------------");
        
        File file = new File(DEMO_DIR + "/custom_delim.txt");
        
        // Using pipe (|) as delimiter
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\||\n"); // pipe or newline as delimiter
            
            System.out.println("   Tokens with '|' delimiter:");
            int count = 0;
            while (scanner.hasNext()) {
                String token = scanner.next();
                System.out.print("   [" + token + "]");
                count++;
                if (count % 4 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Using multiple delimiters
        System.out.println("   Reading scanner_demo.txt with space/punctuation delimiters:");
        try (Scanner scanner = new Scanner(new File(DEMO_DIR + "/scanner_demo.txt"))) {
            // Delimiter: space, period, exclamation, newline
            scanner.useDelimiter("[\\s.!\\n]+");
            
            int count = 0;
            while (scanner.hasNext() && count < 10) {
                System.out.print("   [" + scanner.next() + "]");
                count++;
            }
            System.out.println("...");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading different data types
     */
    private static void demonstrateDataTypes() {
        System.out.println("4. READING DIFFERENT DATA TYPES");
        System.out.println("--------------------------------");
        
        File file = new File(DEMO_DIR + "/data_types.txt");
        
        try (Scanner scanner = new Scanner(file)) {
            
            // Read int
            if (scanner.hasNextInt()) {
                int intValue = scanner.nextInt();
                System.out.println("   Integer: " + intValue);
            }
            
            // Read double
            if (scanner.hasNextDouble()) {
                double doubleValue = scanner.nextDouble();
                System.out.println("   Double: " + doubleValue);
            }
            
            // Read boolean
            if (scanner.hasNextBoolean()) {
                boolean boolValue = scanner.nextBoolean();
                System.out.println("   Boolean: " + boolValue);
            }
            
            // Read string
            if (scanner.hasNext()) {
                String strValue = scanner.next();
                System.out.println("   String: " + strValue);
            }
            
            // Read multiple ints from one line
            System.out.print("   Multiple ints: ");
            while (scanner.hasNextInt()) {
                System.out.print(scanner.nextInt() + " ");
            }
            scanner.nextLine(); // consume remaining newline
            System.out.println();
            
            // Read multiple doubles from one line
            System.out.print("   Multiple doubles: ");
            while (scanner.hasNextDouble()) {
                System.out.print(scanner.nextDouble() + " ");
            }
            System.out.println();
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Pattern matching with Scanner
     */
    private static void demonstratePatternMatching() {
        System.out.println("5. PATTERN MATCHING WITH SCANNER");
        System.out.println("---------------------------------");
        
        File file = new File(DEMO_DIR + "/scanner_demo.txt");
        
        try (Scanner scanner = new Scanner(file)) {
            
            // Find words matching a pattern
            Pattern wordPattern = Pattern.compile("[A-Z][a-z]+"); // Words starting with capital
            
            System.out.println("   Words starting with capital letter:");
            while (scanner.hasNext()) {
                if (scanner.hasNext(wordPattern)) {
                    System.out.println("   -> " + scanner.next(wordPattern));
                } else {
                    scanner.next(); // skip non-matching word
                }
            }
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Finding specific patterns in file
        try (Scanner scanner = new Scanner(new File(DEMO_DIR + "/scanner_demo.txt"))) {
            String content = scanner.useDelimiter("\\A").next(); // Read entire file
            
            // Find all words with 'ing'
            Scanner lineScanner = new Scanner(content);
            Pattern ingPattern = Pattern.compile("\\w*ing\\w*", Pattern.CASE_INSENSITIVE);
            
            System.out.println("   Words containing 'ing':");
            while (lineScanner.hasNext()) {
                String word = lineScanner.next();
                if (ingPattern.matcher(word).matches()) {
                    System.out.println("   -> " + word);
                }
            }
            lineScanner.close();
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Reading CSV-like data
     */
    private static void demonstrateCSVReading() {
        System.out.println("6. READING CSV DATA");
        System.out.println("-------------------");
        
        File file = new File(DEMO_DIR + "/students.csv");
        
        try (Scanner scanner = new Scanner(file)) {
            
            // Read header
            if (scanner.hasNextLine()) {
                String header = scanner.nextLine();
                System.out.println("   Header: " + header);
                System.out.println("   " + "-".repeat(40));
            }
            
            // Read data rows
            int rowCount = 0;
            double totalScore = 0;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length >= 4) {
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    String grade = parts[2];
                    double score = Double.parseDouble(parts[3]);
                    
                    System.out.printf("   %s (Age: %d) - Grade: %s, Score: %.1f%n", 
                        name, age, grade, score);
                    
                    totalScore += score;
                    rowCount++;
                }
            }
            
            System.out.println("   " + "-".repeat(40));
            System.out.printf("   Total Students: %d%n", rowCount);
            System.out.printf("   Average Score: %.2f%n", totalScore / rowCount);
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Alternative: Process CSV with Scanner for each line
        System.out.println("\n   Alternative CSV parsing (Scanner per line):");
        try (Scanner fileScanner = new Scanner(file)) {
            fileScanner.nextLine(); // Skip header
            
            while (fileScanner.hasNextLine()) {
                Scanner lineScanner = new Scanner(fileScanner.nextLine());
                lineScanner.useDelimiter(",");
                
                if (lineScanner.hasNext()) {
                    String name = lineScanner.next();
                    int age = lineScanner.nextInt();
                    String grade = lineScanner.next();
                    double score = lineScanner.nextDouble();
                    
                    System.out.printf("   -> %s scored %.1f%n", name, score);
                }
                lineScanner.close();
            }
            
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

