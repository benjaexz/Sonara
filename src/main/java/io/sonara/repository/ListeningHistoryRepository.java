package io.sonara.repository;

import io.sonara.entity.ListeningHistory;
import io.sonara.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, UUID> {

    List<ListeningHistory> findByUserOrderByListenedAtDesc(User user);
}