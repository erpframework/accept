package org.accept.api;

import org.accept.impl.gwz.GWZRunner;
import org.accept.impl.junit.AcceptFrameworkMethod;
import org.accept.impl.junit.JUnitSucks;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by IntelliJ IDEA.
 * User: Szczepan Faber
 * Date: Jun 24, 2010
 * Time: 10:11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AcceptRunner extends BlockJUnit4ClassRunner {

    private String folder;

    public AcceptRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    private void initFolder() {
        List<Annotation> anns = asList(this.getTestClass().getAnnotations());
        folder = null;
        for (Annotation ann : anns) {
            if (ann.annotationType() == RootFolder.class) {
                folder = ((RootFolder) ann).value();
            }
        }
        if (folder == null) {
            throw new RuntimeException("\nInvalid configuration of AcceptRunner.\n" +
                    "Missing @RootFolder annotation\n" +
                    "Example of correct usage:\n" +
                    "  @RunWith(AcceptRunner.class)\n" +
                    "  @RootFolder(\"path_to_folder_with_stories\")\n");
        }
    }

    @Override
	protected java.util.List<FrameworkMethod> computeTestMethods() {
        initFolder();
        JUnitSucks sucker = new JUnitSucks();
        return sucker.createMethods(folder);
	}

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, Object test) {
        Statement s = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
                new GWZRunner().runContent(m.content, m.file);
            }
        };
        return s;
    }

    @Override
    protected String testName(FrameworkMethod method) {
        AcceptFrameworkMethod m = (AcceptFrameworkMethod) method;
        return m.file.getParentFile().getName() + "/" + m.file.getName();
    }
}