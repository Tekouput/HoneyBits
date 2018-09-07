package com.teko.honeybits.honeybits.API;

import java.util.Map;

public class Request {
    public String url;
    public String type;
    public Map<String, String> parameters;
    public Map<String, String> headers;

    public Request(String url, String type, Map<String, String> parameters, Map<String, String> headers) {
        this.url = "http://teko1.servehttp.com:10092/" + url;
        this.type = type;
        this.parameters = parameters;
        this.headers = headers;
    }
}
