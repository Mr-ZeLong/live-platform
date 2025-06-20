package com.logilong.live.web.starter.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LiveErrorException extends RuntimeException{

    private int errorCode;
    private String errorMsg;

    public LiveErrorException(LiveBaseError liveBaseError) {
        this.errorCode = liveBaseError.getErrorCode();
        this.errorMsg = liveBaseError.getErrorMsg();
    }

}
