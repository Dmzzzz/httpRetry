package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.ResponseLog;
import org.example.model.ResponseLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ResponseLogService {

    private final ResponseLogRepository responseLogRepository;

    @Transactional
    public Long saveResponseLog(
            String responseCode,
            String data,
            Integer counter,
            String error) {
        ResponseLog responseLog = new ResponseLog();
        responseLog.setResponseCode(responseCode);
        responseLog.setData(data.length() > 255 ? data.substring(0, 255) : data);
        responseLog.setCountOfRetry(counter);
        responseLog.setError(error.length() > 255 ? error.substring(0, 255): error);
        ResponseLog saved = responseLogRepository.save(responseLog);
        return saved.getId();
    }
}
