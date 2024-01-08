package com.mikolka9144.worldcraft.utills.exception;

public class CompressionException extends RuntimeException{
    public CompressionException(String s, Exception x) {
        super(s,x);
    }
}
