package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.publishing.*;

import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(
      ScheduledExecutorService scheduledExecutorService,
      PublishingTask publishingTask) {
        return new BookPublisher(scheduledExecutorService, publishingTask);
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }
    
    @Provides
    @Singleton
    public PublishRequestManager providePublishRequestManager() {
        return new PublishRequestManager();
    }
}
