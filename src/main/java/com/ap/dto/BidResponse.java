package com.ap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object for bid operations")
public class BidResponse {
    
    @Schema(description = "Status message", example = "Bid placed successfully")
    private String message;
    
    public BidResponse(String message) {
        this.message = message;
    }
}
