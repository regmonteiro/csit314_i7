package com.i7.controller.cleaner;

import com.i7.entity.JobMatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CleanerJobController {
    public List<Map<String, String>> getPendingAndConfirmedJobs(String cleanerUid) {
        List<Map<String, String>> pendingJobs = JobMatch.fetchPendingJobs(cleanerUid);
        List<Map<String, String>> confirmedJobs = JobMatch.fetchConfirmedJobs(cleanerUid);
        pendingJobs.addAll(confirmedJobs); // Combine the lists of pending and confirmed jobs
        return pendingJobs;
    }

    public List<Map<String, String>> getPendingJobs(String cleanerUid) {
        return JobMatch.fetchPendingJobs(cleanerUid);
    }

    public List<Map<String, String>> getConfirmedJobs(String cleanerUid) {
        return JobMatch.fetchConfirmedJobs(cleanerUid);
    }

    public List<Map<String, String>> getCompletedJobs(String cleanerUid) {
        return JobMatch.fetchCompletedJobs(cleanerUid);
    }

    public Map<String, String> getJobDetails(String matchId) {
        return JobMatch.fetchJobDetails(matchId);
    }

    public boolean confirmJob(String matchId) {
        return JobMatch.changeJobStatus(matchId, "confirmed");
    }

    public boolean completeJob(String matchId, String cleanerUid) {
        return JobMatch.updateCleanerCompleted(matchId, cleanerUid);
    }
}