package com.utils;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
//import java.io.File;
//import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testData.LoginData;

public class JsonUtils {

    public static List<LoginData> getLoginData(String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                new File(path),
                new TypeReference<List<LoginData>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("JSON Read Failed: " + e.getMessage());
        }
    }
}