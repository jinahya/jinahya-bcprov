package io.github.jinahya.util.kisa;

import _javax.crypto._Cipher_TestUtils;
import _org.bouncycastle.jce.provider._BouncyCastleProvider_TestUtils;
import io.github.jinahya.util._RandomTestUtils;
import io.github.jinahya.util.bouncycastle.crypto._BufferedBlockCipherTestUtils;
import io.github.jinahya.util.bouncycastle.crypto._CipherParametersTestUtils;
import io.github.jinahya.util.bouncycastle.crypto.padding._BlockCipherPaddingTestUtils;
import io.github.jinahya.util.bouncycastle.crypto.params._KeyParametersTestUtils;
import io.github.jinahya.util.bouncycastle.crypto.params._ParametersWithIVTestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.LEAEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class LEA_CBC_Test
        extends LEA__Test {

    private static final String MODE = "CBC";

    private static Stream<Arguments> getArgumentsStream() {
        return _BlockCipherPaddingTestUtils.getBlockCipherPaddingStream().flatMap(p -> {
            return getKeySizeStream().mapToObj(ks -> {
                final var engine = new LEAEngine();
                final var cipher = new PaddedBufferedBlockCipher(CBCBlockCipher.newInstance(engine), p);
                final var params = _ParametersWithIVTestUtils.newRandomInstanceOfParametersWithIV(null, ks,
                                                                                                  cipher.getUnderlyingCipher());
                return Arguments.of(
                        Named.of(_BufferedBlockCipherTestUtils.cipherName(cipher, p), cipher),
                        Named.of(_CipherParametersTestUtils.paramsName(params), params)
                );
            });
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"getArgumentsStream"})
    @ParameterizedTest
    void __(final BufferedBlockCipher cipher, final CipherParameters params) throws Exception {
        _BufferedBlockCipherTestUtils.__(cipher, params);
    }

    @MethodSource({"getArgumentsStream"})
    @ParameterizedTest
    void __(final BufferedBlockCipher cipher, final CipherParameters params, @TempDir final File dir)
            throws Exception {
        _BufferedBlockCipherTestUtils.__(cipher, params, dir);
    }

    // -----------------------------------------------------------------------------------------------------------------
//    @ValueSource(ints = {
//            128,
//            192,
//            256
//    })
//    @ParameterizedTest
//    void __(final int keySize) throws Throwable {
//        _BouncyCastleProvider_TestUtils.callWithinBouncyCastleProvider(() -> {
//            final var transformation = ALGORITHM + '/' + MODE + "/PKCS5Padding";
//            final var cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
//            final var key = new SecretKeySpec(
//                    _KeyParametersTestUtils.newRandomKey(null, keySize),
//                    ALGORITHM
//            );
//            final var params = new IvParameterSpec(_RandomTestUtils.newRandomBytes(BLOCK_BYTES));
//            _Cipher_TestUtils.__(cipher, key, params);
//            return null;
//        });
//    }
//
//    @ValueSource(ints = {
//            128,
//            192,
//            256
//    })
//    @ParameterizedTest
//    void __(final int keySize, @TempDir final Path dir) throws Throwable {
//        _BouncyCastleProvider_TestUtils.callWithinBouncyCastleProvider(() -> {
//            final var transformation = ALGORITHM + '/' + MODE + "/PKCS5Padding";
//            final var cipher = Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
//            final var key = new SecretKeySpec(
//                    _KeyParametersTestUtils.newRandomKey(null, keySize),
//                    ALGORITHM
//            );
//            final var params = new IvParameterSpec(_RandomTestUtils.newRandomBytes(BLOCK_BYTES));
//            _Cipher_TestUtils.__(cipher, key, params, dir);
//            return null;
//        });
//    }
}
