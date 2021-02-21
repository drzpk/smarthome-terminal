export abstract class Validator {
    abstract readonly name: string;

    abstract validate(value: any): boolean

    abstract getErrorMessage(fieldName: string, value: any): string
}

class RequiredValidator extends Validator {
    name = "required";

    validate(value: any): boolean {
        if (typeof value === "string") {
            return value.length > 0;
        }

        return !!value;
    }

    getErrorMessage(fieldName: string, value: any): string {
        return `${fieldName} is required.`;
    }
}

const Validators = {
    required: new RequiredValidator()
};

export {Validators};