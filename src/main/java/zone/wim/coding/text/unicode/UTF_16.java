package zone.wim.coding.text.unicode;
//
//import zone.wim.coding.CoderResult;
//import zone.wim.coding.text.TextCodec;
//import zone.wim.coding.text.TextDecoder;
//import zone.wim.coding.text.TextEncoder;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.CharBuffer;


import io.netty.buffer.ByteBuf;
import zone.wim.coding.text.TextDecoder;
import zone.wim.coding.text.TextEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public class UTF_16 extends UnicodeCodec {

//    protected static final char BYTE_ORDER_MARK = '\uFEFF';
//    protected static final char REVERSED_MARK = '\uFFFE';

    public static String CANONICAL_NAME = "UTF-16";
    public static String[] ALIASES = {"utf16", "utf-16"};
    public static float AVERAGE_BYTES_PER_CHAR = 2.0f;
    public static float MAX_BYTES_PER_CHAR = 4.0f;
    public static float AVERAGE_CHARS_PER_BYTE = 0.6f;
    public static float MAX_CHARS_PER_BYTE = 0.5f;

    public String canonicalName() { return CANONICAL_NAME; }
    public String[] aliases() { return ALIASES; }
    public float averageBytesPerChar() { return AVERAGE_BYTES_PER_CHAR; }
    public float maxBytesPerChar() { return MAX_BYTES_PER_CHAR; }
    public float averageCharsPerByte() { return AVERAGE_CHARS_PER_BYTE; }
    public float maxCharsPerByte() { return MAX_CHARS_PER_BYTE; }

    public boolean byteOrderSensitive() { return true; }

    @Override
    public TextDecoder decoder(ByteBuf src, CharBuffer dst) {
        return null;
    }

    @Override
    public TextEncoder encoder(CharBuffer src, ByteBuf dst) {
        return null;
    }

    public UTF_16(ByteOrder byteOrder) {
        super(byteOrder);
    }

}

//public class UTF_16 extends UnicodeCodec {
//    private static class Encoder extends TextEncoder {
//

//
//        private boolean usesMark;   /* Write an initial BOM */
//        private boolean needsMark;
//
//        private Encoder(TextCodec tc, byte[] replacement) {
//            super(tc, replacement);
////            usesMark = needsMark = m;
////            byteOrder = bo;
//
//        }
//
//        private void put(char c, ByteBuffer dst) {
//            if (codec.byteOrder() == ByteOrder.BIG_ENDIAN) {
//                dst.put((byte)(c >> 8));
//                dst.put((byte)(c & 0xff));
//            } else {
//                dst.put((byte)(c & 0xff));
//                dst.put((byte)(c >> 8));
//            }
//        }
//
//        private final Surrogate.Parser sgp = new Surrogate.Parser();
//
//        protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
//            int mark = src.position();
//
//            if (needsMark && src.hasRemaining()) {
//                if (dst.remaining() < 2)
//                    return CoderResult.OVERFLOW;
//                put(BYTE_ORDER_MARK, dst);
//                needsMark = false;
//            }
//            try {
//                while (src.hasRemaining()) {
//                    char c = src.get();
//                    if (!Character.isSurrogate(c)) {
//                        if (dst.remaining() < 2)
//                            return CoderResult.OVERFLOW;
//                        mark++;
//                        put(c, dst);
//                        continue;
//                    }
//                    int d = sgp.parse(c, src);
//                    if (d < 0)
//                        return sgp.error();
//                    if (dst.remaining() < 4)
//                        return CoderResult.OVERFLOW;
//                    mark += 2;
//                    put(Character.highSurrogate(d), dst);
//                    put(Character.lowSurrogate(d), dst);
//                }
//                return CoderResult.UNDERFLOW;
//            } finally {
//                src.position(mark);
//            }
//        }
//
//        protected void implReset() {
//            needsMark = usesMark;
//        }
//
//        public boolean canEncode(char c) {
//            return ! Character.isSurrogate(c);
//        }
//    }
//
//    private static class Decoder extends TextDecoder {
//
//        private ByteOrder expectedByteOrder;
//        private ByteOrder defaultByteOrder = ByteOrder.BIG_ENDIAN;
//
//        private Decoder(TextCodec cs, String replacement) {
//            super(cs, replacement);
//            expectedByteOrder = codec.byteOrder();
//
//        }
//
//        private char decode(int b1, int b2) {
//            if (codec.byteOrder() == ByteOrder.BIG_ENDIAN)
//                return (char)((b1 << 8) | b2);
//            else
//                return (char)((b2 << 8) | b1);
//        }
//
//        protected CoderResult decodeLoop(ByteBuffer src, CharBuffer dst) {
//            int mark = src.position();
//
//            try {
//                while (src.remaining() > 1) {
//                    int b1 = src.get() & 0xff;
//                    int b2 = src.get() & 0xff;
//
//                    // Byte Order Mark interpretation
//                    if (codec.byteOrder() == null) {
//                        char c = (char)((b1 << 8) | b2);
//                        if (c == BYTE_ORDER_MARK_CHAR) {
//                            codec.byteOrder(ByteOrder.BIG_ENDIAN);
//                            mark += 2;
//                            continue;
//                        } else if (c == REVERSED_MARK_CHAR) {
//                            codec.byteOrder(ByteOrder.LITTLE_ENDIAN);
//                            mark += 2;
//                            continue;
//                        } else {
//                            codec.byteOrder(ByteOrder.nativeOrder());
//                            // FALL THROUGH to process b1, b2 normally
//                        }
//                    }
//
//                    char c = decode(b1, b2);
//
//                    // Surrogates
//                    if (Character.isSurrogate(c)) {
//                        if (Character.isHighSurrogate(c)) {
//                            if (src.remaining() < 2)
//                                return CoderResult.UNDERFLOW;
//                            char c2 = decode(src.get() & 0xff, src.get() & 0xff);
//                            if (!Character.isLowSurrogate(c2))
//                                return CoderResult.malformedForLength(4);
//                            if (dst.remaining() < 2)
//                                return CoderResult.OVERFLOW;
//                            mark += 4;
//                            dst.put(c);
//                            dst.put(c2);
//                            continue;
//                        }
//                        // Unpaired low surrogate
//                        return CoderResult.malformedForLength(2);
//                    }
//
//                    if (!dst.hasRemaining())
//                        return CoderResult.OVERFLOW;
//                    mark += 2;
//                    dst.put(c);
//
//                }
//                return CoderResult.UNDERFLOW;
//
//            } finally {
//                src.position(mark);
//            }
//        }
//
//        protected void implReset() {
//            codec.byteOrder(expectedByteOrder);
//        }
//
//
//    }
//
//}
