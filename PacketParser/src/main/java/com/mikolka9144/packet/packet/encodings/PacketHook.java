package com.mikolka9144.packet.packet.encodings;

import com.mikolka9144.packet.packet.enums.PacketCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PacketHook{
    PacketCommand value();
}
