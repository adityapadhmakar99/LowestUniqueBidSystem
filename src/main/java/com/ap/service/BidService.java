package com.ap.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class BidService {
    
    // Maps bid amount to set of user IDs who placed that bid
    private final ConcurrentMap<Integer, Set<String>> amountToUsers = new ConcurrentHashMap<>();
    
    // Thread-safe set of amounts that are currently unique
    private final AtomicReference<Set<Integer>> uniqueAmounts = new AtomicReference<>(ConcurrentHashMap.newKeySet());
    
    /**
     * Adds a new bid to the system and updates the lowest unique bid
     * @param userId The ID of the user placing the bid
     * @param amount The bid amount
     * @return The current lowest unique bid, or -1 if none exists
     */
    public synchronized int addBid(String userId, int amount) {
        // Add the bid to our tracking
        Set<String> users = amountToUsers.computeIfAbsent(amount, k -> new CopyOnWriteArraySet<>());
        boolean isNewBid = users.add(userId);
        
        // Update unique amounts
        if (isNewBid) {
            // If this is the first user for this amount, add to unique amounts
            if (users.size() == 1) {
                uniqueAmounts.get().add(amount);
            } 
            // If this makes it non-unique, remove from unique amounts
            else if (users.size() == 2) {
                uniqueAmounts.get().remove(amount);
            }
        }
        
        return getLowestUniqueBid();
    }
    
    /**
     * Gets the current lowest unique bid amount
     * @return The lowest unique bid amount, or -1 if none exists
     */
    public int getLowestUniqueBid() {
        return uniqueAmounts.get().stream()
                .min(Integer::compareTo)
                .orElse(-1);
    }
    
    /**
     * Gets all bids for monitoring/debugging purposes
     * @return A map of amounts to the set of users who bid that amount
     */
    public ConcurrentMap<Integer, Set<String>> getAllBids() {
        return new ConcurrentHashMap<>(amountToUsers);
    }
}
