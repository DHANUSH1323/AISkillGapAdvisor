package com.skillgap.github.controller;

import com.skillgap.github.service.CloneService;
import com.skillgap.github.service.GitHubAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppTestController {

    private final GitHubAppService appService;
    private final CloneService cloneService;

    public AppTestController(GitHubAppService appService, CloneService cloneService) {
        this.appService = appService;
        this.cloneService = cloneService;
    }

    @GetMapping("/test-clone-private")
    public String clonePrivateRepo(@RequestParam("owner") String owner, @RequestParam("repo") String repo) {
        try {
            String installationId = appService.getInstallationIdForRepo(owner, repo);
            String token = appService.getInstallationAccessToken(installationId);
            String repoUrl = "https://github.com/" + owner + "/" + repo + ".git";
            String targetDir = "cloned_repos/" + repo;
            cloneService.cloneRepository(token, repoUrl, targetDir);
            return "Successfully started cloning " + owner + "/" + repo + " into " + targetDir;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
