package org.accept.impl.junit;

import org.accept.impl.gwz.StepsExtractor;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 24, 2010
 * Time: 10:51:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcceptFrameworkMethod extends FrameworkMethod {
    public File file;
    public String content;
    public StepsExtractor.Step step;

    public AcceptFrameworkMethod(File file, String content) {
        super(getMethodNow());
        this.file = file;
        this.content = content;
    }

    public AcceptFrameworkMethod(StepsExtractor.Step step) {
        super(getMethodNow());
        this.step = step;
    }

    public void method() {}

    public static Method getMethodNow() {
        try {
            return AcceptFrameworkMethod.class.getMethod("method", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("This should never happen. I buy you a beer if you ever see this exception.", e);
        }
    }
}