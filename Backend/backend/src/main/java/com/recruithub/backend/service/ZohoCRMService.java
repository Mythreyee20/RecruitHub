package com.recruithub.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ZohoCRMService {

    @Value("${zoho.crm.client-id}")
    private String clientId;

    @Value("${zoho.crm.client-secret}")
    private String clientSecret;

    @Value("${zoho.crm.refresh-token}")
    private String refreshToken;

    @Value("${zoho.crm.api-domain}")
    private String apiDomain;


    // Generate fresh Zoho Access Token
    public String getAccessToken() {

        RestClient restClient = RestClient.create();

        String url = "https://accounts.zoho.com/oauth/v2/token"
                + "?refresh_token=" + refreshToken
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&grant_type=refresh_token";

        String response = restClient.post()
                .uri(url)
                .retrieve()
                .body(String.class);

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(response);

            return json.get("access_token").asText();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate Zoho access token",
                    e
            );
        }
    }


    // Create Lead in Zoho CRM
    public String createLead(
            String name,
            String email,
            String company,
            String role,
            String skills,
            int matchPercentage) {

        String accessToken = getAccessToken();

        RestClient restClient = RestClient.create();

        String body = """
                {
                    "data": [
                        {
                            "Last_Name": "%s",
                            "Email": "%s",
                            "Company": "%s",
                            "Job_Role": "%s"
                        }
                    ]
                }
                """.formatted(
                        name,
                        email,
                        company,
                        role
                );

        return restClient.post()
                .uri(apiDomain + "/crm/v8/Leads")
                .header(
                        "Authorization",
                        "Zoho-oauthtoken " + accessToken
                )
                .header(
                        "Content-Type",
                        "application/json"
                )
                .body(body)
                .retrieve()
                .body(String.class);
    }
}