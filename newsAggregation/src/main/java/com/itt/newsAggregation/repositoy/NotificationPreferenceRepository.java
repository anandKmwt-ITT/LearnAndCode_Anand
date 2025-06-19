package com.itt.newsAggregation.repositoy;

import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.NotificationPreference;
import com.itt.newsAggregation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Integer> {

    NotificationPreference findByUserAndCategory(User user, Category category);
    List<NotificationPreference> findByUser(User user);
}
