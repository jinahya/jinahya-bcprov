package io.github.jinahya.bouncycastle.crypto.modes;

import io.github.jinahya.bouncycastle.crypto.JinahyaCipherCrypto;
import io.github.jinahya.bouncycastle.crypto.JinahyaCryptoException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.modes.AEADCipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class JinahyaAEADCipherCrypto
        extends JinahyaCipherCrypto<AEADCipher> {

    public JinahyaAEADCipherCrypto(final AEADCipher cipher, final CipherParameters params) {
        super(cipher, params);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public byte[] encrypt(final byte[] in) {
        Objects.requireNonNull(in, "in is null");
        cipher.init(true, params);
        final var out = new byte[Math.max(cipher.getOutputSize(in.length), 1)];
        try {
            final var outlen = JinahyaAEADCipherUtils.processBytesAndDoFinal(cipher, in, 0, in.length, out, 0);
            return Arrays.copyOf(out, outlen);
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to encrypt", icte);
        }
    }

    @Override
    public int encrypt(final ByteBuffer input, final ByteBuffer output) {
        cipher.init(true, params);
        try {
            JinahyaAEADCipherUtils.processBytesAndDoFinal_(cipher, input, output);
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to encrypt", icte);
        }
        return 0;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public byte[] decrypt(byte[] in) {
        Objects.requireNonNull(in, "in is null");
        cipher.init(false, params);
        final var out = new byte[Math.max(cipher.getOutputSize(in.length), 1)];
        try {
            final var outlen = JinahyaAEADCipherUtils.processBytesAndDoFinal(cipher, in, 0, in.length, out, 0);
            return Arrays.copyOf(out, outlen);
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to decrypt", icte);
        }
    }

    @Override
    public int decrypt(final ByteBuffer input, final ByteBuffer output) {
        cipher.init(false, params);
        try {
            JinahyaAEADCipherUtils.processBytesAndDoFinal_(cipher, input, output);
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to decrypt", icte);
        }
        return 0;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long encrypt(final InputStream in, final OutputStream out, final byte[] inbuf) throws IOException {
        cipher.init(true, params);
        try {
            return JinahyaAEADCipherUtils.processAllBytesAndDoFinal(
                    cipher,
                    in,
                    out,
                    inbuf,
                    null
            );
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to encrypt", icte);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long decrypt(final InputStream in, final OutputStream out, final byte[] inbuf) throws IOException {
        cipher.init(false, params);
        try {
            return JinahyaAEADCipherUtils.processAllBytesAndDoFinal(
                    cipher,
                    in,
                    out,
                    inbuf,
                    null
            );
        } catch (final InvalidCipherTextException icte) {
            throw new JinahyaCryptoException("failed to decrypt", icte);
        }
    }
}