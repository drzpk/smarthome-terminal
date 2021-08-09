import {Component, Prop, Vue, Watch} from "vue-property-decorator";
import {PropertyModel} from "@/model/api/api-models";
import {Validator, Validators} from "@/model/property/validators";
import {NotImplementedError} from "@/model/errors";
import {ScreenMutationTypes} from "@/store/screen";

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
        this.getValidators().forEach((validator) => this.addValidator(validator));
        this.validate();
    }

    @Watch("serverError")
    private onServerErrorChanged(): void {
        this.validate();
    }

    getValidators(): Array<Validator> {
        return [];
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
        this.clearServerError();
        if (!this.validate())
            return;

        // Performance isn't important at this stage so just serialize the value every time it's changed
        this.model.value = this.serialize(this.value);
    }

    private validate(): boolean {
        const validationErrorMessage = this.getValidationErrorMessage();

        this.validation.isValid = validationErrorMessage == null;
        this.validation.state = this.validation.isValid ? null : false;
        this.validation.validationErrorMessage = validationErrorMessage;

        this.updatePropertyModel();
        return true;
    }

    private getValidationErrorMessage(): string | null {
        if (this.serverError != null) {
            return this.serverError;
        }

        let message: string | null = null;
        for (const validator of this.validation.validators) {
            const result = validator.validate(this.value);
            if (!result) {
                message = validator.getErrorMessage(this.model.label, this.value);
                break;
            }
        }

        return message;
    }

    private clearServerError(): void {
        if (this.serverError != null)
            this.$store.commit(ScreenMutationTypes.CLEAR_SERVER_ERROR, this.model.id);
    }

    private get serverError(): string | null {
        return this.$store.getters.getServerError(this.model.id);
    }

    private updatePropertyModel() {
        this.model.isValid = this.validation.isValid;
    }
}