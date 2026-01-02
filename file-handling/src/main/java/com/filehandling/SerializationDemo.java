package com.filehandling;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Java Object Serialization:
 * - Basic serialization/deserialization
 * - Serializable interface
 * - transient keyword
 * - serialVersionUID
 * - Serializing collections
 * - Custom serialization
 */
public class SerializationDemo {

    private static final String DEMO_DIR = "demo_files";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    SERIALIZATION DEMONSTRATION");
        System.out.println("========================================\n");

        // Create demo directory
        new File(DEMO_DIR).mkdirs();

        // 1. Basic serialization
        demonstrateBasicSerialization();

        // 2. Transient keyword
        demonstrateTransient();

        // 3. Serializing collections
        demonstrateCollectionSerialization();

        // 4. Object graph serialization
        demonstrateObjectGraphSerialization();

        // 5. Multiple objects in one file
        demonstrateMultipleObjects();

        // 6. Custom serialization
        demonstrateCustomSerialization();

        // Cleanup
        cleanup();

        System.out.println("========================================");
        System.out.println("    DEMONSTRATION COMPLETE!");
        System.out.println("========================================");
    }

    /**
     * Basic serialization and deserialization
     */
    private static void demonstrateBasicSerialization() {
        System.out.println("1. BASIC SERIALIZATION");
        System.out.println("----------------------");
        
        String filePath = DEMO_DIR + "/person.ser";
        
        // Create object to serialize
        Person person = new Person("John Doe", 30, "john@example.com");
        System.out.println("   Original object: " + person);
        
        // Serialize (write object to file)
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            
            oos.writeObject(person);
            System.out.println("   Object serialized to: " + filePath);
            System.out.println("   File size: " + new File(filePath).length() + " bytes");
            
        } catch (IOException e) {
            System.out.println("   Serialization error: " + e.getMessage());
        }
        
        // Deserialize (read object from file)
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println("   Deserialized object: " + deserializedPerson);
            
            // Verify it's a different object
            System.out.println("   Same reference? " + (person == deserializedPerson));
            System.out.println("   Equal values? " + person.getName().equals(deserializedPerson.getName()));
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Deserialization error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Transient keyword - fields not serialized
     */
    private static void demonstrateTransient() {
        System.out.println("2. TRANSIENT KEYWORD");
        System.out.println("--------------------");
        
        String filePath = DEMO_DIR + "/user.ser";
        
        // Create user with sensitive data
        UserWithPassword user = new UserWithPassword("alice", "secretPassword123");
        System.out.println("   Original user: " + user);
        System.out.println("   Password before serialization: " + user.getPassword());
        
        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(user);
            System.out.println("   User serialized");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            UserWithPassword deserializedUser = (UserWithPassword) ois.readObject();
            System.out.println("   Deserialized user: " + deserializedUser);
            System.out.println("   Password after deserialization: " + deserializedUser.getPassword());
            System.out.println("   Note: Password is null because it was marked 'transient'");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Serializing collections (List, Map, etc.)
     */
    private static void demonstrateCollectionSerialization() {
        System.out.println("3. COLLECTION SERIALIZATION");
        System.out.println("---------------------------");
        
        String filePath = DEMO_DIR + "/collection.ser";
        
        // Create a list of people
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25, "alice@email.com"));
        people.add(new Person("Bob", 30, "bob@email.com"));
        people.add(new Person("Charlie", 35, "charlie@email.com"));
        
        System.out.println("   Original list size: " + people.size());
        people.forEach(p -> System.out.println("   - " + p));
        
        // Serialize list
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(people);
            System.out.println("\n   List serialized");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Deserialize list
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            @SuppressWarnings("unchecked")
            List<Person> deserializedList = (List<Person>) ois.readObject();
            System.out.println("   Deserialized list size: " + deserializedList.size());
            deserializedList.forEach(p -> System.out.println("   - " + p));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Serialize Map
        String mapFilePath = DEMO_DIR + "/map.ser";
        Map<String, Person> personMap = new HashMap<>();
        personMap.put("emp001", new Person("Diana", 28, "diana@email.com"));
        personMap.put("emp002", new Person("Eve", 32, "eve@email.com"));
        
        System.out.println("\n   Serializing Map with " + personMap.size() + " entries");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(mapFilePath))) {
            oos.writeObject(personMap);
            System.out.println("   Map serialized");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(mapFilePath))) {
            @SuppressWarnings("unchecked")
            Map<String, Person> deserializedMap = (Map<String, Person>) ois.readObject();
            System.out.println("   Deserialized Map:");
            deserializedMap.forEach((k, v) -> System.out.println("   " + k + " -> " + v));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Object graph serialization (objects referencing other objects)
     */
    private static void demonstrateObjectGraphSerialization() {
        System.out.println("4. OBJECT GRAPH SERIALIZATION");
        System.out.println("-----------------------------");
        
        String filePath = DEMO_DIR + "/company.ser";
        
        // Create object graph
        Employee emp1 = new Employee("Alice", "Developer");
        Employee emp2 = new Employee("Bob", "Designer");
        Employee emp3 = new Employee("Charlie", "Manager");
        
        Department dept = new Department("Engineering");
        dept.addEmployee(emp1);
        dept.addEmployee(emp2);
        
        Company company = new Company("TechCorp");
        company.addDepartment(dept);
        
        Department salesDept = new Department("Sales");
        salesDept.addEmployee(emp3);
        company.addDepartment(salesDept);
        
        System.out.println("   Original company structure:");
        System.out.println("   " + company);
        
        // Serialize entire object graph
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(company);
            System.out.println("\n   Company object graph serialized");
            System.out.println("   File size: " + new File(filePath).length() + " bytes");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            Company deserializedCompany = (Company) ois.readObject();
            System.out.println("   Deserialized company structure:");
            System.out.println("   " + deserializedCompany);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Multiple objects in one file
     */
    private static void demonstrateMultipleObjects() {
        System.out.println("5. MULTIPLE OBJECTS IN ONE FILE");
        System.out.println("--------------------------------");
        
        String filePath = DEMO_DIR + "/multiple.ser";
        
        // Write multiple objects
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            
            oos.writeObject("Header String");
            oos.writeInt(42);
            oos.writeDouble(3.14159);
            oos.writeObject(new Date());
            oos.writeObject(new Person("Multiple", 99, "multi@test.com"));
            oos.writeObject(new int[]{1, 2, 3, 4, 5});
            
            System.out.println("   Multiple objects written to file");
            
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Read multiple objects (in same order)
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            
            String header = (String) ois.readObject();
            int intVal = ois.readInt();
            double doubleVal = ois.readDouble();
            Date date = (Date) ois.readObject();
            Person person = (Person) ois.readObject();
            int[] array = (int[]) ois.readObject();
            
            System.out.println("   Read from file:");
            System.out.println("   - String: " + header);
            System.out.println("   - int: " + intVal);
            System.out.println("   - double: " + doubleVal);
            System.out.println("   - Date: " + date);
            System.out.println("   - Person: " + person);
            System.out.println("   - Array: " + java.util.Arrays.toString(array));
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Reading until EOF
        System.out.println("\n   Reading all objects until EOF:");
        String multiFile = DEMO_DIR + "/multi_eof.ser";
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(multiFile))) {
            for (int i = 1; i <= 5; i++) {
                oos.writeObject(new Person("Person" + i, 20 + i, "p" + i + "@test.com"));
            }
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(multiFile))) {
            int count = 0;
            while (true) {
                try {
                    Person p = (Person) ois.readObject();
                    System.out.println("   - " + p.getName());
                    count++;
                } catch (EOFException eof) {
                    break;  // End of file reached
                }
            }
            System.out.println("   Total objects read: " + count);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Custom serialization with writeObject/readObject
     */
    private static void demonstrateCustomSerialization() {
        System.out.println("6. CUSTOM SERIALIZATION");
        System.out.println("-----------------------");
        
        String filePath = DEMO_DIR + "/custom.ser";
        
        // Create object with custom serialization
        CustomSerializable obj = new CustomSerializable("Test Data", 12345);
        System.out.println("   Original: " + obj);
        
        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            System.out.println("   Object serialized with custom writeObject");
        } catch (IOException e) {
            System.out.println("   Error: " + e.getMessage());
        }
        
        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            CustomSerializable deserializedObj = (CustomSerializable) ois.readObject();
            System.out.println("   Deserialized with custom readObject: " + deserializedObj);
        } catch (IOException | ClassNotFoundException e) {
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

// ==================== Helper Classes ====================

/**
 * Simple Person class for serialization
 */
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private String email;
    
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
}

/**
 * User class with transient password field
 */
class UserWithPassword implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private transient String password;  // Won't be serialized
    
    public UserWithPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }
}

/**
 * Employee class for object graph demo
 */
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String role;
    
    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
    
    @Override
    public String toString() {
        return name + "(" + role + ")";
    }
}

/**
 * Department class for object graph demo
 */
class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<Employee> employees = new ArrayList<>();
    
    public Department(String name) {
        this.name = name;
    }
    
    public void addEmployee(Employee emp) {
        employees.add(emp);
    }
    
    @Override
    public String toString() {
        return name + ": " + employees;
    }
}

/**
 * Company class for object graph demo
 */
class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<Department> departments = new ArrayList<>();
    
    public Company(String name) {
        this.name = name;
    }
    
    public void addDepartment(Department dept) {
        departments.add(dept);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        for (Department dept : departments) {
            sb.append("     - ").append(dept).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Class with custom serialization logic
 */
class CustomSerializable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String data;
    private int value;
    private transient String computed;  // Recomputed after deserialization
    
    public CustomSerializable(String data, int value) {
        this.data = data;
        this.value = value;
        this.computed = computeValue();
    }
    
    private String computeValue() {
        return data + "_" + value;
    }
    
    // Custom serialization
    private void writeObject(ObjectOutputStream oos) throws IOException {
        System.out.println("     [Custom writeObject called]");
        oos.defaultWriteObject();  // Write non-transient fields
        // Could write additional data here
        oos.writeObject("EXTRA_DATA");
    }
    
    // Custom deserialization
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        System.out.println("     [Custom readObject called]");
        ois.defaultReadObject();  // Read non-transient fields
        String extra = (String) ois.readObject();
        System.out.println("     [Read extra data: " + extra + "]");
        // Recompute transient field
        this.computed = computeValue();
    }
    
    @Override
    public String toString() {
        return "CustomSerializable{data='" + data + "', value=" + value + ", computed='" + computed + "'}";
    }
}

