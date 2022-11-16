package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;

import javax.inject.Inject;
import java.util.*;

/**
 * <ul>
 *     <li>{@link RemoveBookFromCatalogActivity}
 *     This class is used to remove a book from the catalog, it uses the
 *     {@link CatalogDao} to remove the book from the catalog.
 *     </li>
 * </ul>
 */
public class RemoveBookFromCatalogActivity {
    private CatalogDao catalogDao;
    @Inject
    RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }
    
    /**
     * This Method removes a book from the catalog.
     * @param removeBookFromCatalogRequest The request containing the book to remove.
     * @return The response containing the book that was removed.
     */
    public RemoveBookFromCatalogResponse execute(
      RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {
        catalogDao.removeBookFromCatalog(
          removeBookFromCatalogRequest.getBookId());
        return new RemoveBookFromCatalogResponse();
    }
}
