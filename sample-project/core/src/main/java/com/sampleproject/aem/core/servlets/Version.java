//package com.sampleproject.aem.core.servlets;
//
//import com.day.cq.wcm.api.Page;
//import com.day.cq.wcm.api.PageManager;
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.*;
//import org.apache.sling.api.resource.LoginException;
//import org.apache.sling.api.servlets.SlingAllMethodsServlet;
//import org.jsoup.Jsoup;
//import org.osgi.service.component.annotations.Component;
//import org.osgi.service.component.annotations.Reference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.jcr.*;
//import javax.jcr.version.VersionHistory;
//import javax.jcr.version.VersionIterator;
//import javax.jcr.version.VersionManager;
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//
//
//@Component(service = Servlet.class,
//        property = {"sling.Servlet.paths=/bin/version",
//                "sling.servlet.methods=GET"
//        })
//public class Version extends SlingAllMethodsServlet {
//
//    private static final long serialVersionUID = 2347766672883L;
//
//    private static final Logger LOGGER= LoggerFactory.getLogger(Version.class);
//
//    @Reference
//    private transient ResourceResolverFactory resolverFactory;
//
//    @Override
//    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//
//        String url = request.getHeader("Referer");
//
//        request.getResource().adaptTo(GraphqlClient.class);
//
//
//        int i = url.indexOf("?");
//        Map<String, String> paramsMap = new HashMap<>();
//        if (i > -1) {
//            String searchURL = url.substring(url.indexOf("?") + 1);
//            String params[] = searchURL.split("&");
//
//            for (String param : params) {
//                String temp[] = param.split("=");
//                paramsMap.put(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
//
//            }
//        }
//        String s = paramsMap.get("src");
//        LOGGER.info(String.valueOf(paramsMap.get("src")));
//
//        try {
//
//            ResourceResolver resolver = request.getResourceResolver();
//            Session session = resolver.adaptTo(Session.class);
//            VersionManager versionManager = session.getWorkspace().getVersionManager();
//
//
//            VersionHistory versionHistory = versionManager.getVersionHistory(s);
//            String[] versionLabels = versionHistory.getVersionLabels();
//            Node node = (Node) versionHistory.getNode("1.1");
//          LOGGER.info("node get by vasu"+String.valueOf(node));
//
//            List<String> versions = new ArrayList<>();
//            for (VersionIterator it = versionHistory.getAllVersions(); it.hasNext(); ) {
//                javax.jcr.version.Version version = it.nextVersion();
//
//                //versionManager.versionableNode
//
//
//                String path = version.getPath()+"/jcr:frozenNode/jcr:content/renditions/original/jcr:content";
//               LOGGER.info("path of version"+ path);
//               if (!path.contains("jcr:rootVersion")) {
//                   Resource resource = resolver.getResource(path);
//                   Node node1 = resource.adaptTo(Node.class);
//                   String property = node1.getProperty("jcr:data").getValue().toString();
//                   String plainText= Jsoup.parse(property).text();
//                   LOGGER.info("versions data "+plainText);
//                    }
//
//            }
//
//
//
//
//
//
//
//        } catch (UnsupportedRepositoryOperationException e) {
//            throw new RuntimeException(e);
//        } catch (RepositoryException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//
//
//
//
//}
