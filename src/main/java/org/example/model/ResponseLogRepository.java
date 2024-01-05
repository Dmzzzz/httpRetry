package org.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseLogRepository extends JpaRepository<ResponseLog, Long> {

}