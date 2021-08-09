import {
    ProcessScreenUpdateResponse,
    ScreenUpdatedResponse,
    ScreenUpdateErrorResponse,
    ScreenUpdateStatus,
    ScreenValidationErrorResponse
} from "@/model/api/screen-update";
import store from '@/store/index';
import {ScreenMutationTypes} from "@/store/screen";
import ToasterService from "@/services/ToasterService";

class ScreenProcessingResponseHandler {

    handle(response: ProcessScreenUpdateResponse): void {
        switch (response.status) {
            case ScreenUpdateStatus.UPDATED:
                ScreenProcessingResponseHandler.handleUpdated(response);
                break;
            case ScreenUpdateStatus.VALIDATION_ERROR:
                ScreenProcessingResponseHandler.handleValidationError(response);
                break;
            case ScreenUpdateStatus.ERROR:
                ScreenProcessingResponseHandler.handleError(response);
                break;
        }
    }

    private static handleUpdated(response: ScreenUpdatedResponse): void {
        const message = response.message || "Screen has been updated";
        ToasterService.success("Update successful", message);
    }

    private static handleValidationError(response: ScreenValidationErrorResponse): void {
        store.commit(ScreenMutationTypes.SET_SERVER_ERRORS, response.errors);
    }

    private static handleError(response: ScreenUpdateErrorResponse): void {
        const message = response.message || "Unknown error while updating screen";
        ToasterService.error("Screen update error", message);
    }
}

export default new ScreenProcessingResponseHandler();

