package com.tayek.util.misc;
import static com.tayek.util.concurrent.Threads.printThreads;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
class Main {
    public static void main(String[] arguments) {
        System.out.println("you just ran the default main class for the dispatcher.");
        System.out.println("all it does is print this message.");
    }
}
public class Dispatcher {
    public Dispatcher(final String[] arguments) {
        this.arguments=arguments;
        add(Main.class);
    }
    public void add(Class<?> clazz) {
        entryPoints.put(entryPoints.size()+1,clazz);
    }
    public void remove(int i) {
        entryPoints.remove(i);
    }
    void menu() {
        System.out.println("menu:");
        for(int x:entryPoints.keySet())
            System.out.println(x+" "+entryPoints.get(x).getSimpleName()+" ("+entryPoints.get(x).getName()+")");
    }
    public void run(int i) throws IllegalAccessException,IllegalArgumentException,InvocationTargetException,NoSuchMethodException,SecurityException,IOException {}
    public void run() throws IllegalAccessException,IllegalArgumentException,InvocationTargetException,NoSuchMethodException,SecurityException,IOException {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        loop:while(true) {
            menu();
            Integer number=null;
            String string=null;
            boolean ok=false;
            String[] parts=null;
            while(!ok) {
                System.out.println("enter a number and any argumemts.");
                string=in.readLine();
                System.out.println("string is: '"+string+"'");
                if(string==null) break loop; // end of input
                if(!string.isEmpty()) {
                    parts=string.split(" ");
                    number=toInteger(parts[0]);
                    if(number!=null) ok=true;
                } else System.out.println("empty line is not a valid choice");
            }
            run(number,string,parts);
        }
    }
    Class<?> run(Integer number,String string,String[] parts) {
        final Class<?> entryPoint=entryPoints.get(number);
        if(entryPoint==null) {
            System.out.println(string+" is not a valid choice");
        } else {
            final String[] theRest=parts.length>1?Arrays.copyOfRange(parts,1,parts.length):new String[0];
            System.out.println("the rest: "+Arrays.asList(theRest));
            new Thread(()-> {
                System.out.println("running: "+entryPoint+" with: "+Arrays.asList(theRest));
                try {
                    entryPoint.getMethod("main",String[].class).invoke(null,(Object)theRest);
                } catch(ReflectiveOperationException|IllegalArgumentException|SecurityException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                printThreads();
            },entryPoint.toString()).start();
        }
        return entryPoint;
    }
    public static void main(final String[] arguments) throws Exception {
        new Dispatcher(arguments).run();
    }
    public final String[] arguments; // given to main
    public final Map<Integer,Class<?>> entryPoints=new TreeMap<>();
    private static Integer toInteger(String argument) {
        try {
            return Integer.valueOf(argument);
        } catch(NumberFormatException e) {
            System.out.println(argument+" is not a valid integer!");
            return null;
        }
    }
}
