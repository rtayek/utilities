package io;
import java.io.IOException;
import java.net.Socket;
public class IO {
    public static void closeSocket(Socket socket) {
        if(socket!=null) try {
            if(!socket.isClosed()) {
                Logging.logger.info("shutdown input");
                if(!socket.isInputShutdown()) socket.shutdownInput();
                Logging.logger.info("shutdown output");
                if(!socket.isOutputShutdown()) socket.shutdownOutput();
                Logging.logger.info("closing socket");
                if(!socket.isClosed()) socket.close();
            }
        } catch(IOException e) {
            Logging.logger.info("close caught: "+e);
        }
    }
}
