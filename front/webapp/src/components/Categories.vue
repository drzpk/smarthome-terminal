<template>
    <div>
        <Category v-for="category in categories" :key="category.id" :category-model="category"></Category>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import Category from "@/components/Category.vue";

    @Component({
        components: {
            Category
        },
        computed: {
            categories() {
                return this.$store.state.categories;
            }
        }
    })
    export default class Categories extends Vue {
        mounted(): void {
            this.$store.dispatch("setActiveApplication"); // todo: move to the right place
            this.$store.dispatch("setActiveCategory");
        }
    }
</script>

<style lang="scss" scoped>

</style>