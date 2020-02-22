package com.api.test;

import java.io.IOException;
import java.util.Properties;

public class TestBase{


    Properties props = new Properties();

    public TestBase() {
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

  }
