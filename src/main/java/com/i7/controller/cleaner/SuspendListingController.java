package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cleaner")
public class SuspendListingController {
    
    public Listing getListingById(String id2) {
        return Listing.getListingById(id2);
    }
    public boolean setSuspendListing(String id2,Listing suspendedListing)
    {
        return Listing.suspendListing(id2,suspendedListing);
    }
    public List<Listing> suspendedListings(String uid){
        return Listing.fetchSuspendedListings(uid);
    }
}