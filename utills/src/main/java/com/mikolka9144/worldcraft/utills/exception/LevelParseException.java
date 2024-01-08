package com.mikolka9144.worldcraft.utills.exception;

public class LevelParseException extends RuntimeException{
    public LevelParseException(String msg,Exception cause){
        super(msg,cause);
    }
    public LevelParseException(String msg){
        super(msg);
    }
}
