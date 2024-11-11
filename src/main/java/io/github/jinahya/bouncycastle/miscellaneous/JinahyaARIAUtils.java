package io.github.jinahya.bouncycastle.miscellaneous;

import io.github.jinahya.bouncycastle.crypto.JinahyaBufferedBlockCipherCrypto;
import io.github.jinahya.bouncycastle.crypto.JinahyaCrypto;
import io.github.jinahya.bouncycastle.crypto.modes.JinahyaAEADCipherCrypto;
import org.bouncycastle.crypto.engines.ARIAEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utilities for the {@value _ARIA__Constants#ALGORITHM} algorithm.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JinahyaARIAUtils {

    // ----------------------------------------------------------------------------------------------- /CBC/PKCS7Padding
    private static JinahyaCrypto _CBC_PKCS7Padding(final byte[] key, final byte[] iv) {
        _ARIA__Utils.requireValidKey(key);
        _ARIA__Utils.requireValidIv(iv);
        final var cipher = new PaddedBufferedBlockCipher(
                CBCBlockCipher.newInstance(new ARIAEngine()),
                new PKCS7Padding()
        );
        final var params = new ParametersWithIV(
                new KeyParameter(key),
                iv
        );
        return new JinahyaBufferedBlockCipherCrypto(cipher, params);
    }

    /**
     * Encrypts, using specified key and iv, specified input bytes, and returns the result.
     *
     * @param key the key.
     * @param iv  the iv.
     * @param in  the input bytes to encrypt.
     * @return an array of encrypted bytes.
     */
    public static byte[] encrypt_CBC_PKCS7Padding(final byte[] key, final byte[] iv, final byte[] in) {
        return _CBC_PKCS7Padding(key, iv)
                .encrypt(in);
    }

    /**
     * Decrypts, using specified key and iv, specified input bytes, and returns the result.
     *
     * @param key the key.
     * @param iv  the iv.
     * @param in  the input bytes to decrypt.
     * @return an array of decrypted bytes.
     */
    public static byte[] decrypt_CBC_PKCS7Padding(final byte[] key, final byte[] iv, final byte[] in) {
        return _CBC_PKCS7Padding(key, iv)
                .decrypt(in);
    }

    /**
     * Encrypts, using specified key and iv, all bytes from specified input stream, and writes encrypted bytes to
     * specified output stream.
     *
     * @param key   the key.
     * @param iv    the iv.
     * @param in    the input stream from which plain bytes are read.
     * @param out   the output stream to which encrypted bytes are written.
     * @param inbuf a buffer for reading bytes from the input stream.
     * @return the number of bytes written to the output stream.
     * @throws IOException if an I/O error occurs.
     */
    public static long encrypt_CBC_PKCS7Padding(final byte[] key, final byte[] iv, final InputStream in,
                                                final OutputStream out, final byte[] inbuf)
            throws IOException {
        return _CBC_PKCS7Padding(key, iv)
                .encrypt(in, out, inbuf);
    }

    /**
     * Decrypts, using specified key and iv, all bytes from specified input stream, and writes decrypted bytes to
     * specified output stream.
     *
     * @param key   the key.
     * @param iv    the iv.
     * @param in    the input stream from which encrypted bytes are read.
     * @param out   the output stream to which decrypted bytes are written.
     * @param inbuf a buffer for reading bytes from the input stream.
     * @return the number of bytes written to the output stream.
     * @throws IOException if an I/O error occurs.
     */
    public static long decrypt_CBC_PKCS7Padding(final byte[] key, final byte[] iv, final InputStream in,
                                                final OutputStream out, final byte[] inbuf)
            throws IOException {
        return _CBC_PKCS7Padding(key, iv)
                .decrypt(in, out, inbuf);
    }

    // -------------------------------------------------------------------------------------------------- /GCM/NoPadding
    private static JinahyaCrypto _GCM_NoPadding(final byte[] key, final int tLen, final byte[] iv, final byte[] aad) {
        _ARIA__Utils.requireValidKey(key);
        __GCM_Utils.requireValid_tLen_GCM(tLen);
        __GCM_Utils.requireValid_iv_GCM(iv);
        final var cipher = GCMBlockCipher.newInstance(new ARIAEngine());
        final var params = new AEADParameters(
                new KeyParameter(key),
                tLen,
                iv,
                aad
        );
        return new JinahyaAEADCipherCrypto(cipher, params);
    }

    /**
     * Encrypts specified input bytes, in {@code GCM} mode with no padding, and returns result.
     *
     * @param key  a key.
     * @param tLen a length (in bits) of authentication tag.
     * @param iv   an initialization vector whose length should be greater than or equals to
     *             {@value __GCM_Constants#IV_SIZE_GCM_MINIMUM}.
     * @param aad  an additional authenticated data. may be {@code null}.
     * @param in   the input bytes to encrypt.
     * @return an array of encrypted bytes.
     */
    public static byte[] encrypt_GCM_NoPadding(final byte[] key, final int tLen, final byte[] iv, final byte[] aad,
                                               final byte[] in) {
        return _GCM_NoPadding(key, tLen, iv, aad)
                .encrypt(in);
    }

    /**
     * Decrypts specified input bytes, in {@code GCM} mode with no padding, and returns result.
     *
     * @param key  a key.
     * @param tLen a length (in bits) of authentication tag.
     * @param iv   an initialization vector whose length should be greater than or equals to
     *             {@value __GCM_Constants#IV_SIZE_GCM_MINIMUM}.
     * @param aad  an additional authenticated data. may be {@code null}.
     * @param in   the input bytes to decrypt.
     * @return an array of decrypted bytes.
     */
    public static byte[] decrypt_GCM_NoPadding(final byte[] key, final int tLen, final byte[] iv, final byte[] aad,
                                               final byte[] in) {
        return _GCM_NoPadding(key, tLen, iv, aad)
                .decrypt(in);
    }

    /**
     * Encrypts all bytes from specified input stream, and writes encrypted bytes to specified output stream.
     *
     * @param key   the key.
     * @param tLen  a length (in bits) of authentication tag.
     * @param iv    an initialization vector whose length should be greater than or equals to
     *              {@value __GCM_Constants#IV_SIZE_GCM_MINIMUM}.
     * @param aad   an additional authenticated data. may be {@code null}.
     * @param in    the input stream from which plain bytes are read.
     * @param out   the output stream to which encrypted bytes are written.
     * @param inbuf a buffer for reading bytes from the input stream.
     * @return the number of bytes written to the output stream.
     * @throws IOException if an I/O error occurs.
     */
    public static long encrypt_GMM_NoPadding(final byte[] key, final int tLen, final byte[] iv, final byte[] aad,
                                             final InputStream in, final OutputStream out, final byte[] inbuf)
            throws IOException {
        return _GCM_NoPadding(key, tLen, iv, aad)
                .encrypt(in, out, inbuf);
    }

    /**
     * Decrypts all bytes from specified input stream, and writes decrypted bytes to specified output stream.
     *
     * @param key   the key.
     * @param tLen  a length (in bits) of authentication tag.
     * @param iv    an initialization vector whose length should be greater than or equals to
     *              {@value __GCM_Constants#IV_SIZE_GCM_MINIMUM}.
     * @param aad   an additional authenticated data. may be {@code null}.
     * @param in    the input stream from which plain bytes are read.
     * @param out   the output stream to which decrypted bytes are written.
     * @param inbuf a buffer for reading bytes from the input stream.
     * @return the number of bytes written to the output stream.
     * @throws IOException if an I/O error occurs.
     */
    public static long decrypt_GMM_NoPadding(final byte[] key, final int tLen, final byte[] iv, final byte[] aad,
                                             final InputStream in, final OutputStream out, final byte[] inbuf)
            throws IOException {
        return _GCM_NoPadding(key, tLen, iv, aad)
                .decrypt(in, out, inbuf);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JinahyaARIAUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
