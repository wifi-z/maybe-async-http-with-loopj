package com.maybe.async.http.component;

public enum EncodingType {
    UTF_8("UTF-8"),
    EUC_KR("EUC-KR"),
    ISO_8859_1("ISO-8859-1"),
    KSC_5601("ksc5601"),
    X_WINDOWS_949("x-windows-949");

    private String type;

    EncodingType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
