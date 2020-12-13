import Vue from 'vue'
import Vuex from 'vuex'
import ApiService from "@/services/ApiService";
import {ApplicationModel, CategoryModel} from "@/model/api-models";

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        applications: [],
        categories: [],
        application: null,
        category: null,
        screen: null
    },
    getters: {
        getApplicationById(state) {
            return (id: number) => {
                return state.applications.filter(app => (app as ApplicationModel).id === id)[0];
            }
        }
    },
    mutations: {
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
        },
        setActiveScreen(state, screen) {
            state.screen = screen;
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
            const currentApplication = context.state.application as unknown as ApplicationModel;
            ApiService.getScreen(currentApplication.id, category.id).then((screen) => {
                context.commit("setScreen", screen);
            });
        }
    },
    modules: {}
})