package com.mikolka9144.WoCserver;

import com.mikolka9144.WoCserver.config.CmdExt;
import com.mikolka9144.WoCserver.config.GigachadWoC;
import com.mikolka9144.WoCserver.config.DefaultWoC;

public class Main {
    public static void main(String[] args){
        try {

            String hostname = args[1]; //64.237.54.60
            String config = args[0];
            int port = Integer.parseInt(args[2]); //443
            int hostingPort = Integer.parseInt(args[3]); //443
            switch (config){
                case "default":
                    DefaultWoC.main(hostname,port);
                    break;
                case "legacy":
                    GigachadWoC.main(hostname,port);
                    break;
                case "cmd":
                    CmdExt.main(hostname,port);
                    break;
                default:
                    System.exit(-1);
             }
        }
        catch (Exception x){

            System.out.println("You need to specify configuration for a server");
            System.out.println("Avaliable configurations: default legacy cmd");
            System.out.println("Argument layout ./server <config> <target-hostname> <target-port> <host-port>");
        }


    }
}
