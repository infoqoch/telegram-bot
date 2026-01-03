package infoqoch.telegrambot.util;

import infoqoch.telegrambot.bot.response.HttpResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public class HttpClientHttpHandler implements HttpHandler{
    private final HttpClient httpClient;

    public static HttpClientHttpHandler createDefault(){
        return new HttpClientHttpHandler(HttpClient.newHttpClient());
    }

    @Override
    public HttpResponseWrapper get(HttpGetParamMap httpGetParamMap) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(httpGetParamMap.createUrl()))
                .GET()
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return HttpResponseWrapper.of(response.statusCode(), response.body());

        }catch (IOException | InterruptedException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HttpResponseWrapper post(String url, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return HttpResponseWrapper.of(response.statusCode(), response.body());

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
