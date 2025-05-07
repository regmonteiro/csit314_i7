package com.i7.controller.cleaner;

import com.i7.entity.Listing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cleaner")
public class SuspendListingController {
    
    public Listing getListingById(String id2) {
        int id = Integer.parseInt(id2);
        return Listing.getListingById(id);
    }
    
}