package fr.eni.javaee.encheres.messages;

import java.util.Properties;

public class PropertiesReader {
    private static Properties properties;
    static {
        try {
            properties = new Properties();
            properties.load(PropertiesReader.class.getResourceAsStream("errors.properties"));
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static String getProperty(int key){
        return properties.getProperty(String.valueOf(key),null);
    }
}
