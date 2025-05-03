package com.i7.controller.cleaner;

import com.i7.entity.JobMatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PastJobSearchController {
    public List<Map<String, String>> searchPastJobs(String cleanerUid, String keyword) {
        return JobMatch.searchPastJobs(cleanerUid, keyword);
    }
}