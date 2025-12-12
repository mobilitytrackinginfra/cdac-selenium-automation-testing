const { add, subtract } = require("../../calculator");

describe("Jasmine Matchers Demo", () => {

    it("toBe example", () => {
        expect(add(10,20)).toBe(30);
    });

    it("toEqual example", () => {
        expect({x:1}).toEqual({x:1});
    });

    it("truthy/falsy tests", () => {
        expect(add(10,20)==30).toBeTruthy();
        expect(null).toBeFalsy();
    });

    it("array contains value", () => {
        expect([1,2,3]).toContain(2);
    });

    it("throws an error", () => {
        expect(() => { throw new Error("boom") }).toThrow();
    });

});
