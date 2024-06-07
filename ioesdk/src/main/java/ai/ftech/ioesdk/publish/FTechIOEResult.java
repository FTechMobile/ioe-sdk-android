package ai.ftech.ioesdk.publish;

import androidx.annotation.NonNull;

import ai.ftech.ioesdk.domain.APIException;

public final class FTechIOEResult<T> {
    private T data;
    private FTECH_IOE_RESULT_TYPE type = FTECH_IOE_RESULT_TYPE.CANCEL;
    private APIException error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public APIException getError() {
        return error;
    }

    public void setError(APIException error) {
        this.error = error;
    }

    @NonNull
    public FTECH_IOE_RESULT_TYPE getType() {
        return type;
    }

    public void setType(@NonNull FTECH_IOE_RESULT_TYPE type) {
        this.type = type;
    }
}
