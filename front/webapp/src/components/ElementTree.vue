<template>
    <div>
        <component v-for="child in rootNode.children" :key="child.id" :is="resolveComponentName(child)" :model="child"></component>
    </div>
</template>
<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import {ScreenModel} from "@/model/api-models";
    import ElementManager from "@/services/ElementManager";

    @Component({})
    export default class ElementTree extends Vue {
        @Prop(Object)
        rootNode!: ScreenModel;

        resolveComponentName(definition: object): string {
            return ElementManager.resolveComponentName(definition);
        }
    }
</script>
<style lang="scss" scoped>
</style>