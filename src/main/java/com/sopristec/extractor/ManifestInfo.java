package com.sopristec.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ManifestInfo {

    public static void PrintInfo(){
        InputStream is = Application.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");

        Properties prop = new Properties();
        try {
            prop.load( is );
            SopristecLogManager.logger.info("PACKAGE INFO\n-------------------------------------------\nJava Extensions Extractor VERSION " + prop.getProperty("Manifest-Version")+"\n-------------------------------------------");
        } catch (
                IOException ex) {
            SopristecLogManager.logger.error(Application.class.getName() + " " + ex);
        }
    }
}
