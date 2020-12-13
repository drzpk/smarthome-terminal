export interface ApplicationModel {
    id: number;
    name: string
}

export interface CategoryModel {
    id: number;
    name: string;
    description?: string|null;
}

export interface ScreenModel {
    id: number;
}

export interface ElementModel {
    elementType: string;
}

export interface PropertyModel extends ElementModel {
    elementType: "property";
}

export type StringPropertyModel = PropertyModel;