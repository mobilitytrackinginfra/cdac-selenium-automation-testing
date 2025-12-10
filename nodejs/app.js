// app.js - Main file that uses the calculator module

// Import the calculator module
const calculator = require('./calculator');

// You can also destructure specific functions:
// const { add, subtract, multiply, divide, power } = require('./calculator');

console.log("=== Calculator Demo ===\n");

// Using the imported functions
const num1 = 10;
const num2 = 5;

console.log(`Numbers: ${num1} and ${num2}\n`);

// Addition
const sum = calculator.add(num1, num2);
console.log(`Addition: ${num1} + ${num2} = ${sum}`);

// Subtraction
const difference = calculator.subtract(num1, num2);
console.log(`Subtraction: ${num1} - ${num2} = ${difference}`);

// Multiplication
const product = calculator.multiply(num1, num2);
console.log(`Multiplication: ${num1} * ${num2} = ${product}`);

// Division
const quotient = calculator.divide(num1, num2);
console.log(`Division: ${num1} / ${num2} = ${quotient}`);

// Power
const result = calculator.power(num1, 2);
console.log(`Power: ${num1}^2 = ${result}`);

// Demonstrating error handling for division by zero
console.log("\n=== Error Handling Demo ===\n");
try {
    const errorResult = calculator.divide(10, 0);
    console.log(errorResult);
} catch (error) {
    console.log(`Error caught: ${error.message}`);
}

// Chaining operations
console.log("\n=== Chaining Operations ===\n");
const chainResult = calculator.add(
    calculator.multiply(3, 4),
    calculator.subtract(10, 5)
);
console.log(`(3 * 4) + (10 - 5) = ${chainResult}`);

