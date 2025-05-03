package com.i7.controller.homeowner;

import com.i7.entity.JobMatch;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class JobRequestController {

    public boolean createJobRequest(String listingId, String homeownerUid, String cleanerUid, String serviceDate) {
        LocalDate date = LocalDate.parse(serviceDate);
        return JobMatch.insertJobMatch(listingId, homeownerUid, cleanerUid, date);
    }

    public List<Map<String, String>> getJobMatchesByHomeowner(String homeownerUid) {
        return JobMatch.getMatchesByHomeowner(homeownerUid);
    }

    public boolean markHomeownerCompleted(String matchId, String homeownerUid) {
        return JobMatch.updateHomeownerCompleted(matchId, homeownerUid);
    }

    public Map<String, String>  getMatchById(String matchId) {
        return JobMatch.fetchJobDetails(matchId);
    }
}
