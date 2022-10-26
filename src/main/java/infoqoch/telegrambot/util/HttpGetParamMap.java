package infoqoch.telegrambot.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpGetParamMap {
    private final Map<String, String> map = new HashMap<>();
    private final String url;

    public HttpGetParamMap(String url) {
        this.url = url;
    }

    public void addParam(String key, String value){
        map.put(key, value);
    }

    public String createUrl(){
        String query = map.keySet().stream()
                .map(k -> k + "=" + map.get(k))
                .collect(Collectors.joining("&"));
        return url + "?" + query;
    }
}

