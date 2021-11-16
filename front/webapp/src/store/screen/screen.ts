import {ActionContext, ActionTree, GetterTree, Module, MutationTree} from 'vuex'
import {RootState} from "@/store/root/root-types";
import {ApplicationModel, ScreenModel} from "@/model/api/api-models";
import ApiService from "@/services/ApiService";
import {ScreenUpdateData} from "@/model/screen";
import ScreenProcessingResponseHandler from "@/services/ScreenProcessingResponseHandler";
import {ScreenActionTypes, ScreenMutationTypes, ScreenState} from "@/store/screen/screen-types";

type Getters = {
    getServerError(state: ScreenState): (fieldId: number) => string | null;
    screen(state: ScreenState): ScreenModel | null;
    screenLoading(state: ScreenState): boolean;
}

type Mutations<S = ScreenState> = {
    [ScreenMutationTypes.SET_SCREEN](state: S, screen: ScreenModel | null): void;
    [ScreenMutationTypes.SET_SCREEN_LOADING](state: S, loading: boolean): void;
    [ScreenMutationTypes.SET_SERVER_ERRORS](state: S, errors: { [key: number]: string }): void;
    [ScreenMutationTypes.CLEAR_SERVER_ERROR](state: S, fieldId: number): void;
}

type ScreenActionContext = {
    commit<K extends keyof Mutations>(key: K, payloadD: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>;
} & Omit<ActionContext<ScreenState, RootState>, "commit">

interface Actions {
    [ScreenActionTypes.SET_ACTIVE_SCREEN](context: ScreenActionContext, screenId: number): void;
    [ScreenActionTypes.UPDATE_SCREEN](context: ScreenActionContext, data: ScreenUpdateData): void;
}

//////////////////

const getters: GetterTree<ScreenState, RootState> & Getters = {
    getServerError(state: ScreenState): (fieldId: number) => (string | null) {
        return fieldId => {
            const error = state.serverErrors.get(fieldId);
            return error ? error : null;
        };
    },

    screen(state: ScreenState): ScreenModel | null {
        return state.screen;
    },

    screenLoading(state: ScreenState): boolean {
        return state.screenLoading;
    }
}

const mutations: MutationTree<ScreenState> & Mutations = {
    [ScreenMutationTypes.SET_SCREEN](state: ScreenState, screen: ScreenModel | null): void {
        state.screenLoading = false;
        state.screen = screen;
    },

    [ScreenMutationTypes.SET_SCREEN_LOADING](state: ScreenState, loading: boolean): void {
        state.screenLoading = loading;
    },

    [ScreenMutationTypes.SET_SERVER_ERRORS](state: ScreenState, errors: { [key: number]: string }): void {
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

const actions: ActionTree<ScreenState, RootState> & Actions = {
    [ScreenActionTypes.SET_ACTIVE_SCREEN](context: ScreenActionContext, screenId: number): void {
        context.commit(ScreenMutationTypes.SET_SCREEN_LOADING, true);

        const currentApplication = context.rootState.application as unknown as ApplicationModel;
        ApiService.getScreen(currentApplication.id, screenId).then((screen) => {
            context.commit(ScreenMutationTypes.SET_SCREEN, screen);
        }).catch(error => {
            console.error(error);
        }).then(() => {
            context.commit(ScreenMutationTypes.SET_SCREEN_LOADING, false);
        });
    },

    [ScreenActionTypes.UPDATE_SCREEN](context: ScreenActionContext, data: ScreenUpdateData): void {
        context.commit(ScreenMutationTypes.SET_SCREEN_LOADING, true);

        ApiService.updateScreen(data).then(response => {
            ScreenProcessingResponseHandler.handle(response);
        }).catch(error => {
            console.error(error);
        }).then(() => {
            context.commit(ScreenMutationTypes.SET_SCREEN_LOADING, false);
        });
    }
}

export const ScreenModule: Module<ScreenState, RootState> = {
    state: {
        screen: null,
        screenLoading: false,
        serverErrors: new Map()
    },
    getters: getters,
    mutations: mutations,
    actions: actions,
    modules: {}
}
