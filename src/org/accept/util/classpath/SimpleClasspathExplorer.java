package org.accept.util.classpath;

import org.accept.util.files.FolderActions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static org.accept.util.files.FolderActions.*;

/**
 * Very simple classpath explorer. Limitations:
 *  - does not find classes inside jars (but this should be ok, for most cases :)
 *  - does not find inner classes even if they are public static (this should be fine)
 */
public class SimpleClasspathExplorer {

    public void findClasses(final String pkg, final ClassHandler handler) {
        final String root = findRoot(pkg);

        FolderActions.eachFile(root, ".*\\.class").act(new FileHandler() {
            public void handle(File file) {
                String clsName = new ClassNameBuilder().build(root, pkg, file);
                Class cls = loadClass(clsName);
                if (cls != null) {
                    handler.handle(cls);
                }
            }
        });
    }

    private String findRoot(String pkg) {
        Enumeration<URL> res;
        try {
            res = this.getClass().getClassLoader().getResources(pkg);
        } catch (IOException e) {
            throw new UnableToFindPackageException("Due to IO exception, I cannot find this package: " + pkg);
        }
        while (res.hasMoreElements()) {
            URL url =  res.nextElement();
            File candidate = new File(url.getPath());
            if (candidate.exists() && candidate.isDirectory()) {
                return candidate.getPath();
            }
        }
        throw new UnableToFindPackageException("Unable to find this package: " + pkg);
    }

    private Class loadClass(String cls) {
        try {
            return this.getClass().getClassLoader().loadClass(cls);
        } catch (ClassNotFoundException e) {
            //ignore it...
            return null;
        }
    }

    public static class UnableToFindPackageException extends RuntimeException {
        public UnableToFindPackageException(String s) {
            super(s);
        }
    }

    public static interface ClassHandler {
        public void handle(Class cls);
    }
}
