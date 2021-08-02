import {ApplicationModel, CategoryModel, ScreenModel} from "@/model/api-models";
import Axios, {AxiosResponse} from "axios";

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
}

export default new ApiService();