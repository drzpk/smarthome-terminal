import {GetterTree, Module, MutationTree} from 'vuex'
import {RootState} from "@/store/index";

export interface ScreenState {
    serverErrors: Map<number, string>;
}

export enum ScreenMutationTypes {
    SET_SERVER_ERRORS = "SET_SERVER_ERRORS",
    CLEAR_SERVER_ERROR = "CLEAR_SERVER_ERROR"
}

type Getters = {
    getServerError(state: ScreenState): (fieldId: number) => string | null;
}

type Mutations<S = ScreenState> = {
    [ScreenMutationTypes.SET_SERVER_ERRORS](state: S, errors: {[key: number]: string}): void;
    [ScreenMutationTypes.CLEAR_SERVER_ERROR](state: S, fieldId: number): void;
}

//////////////////

const getters: GetterTree<ScreenState, RootState> & Getters = {
    getServerError(state: ScreenState): (fieldId: number) => (string | null) {
        return fieldId => {
            const error = state.serverErrors.get(fieldId);
            return error ? error : null;
        };
    }
}

const mutations: MutationTree<ScreenState> & Mutations = {
    [ScreenMutationTypes.SET_SERVER_ERRORS](state: ScreenState, errors: {[key: number]: string}): void {
        const newErrors = new Map<number, string>();
        for (const key in errors) {
            newErrors.set(parseInt(key), errors[key]);
        }

        state.serverErrors = newErrors;
    },

    [ScreenMutationTypes.CLEAR_SERVER_ERROR](state: ScreenState, fieldId: number): void {
        if (!state.serverErrors.has(fieldId))
            return;

        const newErrors = new Map<number, string>();
        for (const [key, value] of state.serverErrors) {
            if (key !== fieldId)
                newErrors.set(key, value);
        }

        state.serverErrors = newErrors;
    }
}

export const ScreenModule: Module<ScreenState, RootState> = {
    state: {
        serverErrors: new Map()
    },
    getters: getters,
    mutations: mutations,
    actions: {},
    modules: {}
}
