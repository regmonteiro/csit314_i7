package com.i7.controller.homeowner;

import com.i7.entity.Shortlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShortlistSearchController {

    public List<Map<String, String>> searchShortlist(String uid, String keyword) {
        return Shortlist.searchShortlist(uid, keyword);
    }
}
