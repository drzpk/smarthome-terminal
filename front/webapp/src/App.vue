<template>
  <div id="app">
    <Header></Header>
    <Configurator></Configurator>
  </div>
</template>

<script lang="ts">
import {Component, Vue, Watch} from "vue-property-decorator";
import Header from "@/components/Header.vue";
import Configurator from "@/components/Configurator.vue";
import ElementManager from "@/services/ElementManager";
import {BvToast} from "bootstrap-vue";
import {mapState} from "vuex";
import {Toast} from "@/model/Toast";
import ToasterService from "@/services/ToasterService";

@Component({
  components: {Header, Configurator},
  computed: {
    ...mapState(["toasts"])
  }
})
export default class App extends Vue {
  $bvToast!: BvToast;

  private toasts!: Toast[];

  mounted(): void {
    ElementManager.registerElementComponents();
  }

  @Watch("toasts")
  onToastsChanged(): void {
    if (this.toasts.length == 0)
      return;

    const toasts = this.toasts;
    for (const toast of toasts) {
      ToasterService.displayToast(this.$bvToast, toast);
    }

    this.$store.commit("removeToasts", toasts);
  }
}
</script>

<style lang="scss">
@import "styles/layout";

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
