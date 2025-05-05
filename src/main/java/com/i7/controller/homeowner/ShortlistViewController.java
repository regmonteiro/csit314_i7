package com.i7.controller.homeowner;

import com.i7.entity.Shortlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShortlistViewController {

    public List<Map<String, String>> fetchAll(String uid) {
        return Shortlist.fetchAllShortlistedListings(uid);
    }
    
    public Map<String, String> getListingDetails(String listingId) {
        return Shortlist.getListingWithCleanerDetails(listingId);
    }
}
