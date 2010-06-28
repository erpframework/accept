package org.accept.domain;

import org.accept.json.JSONObject;

public class ValidationResult {

    public enum Status { red, green, not_run };
	
	private String fullLog;
	private Enum<Status> status;
	private int stepIndex;
	private String step;
	private String info;
	private Throwable exception;

	public ValidationResult() {
		status = Status.green;
	}

	public ValidationResult(int stepIndex, String step, String info, Throwable e) {
		this.stepIndex = stepIndex;
		this.step = step;
		this.info = info;
		this.exception = e;
		status = Status.red;
	}

	public String toJSON() {
		return new JSONObject(this).toString();
	}

	public String getFullLog() {
		return fullLog;
	}

	public ValidationResult setFullLog(String fullLog) {
		this.fullLog = fullLog;
        return this;
	}

	public String getStatus() {
		return status.name();
	}

	public ValidationResult setStatus(Enum<Status> status) {
		this.status = status;
        return this;
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

	public String getInfo() {
		if (info == null) {
			return "Validation status: " + getStatus();
		}
		return info;
	}

	public ValidationResult setInfo(String info) {
		this.info = info;
        return this;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}