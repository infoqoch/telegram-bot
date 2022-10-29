package infoqoch.telegrambot.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpGetParamMap {
    private final Map<String, String> map = new HashMap<>();
    private final String url;

    public HttpGetParamMap(String url, Map<String, String> defaultQueryString) {
        final Optional<String> queryString = generateDefaultQueryString(defaultQueryString);
        if(queryString.isPresent()){
            this.url = url + "?" + queryString.get() +"&";
        }else{
            this.url = url + "?";
        }
    }

    public HttpGetParamMap(String url) {
        this.url = url + "?";
    }

    private Optional<String> generateDefaultQueryString(Map<String, String> map) {
        if(map.isEmpty()) return Optional.empty();
        return Optional.of(mapToQueryString(map));
    }

    public void addParam(String key, String value){
        map.put(key, value);
    }

    public String createUrl(){
        final String result = url + mapToQueryString(map);

        map.clear();

        return result;
    }

    private String mapToQueryString(Map<String, String> map) {
        return map.keySet().stream()
                .map(k -> k + "=" + map.get(k))
                .collect(Collectors.joining("&"));
    }
}