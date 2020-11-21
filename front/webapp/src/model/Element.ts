/**
 * Element to component mapping.
 */
import StringProperty from "@/components/elements/properties/StringProperty.vue";

export class ElementDefinition {
    readonly vueComponentName: string;
    readonly vueComponent: Function;

    constructor(vueComponentName: string, vueComponent: Function) {
        this.vueComponentName = vueComponentName;
        this.vueComponent = vueComponent;
    }
}

export class Element {
    static readonly STRING_PROPERTY = new ElementDefinition("StringProperty", StringProperty);
}
