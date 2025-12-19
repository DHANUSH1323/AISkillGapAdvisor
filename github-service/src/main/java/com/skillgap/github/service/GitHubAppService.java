package com.skillgap.github.service;

import io.jsonwebtoken.Jwts;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GitHubAppService {

    @Value("${github.app-id:2454583}")
    private String appId;

    @Value("${github.private-key-pem}")
    private String privateKeyPem;

    private final RestTemplate restTemplate;

    public GitHubAppService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateJwt() throws Exception {
        PrivateKey privateKey = getPrivateKeyFromString(privateKeyPem);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .issuedAt(new Date(now - 60000))
                .expiration(new Date(now + 600000))
                .issuer(appId)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    private PrivateKey getPrivateKeyFromString(String key) throws Exception {
        String keyContent = key;
        if (key != null && key.startsWith("file:")) {
            String filePath = key.substring(5);
            try {
                keyContent = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to read private key from file: " + filePath, e);
            }
        }

        try (PEMParser parser = new PEMParser(new StringReader(keyContent))) {
            Object object = parser.readObject();
            if (object instanceof org.bouncycastle.openssl.PEMKeyPair) {
                return new JcaPEMKeyConverter()
                        .getPrivateKey(((org.bouncycastle.openssl.PEMKeyPair) object).getPrivateKeyInfo());
            } else if (object instanceof PrivateKeyInfo) {
                return new JcaPEMKeyConverter().getPrivateKey((PrivateKeyInfo) object);
            }
            throw new IllegalArgumentException(
                    "Invalid key format. Found: " + (object == null ? "null" : object.getClass().getName()));
        }
    }

    public String getInstallationAccessToken(String installationId) throws Exception {
        String jwt = generateJwt();
        String url = "https://api.github.com/app/installations/" + installationId + "/access_tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.set("Accept", "application/vnd.github+json");
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        if (response.getBody() != null && response.getBody().containsKey("token")) {
            return (String) response.getBody().get("token");
        }
        throw new RuntimeException("Failed to get installation token");
    }

    public String getInstallationIdForRepo(String owner, String repo) throws Exception {
        String jwt = generateJwt();
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/installation";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.set("Accept", "application/vnd.github+json");
        try {
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("id")) {
                return String.valueOf(response.getBody().get("id"));
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "App not installed on repo " + owner + "/" + repo + ". API Response: " + e.getMessage());
        }
        throw new RuntimeException("Could not find installation ID");
    }
}
