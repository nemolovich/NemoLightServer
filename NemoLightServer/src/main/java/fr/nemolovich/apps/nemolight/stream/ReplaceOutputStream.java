package fr.nemolovich.apps.nemolight.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.nemolovich.apps.concurrentmultimap.ConcurrentMultiMap;

public class ReplaceOutputStream extends OutputStream {

	private static final int ARR_SIZE = 2048;

	private int nbBytes;
	private byte[] tmpBuff;
	private final OutputStream out;
	private final ConcurrentMap<Integer, byte[]> bytes;
	private final String regex;
	private final String target;

	public ReplaceOutputStream(OutputStream out, String regex, String target) {
		this.nbBytes = 0;
		this.tmpBuff = new byte[ARR_SIZE];
		this.out = out;
		this.regex = regex;
		this.target = target;
		this.bytes = new ConcurrentMultiMap<>();
	}

	@Override
	public void write(byte[] buff, int off, int len) throws IOException {
		byte[] tmp = new byte[len];
		System.arraycopy(buff, 0, tmp, 0, len);
		this.bytes.put(len, tmp);
	}

	@Override
	public void write(int b) throws IOException {
		if (this.nbBytes >= ARR_SIZE) {
			byte[] tmp = new byte[ARR_SIZE];
			System.arraycopy(this.tmpBuff, 0, tmp, 0, ARR_SIZE);
			this.bytes.put(ARR_SIZE, tmp);
			this.nbBytes = 0;
		}
		this.tmpBuff[this.nbBytes++] = (byte) b;
	}

	@Override
	public void close() throws IOException {
		if (this.nbBytes > 0) {
			byte[] tmp = new byte[this.nbBytes];
			System.arraycopy(this.tmpBuff, 0, tmp, 0, this.nbBytes);
			this.bytes.put(this.nbBytes, tmp);
		}
		byte[] prev = null;
		byte[] tab = null;
		String concat;
		Pattern p = Pattern.compile(this.regex);
		Matcher m;
		String replace;
		for (Entry<Integer, byte[]> entry : this.bytes.entrySet()) {
			tab = entry.getValue();
			if (prev != null) {
				concat = new String(prev).concat(new String(tab));
				m = p.matcher(concat);
				boolean found = false;
				while (m.find()) {
					found = true;
					replace = m.replaceFirst(this.target).substring(0,
							m.start() + this.target.length());
					concat = concat.substring(m.end(), concat.length());
					prev = new byte[replace.getBytes().length];
					tab = new byte[concat.getBytes().length];
					System.arraycopy(replace.getBytes(), 0, prev, 0,
							replace.getBytes().length);
					System.arraycopy(concat.getBytes(), 0, tab, 0,
							concat.getBytes().length);
					this.out.write(prev, 0, prev.length);
					m = p.matcher(concat);
				}
				if (!found) {
					this.out.write(prev, 0, prev.length);
				}

			}
			prev = new byte[tab.length];
			System.arraycopy(tab, 0, prev, 0, tab.length);
		}
		if (prev != null) {
			concat = new String(prev);
			m = p.matcher(concat);
			boolean found = false;
			while (m.find()) {
				found = true;
				replace = m.replaceFirst(this.target).substring(0,
						m.start() + this.target.length());
				concat = concat.substring(m.end(), concat.length());
				prev = new byte[replace.getBytes().length];
				System.arraycopy(replace.getBytes(), 0, prev, 0,
						replace.getBytes().length);
				this.out.write(prev, 0, prev.length);
				m = p.matcher(concat);
			}
			if (!found) {
				this.out.write(prev, 0, prev.length);
			}
		}
		this.out.close();
	}

}
