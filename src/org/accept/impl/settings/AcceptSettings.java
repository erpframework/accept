package org.accept.impl.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 23, 2010
 * Time: 3:01:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcceptSettings {
    
    private JSONObject settings;

    public AcceptSettings(String settings) {
        StringTokenizer t = new StringTokenizer(settings, "\n");
        this.settings = new JSONObject();
        while(t.hasMoreTokens()) {
            String token = t.nextToken().trim();
            if (token.startsWith("#")) {
                continue;
            }
            String[] split = token.split("=");
            try {
                this.settings.accumulate(split[0], split[1]);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String buildCommand() {
        return getJavaCommand() + " -cp " + getJavaClassPath();
    }

    public String getJavaClassPath() {
        StringBuilder cp = new StringBuilder();
        JSONArray arr = settings.getJSONArraySmartly("path");
        for(int i = 0 ; i < arr.length() ; i++) {
            String value = arr.get(i).toString();
            cp.append(value).append(";");
        }
        String cps = cp.toString();
        return cps;
    }

    public String getJavaCommand() {
        return settings.getString("java");
    }
}
