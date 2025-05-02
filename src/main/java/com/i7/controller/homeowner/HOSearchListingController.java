package com.i7.controller.homeowner;

import com.i7.entity.Listing;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HOSearchListingController {

    public List<Map<String, String>> searchListings(String searchQuery) {
        return Listing.searchWithKeyword(searchQuery);
    }
}
