const { expect } = require("chai");
const { add } = require("../calculator");

describe("Calculator Tests", () => {
    it("should return 5 when adding 2 and 3", () => {
        expect(add(2, 3)).to.equal(5);
    });
});
