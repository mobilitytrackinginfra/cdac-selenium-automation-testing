const { expect } = require("chai");
const { add, subtract } = require("../calculator");

describe("Chai Assertion Examples", () => {

    it("Equality tests", () => {
        expect(add(2, 3)).to.equal(5);
    });

    it("Deep equality tests", () => {
        expect({a:1}).to.deep.equal({a:1});
    });

    it("Truth / Fals tests", () => {
        expect(add(2, 3)==5).to.be.true;
        expect(null).to.be.null;
    });

    it("Type checking", () => {
        expect(add(2, 3)).to.be.a("number");
    });

    it("Array assertions", () => {
        expect([1,2,3]).to.include(2);
    });

    it("Exception testing", () => {
        expect(() => JSON.parse("INVALID")).to.throw();
    });

});
