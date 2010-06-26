package org.accept.impl.gwz;

import java.io.File;

import org.accept.domain.ValidationResult;

public class GivWenZenMain {
	
	public static void main(String[] args) throws Throwable {
		String contentFile = args[0];
		String outputFile = args[1];
		
		String content = new org.accept.util.files.FileIO().read(new File(contentFile));
		
		GWZRunner runner = new GWZRunner();
		ValidationResult result = runner.run(content, new File(contentFile));
		
		new org.accept.util.files.FileIO().write(new File(outputFile), result.toJSON());
		if (result.getException() != null) {
			throw result.getException();
		}
	}
}