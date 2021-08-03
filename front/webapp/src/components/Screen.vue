<template>
  <div id="screen">
    <div id="screen-content" v-if="screen != null">
      <h3>{{ category.name }}</h3>
      <b-form name="screen-form" novalidate>
        <div id="tree-container">
          <ElementTree :root-node="screen"></ElementTree>
        </div>

        <div id="button-container">
          <b-button class="send-button" variant="outline-primary" @click="updateScreen()">Save</b-button>
        </div>
      </b-form>
    </div>
  </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import {mapState} from "vuex";
import ElementTree from "@/components/ElementTree.vue";
import {ApplicationModel, CategoryModel, ElementModel, PropertyModel, ScreenModel} from "@/model/api-models";
import ApiService from "@/services/ApiService";
import ScreenProcessingResponseHandler from "@/services/ScreenProcessingResponseHandler";

@Component({
  components: {
    ElementTree
  },
  computed: mapState([
    "application",
    "category",
    "screen"
  ]),
  data() {
    return {}
  }
})
export default class Screen extends Vue {
  private application!: ApplicationModel;
  private category!: CategoryModel;
  private screen!: ScreenModel;

  private $bvToast: any;

  updateScreen() {
    if (!this.arePropertiesValid()) {
      this.$bvToast.toast("Cannot perform screen update - there are validation errors", {
        title: "Validation error",
        variant: "danger"
      });
      return;
    }

    // todo: spinner when waiting for server response
    ApiService.updateScreen(this.application.id, this.category.id, this.serializeProperties()).then(response => {
      ScreenProcessingResponseHandler.handle(response);
    });
  }

  private arePropertiesValid(root: ElementModel = this.screen): boolean {
    if (root.elementType === "property") {
      const property = root as PropertyModel;
      if (property.isValid === undefined)
        console.warn(`Validation flag for property ${property.id} ("${property.label}") is undefined`);

      return (property).isValid || false;
    } else {
      for (const child of root.children) {
        if (!this.arePropertiesValid(child))
          return false;
      }
      return true;
    }
  }

  private serializeProperties(): Map<number, string | null> {
    const queue: Array<ElementModel> = [this.screen];
    const map = new Map<number, string | null>();

    while (queue.length > 0) {
      const item = queue.shift()!;
      if (item.elementType === "property") {
        map.set(item.id, (item as PropertyModel).value)
      }

      queue.push(...item.children);
    }

    return map;
  }

}
</script>

<style lang="scss" scoped>
@import "../styles/layout";

#screen {
  min-height: 500px;
}

#button-container {
  margin: 2em $property-margin 0;
  display: flex;
  justify-content: flex-end;
}
</style>