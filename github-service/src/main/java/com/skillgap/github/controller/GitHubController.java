package com.skillgap.github.controller;

import com.skillgap.github.service.CloneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GitHubController {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    // We'll use a hardcoded state for simplicity in this demo, but it should be
    // random
    private static final String STATE = "random_state_string";

    private final CloneService cloneService;
    private final RestTemplate restTemplate;

    public GitHubController(CloneService cloneService) {
        this.cloneService = cloneService;
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/connect")
    public RedirectView connect() {
        String authorizationUri = "https://github.com/login/oauth/authorize" +
                "?client_id=" + clientId +
                "&scope=repo" +
                "&state=" + STATE;
        return new RedirectView(authorizationUri);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        if (!STATE.equals(state)) {
            return "Invalid state parameter";
        }

        String tokenUrl = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("code", code);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            String accessToken = (String) response.getBody().get("access_token");

            // For demonstration, we'll clone a specific repo or just return the token
            // In a real app, you'd probably store this token or ask the user which repo to
            // clone

            return "Access Token received: " + accessToken + ". <br/> Ready to clone repositories.";
        }

        return "Failed to retrieve access token";
    }

    @GetMapping("/clone")
    public String cloneRepo(@RequestParam("token") String token, @RequestParam("repo") String repoUrl) {
        // Example usage:
        // /clone?token=gho_...&repo=https://github.com/ashraf8ila/some-repo.git
        cloneService.cloneRepository(token, repoUrl, "cloned_repos/" + repoUrl.substring(repoUrl.lastIndexOf("/") + 1));
        return "Cloning initiated...";
    }
}
