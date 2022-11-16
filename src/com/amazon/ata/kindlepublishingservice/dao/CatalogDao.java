package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;

import java.util.*;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression =
          new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results =
          dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
    
    
    /**
     * Implement RemoveBookFromCatalog
     * <p>
     * We already got a head start on this. You’ll need to now add some logic
     * to do a soft delete. We don’t want to lose previous versions of the book
     * that we have sold to customers. Instead, we’ll mark the current version
     * as inactive so that it can never be returned by the GetBook operation,
     * essentially deleted.
     * <p>
     * We’ll need to update our CatalogDao to implement this “delete”
     * functionality and use that in our Activity class.
     */
    public boolean removeBookFromCatalog(String bookId) {
        //MARKER:removeBookFromCatalog boolean function
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
        book.setInactive(Boolean.parseBoolean(eventId));
    
        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap =
          new HashMap<>();
        expectedAttributeValueMap
          .put("RemoveBookFromCatalog",
             new ExpectedAttributeValue()
               .withValue(new AttributeValue().withBOOL(true)));
        try {
            deleteExpression.setExpected(expectedAttributeValueMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        dynamoDbMapper.delete(book, deleteExpression);
        return true;
    }
}
