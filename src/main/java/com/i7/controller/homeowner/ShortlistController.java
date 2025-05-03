package com.i7.controller.homeowner;

import com.i7.entity.Shortlist;
import org.springframework.stereotype.Component;

@Component
public class ShortlistController {

    public boolean addCleanerToShortlist(String uid, String cleanerId) {
        return Shortlist.saveToShortlist(uid, cleanerId);
    }
}