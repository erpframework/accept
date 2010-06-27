package org.accept.util.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 23, 2010
 * Time: 7:52:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class StackTracePrinter {
    //TODO: is it needed?
    public String print(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
