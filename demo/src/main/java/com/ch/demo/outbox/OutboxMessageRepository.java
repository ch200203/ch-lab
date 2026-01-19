package com.ch.demo.outbox;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

    List<OutboxMessage> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
