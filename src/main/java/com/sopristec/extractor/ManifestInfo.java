package com.sopristec.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ManifestInfo {

    // Updates version from this hard-code since modifying META-INF/MANIFEST.MF at resources folder
    // does not seem to reflect any change to Manifest version.
    private static String MANIFEST_VERSION = "1.0.2";

    public static void PrintInfo(){
        InputStream is = Application.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");

        Properties prop = new Properties();
        try {
            prop.load( is );
            //String manifestVersion = prop.getProperty("Manifest-Version");
            SopristecLogManager.logger.info("PACKAGE INFO" +
                    "\n-------------------------------------------\n" +
                    "Java Extensions Extractor VERSION " + MANIFEST_VERSION +
                    "\n-------------------------------------------");
        } catch (
                IOException ex) {
            SopristecLogManager.logger.error(Application.class.getName() + " " + ex);
        }
    }
}
