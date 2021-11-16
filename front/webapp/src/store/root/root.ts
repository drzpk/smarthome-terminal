import Vue from 'vue'
import Vuex from 'vuex'
import ApiService from "@/services/ApiService";
import {ApplicationModel, CategoryModel} from "@/model/api/api-models";
import {ScreenActionTypes} from './../screen/screen-types';
import {Toast} from "@/model/Toast";
import {RootState} from "@/store/root/root-types";
import {ScreenModule} from "@/store/screen/screen";
import StoreProviderService from "@/services/StoreProviderService";

Vue.use(Vuex);

const root = new Vuex.Store<RootState>({
    state: {
        toasts: [],
        applications: [],
        categories: [],
        application: null,
        category: null
    },
    getters: {
        getApplicationById(state) {
            return (id: number) => {
                return state.applications.filter(app => (app as ApplicationModel).id === id)[0];
            }
        }
    },
    mutations: {
        addToasts(state, toasts: Toast[]) {
            state.toasts.push(...toasts);
        },
        removeToasts(state, toasts: Toast[]) {
            for (const toast of toasts) {
                const index = state.toasts.indexOf(toast);
                if (index > -1) {
                    Vue.delete(state.toasts, index);
                }
            }
        },
        setApplications(state, applications) {
            state.applications = applications;
        },
        setCategories(state, categories) {
            state.categories = categories;
        },
        setActiveApplication(state, application) {
            state.application = application;
        },
        setActiveCategory(state, category) {
            state.category = category;
        }
    },
    actions: {
        updateApplicationList({commit}) {
            ApiService.getApplications().then((response) => {
                commit("setApplications", response);
            });
        },

        setActiveApplication: function (context, value: number | ApplicationModel) {
            let application!: ApplicationModel;
            if (typeof value === "object")
                application = value;
            else
                application = context.getters.getApplicationById(value);

            console.debug(`Setting active application to ${application}`);
            context.commit("setActiveApplication", application);

            ApiService.getCategories(application.id).then((categories) => {
                context.commit("setCategories", categories);
            });
        },

        setActiveCategory: function (context, category: CategoryModel) {
            console.debug(`Setting active category to ${category}`);
            context.commit("setActiveCategory", category);

            context.dispatch(ScreenActionTypes.SET_ACTIVE_SCREEN, category.id);
        }
    },

    modules: {
        ScreenModule
    }
});

StoreProviderService.register(root);
export default root;