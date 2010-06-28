package org.accept.impl.gwz;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.accept.domain.ValidationResult;
import org.accept.util.files.FileIO;
import org.accept.json.JSONObject;

public class ProcessRunner {

    private static ConcurrentMap<String, Proc> map = new ConcurrentHashMap();

    static class Proc {
        StringBuffer output;
        Process process;
        boolean killed = false;

        public Proc(StringBuffer output, Process process) {
            this.output = output;
            this.process = process;
        }

        public void destroy() {
            killed = true;
            this.process.destroy();
        }
    }

	public String run(String command, File output, String guid) {
		StringBuffer sb = new StringBuffer();
		try {
			ProcessBuilder pb = new ProcessBuilder(command.split("\\s"))
				.redirectErrorStream(true);

			Process p = pb.start();
            map.put(guid, new Proc(sb, p));
	        
	        BufferedReader stdInput = new BufferedReader(new 
	             InputStreamReader(p.getInputStream()));
	
	        String s;
	        while ((s = stdInput.readLine()) != null) {
	        	sb.append(s + "\n");
	        }

	        String resultJson = new FileIO().read(output);
            if (resultJson.trim().length() != 0) {
                JSONObject json = new JSONObject(resultJson);
                json.put("fullLog", sb.toString());
                return json.toString();
            } else if (map.get(guid).killed) {
                String outputSoFar = map.get(guid).output.toString();
                return new ValidationResult()
                    .setInfo("Killed by user!")
                    .setFullLog(outputSoFar + "\n*********************\n" + "Killed by user!")
                    .setStatus(ValidationResult.Status.not_run).toJSON();
            } else {
                throw new RuntimeException("Process did not start or end gracefully.");
            }
		} catch (Exception e) {
			throw new RuntimeException("Unable to fork java process.\n" +
					"Nested exception is: " + e.getMessage() + "\n" +
                    "\nThe output from process so far was:\n********************\n" + sb.toString() + "\n", e);
		}
	}

    public String getOutput(String guid) {
        return map.get(guid).output.toString();
    }

    public void kill(String guid) {
        map.get(guid).destroy();
    }
}