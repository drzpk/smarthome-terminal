import {Vue} from "vue-property-decorator";
import StringProperty from "@/components/elements/properties/StringProperty.vue";
import {Component} from "vue";
import {Element, ElementDefinition} from "@/model/Element";

interface TreeNode {
    selector?: string;
    element?: ElementDefinition;
    children?: { [key: string]: TreeNode };
}

const elementTree: TreeNode = {
    selector: "elementType",
    children: {
        "property": {
            selector: "propertyType",
            children: {
                "string": {
                    element: Element.STRING_PROPERTY
                },
                "int": {}
            }
        }
    }
};

class ElementManager {
    registerElementComponents() {
        Object.entries(Element).forEach((entry) => {
            const definition = entry[1] as ElementDefinition;
            Vue.component(definition.vueComponentName, definition.vueComponent);
        });
    }

    resolveComponentName(definition: object): string | undefined {
        return this.doResolveComponentName(definition, elementTree);
    }

    private doResolveComponentName(definition: any, node: TreeNode): string | undefined {
        ElementManager.verifyNode(node);
        if (node.selector! in definition) {
            const value = definition[node.selector!];
            const childNode = node.children![value];

            if (childNode)
                return this.doResolveComponentName(definition, childNode);
        } else if (node.element!) {
            return node.element!.vueComponentName;
        }

        return undefined;
    }

    private static verifyNode(node: TreeNode) {
        if (!node.selector && !node.element)
            throw new Error("Node should have selector or element");
        if (node.selector && !node.children)
            throw new Error("Node with selector must have children");
        if (node.selector && node.element)
            throw new Error("Node can't have both selector and element");
    }
}

export default new ElementManager();