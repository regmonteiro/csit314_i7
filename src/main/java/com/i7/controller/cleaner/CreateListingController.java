package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import com.i7.entity.ServiceCategory;
import java.util.List;

public class CreateListingController {
    public boolean createListing(Listing listingDetails) {
        return listingDetails.createListing();
    }

    public List<ServiceCategory> getAllCategories() {
        return ServiceCategory.fetchCategories();
    }
}
