# SonarQube Error Simulation Project

This project contains Java classes that intentionally simulate various types of issues detectable by SonarQube. It's designed for **educational and testing purposes** to understand what kinds of problems SonarQube can identify.

## ⚠️ Warning

**DO NOT use any code from this project in production!** All code is intentionally flawed to demonstrate security vulnerabilities, bugs, and bad practices.

## Project Structure

```
src/main/java/com/sonarqube/
├── bugs/                       # Bug simulations
│   ├── BugSimulations.java     # Core bug patterns
│   └── MoreBugs.java           # Additional bug patterns
├── vulnerabilities/            # Security vulnerability simulations
│   ├── SecurityVulnerabilities.java  # Core security issues
│   └── MoreVulnerabilities.java      # Additional security issues
├── codesmells/                 # Code smell simulations
│   ├── CodeSmellSimulations.java     # Core code smells
│   └── MoreCodeSmells.java           # Additional code smells
└── securityhotspots/           # Security hotspot simulations
    ├── SecurityHotspots.java         # Core security hotspots
    └── AdditionalHotspots.java       # Additional security hotspots
```

## Categories of Issues

### 1. Bugs (Reliability Issues)
Issues that represent clearly wrong code or code that will cause unexpected behavior:

- **Null Pointer Dereferences** - Using objects without null checks
- **Resource Leaks** - Streams, connections not properly closed
- **Comparison Issues** - Using == instead of equals(), boxing issues
- **Array/Collection Issues** - Index out of bounds, concurrent modification
- **Mathematical Issues** - Division by zero, integer overflow, precision loss
- **Synchronization Issues** - Thread-safety problems, broken double-checked locking
- **Control Flow Issues** - Infinite loops, unreachable code, dead stores
- **Equals/HashCode Issues** - Inconsistent implementations
- **Exception Handling Issues** - Catching Throwable, ignoring InterruptedException
- **Serialization Issues** - Missing serialVersionUID, non-serializable fields

### 2. Vulnerabilities (Security Issues)
Issues that can be exploited by attackers:

- **SQL Injection** - Concatenating user input in queries
- **Command Injection** - Executing system commands with user input
- **Path Traversal** - Accessing files outside intended directory
- **Hardcoded Credentials** - Passwords, API keys in source code
- **Weak Cryptography** - MD5, SHA1, DES, ECB mode
- **Insecure Random** - Using java.util.Random for security
- **LDAP Injection** - User input in LDAP queries
- **XXE (XML External Entity)** - Unsafe XML parsing
- **XPath Injection** - User input in XPath expressions
- **XSS (Cross-Site Scripting)** - Outputting unsanitized user input
- **Insecure SSL/TLS** - Trusting all certificates, weak protocols
- **Cookie Vulnerabilities** - Missing Secure/HttpOnly flags
- **Information Exposure** - Stack traces, sensitive data in errors
- **SSRF (Server-Side Request Forgery)** - User-controlled URLs
- **Insecure Deserialization** - Deserializing untrusted data

### 3. Code Smells (Maintainability Issues)
Issues that don't necessarily cause bugs but make code harder to maintain:

- **Unused Code** - Unused fields, methods, variables, parameters
- **Empty Blocks** - Empty catch, if, finally, synchronized blocks
- **Complexity Issues** - High cognitive complexity, too many branches
- **Magic Numbers/Strings** - Hardcoded values without constants
- **Naming Conventions** - Non-descriptive names, wrong casing
- **Code Duplication** - Repeated code blocks
- **Commented Out Code** - Dead code in comments
- **Too Many Parameters** - Methods with excessive parameters
- **Long Methods** - Methods doing too much
- **String Issues** - Concatenation in loops, unnecessary toString()
- **Boolean Issues** - Redundant literals, negated conditions
- **Class Design Issues** - Empty classes, utility class constructors
- **Exception Handling Smells** - Generic exceptions, log-and-rethrow
- **Switch Statement Issues** - Missing default, fallthrough
- **Loop Issues** - Can be foreach, modified counter

### 4. Security Hotspots (Requires Review)
Security-sensitive code that needs manual review:

- **Authentication** - Custom auth, session management
- **Cryptography** - Encryption operations, random generation
- **File System** - Reading/writing files, permissions
- **Network** - Opening connections, server sockets
- **Command Execution** - ProcessBuilder, Runtime.exec
- **Database Configuration** - Connection settings
- **Logging** - User data logging
- **Regex** - User-supplied patterns (ReDoS risk)
- **Serialization** - Object deserialization
- **HTTP** - Headers, cookies, CORS
- **Environment** - Environment variables, system properties
- **Reflection** - Dynamic class loading, private field access
- **XML Parsing** - Parser configuration
- **Threading** - Thread creation, pool configuration

## Running SonarQube Analysis

### Prerequisites
1. SonarQube server running (default: http://localhost:9000)
2. Maven installed
3. Java 11 or higher

### Run Analysis

```bash
# Basic analysis
mvn clean verify sonar:sonar

# With specific SonarQube server
mvn clean verify sonar:sonar -Dsonar.host.url=http://your-sonar-server:9000

# With authentication token
mvn clean verify sonar:sonar -Dsonar.login=your-token

# Using the sonar profile
mvn clean verify sonar:sonar -Psonar
```

### Expected Results
After running the analysis, you should see hundreds of issues in SonarQube categorized as:
- 🐛 **Bugs** - Reliability issues
- 🔓 **Vulnerabilities** - Security vulnerabilities  
- 🔒 **Security Hotspots** - Code needing security review
- 💩 **Code Smells** - Maintainability issues

## Learning Objectives

This project helps you understand:

1. **What SonarQube detects** - Types of issues and their severity
2. **Why these issues matter** - Security and reliability implications
3. **How to fix issues** - Each issue in SonarQube includes remediation guidance
4. **Best practices** - Learn what NOT to do by seeing bad examples

## License

This project is for educational purposes only. Use at your own risk.

