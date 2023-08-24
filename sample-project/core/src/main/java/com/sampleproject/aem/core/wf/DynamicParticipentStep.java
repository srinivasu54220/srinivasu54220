package com.sampleproject.aem.core.wf;

import com.adobe.granite.workflow.exec.HistoryItem;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component(service = ParticipantStepChooser.class,
        property = { "chooser.label=" + "Dynamic Participant Workflow"
        })
public class DynamicParticipentStep implements ParticipantStepChooser {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession,
                                 MetaDataMap metaDataMap) throws WorkflowException {

        List<HistoryItem> history = workflowSession.getHistory(workItem.getWorkflow());
        if(!history.isEmpty()){
            HistoryItem historyItem = history.get(history.size() - 1);
            log.info("Dynamic Participant Workflow history" + historyItem);
        }



        String language = workItem.getWorkflow().getMetaDataMap().get("language", String.class);
        log.info("language" + language);
        String group;

        if (language.equals("en_IN")){
            group="en-fr";

        }
        else {
            group="en-us";

        }
        log.info("group" +group);
        return group;
    }
}
