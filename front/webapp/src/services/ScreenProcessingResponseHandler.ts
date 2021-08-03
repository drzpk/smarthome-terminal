import {ProcessScreenUpdateResponse, ScreenUpdateStatus} from "@/model/api-models";
import store from '@/store/index';
import {ScreenMutationTypes} from "@/store/screen";

class ScreenProcessingResponseHandler {

    handle(response: ProcessScreenUpdateResponse): void {
        switch (response.status) {
            case ScreenUpdateStatus.UPDATED:
                this.handleUpdated();
                break;
            case ScreenUpdateStatus.VALIDATION_ERROR:
                ScreenProcessingResponseHandler.handleValidationError(response.errors!);
                break;
            case ScreenUpdateStatus.ERROR:
                this.handleError();
                break;
            case ScreenUpdateStatus.UNKNOWN:
                this.handleUnknown();
                break;

        }
    }

    private handleUpdated(): void {
        // todo
    }

    private static handleValidationError(errors: Map<number, string>): void {
        store.commit(ScreenMutationTypes.SET_SERVER_ERRORS, errors);
    }

    private handleError(): void {
        // todo
    }

    private handleUnknown(): void {
        // todo
    }
}

export default new ScreenProcessingResponseHandler();

