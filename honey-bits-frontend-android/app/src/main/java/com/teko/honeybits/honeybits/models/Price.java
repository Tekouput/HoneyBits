package com.teko.honeybits.honeybits.models;

public class Price {
    private String raw;
    private String formatted;

    public Price(String raw, String formatted) {
        this.raw = raw;
        this.formatted = formatted;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}
