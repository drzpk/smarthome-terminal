import StringProperty from "@/components/elements/properties/simple/StringProperty.vue";
import ElementManager from "@/services/ElementManager";

describe("ElementResolver", () => {
    const manager = ElementManager;

    test("should resolve string property", () => {
        const definition = {
            elementType: "property",
            propertyType: "string"
        };
        expect(manager.resolveComponentName(definition)).toBe("StringProperty");
    });

    test("shouldn't resolve unknown property", () => {
        const definition = {
            elementType: "property",
            propertyType: "i don't exist"
        };
        expect(() => manager.resolveComponentName(definition)).toThrow(/Component wasn't found for selector.*/);
    });
});
