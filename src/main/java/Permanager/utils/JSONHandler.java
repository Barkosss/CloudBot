package Permanager.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONHandler {
    LoggerHandler logger = new LoggerHandler();

    public Object read(String pathJSON, String keys) {
        try {
            Object object = new JSONParser().parse(new FileReader(String.format("./src/main/resources/%s", pathJSON)));
            JSONObject jsonObject = (JSONObject) object;
            for (Object key : keys.split("\\.")) {
                try {
                    jsonObject = (JSONObject) jsonObject.get(key);
                } catch (Exception err) {
                    return jsonObject.get(key);
                }
            }
            return jsonObject;
        } catch (IOException | ParseException err) {
            logger.error(String.format("JSONHandler (read): %s", err));
            return null;
        }
    }

    public boolean check(String pathJSON, String keys) {
        try {
            Object object = new JSONParser().parse(new FileReader(String.format("./src/main/resources/%s", pathJSON)));
            JSONObject jsonObject = (JSONObject) object;
            for (Object key : keys.split("\\.")) {
                try {
                    jsonObject = (JSONObject) jsonObject.get(key);
                } catch (Exception err) {
                    return jsonObject != null;
                }
            }
            return jsonObject != null;
        } catch (Exception err) {
            return false;
        }
    }
}
