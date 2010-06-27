package org.accept.domain;

import org.accept.json.JSONObject;

public class ValidationResult {

    public enum Status { red, green, not_run };
	
	private String output;
	private Enum<Status> status;
	private int stepIndex;
	private String step;
	private String message;
	private Throwable exception;

	public ValidationResult() {
		status = Status.green;
	}

	public ValidationResult(int stepIndex, String step, String message, Throwable e) {
		this.stepIndex = stepIndex;
		this.step = step;
		this.message = message;
		this.exception = e;
		status = Status.red;
	}

    public void appendOutput(String text) {
        if (output == null) {
            output = text;
        }
        output += "\n" + text;
    }

	public String toJSON() {
		return new JSONObject(this).toString();
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getStatus() {
		return status.name();
	}

	public void setStatus(Enum<Status> status) {
		this.status = status;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getMessage() {
		if (message == null) {
			return "Validation status: " + getStatus();
		}
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}