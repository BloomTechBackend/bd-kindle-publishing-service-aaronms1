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
 *     This class is used to handle the task of injecting the
 *     {@link CatalogDao},
 *     {@link PublishingStatusDao}, and the
 *     {@link PublishRequestManager}objects into the {@link BookPublisher}
 *     </li>
 * </ul>
 */
public class PublishingTask implements Runnable {
    private CatalogDao catalogDao;
    private PublishingStatusDao publishingStatusDao;
    private PublishRequestManager publishRequestManager;

    @Inject
    public PublishingTask(CatalogDao catalogDao,
                          PublishingStatusDao publishingStatusDao,
                          PublishRequestManager publishRequestManager) {
        this.catalogDao = catalogDao;
        this.publishingStatusDao = publishingStatusDao;
        this.publishRequestManager = publishRequestManager;
    }

    @Override
    public void run() {
        BookPublishRequest request =
          PublishRequestManager.getBookPublishRequestToProcess();
        int i = 0;
        while (request == null && i < 10) {
            try {
                Thread.sleep(1000);
                request =
                  PublishRequestManager.getBookPublishRequestToProcess();
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //assert request != null;
        //assert not null to avoid a null pointer exception
        publishingStatusDao.setPublishingStatus(
          request.getPublishingRecordId(),
          PublishingRecordStatus.IN_PROGRESS, request.getBookId());
        KindleFormattedBook formattedBook =
          KindleFormatConverter.format(request);
        try {
            CatalogItemVersion book =
              catalogDao.updateBookStatus(formattedBook);
            publishingStatusDao.setPublishingStatus(
                request.getPublishingRecordId(),
                PublishingRecordStatus.SUCCESSFUL, book.getBookId());
        } catch (BookNotFoundException e) {
            publishingStatusDao.setPublishingStatus(
                request.getPublishingRecordId(),
                PublishingRecordStatus.FAILED,
                request.getBookId(), e.getMessage());
            if (request.getPublishingRecordId() == null) {
                throw new BookNotFoundException(
                  "Book not found in catalog: " + request.getBookId());
            }
        }
    }
}
