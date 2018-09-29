package com.teko.honeybits.honeybits.API;

import java.util.Map;

public class Request {
    public String url;
    public Map<String, Object> parameters;
    public Map<String, String> headers;

    public Request(String url, Map<String, Object> parameters, Map<String, String> headers) {
        this.url = "http://teko1.servehttp.com:10092/" + url;
        this.parameters = parameters;
        this.headers = headers;
    }
}
