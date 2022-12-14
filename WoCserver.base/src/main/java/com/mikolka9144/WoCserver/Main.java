package com.mikolka9144.WoCserver;

import com.mikolka9144.WoCserver.config.CmdExt;
import com.mikolka9144.WoCserver.config.GigachadWoC;
import com.mikolka9144.WoCserver.config.DefaultWoC;
import com.mikolka9144.WoCserver.model.ServerConfig;
import lombok.val;
import lombok.var;

public class Main {
    public static void main(String[] args){
        ServerConfig config;
        try {

             //

            }
        }
        catch (Exception x){

            System.out.println("You need to specify configuration for a server");
            System.out.println("Avaliable configurations: default legacy cmd");
            System.out.println("Avaliable presets are: cmd-proxy official legacial");
            System.out.println("Argument layout ./server <config/preset> <target-hostname> <target-port> <host-port>");
        }


    }
}
