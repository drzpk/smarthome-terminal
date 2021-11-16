import {Toast} from "@/model/Toast";
import {ApplicationModel, CategoryModel} from "@/model/api/api-models";

export interface RootState {
    toasts: Toast[];
    applications: ApplicationModel[];
    categories: CategoryModel[];
    application: ApplicationModel | null;
    category: CategoryModel | null;
}
