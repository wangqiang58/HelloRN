package com.hellorn.core;

public class Qp {
    public String hybrideId;

    public int version;

    public String url;

    public Qp(String hybrideId, int version, String url) {
        this.hybrideId = hybrideId;
        this.version = version;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Qp{" +
                "hybrideId='" + hybrideId + '\'' +
                ", version=" + version +
                ", url='" + url + '\'' +
                '}';
    }
}
