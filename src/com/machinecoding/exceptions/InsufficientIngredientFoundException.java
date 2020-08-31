package com.machinecoding.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class InsufficientIngredientFoundException extends BeverageNotPreparedException {

    private final String code;
    private final String errorMessage;

    public InsufficientIngredientFoundException(final String code, final String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
