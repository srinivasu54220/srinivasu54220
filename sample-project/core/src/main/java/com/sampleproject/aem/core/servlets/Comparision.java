package com.sampleproject.aem.core.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;

import javax.servlet.Servlet;

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

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "/bin/comparision", })
public class Comparision extends SlingSafeMethodsServlet {

    @Reference
    private transient ResourceResolverFactory resolverFactory;
    protected final Logger logger = LoggerFactory.getLogger(Comparision.class);
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
        try {

            destinationPath = servletRequest.getParameter("destinationPath");
            sourcePath = servletRequest.getParameter("sourcePath");

            logger.info("Uncomment the example(s) that you want to run in RunExamples.java file.");
            logger.info("=======================================================================");

            HashMap<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "ABS_Service");
            logger.info("resolver factory:" + resolverFactory);
            resolver = resolverFactory.getServiceResourceResolver(param);

//        SetLicenseFromFile.run();
            getLicense();
            // SetLicenseFromStream.run();
            // SetMeteredLicense.run();

            infoStreamComparision();

            System.out.println("All done.");
        } catch (Exception e) {
            logger.info("exception e:" + e.getMessage());
        }
    }

    protected void infoStreamComparision() {
        try {

            Resource sourceResource = resolver.getResource(sourcePath);
            Asset asset = sourceResource.adaptTo(Asset.class);
            logger.info("source asset:" + asset.getPath());
            Rendition sourceOriginal = asset.getOriginal();
            logger.info("rendtion:" + sourceOriginal.getName());

            Resource destinationResource = resolver.getResource(destinationPath);
            asset = destinationResource.adaptTo(Asset.class);
            logger.info("destination asset:" + asset.getPath());
            Rendition destinationOriginal = asset.getOriginal();
            logger.info("rendtion:" + destinationOriginal.getName());

            String outputFileName = Utils.getOutputDirectoryPath("result-%s.docx", "CompareDocumentsFromStream");

            try (InputStream sourceStream = sourceOriginal.adaptTo(InputStream.class);
                 InputStream destinationStream = destinationOriginal.adaptTo(InputStream.class);
                 OutputStream resultStream = new FileOutputStream(outputFileName);
                 Comparer comparer = new Comparer(sourceStream)) {
                comparer.add(destinationStream);
                final Path resultPath = comparer.compare(resultStream);
            }
            logger.info("\nDocuments compared successfully.\nCheck output in " + Utils.OUTPUT_PATH + ".");
        } catch (Exception e) {
            logger.info("exception:" + e.getMessage());
        }
    }

    protected void getLicense() {
        if (Utils.LICENSE_URL != null) {
            try {
                URL website = new URL(Utils.LICENSE_URL);
                License license = new License();
                try (final InputStream inputStream = website.openStream()) {
                    license.setLicense(inputStream);
                }
                logger.info("\nLicense set without errors.");
            } catch (Exception e) {
                logger.info("Can't load remote license from '" + Utils.LICENSE_URL + "'");
                e.printStackTrace();
            }
        } else {
            System.out.println("\nWe do not ship any license with this example. "
                    + "\nEnvironment variable GROUPDOCS_LIC_PATH is null. "
                    + "\nVisit the GroupDocs site to obtain either a temporary or permanent license. "
                    + "\nLearn more about licensing at https://purchase.groupdocs.com/faqs/licensing. "
                    + "\nLear how to request temporary license at https://purchase.groupdocs.com/temporary-license.");
        }
    }
}