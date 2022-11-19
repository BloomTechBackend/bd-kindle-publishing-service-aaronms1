package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

/**
 * <ul>
 *     <li>{@link PublishingTask}
 *     This immutable  class is used to handle the task of injecting the
 *     {@link CatalogDao},
 *     {@link PublishingStatusDao}, and the
 *     {@link PublishRequestManager}objects into the {@link BookPublisher}
 *     class
 *     </li>
 * </ul>
 */
public class PublishingTask implements Runnable {
    private final CatalogDao            catalogDao;
    private final PublishingStatusDao   publishingStatusDao;
    private final PublishRequestManager requestManager;

    @Inject
    public PublishingTask(CatalogDao catalogDao,
                          PublishingStatusDao publishingStatusDao,
                          PublishRequestManager requestManager) {
        this.catalogDao = catalogDao;
        this.publishingStatusDao = publishingStatusDao;
        this.requestManager =  requestManager;
    }

    /**
     * <ul>
     *     <li>{@link PublishingTask#run()}
     *     This method is used to run the task of publishing a book, and also
     *     handles catching {@link BookNotFoundException}if the request is not
     *     found, then the status of the book is set to
     *     {@link PublishingRecordStatus#FAILED}, and the thread can be
     *     terminated.
     *     Otherwise {@link PublishingRecordStatus} is set to SUCCESSFUL.
     *     </li>
     * </ul>
     */
    @Override
    public void run() {
        BookPublishRequest request =
          requestManager.getBookPublishRequestToProcess();
        int i = 0;
        while (request == null && i < 500) {
            try {
                Thread.sleep(1000);
                request = requestManager.getBookPublishRequestToProcess();
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        publishingStatusDao.setPublishingStatus(
          request.getPublishingRecordId(),
          PublishingRecordStatus.IN_PROGRESS, request.getBookId());
        KindleFormattedBook fBook =
          KindleFormatConverter.format(request);
        try {
            CatalogItemVersion book = catalogDao.updateBookStatus(fBook);
            publishingStatusDao.setPublishingStatus(
                request.getPublishingRecordId(),
                PublishingRecordStatus.SUCCESSFUL, book.getBookId());
        } catch (BookNotFoundException e) {
            publishingStatusDao.setPublishingStatus(
              request.getPublishingRecordId(),
              PublishingRecordStatus.FAILED, request.getBookId(),
              e.getMessage());
        } catch (Exception e) {
            if (request.getPublishingRecordId() == null) {
                throw new BookNotFoundException(
                  "Book not found in catalog: " + request.getBookId());
            }
        }
    }
}
