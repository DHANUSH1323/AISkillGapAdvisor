package com.skillgap.github.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
public class CloneService {

    private static final Logger log = LoggerFactory.getLogger(CloneService.class);

    public void cloneRepository(String accessToken, String repoUrl, String targetDir) {
        log.info("Cloning repository {} into {}", repoUrl, targetDir);

        // Construct the authenticated URL
        // Format: https://x-access-token:<token>@github.com/user/repo.git
        String authenticatedUrl = repoUrl.replace("https://", "https://x-access-token:" + accessToken + "@");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("git", "clone", authenticatedUrl, targetDir);
        processBuilder.directory(new File(".")); // Or a specific base directory for clones

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Repository cloned successfully.");
            } else {
                log.error("Failed to clone repository. Exit code: {}", exitCode);
                // Read error stream likely
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error cloning repository", e);
            Thread.currentThread().interrupt();
        }
    }
}
