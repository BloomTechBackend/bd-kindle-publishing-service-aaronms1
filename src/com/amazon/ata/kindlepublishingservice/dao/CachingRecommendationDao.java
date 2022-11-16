package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;
import com.google.common.cache.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachingRecommendationDao {
    private final LoadingCache<BookGenre,
        List<BookRecommendation>> recommendationsCache;
    
    @Inject
    public CachingRecommendationDao(RecommendationsService service) {
        recommendationsCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(CacheLoader.from(service::getBookRecommendations));
    }
    
    public List<BookRecommendation> getBookRecommendations(BookGenre genre) {
        return recommendationsCache.getUnchecked(genre);
    }
}
