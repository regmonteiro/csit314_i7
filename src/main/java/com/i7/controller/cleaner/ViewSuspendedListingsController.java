package com.i7.controller.cleaner;

import com.i7.entity.Listing;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/cleaner")
public class ViewSuspendedListingsController {

    public List<Listing> getSuspendedListings(String uid) {
        return Listing.fetchSuspendedListings(uid);
    }
    
}