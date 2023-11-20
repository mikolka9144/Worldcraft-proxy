package com.mikolka9144.worldcraft.programs.simba;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class MonikaCommandReader {
    private final Iterator<String> input;

    public MonikaCommandReader(Iterator<String> input){

        this.input = input;
    }
    public boolean hasNext(){
        return input.hasNext();
    }
    public String readNext() throws NoSuchElementException {
        return input.next();
    }
    public int readNextInt() throws NoSuchElementException {
        return Integer.parseInt(input.next());
    }
    public List<String> readNextCodeBlock() throws IllegalStateException {
        if (!input.next().equals("[")) throw new IllegalStateException("Input is not a code block!");
        List<String> code = new ArrayList<>();
        while (true){
            String seg = readNext();
            if (seg.equals("]")) break;
            code.add(seg);
        }
        return code;
    }
}
