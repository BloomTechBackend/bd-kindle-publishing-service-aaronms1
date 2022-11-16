package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.clients.RecommendationsServiceClient;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.kindlepublishingservice.dao.CachingRecommendationDao;
import com.amazon.ata.recommendationsservice.RecommendationsService;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ClientsModule {

    @Singleton
    @Provides
    public RecommendationsServiceClient provideRecommendationsServiceClient(
        RecommendationsService recommendationsService) {
        return new RecommendationsServiceClient(recommendationsService);
    }
    
    @Singleton
    @Provides
    public CachingRecommendationDao provideCachingRecommendationDao(
        RecommendationsService recommendationsService) {
        //MARKER: MT
        return new CachingRecommendationDao(recommendationsService);
    }
}
