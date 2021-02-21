export interface ApplicationModel {
    id: number;
    name: string
}

export interface CategoryModel {
    id: number;
    name: string;
    description?: string | null;
}

export interface ElementModel {
    elementType: string;
    children: Array<ElementModel>
}

export interface ScreenModel extends ElementModel {
    elementType: "screen";
    id: number;
}


export interface PropertyModel extends ElementModel {
    elementType: "property";
    label: string;
    required: boolean;
    value: string | null;
}

export type StringPropertyModel = PropertyModel;

export type IntPropertyModel = PropertyModel;