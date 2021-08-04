<template>
  <b-container id="container" v-if="application != null">
    <b-row>
      <b-col cols="3" id="category-container">
        <Categories></Categories>
      </b-col>
      <b-col cols="9" id="screen-container" v-show-loader="screenLoading">
        <Screen></Screen>
      </b-col>
    </b-row>
  </b-container>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import Categories from "@/components/Categories.vue";
import Screen from "@/components/Screen.vue";
import {mapGetters, mapState} from "vuex";

@Component({
  components: {
    Categories,
    Screen
  },
  computed: {
    ...mapState([
      "application"
    ]),
    ...mapGetters([
      "screenLoading"
    ])
  }
})
export default class Configurator extends Vue {
  screenLoading!: boolean;
}
</script>

<style lang="scss" scoped>
@import "../styles/_variables";

#container {
  border: 1px solid $border-color-primary;
  border-radius: 5px;
  min-height: 20em;
  min-width: 1400px;
  margin-top: 3em;
}

#category-container {
  border-right: 1px solid $border-color-primary;
}

#screen-container {
  padding: 1em;
}
</style>