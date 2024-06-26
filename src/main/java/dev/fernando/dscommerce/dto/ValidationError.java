package dev.fernando.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationError extends CustomError {
    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return Collections.unmodifiableList(errors);
    }
    
    public void addError(String fieldName, String fieldMessage) {
        this.errors.add(new FieldMessage(fieldName, fieldMessage));
    }
}
