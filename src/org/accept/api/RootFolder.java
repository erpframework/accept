package org.accept.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 24, 2010
 * Time: 10:42:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RootFolder {
    public String value();
}
