package com.i7.controller.cleaner;

import com.i7.entity.Listing;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cleaner")
  public class UpdateListingController {

    public List<Listing> getListings(String uid) {
          return Listing.fetchListings(uid);
    }
    
  public Listing getListingById(String id2) {
      return Listing.getListingById(id2);
    }
  public boolean updateListing(Listing listing){
    return Listing.updateListing(listing);
  }
}