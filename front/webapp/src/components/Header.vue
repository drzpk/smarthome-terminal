<template>
    <header>
        <b-container fluid="true">
            <b-row align-v="center">
                <b-col>
                    <h1>Terminal</h1>
                </b-col>
                <b-col cols="2" offset="4">
                    <div id="app-selector">
                        <label>Active application:</label>
                        <b-form-select :options="applications" @change="onApplicationChanged" text-field="name"
                                       value-field="id" size="sm"></b-form-select>
                    </div>
                </b-col>
            </b-row>
        </b-container>
    </header>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import {mapState} from "vuex";

@Component({
        computed: {
            ...mapState([
                "applications"
            ])
        }
    })
    export default class Header extends Vue {
        mounted(): void {
            this.$store.dispatch("updateApplicationList")
        }

        onApplicationChanged(applicationId: number) {
            if (applicationId)
                this.$store.dispatch("setActiveApplication", applicationId)
        }
    }
</script>

<style lang="scss" scoped>
    header {
        height: 5em;
        background-color: gray;
    }

    #app-selector {

    }
</style>