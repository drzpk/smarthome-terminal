import {ApplicationModel, CategoryModel, ScreenModel} from "@/model/api/api-models";

import Axios, {AxiosResponse} from "axios";
import {ProcessScreenUpdateRequest, ProcessScreenUpdateResponse} from "@/model/api/screen-update";

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

    updateScreen(applicationId: number, categoryid: number, properties: Map<number, string | null>): Promise<ProcessScreenUpdateResponse> {
        const request: ProcessScreenUpdateRequest = {
            screenId: categoryid,
            properties: Object.fromEntries(properties)
        };

        return Axios.post(API_URL + `/applications/${applicationId}/categories/${categoryid}/screen`, request).then((response) => {
            return response.data as ProcessScreenUpdateResponse;
        });
    }
}

export default new ApiService();