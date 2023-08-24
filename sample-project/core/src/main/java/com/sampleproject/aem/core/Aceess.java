package com.sampleproject.aem.core;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Aceess {
    public static void main(String[] args) {
        // Replace with your AEM server's token endpoint URL
        String tokenEndpointUrl = "http://localhost:4502";

        // Replace with your client ID and client secret
        String clientId = "ps032f9rp70s2f08qbcsfbg987-niy3w707";
        String clientSecret = "8jqtfbko2hkljbimaplaoeofv3";

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder(tokenEndpointUrl);

            // Set the necessary parameters for token request
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
            parameters.add(new BasicNameValuePair("client_id", clientId));
            parameters.add(new BasicNameValuePair("client_secret", clientSecret));

            uriBuilder.setParameters(parameters);

            URI uri = uriBuilder.build();
            HttpPost httpPost = new HttpPost(uri);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println("Access Token: " + entity);

            if (entity != null) {
                // Parse the response and retrieve the access token
                String responseString = EntityUtils.toString(entity,"UTF-8");
                // Handle the responseString to extract the access token
                // Example: JSONObject jsonObject = new JSONObject(responseString);
                //          String accessToken = jsonObject.getString("access_token");
                System.out.println("Access Token: " + responseString);
            }

            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
