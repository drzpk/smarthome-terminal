import {Component, Prop, Vue} from "vue-property-decorator";
import {PropertyModel} from "@/model/api-models";
import {Validator, Validators} from "@/model/property/validators";
import {NotImplementedError} from "@/model/errors";

interface Validation {
    isValid: boolean;
    state: boolean | null; // BootstrapVue field state
    validationErrorMessage: string | null;
    validators: Array<Validator>;
}

@Component({})
export default class VueProperty<T> extends Vue {
    /** Property model from the server. */
    @Prop()
    model!: PropertyModel;

    /** Deserialized value. Values in property models are always transferred between server and web client as strings. */
    value: T | null = null;

    validation: Validation = {
        isValid: false,
        state: null,
        validationErrorMessage: null,
        validators: []
    };

    mounted() {
        if (!this.model) {
            throw "Property model is not available";
        }

        this.value = this.deserialize(this.model.value);

        this.addValidator(Validators.required);
    }

    deserialize(input: string | null): T | null {
        // Vue @Component classes cannot be abstract (https://github.com/vuejs/vue-class-component/issues/342)
        throw new NotImplementedError();
    }

    serialize(input: T | null): string | null {
        throw new NotImplementedError();
    }

    addValidator(validator: Validator) {
        this.validation.validators.push(validator);
    }

    valueChanged() {
        for (const validator of this.validation.validators) {
            const result = validator.validate(this.value);
            if (!result) {
                this.validation.isValid = false;
                this.validation.state = false;
                this.validation.validationErrorMessage = validator.getErrorMessage(this.model.label, this.value);
                return;
            }
        }

        this.validation.isValid = true;
        this.validation.state = null;
        this.validation.validationErrorMessage = null;

        // Performance isn't important at this stage so just serialize the value every time it's changed
        this.model.value = this.serialize(this.value);
    }
}