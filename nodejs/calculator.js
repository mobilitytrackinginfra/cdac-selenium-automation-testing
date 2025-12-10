// calculator.js - Module containing calculator functions

/**
 * Adds two numbers
 * @param {number} a - First number
 * @param {number} b - Second number
 * @returns {number} Sum of a and b
 */
function add(a, b) {
    return a + b;
}

/**
 * Subtracts second number from first
 * @param {number} a - First number
 * @param {number} b - Second number
 * @returns {number} Difference of a and b
 */
function subtract(a, b) {
    return a - b;
}

/**
 * Multiplies two numbers
 * @param {number} a - First number
 * @param {number} b - Second number
 * @returns {number} Product of a and b
 */
function multiply(a, b) {
    return a * b;
}

/**
 * Divides first number by second
 * @param {number} a - Dividend
 * @param {number} b - Divisor
 * @returns {number} Quotient of a and b
 * @throws {Error} If divisor is zero
 */
function divide(a, b) {
    if (b === 0) {
        throw new Error("Cannot divide by zero!");
    }
    return a / b;
}

/**
 * Calculates the power of a number
 * @param {number} base - Base number
 * @param {number} exponent - Exponent
 * @returns {number} base raised to exponent
 */
function power(base, exponent) {
    return Math.pow(base, exponent);
}

// Export all functions to make them available to other files
module.exports = {
    add,
    subtract,
    multiply,
    divide,
    power
};

