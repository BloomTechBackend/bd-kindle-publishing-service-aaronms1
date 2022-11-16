package com.amazon.ata.kindlepublishingservice.controllers;

import com.amazon.ata.kindlepublishingservice.*;
import com.amazon.ata.kindlepublishingservice.activity.*;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.models.*;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class Controller {
    private static final ApplicationComponent component = App.component;

    @GetMapping(value = "/books/{id}", produces = {"application/json"})
    public ResponseEntity<?> getBook(@PathVariable String id) {
        GetBookActivity bookActivity = component.provideGetBookActivity();
        GetBookRequest getBookRequest = GetBookRequest.builder().withBookId(id).build();
        return new ResponseEntity<>(bookActivity.execute(getBookRequest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/books/{id}")
    public ResponseEntity<?> removeBook(@PathVariable String id) {
        //MARKER: RemoveBookFromCatalogActivity, was (return null)
        RemoveBookFromCatalogActivity removeBookFromCatalogActivity = component.provideRemoveBookFromCatalogActivity();
        RemoveBookFromCatalogRequest  removeBookFromCatalogRequest  = RemoveBookFromCatalogRequest.builder().withBookId(id).build();
        removeBookFromCatalogActivity.execute(removeBookFromCatalogRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/books", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> submitBookForPublishing(@Valid @RequestBody Book book) {
        //MARKER: SubmitBookForPublishingActivity, was (return null)
        SubmitBookForPublishingActivity submitBookForPublishingActivity = component.provideSubmitBookForPublishingActivity();
       // return new ResponseEntity<>(submitBookForPublishingActivity
        // .execute(book), HttpStatus.CREATED);
        return null;
    }
}
