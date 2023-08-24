package com.sampleproject.aem.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Articlelist {

    private static final Logger LOG = LoggerFactory.getLogger(Articlelist.class);


    @Inject
    private String path;


    @SlingObject
    ResourceResolver resourceResolver;


    /*@Self
    Resource resource;*/

    List<Articlelist> articlelists;


    public String getPath(){

        return this.path;
    }



    /* @PostConstruct
     public void main(){

        ResourceResolver resourceResolver=resource.getResourceResolver();
        Session session=resourceResolver.adaptTo(Session.class);
        QueryBuilder queryBuilder =resourceResolver.adaptTo(QueryBuilder.class);

        Map<String,String> predicate=new HashMap<>();
        predicate.put("path", path);
        predicate.put("type", "cq:page");

            Query query = null;

         articlelists = new ArrayList<Articlelist>();

         try {
             query =queryBuilder.createQuery(PredicateGroup.create(predicate), session);
         }
         catch (Exception e){
             LOG.error("Error in query");
         }

        SearchResult searchResult =query.getResult();
        for (Hit hit:searchResult.getHits()) {

            Articlelist articlelist= new Articlelist();

            String Articlespath;

            try {
                Articlespath=hit.getPath();
                Resource resourc=resourceResolver.getResource(Articlespath);
                Page page=resourc.adaptTo(Page.class);
               String navigarionTitle=page.getNavigationTitle();
               String description=page.getTitle();

                LOG.debug("title of Articlelist {}",navigarionTitle);
                LOG.debug("path of Articlelist {}", description);

            }
            catch (RepositoryException e){
                throw new RuntimeException(e);
            }


        }

    }*/

     /*public List<Articlelist> getArticlelists(){

        return articlelists;
     }*/



    public ArrayList<Page> getList(){
        PageManager pageManager=resourceResolver.adaptTo(PageManager.class);
        Page pageManagerPage =pageManager.getPage(path);
        Iterator<Page> pageIterator= pageManagerPage.listChildren();

        ArrayList<Page> list=new ArrayList<>();
        if (pageIterator!=null){

            while (pageIterator.hasNext()){

                Page opi=pageIterator.next();
                list.add(opi);
            }
        }


        return list;

    }




}
