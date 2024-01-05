/*
package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class MyService {

    private final HttpClient httpClient;
    private final ResponseLogService responseLogService;
    public static int MAX_RESEND = 5;

    @Autowired
    public MyService(final HttpClient httpClient, ResponseLogService responseLogService) {
        this.httpClient = httpClient;
        this.responseLogService = responseLogService;
    }

    @Value("${amur.getUrl}")
    private String urlForSend;

    @Value("${time.retryEveryMinute}")
    private Long retryMinutes;
    @Value("${time.retryHour}")
    private Integer retryHour;

    public void send() {
        try {
            final HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                            .uri(new URI(urlForSend))
                            .headers("Content-Type", "text/plain;charset=UTF-8")
                            .GET()
                            .build();

            HttpResponse<String> response = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String g = "";
        } catch (Exception e) {
            String a = e.getCause().getMessage();
        }
    }

    public void send2() throws URISyntaxException {
        final HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(new URI(urlForSend))
                        .headers("Content-Type", "text/plain;charset=UTF-8")
                        .GET()
                        .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(httpRequest, handler)
                        .handleAsync((r, t) -> tryResend(httpClient, httpRequest, handler, 1, r, t))
                        .thenCompose(Function.identity());

        try {
            HttpResponse<String> httpResponse = response.get();
            responseLogService.saveResponseLog(
                    String.valueOf(httpResponse.statusCode()),
                    httpResponse.body());
        } catch (InterruptedException | ExecutionException e) {
            responseLogService.saveResponseLog(
                    "500",
                    e.getCause().getMessage());
        }
    }

    public static <T> CompletableFuture<HttpResponse<T>>
    tryResend(HttpClient client, HttpRequest request,
              HttpResponse.BodyHandler<T> handler,
              int count,
              HttpResponse<T> resp, Throwable t) {

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

    public static boolean shouldRetry(HttpResponse<?> r, Throwable t, int count) {
        if (r != null && r.statusCode() == 200 || count >= MAX_RESEND) {
            return false;
        }

        return true;
    }

    public void send3() throws URISyntaxException {
        final HttpRequest httpRequest =
                HttpRequest.newBuilder()
                        .uri(new URI(urlForSend))
                        .headers("Content-Type", "text/plain;charset=UTF-8")
                        .GET()
                        .build();

        LocalDateTime endTime = LocalDateTime.now().plusMinutes(retryHour);

        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());

        boolean success = false;

        while (!success && LocalDateTime.now().isBefore(endTime)) {
            try {
                HttpResponse<String> response = future.get();
                if (response.statusCode() == 200) {
                    responseLogService.saveResponseLog(
                            String.valueOf(response.statusCode()),
                            response.body());
                    success = true;
                } else {
                    future = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
                    TimeUnit.MINUTES.sleep(retryMinutes);
                }
            } catch (InterruptedException | ExecutionException e) {
                responseLogService.saveResponseLog(
                        "500",
                        e.getCause().getMessage());
            }
        }
    }
}*/
