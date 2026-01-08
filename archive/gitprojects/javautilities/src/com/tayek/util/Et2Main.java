package com.tayek.util;
import java.io.*;
// 3.5-4.8 x 10^8 bytes/second single threaded
class ET2 {
	ET2() {
		this(System.currentTimeMillis());
	}
	ET2(long t0) {
		this.t0=t0;
	}
	long et() {
		return et(System.currentTimeMillis());
	}
	long et(long t) {
		return t-t0;
	}
	double rate(long n) {
		return rate(n,System.currentTimeMillis());
	}
	double rate(long n,long t) {
		return n*1000./et(t);
	}
	void print(long n) {
		System.out.println("done "+n+" bytes in "+(et()/1000.)+" seconds, "+rate(n)+" bytes/second.");
	}
	void print(long n,long t) {
		System.out.println("done "+n+" bytes in "+(et(t)/1000.)+" seconds, "+rate(n,t)+" bytes/second.");
	}
	final long t0;
}
class Read implements Runnable {
	Read(File file) {
		try {
			InputStream inputStream=new FileInputStream(file);
			Reader reader=new InputStreamReader(inputStream);
			bufferedReader=new BufferedReader(reader);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	Read(BufferedReader bufferedReader) {
		this.bufferedReader=bufferedReader;
	}
	@Override public void run() {
		try {
			for(int bytes=bufferedReader.read(buffer);bytes!=-1;bytes=bufferedReader.read(buffer)) {
				this.n+=bytes;
			}
		} catch(IOException e) {
			e.printStackTrace();
			close();
			throw new RuntimeException(e);
		}
		//System.out.println("read "+n);
		close();
	}
	void close() {
		if(bufferedReader!=null)
			try {
				bufferedReader.close();
			} catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}
	static void read(String part,boolean parallel) {
		final int threads=Thread.activeCount();
		ET2 et=new ET2();
		for(int i=0;i<Write.files;i++) {
			File file=new File(part+i);
			if(!parallel) {
				System.out.print("read "+file+", ");
				et=new ET2();
			}
			Read read=new Read(file);
			Thread thread=new Thread(read);
			thread.start();
			if(!parallel)
				try {
					thread.join();
					et.print(read.n);
				} catch(InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
		}
		//System.out.println(Thread.activeCount()+" threads started");
		//System.out.flush();
		while(Thread.activeCount()!=threads);
		//System.out.println("done");
		if(parallel)
			et.print(Write.files*Write.fileSize);
		System.out.flush();
	}
	final BufferedReader bufferedReader;
	final int size=1024;
	final char[] buffer=new char[size];
	long n;
}
class Write implements Runnable {
	Write(BufferedWriter bufferedWriter,int n) {
		this.bufferedWriter=bufferedWriter;
		this.n=n;
	}
	@Override public void run() {
		try {
			for(int i=0;i<n/buffer.length;i++)
				bufferedWriter.write(buffer);
			if(n%buffer.length>0)
				bufferedWriter.write(buffer,0,n%buffer.length);
		} catch(IOException e) {
			e.printStackTrace();
			try {
				bufferedWriter.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	static void write(File file) {
		if(reWrite||!file.exists()) {
			try {
				BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
				Write write=new Write(bufferedWriter,fileSize);
				write.run();
				bufferedWriter.close();
			} catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	static void write(String dir) {
		for(int i=0;i<files;i++)
			write(new File(dir,"file."+i));
	}
	BufferedWriter bufferedWriter;
	int n;
	final int bufferSize=1000;
	char[] buffer=new char[bufferSize];
	{
		for(int i=0;i<buffer.length;i++)
			buffer[i]=fillCharacter;
		fillCharacter++;
	}
	static int files=10;
	static int fileSize=100000000;
	static char fillCharacter='a';
	static boolean reWrite=false;
}
public class Et2Main {
	private static void createFiles(String dir) {
		// Write.reWrite=true;
		System.out.print("write "+dir+", ");
		ET2 et=new ET2();
		Write.write(dir);
		et.print(Write.fileSize*Write.files);
		// Write.fillCharacter++;
		// System.out.println("done "+write.n*Write.files+" bytes in "+(et.et()/1000.)+" seconds, "+et.rate(read.n)+" bytes/second.");
		// System.out.println("done "+write.n*Write.files+" bytes in "+(et.et(t)/1000.)+" seconds, "+et.rate(read.n,t)+" bytes/second.");
	}
	private static void readDdo() {
		File file=new File("D:/games/DDO Unlimited/client_sound.dat");
		try {
			Read read=new Read(file);
			Thread thread=new Thread(read);
			thread.start();
			thread.join();
			// read.run();
			read.close();
		} catch(InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		System.out.println(Thread.activeCount()+" active threads");
		createFiles("x:/");
		createFiles("d:/");
		Read.read("x:/file.",true);
		Read.read("d:/file.",true);
		System.out.println(Thread.activeCount()+" active threads");
		// readDdo();
	}
}
