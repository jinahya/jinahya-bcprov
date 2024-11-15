package io.github.jinahya.bouncycastle.crypto;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamCipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * A utility class for {@link StreamCipher}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see <a
 * href="https://downloads.bouncycastle.org/java/docs/bcprov-jdk18on-javadoc/index.html?org/bouncycastle/crypto/StreamCipher.html">org.bouncycastle.crypto.StreamCipher</a>
 * (bcprov-jdk18on-javadoc)
 */
public final class JinahyaStreamCipherUtils {

    // -----------------------------------------------------------------------------------------------------------------
//    private static int processBytes_(final StreamCipher cipher, final byte[] in, final int inoff, final int inlen,
//                                     final byte[] out, final int outoff) {
//        return cipher.processBytes(in, inoff, inlen, out, outoff);
//    }
//
//    public static int processBytes(final StreamCipher cipher, final byte[] in, final int inoff, final int inlen,
//                                   final byte[] out, final int outoff) {
//        Objects.requireNonNull(cipher, "cipher is null");
//        Objects.requireNonNull(in, "in is null");
//        if (inoff < 0) {
//            throw new IllegalArgumentException("inoff(" + inoff + ") is negative");
//        }
//        if (inlen < 0) {
//            throw new IllegalArgumentException("inlen(" + inlen + ") is negative");
//        }
//        if (inoff + inlen > in.length) {
//            throw new IllegalArgumentException(
//                    "inoff(" + inoff + ") + inlen(" + inlen + ") > in.length(" + in.length + ")");
//        }
//        Objects.requireNonNull(out, "out is null");
//        if (outoff < 0) {
//            throw new IllegalArgumentException("outoff(" + inoff + ") is negative");
//        }
//        if (outoff > out.length) {
//            throw new IllegalArgumentException("outoff(" + outoff + ") > out.length(" + out.length + ")");
//        }
//        return processBytes_(cipher, in, inoff, inlen, out, outoff);
//    }

    public static byte[] processBytes(final StreamCipher cipher, final byte[] in, final int inoff, final int inlen) {
        Objects.requireNonNull(cipher, "cipher is null");
        Objects.requireNonNull(in, "in is null");
        if (inoff < 0) {
            throw new IllegalArgumentException("inoff(" + inoff + ") is negative");
        }
        if (inlen < 0) {
            throw new IllegalArgumentException("inlen(" + inlen + ") is negative");
        }
        if (inoff + inlen > in.length) {
            throw new IllegalArgumentException(
                    "inoff(" + inoff + ") + inlen(" + inlen + ") > in.length(" + in.length + ")");
        }
        for (var out = new byte[in.length == 0 ? 1 : in.length]; ; ) {
            try {
                final var outlen = cipher.processBytes(in, inoff, inlen, out, 0);
                return Arrays.copyOf(out, outlen);
            } catch (final DataLengthException dle) {
                System.err.println("doubling up out.length(" + out.length + ")");
                out = new byte[out.length << 1];
            }
        }
    }

    public static int processBytes(final StreamCipher cipher, final ByteBuffer input, final ByteBuffer output) {
        Objects.requireNonNull(cipher, "cipher is null");
        Objects.requireNonNull(input, "input is null");
        Objects.requireNonNull(output, "output is null");
        final byte[] in;
        final int inoff;
        final int inlen = input.remaining();
        if (input.hasArray()) {
            in = input.array();
            inoff = input.arrayOffset() + input.position();
        } else {
            in = new byte[inlen];
//            input.get(0, in); // Since 13
            for (int p = input.position(), i = 0; i < in.length; p++, i++) {
                in[i] = input.get(p);
            }
            inoff = 0;
        }
        final var out = processBytes(cipher, in, inoff, inlen);
        output.put(out); // BufferOverflowException
        // input's position should be modified only after the output.put(out) succeeded
        input.position(input.position() + inlen);
        return out.length;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static long processAllBytes(final StreamCipher cipher, final InputStream in, final OutputStream out,
                                       final byte[] inbuf, byte[] outbuf)
            throws IOException {
        Objects.requireNonNull(cipher, "cipher is null");
        Objects.requireNonNull(in, "in is null");
        Objects.requireNonNull(out, "out is null");
        if (Objects.requireNonNull(inbuf, "inbuf is null").length == 0) {
            throw new IllegalArgumentException("inbuf.length is zero");
        }
        if (outbuf == null || outbuf.length == 0) {
            outbuf = new byte[inbuf.length];
        }
        var bytes = 0L;
        for (int outlen, r; (r = in.read(inbuf)) != -1; ) {
            while (true) {
                try {
                    outlen = cipher.processBytes(inbuf, 0, r, outbuf, 0);
                    out.write(outbuf, 0, outlen);
                    bytes += outlen;
                    break;
                } catch (final DataLengthException dle) {
                    System.err.println("doubling up outbuf.length(" + outbuf.length + ")");
                    Arrays.fill(outbuf, (byte) 0);
                    outbuf = new byte[outbuf.length << 1];
                }
            }
        }
        return bytes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JinahyaStreamCipherUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
