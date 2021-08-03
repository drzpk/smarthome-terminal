<template>
    <div id="app">
        <Header></Header>
        <Configurator></Configurator>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import Header from "@/components/Header.vue";
import Configurator from "@/components/Configurator.vue";
import ElementManager from "@/services/ElementManager";

@Component({
      components: {Header, Configurator}
    })
    export default class App extends Vue {
        mounted(): void {
            ElementManager.registerElementComponents();
        }
    }
</script>

<style lang="scss">
    @import "styles/layout";

    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
    }
</style>
