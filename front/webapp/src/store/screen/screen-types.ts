import {ScreenModel} from "@/model/api/api-models";

export interface ScreenState {
    screen: ScreenModel | null;
    screenLoading: boolean;
    serverErrors: Map<number, string>;
}

export enum ScreenMutationTypes {
    SET_SCREEN = "SET_SCREEN",
    SET_SCREEN_LOADING = "SET_SCREEN_LOADING",
    SET_SERVER_ERRORS = "SET_SERVER_ERRORS",
    CLEAR_SERVER_ERROR = "CLEAR_SERVER_ERROR"
}

export enum ScreenActionTypes {
    SET_ACTIVE_SCREEN = "SET_ACTIVE_SCREEN",
    UPDATE_SCREEN = "UPDATE_SCREEN"
}