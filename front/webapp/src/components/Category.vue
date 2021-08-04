<template>
    <div class="category" @click="selectCategory">
        <span></span>
        <div class="category-content">
            <p>{{categoryModel.name}}</p>
            <p v-show="categoryModel.description != null">{{categoryModel.description}}</p>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import {CategoryModel} from "@/model/api-models";

@Component
    export default class Category extends Vue {
        @Prop(Object)
        categoryModel!: CategoryModel;

        selectCategory() {
            this.$store.dispatch("setActiveCategory", this.categoryModel)
        }
    }
</script>

<style lang="scss">
    @import "../styles/_variables";

    .category {
        border-bottom: 1px solid $border-color-primary;
        overflow: auto;
        cursor: pointer;

        &:last-child {
            border-bottom: none;
        }
    }

    .category-content {
        padding: 0.45em;

        > p {
            margin: 0;

            &:first-child {
                font-size: 1.1em;
            }

            &:last-child {
                font-size: 0.85em;
                color: gray;
            }
        }
    }
</style>