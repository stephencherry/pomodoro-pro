package com.platform.pomodoropro.entity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {
    private String responseCode;
    private String message;
    private Object data;

    public ResponseModel(Object object){
        this.data = object;

        if(data==null){
            this.responseCode = "XX";
            this.message = "failed";
        }else{
            this.responseCode = "00";
            this.message = "success";
        }
    }
}
