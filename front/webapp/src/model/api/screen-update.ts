export interface ProcessScreenUpdateRequest {
    screenId: number;
    properties: { [p: string]: string | null };
}

export interface ProcessScreenUpdateResponse {
    status: ScreenUpdateStatus;
}

export interface ScreenUpdatedResponse extends ProcessScreenUpdateResponse {
    message?: string;
}

export interface ScreenValidationErrorResponse extends ProcessScreenUpdateResponse {
    errors?: Map<number, string>;
}

export interface ScreenUpdateErrorResponse extends ProcessScreenUpdateResponse {
    message?: string;
}

export enum ScreenUpdateStatus {
    UPDATED = "UPDATED",
    ERROR = "ERROR",
    VALIDATION_ERROR = "VALIDATION_ERROR"
}
