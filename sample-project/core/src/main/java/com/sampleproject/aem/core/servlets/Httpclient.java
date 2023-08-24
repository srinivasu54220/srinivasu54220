package com.sampleproject.aem.core.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;

import javax.servlet.Servlet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.sampleproject.aem.core.servlets.Utils;
import com.groupdocs.comparison.Comparer;
import com.groupdocs.comparison.license.License;

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.resourceTypes=" + "/bin/httpclient", })
public class Httpclient extends SlingSafeMethodsServlet {

    @Reference
    private transient ResourceResolverFactory resolverFactory;
    protected final Logger logger = LoggerFactory.getLogger(Httpclient.class);
    private ResourceResolver resolver = null;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The main method.
     */

    private String sourcePath = "";

    private String destinationPath = "";

    protected void doGet(SlingHttpServletRequest servletRequest, SlingHttpServletResponse servletResponse)
            throws IOException {

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

            servletResponse.getWriter().write(responseBody+"entity "+entity);

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}