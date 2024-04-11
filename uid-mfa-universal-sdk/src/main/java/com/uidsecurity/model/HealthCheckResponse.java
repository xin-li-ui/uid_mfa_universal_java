package com.uidsecurity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HealthCheckResponse {

    /**
     * OK,FAIL
     */
    private String stat;

    /**
     * An API error code which corresponds to our existing API error documentation
     */
    private String code;

    /**
     * The time at which the health check occurred
     */
    private String timestamp;

    /**
     * The failure error message
     */
    private String message;

    /**
     * Additional information about the error
     */
    private String messageDetail;

    public Boolean wasSuccess() {
        return "OK".equalsIgnoreCase(stat);
    }
}
