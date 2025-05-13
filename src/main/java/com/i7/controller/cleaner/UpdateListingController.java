package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import com.i7.entity.ServiceCategory;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cleaner")
public class UpdateListingController {
  public Listing getListingById(String id2) {
    return Listing.getListingById(id2);
  }
  public boolean setListing(String id2, Listing listingDetails){
    return Listing.updateListing(id2,listingDetails);
  }
  
      public List<ServiceCategory> getAllCategories() {
        return ServiceCategory.fetchCategories();
    }
}