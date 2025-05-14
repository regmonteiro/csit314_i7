package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cleaner")
public class ViewShortlistController {
    public Listing getListingById(String id){
        return Listing.getListingById(id);
    }

    public String getShortlistCount(String listing_id){
        return Listing.getShortlistCount(listing_id);
    }
}
