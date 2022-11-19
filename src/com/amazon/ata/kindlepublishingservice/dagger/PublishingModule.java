package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.publishing.*;

import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    /**
     * <ul>
     *     <li>{@link PublishingModule#provideBookPublisher(ScheduledExecutorService, PublishingTask)}
     *     This method is used to provide the {@link PublishingTask} object
     *     to the {@link BookPublisher} class
     *     </li>
     * </ul>
     * @param scheduledExecutorService The {@link ScheduledExecutorService} object
     * @param publishingTask The {@link PublishingTask} object
     * @return PublishingTask
     */
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
    
    /**
     * <ul>
     *     <li>{@link PublishingModule#providePublishRequestManager}
     *     This method is used to provide the {@link PublishingTask} object
     *     to the {@link BookPublisher} class
     *     </li>
     * </ul>
     */
    @Provides
    @Singleton
    public PublishRequestManager providePublishRequestManager() {
        return new PublishRequestManager();
    }
}
