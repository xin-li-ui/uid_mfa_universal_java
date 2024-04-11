package com.uidsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateInfo implements Serializable {

    private String stateId;

    private String userId;

    private String username;

    private String action;

    private StateStatus status;

    public boolean isSucceed() {
        return StateStatus.SUCCESS.equals(status);
    }

}
