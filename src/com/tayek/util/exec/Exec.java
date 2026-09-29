package com.tayek.util.exec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import static com.tayek.util.io.Print.*;
import java.util.*;
import java.util.logging.Logger;
public class Exec {
    public Exec(String command) { // split on whitespace like exec(String) does.
        processBuilder=new ProcessBuilder(splitCommand(command));
    }
    public Exec(String[] strings) {
        List<String> command=Arrays.asList(strings);
        processBuilder=new ProcessBuilder(command);
    }
    public static String output(InputStream inputStream) throws IOException {
        StringBuilder sb=new StringBuilder();
        try(BufferedReader br=new BufferedReader(new InputStreamReader(inputStream))) {
            for(String line=br.readLine();line!=null;line=br.readLine())
                sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }
    public Exec run() {
        Process process;
        try {
            process=processBuilder.start();
            // drain stderr on its own thread and stdout here, *then* wait.
            // waiting first can hang forever once the process fills a pipe buffer.
            final Process started=process;
            CompletableFuture<String> errorFuture=CompletableFuture.supplyAsync(()-> {
                try {
                    return output(started.getErrorStream());
                } catch(IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            output=output(process.getInputStream());
            error=errorFuture.join();
            rc=process.waitFor();
        } catch(InterruptedException e) {
            l.warning("caught: "+e);
            Thread.currentThread().interrupt();
        } catch(Exception e) {
            l.warning("caught: "+e);
            e.printStackTrace();
        }
        return this;
    }
    public void print() {
        print(rc,output,error);
    }
    public int rc() {
        return rc;
    }
    public String output() {
        return output;
    }
    public String error() {
        return error;
    }
    public static void print(int rc,String output,String error) {
        p("return code: "+rc);
        p("output: '"+output+"'");
        p("err: '"+error+"'");
    }
    public static int exec(String[] strings) {
        //p("building process: "+Arrays.asList(strings));
        Exec exec=new Exec(strings);
        exec.run();
        //exec.print();
        return exec.rc;
    }
    public static int exec(String command) {
        List<String> parts=splitCommand(command);
        if(parts.isEmpty()) return -1;
        Exec exec=new Exec(parts.toArray(new String[0]));
        exec.run();
        print(exec.rc,exec.output,exec.error);
        return exec.rc;
    }
    private static List<String> splitCommand(String command) {
        if(command==null) return Collections.emptyList();
        StringTokenizer tokenizer=new StringTokenizer(command);
        List<String> parts=new ArrayList<>(tokenizer.countTokens());
        while(tokenizer.hasMoreTokens())
            parts.add(tokenizer.nextToken());
        return parts;
    }
    public static int ping(String host) {
        return exec(new String[] {"ping",host});
    }
    /** True if host answers within timeout ms. Uses ICMP where the OS allows it, else a TCP probe. */
    public static boolean canWePing(String host,int timeout) {
        try {
            return InetAddress.getByName(host).isReachable(timeout);
        } catch(IOException e) {
            return false;
        }
    }
    public static void main(String[] args) throws InterruptedException,IOException {
        p("------");
        Exec exec=new Exec(new String[] {"ping","localhost"});
        exec.run();
        exec.print();
        p("------");
        Exec.exec("ping localhost");
        p("------");
        Exec.ping("localhost");
        p("------");
        new Exec(new String[] {"ping","localhost"}).run().print();
    }
    final ProcessBuilder processBuilder;
    int rc;
    String output="",error="";
    public static final String[] goodhosts=new String[] {"127.0.0.1","localhost",/*tabletRouter*/},badHosts=new String[] {"probablyNotAHostName"};
    public static final Logger l=Logger.getLogger(Exec.class.getName());
}
