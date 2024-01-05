package org.example.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "response_log")
public class ResponseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "data")
    private String data;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "count_of_retry")
    private Integer countOfRetry;

    @Column(name = "error")
    private String error;

    public ResponseLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCountOfRetry() {
        return countOfRetry;
    }

    public void setCountOfRetry(Integer countOfRetry) {
        this.countOfRetry = countOfRetry;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseLog that = (ResponseLog) o;
        return id.equals(that.id) &&
                Objects.equals(responseCode, that.responseCode) &&
                Objects.equals(data, that.data) &&
                createdAt.equals(that.createdAt) &&
                Objects.equals(countOfRetry, that.countOfRetry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, responseCode, data, createdAt, countOfRetry);
    }
}