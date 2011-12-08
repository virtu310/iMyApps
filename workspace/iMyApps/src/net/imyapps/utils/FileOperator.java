package net.imyapps.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileOperator {
	public static String getFilename(String name) {
		int n = name.lastIndexOf('.');
		if (n > 0) {
			return name.substring(0, n);
		}
		return name;
	}
	
	public static long copy(InputStream in, OutputStream out) throws IOException {
		byte buf[] = new byte[4096];
		long size=0;
		int n;
		
		while ((n = in.read(buf)) > 0) {
			out.write(buf, 0, n);
			size += n;
		}
		
		return size;
	}
}
