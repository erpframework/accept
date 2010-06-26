package org.accept.util.commandline;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 26, 2010
 * Time: 8:42:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineConfig {

    Properties props = new Properties();

    //very simple & very naive no error handling whatsover :)
    public CommandLineConfig(String ... appArgs) {
        for (int i = 0; i < appArgs.length; i++) {
            String arg = appArgs[i];
            if (arg.startsWith("-")) {
                props.put(arg.substring(1, arg.length()), appArgs[i+1]);

            }
        }
    }

    /**
     * Returns port or defaultPort in case user not provided port in args
     * @param defaultPort
     * @return
     */
    public int getPort(int defaultPort) {
        return Integer.parseInt(props.getProperty("port", "" + defaultPort));
    }
}
