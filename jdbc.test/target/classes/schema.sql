-- ============================================
-- JDBC Tutorial Database Schema
-- ============================================
-- This schema demonstrates tables for learning
-- all basic JDBC concepts
-- ============================================

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS audit_log;

-- ============================================
-- 1. DEPARTMENTS TABLE
-- ============================================
CREATE TABLE departments (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100),
    budget DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. EMPLOYEES TABLE
-- ============================================
CREATE TABLE employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    hire_date DATE NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    dept_id INT,
    manager_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id),
    FOREIGN KEY (manager_id) REFERENCES employees(emp_id)
);

-- ============================================
-- 3. CUSTOMERS TABLE
-- ============================================
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    country VARCHAR(50) DEFAULT 'USA',
    registration_date DATE DEFAULT (CURRENT_DATE),
    loyalty_points INT DEFAULT 0,
    is_premium BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 4. PRODUCTS TABLE
-- ============================================
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    reorder_level INT DEFAULT 10,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================
-- 5. ORDERS TABLE
-- ============================================
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(12, 2) DEFAULT 0.00,
    shipping_address VARCHAR(255),
    payment_method VARCHAR(50),
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- ============================================
-- 6. ORDER_ITEMS TABLE
-- ============================================
CREATE TABLE order_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0.00,
    subtotal DECIMAL(12, 2) GENERATED ALWAYS AS (quantity * unit_price * (1 - discount_percent/100)) STORED,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ============================================
-- 7. AUDIT_LOG TABLE (for triggers demo)
-- ============================================
CREATE TABLE audit_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(50) NOT NULL,
    operation VARCHAR(20) NOT NULL,
    record_id INT,
    old_values TEXT,
    new_values TEXT,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- SAMPLE DATA INSERTION
-- ============================================

-- Insert Departments
INSERT INTO departments (dept_name, location, budget) VALUES
('Engineering', 'Building A', 500000.00),
('Human Resources', 'Building B', 150000.00),
('Marketing', 'Building C', 300000.00),
('Sales', 'Building D', 400000.00),
('Finance', 'Building E', 250000.00),
('IT Support', 'Building A', 200000.00);

-- Insert Employees
INSERT INTO employees (first_name, last_name, email, phone, hire_date, salary, dept_id, manager_id) VALUES
('John', 'Smith', 'john.smith@company.com', '555-0101', '2020-01-15', 95000.00, 1, NULL),
('Sarah', 'Johnson', 'sarah.johnson@company.com', '555-0102', '2020-03-20', 85000.00, 1, 1),
('Michael', 'Williams', 'michael.williams@company.com', '555-0103', '2019-06-10', 75000.00, 1, 1),
('Emily', 'Brown', 'emily.brown@company.com', '555-0104', '2021-02-28', 65000.00, 2, NULL),
('David', 'Davis', 'david.davis@company.com', '555-0105', '2020-08-15', 70000.00, 3, NULL),
('Jessica', 'Miller', 'jessica.miller@company.com', '555-0106', '2021-05-01', 80000.00, 4, NULL),
('Robert', 'Wilson', 'robert.wilson@company.com', '555-0107', '2019-11-20', 90000.00, 5, NULL),
('Amanda', 'Taylor', 'amanda.taylor@company.com', '555-0108', '2022-01-10', 55000.00, 6, NULL),
('Christopher', 'Anderson', 'chris.anderson@company.com', '555-0109', '2021-07-15', 72000.00, 1, 2),
('Jennifer', 'Thomas', 'jennifer.thomas@company.com', '555-0110', '2022-03-05', 58000.00, 3, 5);

-- Insert Customers
INSERT INTO customers (first_name, last_name, email, phone, address, city, state, zip_code, loyalty_points, is_premium) VALUES
('Alice', 'Cooper', 'alice.cooper@email.com', '555-1001', '123 Main St', 'New York', 'NY', '10001', 500, TRUE),
('Bob', 'Martin', 'bob.martin@email.com', '555-1002', '456 Oak Ave', 'Los Angeles', 'CA', '90001', 250, FALSE),
('Carol', 'White', 'carol.white@email.com', '555-1003', '789 Pine Rd', 'Chicago', 'IL', '60601', 1000, TRUE),
('Daniel', 'Lee', 'daniel.lee@email.com', '555-1004', '321 Elm St', 'Houston', 'TX', '77001', 150, FALSE),
('Eva', 'Garcia', 'eva.garcia@email.com', '555-1005', '654 Maple Dr', 'Phoenix', 'AZ', '85001', 750, TRUE),
('Frank', 'Rodriguez', 'frank.rodriguez@email.com', '555-1006', '987 Cedar Ln', 'Philadelphia', 'PA', '19101', 300, FALSE),
('Grace', 'Martinez', 'grace.martinez@email.com', '555-1007', '147 Birch Way', 'San Antonio', 'TX', '78201', 450, FALSE),
('Henry', 'Clark', 'henry.clark@email.com', '555-1008', '258 Spruce Ct', 'San Diego', 'CA', '92101', 600, TRUE);

-- Insert Products
INSERT INTO products (product_name, description, category, price, stock_quantity, reorder_level) VALUES
('Laptop Pro 15', 'High-performance laptop with 15-inch display', 'Electronics', 1299.99, 50, 10),
('Wireless Mouse', 'Ergonomic wireless mouse with USB receiver', 'Electronics', 29.99, 200, 30),
('USB-C Hub', '7-in-1 USB-C hub with HDMI and card reader', 'Electronics', 49.99, 150, 25),
('Mechanical Keyboard', 'RGB mechanical keyboard with Cherry MX switches', 'Electronics', 129.99, 75, 15),
('27-inch Monitor', '4K IPS monitor with adjustable stand', 'Electronics', 399.99, 40, 8),
('Office Chair', 'Ergonomic office chair with lumbar support', 'Furniture', 299.99, 30, 5),
('Standing Desk', 'Electric height-adjustable standing desk', 'Furniture', 599.99, 20, 4),
('Desk Lamp', 'LED desk lamp with adjustable brightness', 'Furniture', 39.99, 100, 20),
('Notebook Set', 'Pack of 5 premium notebooks', 'Stationery', 19.99, 300, 50),
('Pen Pack', 'Set of 12 gel pens in assorted colors', 'Stationery', 9.99, 500, 100);

-- Insert Orders
INSERT INTO orders (customer_id, order_date, status, total_amount, shipping_address, payment_method) VALUES
(1, '2024-01-15 10:30:00', 'DELIVERED', 1379.97, '123 Main St, New York, NY 10001', 'Credit Card'),
(2, '2024-01-16 14:45:00', 'SHIPPED', 449.98, '456 Oak Ave, Los Angeles, CA 90001', 'PayPal'),
(3, '2024-01-17 09:15:00', 'PROCESSING', 929.97, '789 Pine Rd, Chicago, IL 60601', 'Credit Card'),
(1, '2024-01-18 16:20:00', 'PENDING', 79.98, '123 Main St, New York, NY 10001', 'Debit Card'),
(4, '2024-01-19 11:00:00', 'DELIVERED', 1899.98, '321 Elm St, Houston, TX 77001', 'Credit Card'),
(5, '2024-01-20 13:30:00', 'CANCELLED', 299.99, '654 Maple Dr, Phoenix, AZ 85001', 'PayPal');

-- Insert Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, discount_percent) VALUES
(1, 1, 1, 1299.99, 0.00),
(1, 2, 2, 29.99, 0.00),
(1, 9, 1, 19.99, 0.00),
(2, 5, 1, 399.99, 0.00),
(2, 3, 1, 49.99, 0.00),
(3, 7, 1, 599.99, 0.00),
(3, 4, 1, 129.99, 0.00),
(3, 6, 1, 299.99, 10.00),
(4, 8, 2, 39.99, 0.00),
(5, 1, 1, 1299.99, 0.00),
(5, 7, 1, 599.99, 0.00),
(6, 6, 1, 299.99, 0.00);

-- ============================================
-- STORED PROCEDURES
-- ============================================

-- Procedure to get employee details by ID
DELIMITER //
CREATE PROCEDURE get_employee_by_id(IN p_emp_id INT)
BEGIN
    SELECT e.*, d.dept_name 
    FROM employees e 
    LEFT JOIN departments d ON e.dept_id = d.dept_id 
    WHERE e.emp_id = p_emp_id;
END //
DELIMITER ;

-- Procedure to update employee salary
DELIMITER //
CREATE PROCEDURE update_employee_salary(
    IN p_emp_id INT, 
    IN p_new_salary DECIMAL(10,2),
    OUT p_old_salary DECIMAL(10,2),
    OUT p_status VARCHAR(50)
)
BEGIN
    SELECT salary INTO p_old_salary FROM employees WHERE emp_id = p_emp_id;
    
    IF p_old_salary IS NOT NULL THEN
        UPDATE employees SET salary = p_new_salary WHERE emp_id = p_emp_id;
        SET p_status = 'SUCCESS';
    ELSE
        SET p_status = 'EMPLOYEE_NOT_FOUND';
    END IF;
END //
DELIMITER ;

-- Procedure to get order summary
DELIMITER //
CREATE PROCEDURE get_order_summary(IN p_order_id INT)
BEGIN
    SELECT o.order_id, o.order_date, o.status, o.total_amount,
           c.first_name, c.last_name, c.email,
           COUNT(oi.item_id) as total_items
    FROM orders o
    JOIN customers c ON o.customer_id = c.customer_id
    LEFT JOIN order_items oi ON o.order_id = oi.order_id
    WHERE o.order_id = p_order_id
    GROUP BY o.order_id, o.order_date, o.status, o.total_amount,
             c.first_name, c.last_name, c.email;
END //
DELIMITER ;

-- Function to calculate order total
DELIMITER //
CREATE FUNCTION calculate_order_total(p_order_id INT) 
RETURNS DECIMAL(12,2)
DETERMINISTIC
BEGIN
    DECLARE total DECIMAL(12,2);
    SELECT COALESCE(SUM(subtotal), 0) INTO total 
    FROM order_items 
    WHERE order_id = p_order_id;
    RETURN total;
END //
DELIMITER ;

-- ============================================
-- VIEWS
-- ============================================

-- View for employee details with department
CREATE VIEW v_employee_details AS
SELECT 
    e.emp_id,
    CONCAT(e.first_name, ' ', e.last_name) AS full_name,
    e.email,
    e.salary,
    d.dept_name,
    d.location AS dept_location,
    CONCAT(m.first_name, ' ', m.last_name) AS manager_name
FROM employees e
LEFT JOIN departments d ON e.dept_id = d.dept_id
LEFT JOIN employees m ON e.manager_id = m.emp_id;

-- View for order details
CREATE VIEW v_order_details AS
SELECT 
    o.order_id,
    o.order_date,
    o.status,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    c.email AS customer_email,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.subtotal
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id;

-- ============================================
-- INDEXES (for performance demonstration)
-- ============================================
CREATE INDEX idx_emp_dept ON employees(dept_id);
CREATE INDEX idx_emp_email ON employees(email);
CREATE INDEX idx_customer_email ON customers(email);
CREATE INDEX idx_product_category ON products(category);
CREATE INDEX idx_order_customer ON orders(customer_id);
CREATE INDEX idx_order_status ON orders(status);
CREATE INDEX idx_order_date ON orders(order_date);

