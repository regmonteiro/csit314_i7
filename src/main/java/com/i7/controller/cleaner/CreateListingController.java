package com.i7.controller.cleaner;

import com.i7.entity.Listing;

public class CreateListingController {
    public boolean createListing(Listing listingDetails) {
        return listingDetails.createListing();
    }
}
