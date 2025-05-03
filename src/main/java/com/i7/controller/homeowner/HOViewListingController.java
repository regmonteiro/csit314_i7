package com.i7.controller.homeowner;

import com.i7.entity.Listing;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class HOViewListingController {
    public Map<String, String> getCleanerListingDetails(String listingId) {
        return Listing.getCleanerListingDetails(listingId);
    }
}
