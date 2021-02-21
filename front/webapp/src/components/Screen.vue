<template>
    <div id="screen">
        <div id="screen-content" v-if="screen != null">
            <h3>{{category.name}}</h3>
            <div id="tree-container">
                <b-form name="screen-form" novalidate>
                    <ElementTree :root-node="screen"></ElementTree>
                </b-form>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {mapState} from "vuex";
    import ElementTree from "@/components/ElementTree.vue";

    @Component({
        components: {
            ElementTree
        },
        computed: mapState([
            "category",
            "screen"
        ]),
        data() {
            return {}
        }
    })
    export default class Screen extends Vue {

    }
</script>

<style lang="scss" scoped>
    #screen {
        min-height: 500px;
        border: 1px solid red;
    }
</style>