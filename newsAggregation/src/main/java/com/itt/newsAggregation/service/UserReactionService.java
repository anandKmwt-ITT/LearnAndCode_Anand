package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.UserReactionDto;
import java.util.Map;
import java.util.List;

public interface UserReactionService {
    void addOrUpdateReaction(UserReactionDto dto);
    void removeReaction(UserReactionDto dto);
    Map<String, Long> getReactionCountsForArticle(Integer articleId);
    List<UserReactionDto> getUserReactions(Integer userId);
}
