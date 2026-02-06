package com.tayek.util.core;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;
public class FindMainPrograms {
    static class Triple {
        Triple(File file,String clazz,String main) {
            if(file==null) System.exit(1);
            if(clazz==null) System.exit(1);
            if(main==null) System.exit(1);
            this.file=file;
            this.clazz=clazz;
            this.main=main;
        }
        @Override public String toString() { return "Triple [file="+file+", clazz="+clazz+", main="+main+"]"; }
        final File file;
        final String clazz;
        final String main;
    }
    static void fillFilesRecursively(File file,List<File> resultFiles) {
        if(file.isFile()) {
            if(file.getName().endsWith(javaExtension)) resultFiles.add(file);
        } else {
            File[] children=file.listFiles();
            if(children!=null) for(File child:children) { fillFilesRecursively(child,resultFiles); }
        }
    }
    private static Class<?> processTriple(String name,Triple triple) {
        String dotted=dotted(triple.file);
        Class<?> clazz=null;
        try {
            clazz=Class.forName(dotted);
        } catch(ClassNotFoundException e) {
            // ignore
        }
        return clazz;
    }
    private static String dotted(File file) {
        Path path=Path.of(file.toString());
        int n=path.getNameCount();
        path=path.subpath(1,n);
        String string=path.toString();
        if(string.endsWith(javaExtension)) string=string.substring(0,string.length()-javaExtension.length());
        string=string.replaceAll("\\\\",".");
        string=string.replaceAll("/",".");
        return string;
    }
    private static Pair<String,String> getClassName(String string) {
        String found=null;
        String name=null;
        List<String> words=Arrays.asList(string.split(" "));
        for(String target:targets) { if(words.contains(target)) { found=target; break; } }
        if(found!=null) {
            int index=words.indexOf(found);
            name=words.get(index+1);
        }
        return new Pair<>(found,name);
    }
    private static List<Triple> makeTRiplesFromFiles() throws IOException {
        List<File> files=new ArrayList<>();
        for(String folder:sourceFolders) { fillFilesRecursively(new File(folder),files); }
        logger.warning(files.size()+" java files.");
        List<String> lines=new ArrayList<>();
        List<Triple> mains=new ArrayList<>();
        List<Triple> different=new ArrayList<>();
        for(File file:files) {
            String lastClass=null;
            lines=Files.readAllLines(file.toPath(),StandardCharsets.UTF_8);
            for(String line:lines) {
                if(line.contains("class ")||line.contains("interface ")||line.contains("enum ")) {
                    Pair<String,String> pair=getClassName(line);
                    String name=pair.second;
                    if(name!=null&&Misc.isValidName(name)) {
                        lastClass=line;
                    } else logger.info("not valid identifier: "+name+" "+line);
                }
                if(line.contains(target)) {
                    Triple triple=new Triple(file,lastClass,line);
                    Pair<String,String> pair=getClassName(triple.clazz);
                    if(pair.first==null||pair.second==null) {
                        if(pair.first==null) logger.info("found is null.");
                        if(pair.second==null) logger.info("name is null.");
                        logger.warning("excluding: "+triple);
                    } else {
                        if(triple.file.toString().contains(pair.second+javaExtension)) {
                            mains.add(triple);
                            Class<?> clazz=processTriple(pair.second,triple);
                            if(clazz==null) {
                                logger.info("can not find class!: "+pair+" "+triple);
                                System.exit(0);
                            }
                        } else {
                            different.add(triple);
                        }
                    }
                }
            }
        }
        logger.warning(mains.size()+" mains.");
        logger.warning(different.size()+" different.");
        logger.info("different: ----------------------------");
        if(true) {
            for(Triple triple:different) {
                Pair<String,String> pair=getClassName(triple.clazz);
                if(!triple.file.toString().contains(pair.second+javaExtension))
                    logger.info("still different: "+pair.second+"!="+triple.file);
                logger.info(pair+" "+triple);
                Class<?> clazz=processTriple(pair.second,triple);
                logger.info("class: "+clazz);
            }
        }
        return mains;
    }
    public static void main(String[] args) throws IOException {
        List<Triple> mains=makeTRiplesFromFiles();
        logger.info("after makeMainsFromFiles().");
        if(true) return;
    }
    static String javaExtension=".java";
    static String mainsFilename="mains2.txt";
    static String projectFolder="code4";
    static Set<String> sourceFolders=Set.of("src","tst");
    static Set<String> targets=Set.of("class","interface","enum");
    static String target="public static void main(";
    private static final Logger logger=Logger.getLogger(FindMainPrograms.class.getName());
}
