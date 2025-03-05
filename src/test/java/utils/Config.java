package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Config {
    private static Config instance;
    private ConfigData configData;

    private Config() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File configFile = new File("src/test/resources/config.json");

            if (configFile.exists()) {
                configData = objectMapper.readValue(configFile, ConfigData.class);
            } else {
                // Load from environment variables if config.json is missing
                configData = new ConfigData();
                configData.credentials = new ConfigData.Credentials();

                configData.credentials.admin = new ConfigData.Credentials.User();
                configData.credentials.admin.username = System.getenv("ADMIN_USERNAME");
                configData.credentials.admin.password = System.getenv("ADMIN_PASSWORD");

                configData.credentials.user = new ConfigData.Credentials.User();
                configData.credentials.user.username = System.getenv("USER_USERNAME");
                configData.credentials.user.password = System.getenv("USER_PASSWORD");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + e.getMessage());
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public static class ConfigData {
        public Credentials credentials;

        public static class Credentials {
            public User admin;
            public User user;

            public static class User {
                public String username;
                public String password;
            }
        }
    }
}
