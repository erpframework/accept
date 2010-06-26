package org.accept.impl.gwz;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.accept.util.files.FileIO;
import org.json.JSONObject;

public class ProcessRunner {

    private static ConcurrentMap map = new ConcurrentHashMap();

	public String run(String command, File output, String guid) {
		StringBuffer sb = new StringBuffer();
        map.put(guid, sb);
		try {
			ProcessBuilder pb = new ProcessBuilder(command.split("\\s"))
				.redirectErrorStream(true);

			Process p = pb.start();
	        
	        BufferedReader stdInput = new BufferedReader(new 
	             InputStreamReader(p.getInputStream()));
	
	        String s = null;
	        while ((s = stdInput.readLine()) != null) {
	        	sb.append(s + "\n");
	        }
	        
	        String resultJson = new FileIO().read(output);
            if (resultJson.trim().length() == 0) {
                throw new RuntimeException("Process did not end gracefully");
            }
	        JSONObject json = new JSONObject(resultJson);
	        json.put("output", sb.toString());
	        
	        return json.toString();
		} catch (Exception e) {
			throw new RuntimeException("Unable to run this command:\n" + command +
					"\nNested exception is: " + e.getMessage() + "\n" +
                    "\nThe output from process so far was:\n" + sb.toString() + "\n", e);
		}		
	}

    public String getOutput(String guid) {
        return map.get(guid).toString();
    }
}