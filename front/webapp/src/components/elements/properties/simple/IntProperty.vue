<!--suppress XmlInvalidId -->
<template>
    <div class="property">
        <label for="int-input">{{model.label}}:</label>
        <b-form-input id="int-input" type="number" :placeholder="model.label"></b-form-input>
    </div>
</template>

<script lang="ts">
    import {Component} from "vue-property-decorator";
    import VueProperty from '@/model/property/VueProperty';

    @Component({})
    export default class IntProperty extends VueProperty<number> {

        deserialize(input: string | null): number | null {
            return input ? Number.parseInt(input) : null;
        }

        serialize(input: number | null): string | null {
            return input ? input.toString() : null;
        }
    }
</script>

<style lang="scss">

</style>