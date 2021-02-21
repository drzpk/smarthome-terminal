/**
 * Element to component mapping.
 */
import StringProperty from "@/components/elements/properties/simple/StringProperty.vue";
import IntProperty from "@/components/elements/properties/simple/IntProperty.vue";

export class ElementDefinition {
    readonly vueComponentName: string;
    readonly vueComponent: Function;

    constructor(vueComponentName: string, vueComponent: Function) {
        this.vueComponentName = vueComponentName;
        this.vueComponent = vueComponent;
    }
}

export class Elements {
    static readonly STRING_PROPERTY = new ElementDefinition("StringProperty", StringProperty);
    static readonly INT_PROPERTY = new ElementDefinition("IntProperty", IntProperty);
}
