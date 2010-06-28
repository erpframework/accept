package org.accept.domain;

import org.accept.json.JSONObject;

public class ValidationResult {

    public enum Status { red, green, not_run }

	private String fullLog;

    private Enum<Status> status;
    private int stepIndex;
    private String step;
    private String info;
    private Throwable exception;
    private String story;
    private String storyName;

	public ValidationResult() {
		status = Status.green;
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

	public ValidationResult setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
        return this;
	}

	public String getStep() {
		return step;
	}

	public ValidationResult setStep(String step) {
		this.step = step;
        return this;
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

	public ValidationResult setException(Throwable exception) {
		this.exception = exception;
        return this;
	}

    public String getStory() {
        return story;
    }

    public ValidationResult setStory(String storyContent) {
        this.story = storyContent;
        return this;
    }

    public String getStoryName() {
        return storyName;
    }

    public ValidationResult setStoryName(String storyName) {
        this.storyName = storyName;
        return this;
    }
}