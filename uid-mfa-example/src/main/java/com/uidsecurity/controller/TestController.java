package com.uidsecurity.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.uidsecurity.UidClient;
import com.uidsecurity.cache.StateCache;
import com.uidsecurity.exception.UidException;
import com.uidsecurity.model.StateInfo;
import com.uidsecurity.model.User;
import com.uidsecurity.utils.JSONUtil;
import com.uidsecurity.utils.RequestUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class TestController {

    private final UidClient uidClient;
    private final StateCache stateCache;

    private List<User> users;

    public TestController(UidClient uidClient, StateCache stateCache) {
        this.uidClient = uidClient;
        this.stateCache = stateCache;
    }

    @PostConstruct
    public void initialize() {
        users = new ArrayList<>();
        users.add(User.builder().id("111").username("xin.li@ui.com").password("123456").build());
        users.add(User.builder().id("222").username("xin.li+23@ui.com").password("123456").build());
    }

    @GetMapping({"/", "/login"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        User user = getUser(username, password);
        if (Objects.isNull(user)) {
            ModelAndView model = new ModelAndView("/index");
            model.addObject("message", "Invalid Credentials");
            return model;
        }
        // health check
        String action = "user.login";
        String ip = RequestUtils.getIpAddress(request);
        String ua = RequestUtils.getUserAgent(request);

        try {
            uidClient.healthCheck(username, action, ip, ua);
        } catch (UidException e) {
            ModelAndView model = new ModelAndView("/index");
            model.addObject("message", e.getMessage());
            return model;
        }


        String state = uidClient.generateState();
        StateInfo stateInfo = StateInfo.builder()
                .stateId(state)
                .userId(user.getId())
                .username(username)
                .action(action)
                .build();
        stateCache.saveState(stateInfo);

        String authUrl = uidClient.createAuthUrl(username, action, state, ip, ua);
        return new ModelAndView("redirect:" + authUrl);
    }

    @GetMapping("/callback")
    public ModelAndView callback(@RequestParam("code") String code, @RequestParam("state") String state) throws ParseException {

        StateInfo stateInfo = stateCache.getState(state);
        if (Objects.isNull(stateInfo)) {
            ModelAndView model = new ModelAndView("/index");
            model.addObject("message", "Session Expired");
            return model;
        }
        User user = getUserById(stateInfo.getUserId());

        stateCache.removeState(state);

        IDTokenClaimsSet idToken = uidClient.getIdToken(code);

        if (Objects.nonNull(idToken) && Objects.nonNull(idToken.getAuthenticationTime())) {
            ModelAndView model = new ModelAndView("/welcome");
            model.addObject("username", user.getUsername());
            model.addObject("token", idToken.toString());
            return model;
        } else {
            ModelAndView model = new ModelAndView("/index");
            model.addObject("message", "MFA Failed");
            return model;
        }
    }

    private User getUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    private User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

}
