import Vue from 'vue'
import Vuex from 'vuex'
import ApiService from "@/services/ApiService";

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        categories: [],
        screen: null
    },
    mutations: {
        setCategories(state, categories) {
            state.categories = categories;
        },
        setScreen(state, screen) {
            state.screen = screen;
        }
    },
    actions: {
        setActiveApplication({commit}) {
            ApiService.getCategories().then((categories) => {
                commit("setCategories", categories);
            });
        },
        setActiveCategory({commit}) {
            ApiService.getScreen().then((screen) => {
                commit("setScreen", screen);
            });
        }
    },
    modules: {}
})
