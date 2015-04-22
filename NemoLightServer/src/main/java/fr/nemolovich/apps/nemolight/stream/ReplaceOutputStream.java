package fr.nemolovich.apps.nemolight.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import fr.nemolovich.apps.concurrentmultimap.ConcurrentMultiMap;

public class ReplaceOutputStream {

	private final InputStream in;
	private final OutputStream out;
	
	public ReplaceOutputStream(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	public void replace(String regex, String target) throws IOException {
		ConcurrentMap<Integer, byte[]> bytes = new ConcurrentMultiMap<>();
		byte[] buff = new byte[2048];
		byte[] tmp;
		int count;
		while ((count = this.in.read(buff)) >= 0) {
			tmp = new byte[count];
			System.arraycopy(buff, 0, tmp, 0, count);
			bytes.put(count, tmp);
		}
		for (Entry<Integer, byte[]> entry : bytes.entrySet()) {
			this.out.write(entry.getValue(), 0, entry.getKey());
		}
	}
	
}
