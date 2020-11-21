<template>
    <b-container id="container">
        <b-row>
            <b-col cols="3">
                <Categories></Categories>
            </b-col>
            <b-col cols="9">
                <Screen></Screen>
            </b-col>
        </b-row>
    </b-container>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import Categories from "@/components/Categories.vue";
    import Screen from "@/components/Screen.vue";

    @Component({
        components: {
            Categories,
            Screen
        },
        // todo: get categories from vuex
    })
    export default class Configurator extends Vue {

    }
</script>

<style lang="scss" scoped>
    @import "../styles/colors";

    #container {
        border: 1px solid $border-color-primary;
        border-radius: 5px;
        min-height: 20em;
        min-width: 1400px;
        margin-top: 3em;
    }
</style>