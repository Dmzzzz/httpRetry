package org.example.service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NonBlockingHttpClientExample {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("<http://localhost:8080/all2>"))
                .build();

        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        boolean success = false;

        while (!success && LocalDateTime.now().isBefore(endTime)) {
            try {
                HttpResponse<String> response = future.get();
                if (response.statusCode() == 200) {
                    success = true;
                } else {
                    future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                    TimeUnit.MINUTES.sleep(10);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}