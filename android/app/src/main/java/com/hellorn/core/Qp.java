package com.hellorn.core;

public class Qp {
    public String hybrideId;

    public int version;

    public String updateUrl;

    public String md5;

    public Qp(String hybrideId, int version, String url,String md5) {
        this.hybrideId = hybrideId;
        this.version = version;
        this.updateUrl = url;
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "Qp{" +
                "hybrideId='" + hybrideId + '\'' +
                ", version=" + version +
                ", url='" + updateUrl + '\'' +
                '}';
    }
}
