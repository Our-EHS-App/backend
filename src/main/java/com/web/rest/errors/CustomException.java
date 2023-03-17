package com.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class CustomException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String titleAr;

    private final String errorKey;

    public CustomException(String defaultMessage, String titleAr, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, titleAr, errorKey);
    }

    public CustomException(URI type, String defaultMessage, String titleAr, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(titleAr, errorKey));
        this.titleAr = titleAr;
        this.errorKey = errorKey;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        return parameters;
    }
}
