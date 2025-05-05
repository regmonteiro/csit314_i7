package com.i7.controller.cleaner;

import com.i7.entity.Listing;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@Component
@RequestMapping("/cleaner")
public class UpdateListingController {

  public List<Listing> getListings(String uid) {
        return Listing.fetchListings(uid);
  }
  public Listing getListingById(int lid) { //might be STRING
    return Listing.getListingById(lid);
  }
    

}