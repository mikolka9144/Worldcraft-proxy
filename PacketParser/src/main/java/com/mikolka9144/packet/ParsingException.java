package com.mikolka9144.packet;

import java.io.IOException;

public class ParsingException extends RuntimeException{
    public ParsingException(String s) {
        super(s);
    }

    public ParsingException(String s, IOException e) {
        super(s,e);
    }
}
