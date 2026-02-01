package com.tayek.util.log;
import static com.tayek.util.io.Print.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.*;
public class Lilith {
    private static final Logger logger=Logger.getLogger(Lilith.class.getName());
    
    public static void main(String[] args) throws IOException {
        LoggingHandler.init();
        LoggingHandler.setLevel(Level.INFO);
        if(true) {
            LoggingHandler.startSocketHandlers();
            for(int i=0;i<100;i++)
                logger.warning("i: "+i);
            LoggingHandler.stopSocketHandlers();
        } else {
            SocketHandler socketHandler=new SocketHandler("192.168.1.2",LoggingHandler.lilithLogServerService);
            if(socketHandler!=null) for(Logger logger:LoggingHandler.map.values())
                if(!Arrays.asList(logger.getHandlers()).contains(socketHandler)) {
                    p("adding socket handler to: "+logger);
                    logger.addHandler(socketHandler);
                }
            for(int i=0;i<100;i++)
                logger.warning("i: "+i);
        }
    }
}
