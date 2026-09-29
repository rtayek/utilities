package com.tayek.util.io;
import java.io.*;
import java.nio.file.Files;
public class Serialization {
	public static byte[] save(final Object o) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try(ObjectOutputStream out=new ObjectOutputStream(baos)) {
			out.writeObject(o);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}
	public static Object restore(final ObjectInputStream objectInputStream) {
		try(objectInputStream) {
			return objectInputStream.readObject();
		} catch(IOException|ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object restore(final byte[] b) {
		try {
			ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(b));
			return restore(in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object restore(final File file) {
		try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(file.toPath()))) {
			return restore(in);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
