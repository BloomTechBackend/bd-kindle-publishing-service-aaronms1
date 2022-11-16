package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <ul>
 *     <li>{@link PublishRequestManager}
 *     This class is used to manage the book publishing requests, and
 *     creates a new linked list of book publishing requests.
 *     It works in conjunction with {@link BookPublisher}, and
 *     {@link PublishingTask} to publish the
 *     books into the Kindle Store Queue.
 *     </li>
 * </ul>
 */
public class PublishRequestManager {
    private static Queue<BookPublishRequest> bookPublishRequestQueue;
    
    @Inject
    public PublishRequestManager() {
        bookPublishRequestQueue = new ConcurrentLinkedQueue<>();
    }
    
    public Queue<BookPublishRequest> getBookPublishRequestQueue() {
        Queue<BookPublishRequest> copyQueue =
          new ConcurrentLinkedQueue<>(bookPublishRequestQueue);
        return copyQueue;
    }
    
    public void setBookPublishRequestQueue(
      Queue<BookPublishRequest> bookPublishRequestQueue) {
        PublishRequestManager.bookPublishRequestQueue = bookPublishRequestQueue;
    }
    
    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        bookPublishRequestQueue.add(bookPublishRequest);
    }
    
    public static BookPublishRequest getBookPublishRequestToProcess() {
        return bookPublishRequestQueue.poll();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PublishRequestManager that = (PublishRequestManager) o;
        return getBookPublishRequestQueue()
                 .equals(that.getBookPublishRequestQueue());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getBookPublishRequestQueue());
    }
    
}
