package com.ap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Request object for placing a new bid")
public class BidRequest {
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "Unique identifier of the user placing the bid", example = "john123")
    private String userId;
    
    @Positive(message = "Bid amount must be a positive number")
    @Schema(description = "Bid amount (must be positive)", example = "100")
    private int amount;
}
