package com.machinecoding.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BeverageNotPreparedException extends Exception {

    public BeverageNotPreparedException() {
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

}
