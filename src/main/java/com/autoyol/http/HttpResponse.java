package com.autoyol.http;

import java.util.Map;

/**
 * http请求的响应
 * Created by andy on 15/11/25.
 */
public class HttpResponse {
    private int httpRespCode=-1;
    private Map<String,Object> responseBodyObject;

    public int getHttpRespCode() {
        return httpRespCode;
    }

    public void setHttpRespCode(int httpRespCode) {
        this.httpRespCode = httpRespCode;
    }

    public void setResponseBodyObject(Map<String,Object> responseBodyObject) {
        this.responseBodyObject = responseBodyObject;
    }

    public Map<String,Object> getResponseBodyObject() {
        return responseBodyObject;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpRespCode=" + httpRespCode +
                ", responseBodyObject=" + responseBodyObject +
                '}';
    }
}
