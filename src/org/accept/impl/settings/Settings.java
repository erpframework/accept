package org.accept.impl.settings;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 24, 2010
 * Time: 12:19:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    File storage = new File("accept.properties");

    public Settings() {
        makeSureStorageExists();
    }

    private void makeSureStorageExists() {
        if (!storage.exists()) {
            save("java=java -Xmx512m\n" +
                    "path=./Accept/bin\n" +
                    "path=./Accept/lib/compile/givwenzen-1.0.1-SNAPSHOT.jar\n" +
                    "path=./Accept/lib/test/junit-4.8.2.jar");
        }
    }

    public void save(String settings) {
        synchronized (Settings.class) {
            new org.accept.util.files.FileIO().write(storage, settings);
        }
    }

    public String getContent() {
        synchronized (Settings.class) {
            makeSureStorageExists();
            return new org.accept.util.files.FileIO().read(storage);
        }
    }
}
