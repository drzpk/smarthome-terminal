import {CategoryModel, ScreenModel} from "@/model/api-models";
import Axios from "axios";

// todo: Add production url
const API_URL = "http://192.168.3.196:8081/api";

class ApiService {

    getCategories(): Promise<Array<CategoryModel>> {
        return Promise.resolve([
            {id: 1, name: "name", "description": "desc"},
            {id: 2, name: "name1", "description": "desc"},
            {id: 3, name: "name2", "description": null}
        ]);
    }
    getScreen(): Promise<ScreenModel> {
        return Axios.get(API_URL + "/applications/1/categories/1/screen").then((response) => {
            return response.data as ScreenModel
        });
    }
}

export default new ApiService();