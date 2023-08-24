package com.sampleproject.aem.core.wf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.jcr.Session;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;

@Component(service = WorkflowProcess.class, property = { "process.label=Publish to FT" })
public class OutputPresets implements WorkflowProcess {



	protected final Logger logger = LoggerFactory.getLogger(OutputPresets.class);
	@Reference
	private transient ResourceResolverFactory resolverFactory;



	public void execute(WorkItem workItem, WorkflowSession wfSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		logger.info("execute method started ");
		Object workflowParticipentDialog = workItem.getWorkflow().getMetaDataMap().get("workflowParticipentDialog");
		workItem.getWorkflow().getMetaDataMap().get("workflowParticipentDialog", String.class);
		logger.info("approval status" + workflowParticipentDialog);
		workItem.getWorkflow().getMetaDataMap();

		HttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			logger.info("inside try:" + resolverFactory);
			WorkflowData workflowData = workItem.getWorkflowData();
			Session session = wfSession.adaptTo(Session.class);
			logger.info("session generated");
			String path = workflowData.getPayload().toString();
			MetaDataMap metaData = workItem.getWorkflowData().getMetaDataMap();
			String generatedPath = metaData.get("generatedPath").toString();
			logger.info("generated path:" + generatedPath);
			ResourceResolver resolver = null;
			HashMap<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "ABS_Service");
			logger.info("resolver factory:" + resolverFactory);
			resolver = resolverFactory.getServiceResourceResolver(param);
			logger.info("resolver:" + resolver);

			org.apache.sling.api.resource.Resource resource = resolver.getResource(generatedPath);

			Asset asset = resource.adaptTo(Asset.class);
			logger.info("asset:" + asset.getPath());
			Rendition original = asset.getOriginal();
			logger.info("rendtion:" + original.getName());
			InputStream content = original.adaptTo(InputStream.class);
			logger.info("content:" + content);

			logger.info("Fluid topics");
			logger.info(path);
			String apiUrl = "https://my.fluidtopics.net/gspann/api/admin/khub/sources/dita/upload?publisher=Manikanta%20Ravi%20Kumar";

			String filePath = generatedPath;

			String bearerToken = "2XKIyspoYxbiWT1kp4bSpUZ1Zzv3LhYU";

			httpClient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(apiUrl);

			httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);

			//File file = new File(filePath);
			//logger.info("file exists:" + file.exists());

			HttpEntity multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addBinaryBody("file", content, ContentType.DEFAULT_BINARY, "06-06-2023.zip").build();

			logger.info("done with http entity multipart");
			httpPost.setEntity(multipartEntity);

			response = httpClient.execute(httpPost);
		} catch (IOException | org.apache.sling.api.resource.LoginException e) {
			logger.info("Fluid topics exception {}", e.getMessage());
			throw new RuntimeException(e);
		}

		HttpEntity responseEntity = response.getEntity();
		String responseBody = null;
		try {
			responseBody = EntityUtils.toString(responseEntity);
		} catch (IOException e) {
			logger.info("Fluid tresponse body{}", e.getMessage());

			throw new RuntimeException(e);
		}

		logger.info("Response status code: " + response.getStatusLine().getStatusCode());
		logger.info("Response body: " + responseBody);

		try {
			httpClient.close();
		} catch (Exception e) {
			logger.info("error in io:" + e.getMessage());
			throw new RuntimeException(e);
		}

	}
}