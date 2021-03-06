package uk.gov.gds.dm;

import models.Page;
import play.Play;
import play.utils.Properties;

import java.io.InputStream;
import java.io.IOException;

public class Fixtures {

    private static Properties contentProperties;

    private static void loadContentProperties() throws IOException {
        contentProperties = new Properties();
        InputStream inputStream = Play.classloader.getResourceAsStream("content.properties");
        contentProperties.load(inputStream);
    }

    public static Properties getContentProperties() {
        if (null == contentProperties) {
            try {
                Page.initialiseAutoIncrementId();
                loadContentProperties();
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load content: " + ex.getMessage());
            }
        }
        return contentProperties;
    }

    /**
     * Attempt to load error message from content.properties, otherwise fall back
     * to just returning the provided value.
     */
    public static String getErrorMessage(String key, String value) {
        return Fixtures.getContentProperties().get(key + value, value);
    }
}
