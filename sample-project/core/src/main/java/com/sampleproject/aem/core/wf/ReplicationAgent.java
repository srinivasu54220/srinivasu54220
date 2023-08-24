package com.sampleproject.aem.core.wf;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Session;
import java.util.Locale;
import java.util.Map;


@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label" + " = Payload checker for replication agent"
        }
)
public class ReplicationAgent implements WorkflowProcess {

    
    private static final Logger log = LoggerFactory.getLogger(ReplicationAgent.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {


            String country;
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
                Session session = workflowSession.adaptTo(Session.class);
                String path = workflowData.getPayload().toString();
            log.info("Payload checker for replication agent" + path);

            ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);

            Resource resource = resolver.getResource(path);
            if (resource != null) {
                Page page = resource.adaptTo(Page.class);
                if (page != null) {
                    String language = page.getLanguage().toString();
                    workItem.getWorkflow().getMetaDataMap().put("language", language);
                    log.info("language" +language);


                }
            }


        }catch (Exception e){
             log.error("log printed Payload checker for replication agent country");
        }
    }
}
