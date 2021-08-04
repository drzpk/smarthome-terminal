import Vue from "vue";
import {DirectiveBinding, DirectiveOptions} from "vue/types/options";

const LOADER_CONTAINER_CLASS = "loader-container";
const LOADER_INJECTED_ELEMENT_CLASS = "loader-injected-element";
const LOADER_INJECTED_ELEMENT_ACTIVE_CLASS = "loader-injected-element-active";
const LOADER_CLASS = "lds-dual-ring";
const TRANSITION_DURATION = 100;

class ShowLoader implements DirectiveOptions {
    inserted(el: HTMLElement, binding: DirectiveBinding) {
        if (binding.value)
            ShowLoader.updateLoader(true, el);
    }

    update(el: HTMLElement, binding: DirectiveBinding) {
        if (binding.value != binding.oldValue)
            ShowLoader.updateLoader(binding.value, el);
    }

    private static updateLoader(status: boolean, el: HTMLElement): void {
        if (status)
            ShowLoader.attachLoader(el);
        else
            ShowLoader.detachLoader(el);
    }

    private static attachLoader(el: HTMLElement): void {
        el.classList.add(LOADER_CONTAINER_CLASS);

        const div = document.createElement("div");
        div.classList.add(LOADER_INJECTED_ELEMENT_CLASS);
        div.innerHTML = `<div class="${LOADER_CLASS}"></div>`;
        el.appendChild(div);

        // This class must be added after element is created and appended to DOM,
        // otherwise fade-in animation won't work.
        Vue.nextTick(() => div.classList.add(LOADER_INJECTED_ELEMENT_ACTIVE_CLASS));
    }

    private static detachLoader(el: HTMLElement): void {
        el.classList.remove(LOADER_CONTAINER_CLASS);

        for (const injected of el.getElementsByClassName(LOADER_INJECTED_ELEMENT_CLASS)) {
            injected.classList.remove(LOADER_INJECTED_ELEMENT_ACTIVE_CLASS);
            setTimeout(() => {
                el.removeChild(injected);
            }, TRANSITION_DURATION);
        }
    }
}

Vue.directive("show-loader", new ShowLoader());
