package com.i7.controller.homeowner;

import com.i7.entity.JobMatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JobSearchController {
    public List<Map<String, String>> searchPastJobsByHomeowner(String uid, String keyword) {
        return JobMatch.searchPastJobsByHomeowner(uid, keyword);
    }
}
