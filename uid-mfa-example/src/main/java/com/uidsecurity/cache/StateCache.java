package com.uidsecurity.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.uidsecurity.model.StateInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StateCache {

    private final Cache<String, StateInfo> stateCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

    public void saveState(StateInfo stateInfo) {
        stateCache.put(stateInfo.getStateId(), stateInfo);
    }

    public StateInfo getState(String state) {
        return stateCache.getIfPresent(state);
    }

    public void removeState(String state) {
        stateCache.invalidate(state);
    }
}
