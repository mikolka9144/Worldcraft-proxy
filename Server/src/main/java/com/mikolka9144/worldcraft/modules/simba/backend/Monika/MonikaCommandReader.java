package com.mikolka9144.worldcraft.modules.simba.backend.Monika;

import java.util.*;
import java.util.stream.Stream;

public class MonikaCommandReader {
    private final Iterator<String> input;

    public MonikaCommandReader(Iterator<String> input){

        this.input = input;
    }

    public MonikaCommandReader(String input) {
        this(splitInputCommands(input).iterator());
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
    public static List<String> splitInputCommands(String input){
        return Arrays.stream(input.split(" "))
                .flatMap(s -> s.startsWith("[")? Stream.of("[",s.substring(1)) : Stream.of(s))
                .flatMap(s -> s.endsWith("]")? Stream.of(s.substring(0,s.length()-1),"]") : Stream.of(s))
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
