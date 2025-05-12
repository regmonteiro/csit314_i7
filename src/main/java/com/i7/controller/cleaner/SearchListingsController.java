package com.i7.controller.cleaner;

import java.util.List;

import com.i7.entity.Listing;

public class SearchListingsController {
    public static List<Listing> searchListings(String uid, String searchQuery){
        return Listing.searchListings(uid, searchQuery);
    }
    public List<Listing> getListings(String uid) {
        return Listing.fetchListings(uid);
    }
}
