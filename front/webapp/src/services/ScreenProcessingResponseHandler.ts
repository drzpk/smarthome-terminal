import {
    ProcessScreenUpdateResponse,
    ScreenUpdatedResponse,
    ScreenUpdateErrorResponse,
    ScreenUpdateStatus,
    ScreenValidationErrorResponse
} from "@/model/api/screen-update";
import store from '@/store/index';
import {ScreenMutationTypes} from "@/store/screen";

class ScreenProcessingResponseHandler {

    handle(response: ProcessScreenUpdateResponse): void {
        switch (response.status) {
            case ScreenUpdateStatus.UPDATED:
                this.handleUpdated(response);
                break;
            case ScreenUpdateStatus.VALIDATION_ERROR:
                ScreenProcessingResponseHandler.handleValidationError(response);
                break;
            case ScreenUpdateStatus.ERROR:
                this.handleError(response);
                break;
        }
    }

    private handleUpdated(response: ScreenUpdatedResponse): void {
        // todo
        console.log("update successful");
    }

    private static handleValidationError(response: ScreenValidationErrorResponse): void {
        store.commit(ScreenMutationTypes.SET_SERVER_ERRORS, response.errors);
    }

    private handleError(response: ScreenUpdateErrorResponse): void {
        // todo
    }
}

export default new ScreenProcessingResponseHandler();

