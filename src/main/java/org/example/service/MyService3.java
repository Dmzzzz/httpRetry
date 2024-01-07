package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyService3 {

    private final HttpClient httpClient;
    private final RetryTemplate retryTemplate;
    private final ResponseLogService responseLogService;

    @Value("${amur.getUrl}")
    private String urlForSend;

    @Value("${time.retryEverySecond}")
    private Integer retryMinutes;
    @Value("${time.retryCount}")
    private Integer retryCount;

/*    @Retryable(value = {ConnectException.class, RuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))*/
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
    /*    try {
            httpClient.send(httpRequest, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
*/
        try {
            retryTemplate.execute(new RetryCallback<Object, Throwable>() {
                @Override
                public Object doWithRetry(RetryContext retryContext) throws Throwable {
                    HttpResponse<String> response = httpClient.send(httpRequest, handler);

                    return response;
                }
            });
        } catch (Throwable throwable) {
            log.info("Запрос не был обработан");
        }
    }
/*
    @Recover
    public void recover() {
      log.info("recover метод срабюотал");
    }*/


}