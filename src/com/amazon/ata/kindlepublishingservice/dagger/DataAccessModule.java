package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * <ul>
 *     <li>{@link DataAccessModule}
 *     this class is used to provide a {@link DynamoDBMapper} instance to the
 *    {@link com.amazon.ata.kindlepublishingservice} App.
 *     </li>
 * </ul>
 */
@Module
public class DataAccessModule {
    @Singleton
    @Provides
    public DynamoDBMapper provideDynamoDBMapper() {
        AmazonDynamoDB amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .withRegion(Regions.US_EAST_1)
            .build();

        return new DynamoDBMapper(amazonDynamoDBClient);
    }
}
