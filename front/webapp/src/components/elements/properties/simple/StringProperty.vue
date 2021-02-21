<!--suppress XmlInvalidId -->
<template>
    <div class="property string-property">
        <label for="string-input">{{model.label}}:</label>
        <b-form-input
                id="string-input"
                :placeholder="model.label"
                :required="model.required"
                :state="validation.state"
                v-model="value"
                @change="valueChanged()"
        ></b-form-input>
        <b-form-invalid-feedback :state="validation.state">{{validation.validationErrorMessage}}</b-form-invalid-feedback>
    </div>
</template>

<script lang="ts">
    import {Component} from "vue-property-decorator";
    import VueProperty from "@/model/property/VueProperty";

    @Component({})
    export default class StringProperty extends VueProperty<string> {

        deserialize(input: string | null): string | null {
            return input;
        }

        serialize(input: string | null): string | null {
            return input;
        }
    }
</script>

<style lang="scss">
    .string-property {
    }
</style>