const { expect, assert } = require('chai');
const { add, subtract, multiply, divide } = require('../src/calculator');

describe('Math Tests', function () {

  before(() => {
    console.log("Start Test Suite");
  });

  after(() => {
    console.log("End Test Suite");
  });

  beforeEach(() => {
    console.log("Start Test");
  });

  afterEach(() => {
    console.log("End Test");
  });

  it('should add numbers correctly', function () {
    const result = add([2, 3]);
    expect(result).to.equal(5);
  });

  it('should handle async functions', async function () {
    const data = await Promise.resolve(10);
    expect(data).to.equal(10);
  });

  describe("add()", function () {
    const tests = [
      { args: [1, 2], expected: 3 },
      { args: [1, 2, 3], expected: 6 },
      { args: [1, 2, 3, 4], expected: 10 },
    ];
  
    tests.forEach(({ args, expected }) => {
      it(`correctly adds ${args.length} args`, function () {
        const res = add(args);
        assert.strictEqual(res, expected);
      });
    });
  });

  describe("subtract()", function () {
    it('should subtract numbers correctly', function () {
      const result = subtract([10, 3, 2]);
      expect(result).to.equal(5);
    });
  });

  describe("multiply()", function () {
    it('should multiply numbers correctly', function () {
      const result = multiply([2, 3, 4]);
      expect(result).to.equal(24);
    });
  });

  describe("divide()", function () {
    it('should divide numbers correctly', function () {
      const result = divide([20, 2, 2]);
      expect(result).to.equal(5);
    });
  });

});
