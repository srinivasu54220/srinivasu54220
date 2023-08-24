package com.sampleproject.aem.core.servlets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClientExample {
    public static void main(String[] args) {
        String clientId = "ot2ld1sp6of7ce7mf2t6nths3t-z3jbt6yb";
        String clientSecret = "rtlkbhd93jaoc3p3afc5hd6fg";
        String apiUrl = "http://localhost:4504/graphql/execute.json/Headless/test";

        // Encode client ID and client secret in Base64
        String encodedCredentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);


            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            HttpResponse response = httpClient.execute(httpPost);

            // Process the response
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}