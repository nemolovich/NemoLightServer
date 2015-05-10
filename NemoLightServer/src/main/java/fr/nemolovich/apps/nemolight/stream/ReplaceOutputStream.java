package fr.nemolovich.apps.nemolight.stream;

import fr.nemolovich.apps.concurrentmultimap.ConcurrentMultiMap;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * This kind of stream is used to write a content and replace a sequence by
 * another when this first is found.
 * </p>
 * <p>
 * The replacement is made in the {@link #close() close()} method, if an error
 * occurred during the writing then no replacement is made.
 * </p>
 * <p>
 * The {@link #write(int) write(int)} method writes the byte in a temporary
 * buffer and store the buffer once its size is {@link #ARR_SIZE ARR_SIZE}. Its
 * recommended to use only one of the differents write methods
 * ({@link #write(int) write(int)}, {@link #write(byte[]) write(byte[])} or
 * {@link #write(byte[], int, int) write(byte[], int, int)}) and do not mix.
 * </p>
 *
 * @author Nemolovich
 */
public class ReplaceOutputStream extends OutputStream {

    private static final int ARR_SIZE = 2048;

    private int nbBytes;
    private final byte[] tmpBuff;
    private final OutputStream out;
    private final ConcurrentMap<Integer, byte[]> bytes;
    private final String regex;
    private final String target;
    private final Pattern pattern;

    /**
     * Default constructor with stream in which to write content once replaced.
     *
     * @param out {@link OutputStream}: Stream in which to write.
     * @param regex {@link String}: Regular expression to replace.
     * @param target {@link String}: Replacement of researched expression.
     */
    public ReplaceOutputStream(OutputStream out,
        String regex, String target) {
        this.nbBytes = 0;
        this.tmpBuff = new byte[ARR_SIZE];
        this.out = out;
        this.regex = regex;
        this.target = target;
        this.bytes = new ConcurrentMultiMap<>();
        this.pattern = Pattern.compile(this.regex);
    }

    @Override
    public void write(byte[] buff, int off, int len)
        throws IOException {
        byte[] tmp = new byte[len];
        System.arraycopy(buff, 0, tmp, 0, len);
        this.bytes.put(len, tmp);
    }

    @Override
    public void write(byte[] buff) throws IOException {
        this.write(buff, 0, buff.length);
    }

    @Override
    public void write(int b) throws IOException {
        /*
         * If the size must be stored then add to bytes
         * arrays.
         */
        if (this.nbBytes >= ARR_SIZE) {
            byte[] tmp = new byte[ARR_SIZE];
            System.arraycopy(this.tmpBuff, 0, tmp, 0, ARR_SIZE);
            this.bytes.put(ARR_SIZE, tmp);
            /*
             * Reset bytes number.
             */
            this.nbBytes = 0;
        }
        /*
         * Copy byte to current buffer.
         */
        this.tmpBuff[this.nbBytes++] = (byte) b;
    }

    @Override
    public void close() throws IOException {
        /*
         * Check if the current buffer is not empty.
         */
        if (this.nbBytes > 0) {
            byte[] tmp = new byte[this.nbBytes];
            System.arraycopy(this.tmpBuff, 0, tmp, 0,
                this.nbBytes);
            this.bytes.put(this.nbBytes, tmp);
        }

        /*
         * Copy all the bytes array into the output stream.
         */
        byte[] prev = null;
        byte[] tab;
        for (Entry<Integer, byte[]> entry : this.bytes.entrySet()) {
            tab = entry.getValue();
            prev = this.witeInBuff(prev, tab);
        }
        this.witeInBuff(prev, null);

        /*
         * Don't forget to close the output stream ;)
         */
        this.out.close();
    }

    private byte[] witeInBuff(byte[] prev, byte[] value)
        throws IOException {
        byte[] result = null;

        /*
         * Can not compare if there is no previous buffer.
         */
        if (prev != null) {

            /*
             * If there is already values in previous array
             * then feed the result and check if the content
             * contains the regex.
             */
            result = new byte[prev.length];
            System.arraycopy(prev, 0, result, 0, prev.length);

            /*
             * Construct characters chain with previous buffer
             * and current character chain.
             */
            String concat = new String(result, Charset.defaultCharset())
                .concat(value == null ? "" : new String(value,
                            Charset.defaultCharset()));
            Matcher matcher = this.pattern.matcher(concat);
            String replace;

            /*
             * Loop until the content matches.
             */
            boolean found = false;
            while (matcher.find()) {

                /*
                 * Flag as at least one match.
                 */
                found = true;

                /*
                 * The replacement chain take the start of
                 * the previous chain to start of the regex
                 * occurence plus the target size.
                 * 
                 * Then concat take the previous chain from
                 * the end of regex occurence to the end
                 * of previous chain.
                 *
                 * EG:
                 *  - content: "This is an example."
                 *  - regex:   "[^h]is"
                 *  - target:  "\swas"
                 * replace is also equals to: "This was" and
                 * concat is equals to: "an example".
                 */
                replace = matcher.replaceFirst(this.target).substring(0,
                    matcher.start() + this.target.length());
                concat = concat.substring(matcher.end(), concat.length());

                /*
                 * Save the transform contents in bytes array.
                 */
                result = new byte[replace.getBytes(
                    Charset.defaultCharset()).length];
                value = new byte[concat.getBytes(
                    Charset.defaultCharset()).length];
                System.arraycopy(replace.getBytes(
                    Charset.defaultCharset()), 0, result, 0,
                    replace.getBytes(Charset.defaultCharset()).length);
                System.arraycopy(concat.getBytes(
                    Charset.defaultCharset()), 0, value, 0,
                    concat.getBytes(Charset.defaultCharset()).length);

                /*
                 * Write the previous content to output stream
                 * then update the matcher.
                 */
                this.out.write(result, 0, result.length);
                matcher = this.pattern.matcher(concat);

            }

            /*
             * If the regex did not match then the buffer can
             * be written in the output stream.
             */
            if (!found) {
                this.out.write(result, 0, result.length);
            }

        }

        /*
         * Copy the current value into the previous buffer
         * if there is.
         */
        if (value != null) {
            result = new byte[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

}
