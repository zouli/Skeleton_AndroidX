package com.riverside.skeleton.android.net.jsonbean;

import lombok.Data;

@Data
public class JsonResponse<T> {
    private String error_code;
    private String is_default_pw;
    private String resultflag;
    private String error_msg;
    private T bus_json;
}
