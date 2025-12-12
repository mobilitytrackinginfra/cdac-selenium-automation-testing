/**
 * Calculator module with basic math operations
 */

/**
 * Adds all numbers in an array
 * @param {number[]} numbers - Array of numbers to add
 * @returns {number} Sum of all numbers
 */
function add(numbers) {
  return numbers.reduce((sum, num) => sum + num, 0);
}

/**
 * Subtracts numbers from the first number
 * @param {number[]} numbers - Array of numbers
 * @returns {number} Result of subtraction
 */
function subtract(numbers) {
  if (numbers.length === 0) return 0;
  return numbers.reduce((result, num, index) => 
    index === 0 ? num : result - num
  );
}

/**
 * Multiplies all numbers in an array
 * @param {number[]} numbers - Array of numbers to multiply
 * @returns {number} Product of all numbers
 */
function multiply(numbers) {
  return numbers.reduce((product, num) => product * num, 1);
}

/**
 * Divides numbers sequentially
 * @param {number[]} numbers - Array of numbers
 * @returns {number} Result of division
 */
function divide(numbers) {
  if (numbers.length === 0) return 0;
  return numbers.reduce((result, num, index) => 
    index === 0 ? num : result / num
  );
}

module.exports = {
  add,
  subtract,
  multiply,
  divide
};

