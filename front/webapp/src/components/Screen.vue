<template>
    <div id="screen">
        <h3>This is a screen</h3>
        <div id="element-container">

        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";

    @Component({
        computed: {
            screen() {
                return this.$store.state.screen;
            }
        },
        data() {
            return {

            }
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