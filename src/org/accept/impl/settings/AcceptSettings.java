package org.accept.impl.settings;

import org.accept.json.JSONArray;
import org.accept.json.JSONException;
import org.accept.json.JSONObject;

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
    private String separator = System.getProperty("path.separator");

    public AcceptSettings(String content) {
        init(content);
    }

    AcceptSettings(String content, String separator) {
        this.separator = separator; 
        init(content);
    }

    private void init(String content) {
        StringTokenizer t = new StringTokenizer(content + "\n", "\n");
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
        return getJavaCommand() + " " + getJavaClassPath();
    }

    public String getJavaClassPath() {
        StringBuilder cp = new StringBuilder();
        JSONArray arr = settings.getJSONArraySmartly("path");
        for(int i = 0 ; i < arr.length() ; i++) {
            if (cp.length() == 0) {
                cp.append("-cp ");
            } else {
                cp.append(separator);
            }
            String value = arr.get(i).toString();
            cp.append(value);
        }
        return cp.toString();
    }

    public String getJavaCommand() {
        return settings.getString("java");
    }
}
