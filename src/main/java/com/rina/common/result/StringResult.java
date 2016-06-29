package com.rina.common.result;

/**
 * Created by rinacao on 6/28/16.
 */
public class StringResult {
    public final static StringResult EMPTY = new StringResult(null);

    public final String value;

    public StringResult(String value) {
        this.value = value;
    }
}
