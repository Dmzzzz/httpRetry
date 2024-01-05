package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class MyService2 {

    private final HttpClient httpClient;
    private final ResponseLogService responseLogService;

    public MyService2(HttpClient httpClient, ResponseLogService responseLogService) {
        this.httpClient = httpClient;
        this.responseLogService = responseLogService;
    }

    @Value("${amur.getUrl}")
    private String urlForSend;

    @Value("${time.retryEverySecond}")
    private Integer retryMinutes;
    @Value("${time.retryCount}")
    private Integer retryCount;


    public void send() {
        URI uri = null;
        try {
            uri = new URI(urlForSend);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(uri)
                        .headers("Content-Type", "text/plain;charset=UTF-8")
                        .GET()
                        .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(httpRequest, handler)
                        .handleAsync((r, t) -> tryResend(httpClient, httpRequest, handler, 1, r, t))
                        .thenCompose(Function.identity());

    }

    public <T> CompletableFuture<HttpResponse<T>> tryResend(
            HttpClient client,
            HttpRequest request,
            HttpResponse.BodyHandler<T> handler,
            int count,
            HttpResponse<T> resp,
            Throwable t) {
        if (shouldRetry(resp, t, count)) {
            return client.sendAsync(request, handler)
                    .handleAsync((r, x) -> tryResend(client, request, handler, count + 1, r, x))
                    .thenCompose(Function.identity());
        } else if (t != null) {
            return CompletableFuture.failedFuture(t);
        } else {
            return CompletableFuture.completedFuture(resp);
        }
    }

    public boolean shouldRetry(HttpResponse<?> r, Throwable t, int count) {
        if (r != null && r.statusCode() == 200) {
            responseLogService.saveResponseLog(
                    "200",
                    String.valueOf(r.body()),
                    count,
                    "");
            return false;
        }
        responseLogService.saveResponseLog(
                String.valueOf(r != null ? r.statusCode() : ""),
                "",
                count,
                t.getMessage());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return count < retryCount;
    }

}