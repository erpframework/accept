package org.accept.util.classpath;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 27, 2010
 * Time: 11:03:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassNameBuilder {
    public String build(String root, String pkg, File file) {
        String subPath = file.getParent().substring(root.length());
        return (pkg + subPath).replaceAll("[/\\\\]", ".") + "." + file.getName().replaceFirst("\\.class$", "");
    }
}
