import {Toast} from "@/model/Toast";

import {BvToast} from "bootstrap-vue";
import StoreProviderService from "@/services/StoreProviderService";

class ToasterService {
    DEFAULT_DURATION = 5000;

    info(title: string, text: string, duration = this.DEFAULT_DURATION) {
        this.toast("info", title, text, duration);
    }

    success(title: string, text: string, duration = this.DEFAULT_DURATION) {
        this.toast("success", title, text, duration);
    }

    warning(title: string, text: string, duration = this.DEFAULT_DURATION) {
        this.toast("warning", title, text, duration);
    }

    error(title: string, text: string, duration = this.DEFAULT_DURATION) {
        this.toast("danger", title, text, duration);
    }

    toast(variant: string, title: string, text: string, duration: number) {
        const toast: Toast = {
            variant,
            title,
            text,
            duration
        };

        StoreProviderService.getStore().commit("addToasts", [toast]);
    }

    displayToast(bvToast: BvToast, toast: Toast) {
        bvToast.toast(toast.text, {
            title: toast.title,
            variant: toast.variant,
            autoHideDelay: toast.duration
        });
    }
}

export default new ToasterService();