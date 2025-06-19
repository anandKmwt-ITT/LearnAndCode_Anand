package com.itt.newsAggregation.repositoy;

import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUser(User user);
}
