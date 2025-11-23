package com.ap.controller;

import com.ap.dto.BidRequest;
import com.ap.dto.BidResponse;
import com.ap.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Bid API", description = "APIs for placing and managing bids")
public class BidController {

    private final BidService bidService;
    private final String internalApiKey;

    public BidController(BidService bidService, 
                        @Value("${app.internal-api.key}") String internalApiKey) {
        this.bidService = bidService;
        this.internalApiKey = internalApiKey;
    }

    @PostMapping("/bids")
    @Operation(summary = "Place a new bid", 
               description = "Allows a user to place a new bid with the specified amount")
    public ResponseEntity<BidResponse> placeBid(@RequestBody BidRequest bidRequest) {
        bidService.addBid(bidRequest.getUserId(), bidRequest.getAmount());
        return ResponseEntity.ok(
            new BidResponse("Bid placed successfully")
        );
    }

    @GetMapping("/internal/lowest-unique-bid")
    @Operation(summary = "Get lowest unique bid",
               description = "Internal API to get the current lowest unique bid")
    public ResponseEntity<Integer> getLowestUniqueBid(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String apiKey) {

        if (!internalApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        int lowestBid = bidService.getLowestUniqueBid();
        return ResponseEntity.ok(lowestBid);
    }
}
