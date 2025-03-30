package br.com.homebudget.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private String code;
    private Object errors;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public ErrorResponse(int status, String message, String code, Object errors) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", errors=" + errors +
                '}';
    }
}
