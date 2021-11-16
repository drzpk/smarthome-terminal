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
import {mapGetters, mapState} from "vuex";
import ElementTree from "@/components/ElementTree.vue";
import {ApplicationModel, CategoryModel, ElementModel, PropertyModel, ScreenModel} from "@/model/api/api-models";
import ToasterService from "@/services/ToasterService";
import {ScreenActionTypes} from "@/store/screen/screen-types";
import {ScreenUpdateData} from "@/model/screen";

@Component({
  components: {
    ElementTree
  },
  computed: {
    ...mapState([
      "application",
      "category",
    ]),
    ...mapGetters([
        "screen"
    ])
  },
  data() {
    return {}
  }
})
export default class Screen extends Vue {
  private application!: ApplicationModel;
  private category!: CategoryModel;
  private screen!: ScreenModel;

  updateScreen() {
    if (!this.arePropertiesValid()) {
      ToasterService.error("Validation error", "Cannot perform screen update - there are validation errors");
      return;
    }

    const data: ScreenUpdateData = {
      applicationId: this.application.id,
      categoryId: this.category.id,
      properties: this.serializeProperties()
    };

    this.$store.dispatch(ScreenActionTypes.UPDATE_SCREEN, data);
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