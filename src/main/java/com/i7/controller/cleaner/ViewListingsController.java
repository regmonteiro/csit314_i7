package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import java.util.List;

public class ViewListingsController {

    public List<Listing> getListings(String uid) {
        return Listing.fetchListings(uid);
    }
}