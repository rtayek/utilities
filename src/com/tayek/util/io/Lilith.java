package com.tayek.util.io;
import static com.tayek.util.io.IO.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.*;
public class Lilith {
    
    public static void main(String[] args) throws IOException {
        LoggingHandler.init();
        LoggingHandler.setLevel(Level.INFO);
        if(true) {
            LoggingHandler.startSocketHandlers();
            for(int i=0;i<100;i++)
                l.warning("i: "+i);
            LoggingHandler.stopSocketHandlers();
        } else {
            SocketHandler socketHandler=new SocketHandler("192.168.1.2",lilithLogServerService);
            if(socketHandler!=null) for(Logger logger:LoggingHandler.map.values())
                if(!Arrays.asList(logger.getHandlers()).contains(socketHandler)) {
                    p("adding socket handler to: "+logger);
                    logger.addHandler(socketHandler);
                }
            for(int i=0;i<100;i++)
                l.warning("i: "+i);
        }
    }
}
