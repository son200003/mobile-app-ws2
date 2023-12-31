package com.example.mobileappws.io.repositories;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    private Environment env;

    public String getTokenSecret(){
        return env.getProperty("tokenSecret");
    }
}
