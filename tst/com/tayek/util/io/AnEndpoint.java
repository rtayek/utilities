package com.tayek.util.io;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
final class AnEndpoint implements Runnable {
    AnEndpoint(Duplex.End end) { this.in=end.in(); this.out=end.out(); }
    @Override public void run() {
        try {
            String line=in.readLine();
            if(line!=null) {
                out.write(line+"\n");
                out.flush();
            }
        } catch(IOException e) {
            // ignore for test helper
        }
    }
    final BufferedReader in;
    final Writer out;
}
