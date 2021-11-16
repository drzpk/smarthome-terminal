import {Store} from "vuex";
import {RootState} from "@/store/root/root-types";

/**
 * Provides a way for non-Vue components to communicate with Vuex store without causing circular reference errors.
 */
class StoreProviderService {
    private store: Store<RootState> | null = null;

    register(store: Store<RootState>): void {
        this.store = store;
    }
    getStore(): Store<RootState> {
        if (!this.store)
            throw new Error("Store wasn't registered");

        return this.store;
    }
}

export default new StoreProviderService();
