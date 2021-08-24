package zone.wim.coding.text.unicode;

/*
 * Copyright (c) 2000, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import zone.wim.coding.CoderResult;
import zone.wim.coding.Decoder;
import zone.wim.coding.Encoder;
import zone.wim.coding.text.TextCodec;
import zone.wim.coding.text.TextDecoder;
import zone.wim.coding.text.TextEncoder;

import java.nio.Buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* Legal UTF-8 Byte Sequences
 *
 * #    Code Points      Bits   Bit/Byte pattern
 * 1                     7      0xxxxxxx
 *      U+0000..U+007F          00..7F
 *
 * 2                     11     110xxxxx    10xxxxxx
 *      U+0080..U+07FF          C2..DF      80..BF
 *
 * 3                     16     1110xxxx    10xxxxxx    10xxxxxx
 *      U+0800..U+0FFF          E0          A0..BF      80..BF
 *      U+1000..U+FFFF          E1..EF      80..BF      80..BF
 *
 * 4                     21     11110xxx    10xxxxxx    10xxxxxx    10xxxxxx
 *     U+10000..U+3FFFF         F0          90..BF      80..BF      80..BF
 *     U+40000..U+FFFFF         F1..F3      80..BF      80..BF      80..BF
 *    U+100000..U10FFFF         F4          80..8F      80..BF      80..BF
 *
 */

public final class UTF_8 extends UnicodeCodec {

    public static String CANONICAL_NAME = "UTF-8";
    public static String[] ALIASES = {"utf8", "utf-8"};
    public static float AVERAGE_BYTES_PER_CHAR = 1.1f;
    public static float MAX_BYTES_PER_CHAR = 4.0f;  // jdk UTF_8 class says 3.0f... why? 🤨
    public static float AVERAGE_CHARS_PER_BYTE = 1.5f;  //jdk UTF_8 class says 1.0f.
    public static float MAX_CHARS_PER_BYTE = 1.0f;

    public String canonicalName() { return CANONICAL_NAME; }
    public String[] aliases() { return ALIASES; }
    public float averageBytesPerChar() { return AVERAGE_BYTES_PER_CHAR; }
    public float maxBytesPerChar() { return MAX_BYTES_PER_CHAR; }

    @Override
    public TextDecoder decoder(ByteBuffer src, CharBuffer dst) {
        return new Decoder(this, src, dst);
    }

    @Override
    public TextEncoder encoder(CharBuffer src, ByteBuffer dst) {
        return new Encoder(this, src, dst);
    }

    public float averageCharsPerByte() { return AVERAGE_CHARS_PER_BYTE; }
    public float maxCharsPerByte() { return MAX_CHARS_PER_BYTE; }
    public Charset charset() { return StandardCharsets.UTF_8; }

    @Override
    public boolean byteOrderSensitive() {
        return false;
    }

    static final void updatePositions(Buffer src, int sp, Buffer dst, int dp) {
        src.position(sp - src.arrayOffset());
        dst.position(dp - dst.arrayOffset());
    }

    private static CoderResult escape(int ec) {
        return CoderResult.escaped(ec);
    }

    private static CoderResult escape(Buffer src, int mark, int ec) {
        src.position(mark);
        return CoderResult.escaped(ec);
    }

    private static CoderResult stop(int ec) {
        return CoderResult.stopped(ec);
    }

    private static CoderResult stop(Buffer src, int mark, int ec) {
        src.position(mark);
        return CoderResult.stopped(ec);
    }

    private static final class Encoder extends TextEncoder {

        private Encoder(TextCodec codec, CharBuffer src, ByteBuffer dst) {
            super(codec, src, dst);
        }

        public boolean canEncode(char c) {
            return !Character.isSurrogate(c);
        }

//        public boolean isLegalReplacement(byte[] repl) {
//            return ((repl.length == 1 && repl[0] >= 0) ||
//                    super.isLegalReplacement(repl));
//        }

        private static CoderResult overflow(CharBuffer src, int sp,
                                            ByteBuffer dst, int dp) {
            updatePositions(src, sp, dst, dp);
            return CoderResult.OVERFLOW;
        }

        private static CoderResult overflow(CharBuffer src, int mark) {
            src.position(mark);
            return CoderResult.OVERFLOW;
        }

        private Surrogate.Parser sgp;
        private CoderResult encodeArrayLoop() {
            char[] sa = src.array();
            int sp = src.arrayOffset() + src.position();
            int sl = src.arrayOffset() + src.limit();

            byte[] da = dst.array();
            int dp = dst.arrayOffset() + dst.position();
            int dl = dst.arrayOffset() + dst.limit();
            int dlASCII = dp + Math.min(sl - sp, dl - dp);

            // ASCII only loop
            while (dp < dlASCII && sa[sp] < '\u0080')
                da[dp++] = (byte) sa[sp++];
            while (sp < sl) {
                char c = sa[sp];
                if (c < 0x80) {
                    // Have at most seven bits
                    if (dp >= dl)
                        return overflow(src, sp, dst, dp);
                    da[dp++] = (byte)c;
                } else if (c < 0x800) {
                    // 2 bytes, 11 bits
                    if (dl - dp < 2)
                        return overflow(src, sp, dst, dp);
                    da[dp++] = (byte)(0xc0 | (c >> 6));
                    da[dp++] = (byte)(0x80 | (c & 0x3f));
                } else if (Character.isSurrogate(c)) {
                    // Have a surrogate pair
                    if (sgp == null)
                        sgp = new Surrogate.Parser();
                    int uc = sgp.parse(c, sa, sp, sl);
                    if (uc < 0) {
                        updatePositions(src, sp, dst, dp);
                        return sgp.error();
                    }
                    if (dl - dp < 4)
                        return overflow(src, sp, dst, dp);
                    da[dp++] = (byte)(0xf0 | ((uc >> 18)));
                    da[dp++] = (byte)(0x80 | ((uc >> 12) & 0x3f));
                    da[dp++] = (byte)(0x80 | ((uc >>  6) & 0x3f));
                    da[dp++] = (byte)(0x80 | (uc & 0x3f));
                    sp++;  // 2 chars
                } else {
                    // 3 bytes, 16 bits
                    if (dl - dp < 3)
                        return overflow(src, sp, dst, dp);
                    da[dp++] = (byte)(0xe0 | ((c >> 12)));
                    da[dp++] = (byte)(0x80 | ((c >>  6) & 0x3f));
                    da[dp++] = (byte)(0x80 | (c & 0x3f));
                }
                sp++;
            }
            updatePositions(src, sp, dst, dp);
            return CoderResult.UNDERFLOW;
        }

        private CoderResult encodeBufferLoop() {
            int mark = src.position();
            while (src.hasRemaining()) {
                char c = src.get();
                if (c < 0x80) {
                    // Have at most seven bits
                    if (!dst.hasRemaining())
                        return overflow(src, mark);
                    dst.put((byte)c);
                } else if (c < 0x800) {
                    // 2 bytes, 11 bits
                    if (dst.remaining() < 2)
                        return overflow(src, mark);
                    dst.put((byte)(0xc0 | (c >> 6)));
                    dst.put((byte)(0x80 | (c & 0x3f)));
                } else if (Character.isSurrogate(c)) {
                    // Have a surrogate pair
                    if (sgp == null)
                        sgp = new Surrogate.Parser();
                    int uc = sgp.parse(c, src);
                    if (uc < 0) {
                        src.position(mark);
                        return sgp.error();
                    }
                    if (dst.remaining() < 4)
                        return overflow(src, mark);
                    dst.put((byte)(0xf0 | ((uc >> 18))));
                    dst.put((byte)(0x80 | ((uc >> 12) & 0x3f)));
                    dst.put((byte)(0x80 | ((uc >>  6) & 0x3f)));
                    dst.put((byte)(0x80 | (uc & 0x3f)));
                    mark++;  // 2 chars
                } else {
                    // 3 bytes, 16 bits
                    if (dst.remaining() < 3)
                        return overflow(src, mark);
                    dst.put((byte)(0xe0 | ((c >> 12))));
                    dst.put((byte)(0x80 | ((c >>  6) & 0x3f)));
                    dst.put((byte)(0x80 | (c & 0x3f)));
                }
                mark++;
            }
            src.position(mark);
            return CoderResult.UNDERFLOW;
        }

        protected final CoderResult encodeLoop() {
            if (src.hasArray() && dst.hasArray())
                return encodeArrayLoop();
            else
                return encodeBufferLoop();
        }

    }

    private static class Decoder extends TextDecoder {

        private Decoder(TextCodec codec, ByteBuffer src, CharBuffer dst) {
            super(codec, src, dst);
        }

        private static boolean isNotContinuation(int b) {
            return (b & 0xc0) != 0x80;
        }

        //  [E0]     [A0..BF] [80..BF]
        //  [E1..EF] [80..BF] [80..BF]
        private static boolean isMalformed3(int b1, int b2, int b3) {
            return (b1 == (byte)0xe0 && (b2 & 0xe0) == 0x80) ||
                    (b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80;
        }

        // only used when there is only one byte left in src buffer
        private static boolean isMalformed3_2(int b1, int b2) {
            return (b1 == (byte)0xe0 && (b2 & 0xe0) == 0x80) ||
                    (b2 & 0xc0) != 0x80;
        }

        //  [F0]     [90..BF] [80..BF] [80..BF]
        //  [F1..F3] [80..BF] [80..BF] [80..BF]
        //  [F4]     [80..8F] [80..BF] [80..BF]
        //  only check 80-be range here, the [0xf0,0x80...] and [0xf4,0x90-...]
        //  will be checked by Character.isSupplementaryCodePoint(uc)
        private static boolean isMalformed4(int b2, int b3, int b4) {
            return (b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80 ||
                    (b4 & 0xc0) != 0x80;
        }

        // only used when there is less than 4 bytes left in src buffer.
        // both b1 and b2 should be "& 0xff" before passed in.
        private static boolean isMalformed4_2(int b1, int b2) {
            return (b1 == 0xf0 && (b2  < 0x90 || b2 > 0xbf)) ||
                    (b1 == 0xf4 && (b2 & 0xf0) != 0x80) ||
                    (b2 & 0xc0) != 0x80;
        }

        // tests if b1 and b2 are malformed as the first 2 bytes of a
        // legal`4-byte utf-8 byte sequence.
        // only used when there is less than 4 bytes left in src buffer,
        // after isMalformed4_2 has been invoked.
        private static boolean isMalformed4_3(int b3) {
            return (b3 & 0xc0) != 0x80;
        }

        private static CoderResult lookupN(ByteBuffer src, int n)
        {
            for (int i = 1; i < n; i++) {
                if (isNotContinuation(src.get()))
                    return CoderResult.malformedForLength(i);
            }
            return CoderResult.malformedForLength(n);
        }

        private static CoderResult malformedN(ByteBuffer src, int nb) {
            switch (nb) {
                case 1:
                case 2:                    // always 1
                    return CoderResult.malformedForLength(1);
                case 3:
                    int b1 = src.get();
                    int b2 = src.get();    // no need to lookup b3
                    return CoderResult.malformedForLength(
                            ((b1 == (byte)0xe0 && (b2 & 0xe0) == 0x80) ||
                                    isNotContinuation(b2)) ? 1 : 2);
                case 4:  // we don't care the speed here
                    b1 = src.get() & 0xff;
                    b2 = src.get() & 0xff;
                    if (b1 > 0xf4 ||
                            (b1 == 0xf0 && (b2 < 0x90 || b2 > 0xbf)) ||
                            (b1 == 0xf4 && (b2 & 0xf0) != 0x80) ||
                            isNotContinuation(b2))
                        return CoderResult.malformedForLength(1);
                    if (isNotContinuation(src.get()))
                        return CoderResult.malformedForLength(2);
                    return CoderResult.malformedForLength(3);
                default:
                    assert false;
                    return null;
            }
        }

        private static CoderResult malformed(ByteBuffer src, int sp,
                                                              CharBuffer dst, int dp,
                                                              int nb)
        {
            src.position(sp - src.arrayOffset());
            CoderResult cr = malformedN(src, nb);
            updatePositions(src, sp, dst, dp);
            return cr;
        }


        private static CoderResult malformed(ByteBuffer src, int mark, int nb)
        {
            src.position(mark);
            CoderResult cr = malformedN(src, nb);
            src.position(mark);
            return cr;
        }

        private static CoderResult malformedForLength(ByteBuffer src,
                                                                       int sp,
                                                                       CharBuffer dst,
                                                                       int dp,
                                                                       int malformedNB)
        {
            updatePositions(src, sp, dst, dp);
            return CoderResult.malformedForLength(malformedNB);
        }

        private static CoderResult malformedForLength(ByteBuffer src,
                                                                       int mark,
                                                                       int malformedNB)
        {
            src.position(mark);
            return CoderResult.malformedForLength(malformedNB);
        }


        private static CoderResult xflow(Buffer src, int sp, int sl,
                                                          Buffer dst, int dp, int nb) {
            updatePositions(src, sp, dst, dp);
            return (nb == 0 || sl - sp < nb)
                    ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
        }

        private static CoderResult xflow(Buffer src, int mark, int nb) {
            src.position(mark);
            return (nb == 0 || src.remaining() < nb)
                    ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
        }

        private CoderResult decodeArrayLoop() {
            // This method is optimized for ASCII input.
            byte[] sa = src.array();
            int sp = src.arrayOffset() + src.position();
            int sl = src.arrayOffset() + src.limit();

            char[] da = dst.array();
            int dp = dst.arrayOffset() + dst.position();
            int dl = dst.arrayOffset() + dst.limit();
            int dlASCII = dp + Math.min(sl - sp, dl - dp);

            // ASCII only loop
            while (dp < dlASCII && sa[sp] >= 0) {
                da[dp++] = (char) sa[sp++];
                if (countdownDecrementAndIsReached()) return stop(src, sp, sa[sp]);
                if (isEscape(sa[sp])) return escape(src, sp, sa[sp]);
            }
            while (sp < sl) {
                int b1 = sa[sp];
                if (b1 >= 0) {
                    // 1 byte, 7 bits: 0xxxxxxx
                    if (dp >= dl)
                        return xflow(src, sp, sl, dst, dp, 1);
                    da[dp++] = (char) b1;
                    sp++;
                    if (countdownDecrementAndIsReached()) return stop(src, sp, b1);
                    if (isEscape(b1)) return escape(src, sp, b1);
                } else if ((b1 >> 5) == -2 && (b1 & 0x1e) != 0) {
                    // 2 bytes, 11 bits: 110xxxxx 10xxxxxx
                    //                   [C2..DF] [80..BF]
                    if (sl - sp < 2 || dp >= dl)
                        return xflow(src, sp, sl, dst, dp, 2);
                    int b2 = sa[sp + 1];
                    // Now we check the first byte of 2-byte sequence as
                    //     if ((b1 >> 5) == -2 && (b1 & 0x1e) != 0)
                    // no longer need to check b1 against c1 & c0 for
                    // malformed as we did in previous version
                    //   (b1 & 0x1e) == 0x0 || (b2 & 0xc0) != 0x80;
                    // only need to check the second byte b2.
                    if (isNotContinuation(b2))
                        return malformedForLength(src, sp, dst, dp, 1);
                    char c = (char)(((b1 << 6) ^ b2)
                            ^
                            (((byte) 0xC0 << 6) ^
                                    ((byte) 0x80 << 0)));
                    da[dp++] = c;
                    sp += 2;
                    if (countdownDecrementAndIsReached()) return stop(sp);
                    if (isEscape(c)) return escape(src, sp, c);
                } else if ((b1 >> 4) == -2) {
                    // 3 bytes, 16 bits: 1110xxxx 10xxxxxx 10xxxxxx
                    int srcRemaining = sl - sp;
                    if (srcRemaining < 3 || dp >= dl) {
                        if (srcRemaining > 1 && isMalformed3_2(b1, sa[sp + 1]))
                            return malformedForLength(src, sp, dst, dp, 1);
                        return xflow(src, sp, sl, dst, dp, 3);
                    }
                    int b2 = sa[sp + 1];
                    int b3 = sa[sp + 2];
                    if (isMalformed3(b1, b2, b3))
                        return malformed(src, sp, dst, dp, 3);
                    char c = (char)
                            ((b1 << 12) ^
                                    (b2 <<  6) ^
                                    (b3 ^
                                            (((byte) 0xE0 << 12) ^
                                                    ((byte) 0x80 <<  6) ^
                                                    ((byte) 0x80 <<  0))));
                    if (Character.isSurrogate(c))
                        return malformedForLength(src, sp, dst, dp, 3);
                    da[dp++] = c;
                    sp += 3;
                    if (countdownDecrementAndIsReached()) return stop(src, sp, c);
                    if (isEscape(c)) return escape(src, sp, c);
                } else if ((b1 >> 3) == -2) {
                    // 4 bytes, 21 bits: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                    int srcRemaining = sl - sp;
                    if (srcRemaining < 4 || dl - dp < 2) {
                        b1 &= 0xff;
                        if (b1 > 0xf4 ||
                                srcRemaining > 1 && isMalformed4_2(b1, sa[sp + 1] & 0xff))
                            return malformedForLength(src, sp, dst, dp, 1);
                        if (srcRemaining > 2 && isMalformed4_3(sa[sp + 2]))
                            return malformedForLength(src, sp, dst, dp, 2);
                        return xflow(src, sp, sl, dst, dp, 4);
                    }
                    int b2 = sa[sp + 1];
                    int b3 = sa[sp + 2];
                    int b4 = sa[sp + 3];
                    int uc = ((b1 << 18) ^
                            (b2 << 12) ^
                            (b3 <<  6) ^
                            (b4 ^
                                    (((byte) 0xF0 << 18) ^
                                            ((byte) 0x80 << 12) ^
                                            ((byte) 0x80 <<  6) ^
                                            ((byte) 0x80 <<  0))));
                    if (isMalformed4(b2, b3, b4) ||
                            // shortest form check
                            !Character.isSupplementaryCodePoint(uc)) {
                        return malformed(src, sp, dst, dp, 4);
                    }
                    da[dp++] = Character.highSurrogate(uc);
                    da[dp++] = Character.lowSurrogate(uc);
                    sp += 4;
                    if (countdownDecrementAndIsReached()) return stop(src, sp, uc);
                    if (isEscape(uc)) return escape(src, sp, uc);
                } else
                    return malformed(src, sp, dst, dp, 1);
            }
            return xflow(src, sp, sl, dst, dp, 0);
        }

        private CoderResult decodeBufferLoop() {
            int mark = src.position();
            int limit = src.limit();
            while (mark < limit) {
                int b1 = src.get();
                if (b1 >= 0) {
                    // 1 byte, 7 bits: 0xxxxxxx
                    if (dst.remaining() < 1)
                        return xflow(src, mark, 1); // overflow
                    dst.put((char) b1);
                    mark++;
                    if (countdownDecrementAndIsReached()) return stop(b1);
                    if (isEscape(b1)) return escape(b1);
                } else if ((b1 >> 5) == -2 && (b1 & 0x1e) != 0) {
                    // 2 bytes, 11 bits: 110xxxxx 10xxxxxx
                    if (limit - mark < 2|| dst.remaining() < 1)
                        return xflow(src, mark, 2);
                    int b2 = src.get();
                    if (isNotContinuation(b2))
                        return malformedForLength(src, mark, 1);
                    char c = ((char) (((b1 << 6) ^ b2)
                            ^
                            (((byte) 0xC0 << 6) ^
                                    ((byte) 0x80 << 0))));
                    dst.put(c);
                    mark += 2;
                    if (countdownDecrementAndIsReached()) return stop(c);
                    if (isEscape(c)) return escape(c);
                } else if ((b1 >> 4) == -2) {
                    // 3 bytes, 16 bits: 1110xxxx 10xxxxxx 10xxxxxx
                    int srcRemaining = limit - mark;
                    if (srcRemaining < 3 || dst.remaining() < 1) {
                        if (srcRemaining > 1 && isMalformed3_2(b1, src.get()))
                            return malformedForLength(src, mark, 1);
                        return xflow(src, mark, 3);
                    }
                    int b2 = src.get();
                    int b3 = src.get();
                    if (isMalformed3(b1, b2, b3))
                        return malformed(src, mark, 3);
                    char c = (char)
                            ((b1 << 12) ^
                                    (b2 <<  6) ^
                                    (b3 ^
                                            (((byte) 0xE0 << 12) ^
                                                    ((byte) 0x80 <<  6) ^
                                                    ((byte) 0x80 <<  0))));
                    if (Character.isSurrogate(c))
                        return malformedForLength(src, mark, 3);
                    dst.put(c);
                    mark += 3;
                    if (countdownDecrementAndIsReached()) return stop(c);
                    if (isEscape(c)) return escape(c);
                } else if ((b1 >> 3) == -2) {
                    // 4 bytes, 21 bits: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                    int srcRemaining = limit - mark;
                    if (srcRemaining < 4 || dst.remaining() < 2) {
                        b1 &= 0xff;
                        if (b1 > 0xf4 ||
                                srcRemaining > 1 && isMalformed4_2(b1, src.get() & 0xff))
                            return malformedForLength(src, mark, 1);
                        if (srcRemaining > 2 && isMalformed4_3(src.get()))
                            return malformedForLength(src, mark, 2);
                        return xflow(src, mark, 4);
                    }
                    int b2 = src.get();
                    int b3 = src.get();
                    int b4 = src.get();
                    int uc = ((b1 << 18) ^
                            (b2 << 12) ^
                            (b3 <<  6) ^
                            (b4 ^
                                    (((byte) 0xF0 << 18) ^
                                            ((byte) 0x80 << 12) ^
                                            ((byte) 0x80 <<  6) ^
                                            ((byte) 0x80 <<  0))));
                    if (isMalformed4(b2, b3, b4) ||
                            // shortest form check
                            !Character.isSupplementaryCodePoint(uc)) {
                        return malformed(src, mark, 4);
                    }
                    dst.put(Character.highSurrogate(uc));
                    dst.put(Character.lowSurrogate(uc));
                    mark += 4;
                    if (countdownDecrementAndIsReached()) return stop(uc);
                    if (isEscape(uc)) return escape(uc);
                } else {
                    return malformed(src, mark, 1);
                }
            }
            return xflow(src, mark, 0);
        }

        protected CoderResult decodeLoop()
        {
            if (src.hasArray() && dst.hasArray())
                return decodeArrayLoop();
            else
                return decodeBufferLoop();
        }
    }

}
