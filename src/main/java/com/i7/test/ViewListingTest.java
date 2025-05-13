package com.i7.test;

import com.i7.entity.Listing;

import java.util.List;

public class ViewListingTest {
    public static void run() {
        System.out.println("=== View Listings Test ===");

        String cleanerUid = "23456789";

        List<Listing> listings = Listing.fetchListings(cleanerUid);

        if (listings.isEmpty()) {
            System.out.println("No listings found for cleaner UID: " + cleanerUid);
        } else {
            System.out.println("Found " + listings.size() + " listings for " + cleanerUid + ":");
            for (Listing l : listings) {
                System.out.println("- ID: " + l.getId());
                System.out.println("  Title: " + l.getTitle());
                System.out.println("  Description: " + l.getDescription());
                System.out.println("  Price: $" + l.getPrice());
                System.out.println("  Views: " + l.getViews());
                System.out.println("  Status: " + l.getStatus());
                System.out.println();
            }
        }
    }
}
