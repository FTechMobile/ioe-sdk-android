package ai.ftech.ioesdk.publish;

import ai.ftech.ioesdk.domain.APIException;

public interface IFTechIOECallback<DATA> {
    default void onSuccess(DATA info) {
    }

    default void onFail(APIException error) {
    }

    default void onCancel() {
    }
}
