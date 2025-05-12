package com.i7.controller.cleaner;
import java.util.List;

import com.i7.entity.Listing;

public class IncrementViewsController {
    public List<Listing> getListings(String uid) {
        return Listing.fetchListings(uid);
    }
    public Listing getListingById(String id2){
        return Listing.getListingById(id2);
    }
    public boolean incrementViewCount(String id){
        return Listing.incrementViewCount(id);
    }
}

