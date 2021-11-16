import {ApplicationModel, CategoryModel, ScreenModel} from "@/model/api/api-models";

import Axios, {AxiosResponse} from "axios";
import {ProcessScreenUpdateRequest, ProcessScreenUpdateResponse} from "@/model/api/screen-update";
import {ScreenUpdateData} from "@/model/screen";

const API_URL = "http://localhost:8080/api";

class ApiService {

    getApplications(): Promise<Array<ApplicationModel>> {
        return Axios.get(API_URL + "/applications").then((response: AxiosResponse<Array<ApplicationModel>>) => {
            return response.data;
        });
    }

    getCategories(applicationId: number): Promise<Array<CategoryModel>> {
        return Axios.get(API_URL + `/applications/${applicationId}/categories`).then((response: AxiosResponse<Array<CategoryModel>>) => {
            return response.data;
        });
    }

    getScreen(applicationId: number, categoryId: number): Promise<ScreenModel> {
        return Axios.get(API_URL + `/applications/${applicationId}/categories/${categoryId}/screen`).then((response) => {
            return response.data as ScreenModel
        });
    }

    updateScreen(data: ScreenUpdateData): Promise<ProcessScreenUpdateResponse> {
        const request: ProcessScreenUpdateRequest = {
            screenId: data.categoryId,
            properties: Object.fromEntries(data.properties)
        };

        return Axios.post(API_URL + `/applications/${data.applicationId}/categories/${data.categoryId}/screen`, request).then((response) => {
            return response.data as ProcessScreenUpdateResponse;
        });
    }
}

export default new ApiService();