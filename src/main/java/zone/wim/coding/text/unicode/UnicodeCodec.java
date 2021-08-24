package zone.wim.coding.text.unicode;

import io.netty.buffer.ByteBuf;
import zone.wim.coding.Codec;
import zone.wim.coding.CoderResult;
import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.text.TextCodec;
import zone.wim.coding.text.TextDecoder;

import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public abstract class UnicodeCodec extends TextCodec {

    public static byte[] DEFAULT_REPLACEMENT_BYTES =
            new byte[] { (byte)0xEF, (byte)0xBF, (byte)0xBD };
    public static String DEFAULT_REPLACEMENT_CHARS = "\uFFFD";

    public static String BYTE_ORDER_MARK_STRING = "\uFEFF";
    protected static final char BYTE_ORDER_MARK_CHAR = (char) 0xfeff;
    protected static final char REVERSED_MARK_CHAR = (char) 0xfffe;

    public static UnicodeCodec UTF_8 = new UTF_8();
    public static UnicodeCodec UTF_16LE = new UTF_16(ByteOrder.LITTLE_ENDIAN);
    public static UnicodeCodec UTF_16BE = new UTF_16(ByteOrder.BIG_ENDIAN);
    public static UnicodeCodec UTF_32LE = new UTF_32(ByteOrder.LITTLE_ENDIAN);
    public static UnicodeCodec UTF_32BE = new UTF_32(ByteOrder.BIG_ENDIAN);
    public static UnicodeCodec UTF_7 = new UTF_7();

    public String defaultReplacement() { return DEFAULT_REPLACEMENT_CHARS; }

    public Charset charset() {
        String charsetName = canonicalName();
        if (byteOrder() == ByteOrder.BIG_ENDIAN) {
            charsetName.concat("BE");
        } else if (byteOrder() == ByteOrder.LITTLE_ENDIAN) {
            charsetName.concat("LE");
        }
        return Charset.forName(charsetName);
    }

    /** Can be used in the decode loops of implementations to
     * check if a given character has been set as an escape character
     *
     * @param codepoint	character to test
     * @return true only if given character is an escape
     */

    protected UnicodeCodec() { super(null); }
    protected UnicodeCodec(ByteOrder byteOrder) {
        super(byteOrder);
    }


    /**
     * This method examines the current location in the buffer for a Byte Order Mark (BOM)
     * in any supported encoding.  If one is found, then the correct decoder is returned.
     * If none is found, the buffer is reset and the default of UTF-8 decoder is used.
     *
     * @return CharacterDecoder
     */
    @Override
    public Codec detectEncoding(ByteBuf bytes) {
        TextCodec charset = null;
        bytes.markReaderIndex(); 		// mark the first byte so we can reset if no BOM found
        int[] b = new int[5];
        b[0] = bytes.readByte() & 0xFF;
        b[1] = bytes.readByte() & 0xFF;
        if (b[0] == 0xFE && b[1] == 0xFF) {			// UTF-16BE
            charset = UTF_16BE;
        } else if (b[0] == 0xFF && b[1] == 0xFE) {	// could be UTF-16LE or UTF-32LE
            bytes.markReaderIndex();
            b[2] = bytes.readByte() & 0xFF;
            b[3] = bytes.readByte() & 0xFF;

            if (b[2] == 0x00 && b[3] == 0x00) {		// UTF-32LE
                charset = UTF_32LE;
            } else {										// UTF-16LE
                bytes.resetReaderIndex();	// set the buffer position to after the actual BOM
                charset = UTF_16LE;
            }
        } else if (b[0] == 0x00 && b[1] == 0x00) {	// possibly UTF-32BE
            b[2] = bytes.readByte() & 0xFF;
            b[3] = bytes.readByte() & 0xFF;

            if (b[2] == 0xFE && b[3] == 0xFF) {
                charset = UTF_32BE;
            } else {
                // TODO: throw MisplacedNullException (or some such)?
            }
        } else if (b[0] == 0xEF && b[1] == 0xBB) {	// possibly UTF-8 BOM
            b[3] = bytes.readByte() & 0xFF;

            if (b[3] == 0xBF) {
                charset = UTF_8;
            }
        } else if (b[0] == 0x2B && b[1] == 0x2F) { // possibly UTF-7
            b[2] = bytes.readByte() & 0xFF;

            if (b[2] == 0x76) {
                b[3] = bytes.readByte() & 0xFF;

                if (b[3] == 0x38 || b[3] == 0x39 || b[3] == 0x2B || b[3] == 0x2F) {
                    charset = UTF_7;
                } else if (b[3] == 0x38) {
                    b[4] = bytes.readByte() & 0xFF;

                    if (b[4] == 0x2D) {
                        charset = UTF_7;
                    }
                }
            }
        }
        if (charset == null) {	// there is no BOM here
            bytes.resetReaderIndex();
        }
        charset = new UTF_8();
        return charset;
    }

    static interface UConverterConstants {

        static final short UNSIGNED_BYTE_MASK = 0xff;
        static final int UNSIGNED_SHORT_MASK = 0xffff;
        static final long UNSIGNED_INT_MASK = 0xffffffffL;

        static final int U_IS_BIG_ENDIAN = 0;

        /**
         * Useful constant for the maximum size of the whole locale ID
         * (including the terminating NULL).
         */
        static final int ULOC_FULLNAME_CAPACITY = 56;

        /**
         * This value is intended for sentinel values for APIs that
         * (take or) return single code points (UChar32).
         * It is outside of the Unicode code point range 0..0x10ffff.
         *
         * For example, a "done" or "error" value in a new API
         * could be indicated with U_SENTINEL.
         *
         * ICU APIs designed before ICU 2.4 usually define service-specific "done"
         * values, mostly 0xffff.
         * Those may need to be distinguished from
         * actual U+ffff text contents by calling functions like
         * CharacterIterator::hasNext() or UnicodeString::length().
         */
        static final int U_SENTINEL = -1;

        //end utf.h

        //begin ucnv.h
        /**
         * Character that separates converter names from options and options from each other.
         * @see CharsetICU#forNameICU(String)
         */
        static final byte OPTION_SEP_CHAR  = ',';

        /** Maximum length of a converter name including the terminating NULL */
        static final int MAX_CONVERTER_NAME_LENGTH  = 60;
        /** Maximum length of a converter name including path and terminating NULL */
        static final int MAX_FULL_FILE_NAME_LENGTH = (600+MAX_CONVERTER_NAME_LENGTH);

        /** Shift in for EBDCDIC_STATEFUL and iso2022 states */
        static final int SI = 0x0F;
        /** Shift out for EBDCDIC_STATEFUL and iso2022 states */
        static final int SO = 0x0E;

        //end ucnv.h

        // begin bld.h
        /* size of the overflow buffers in UConverter, enough for escaping callbacks */
        //#define ERROR_BUFFER_LENGTH 32
        static final int ERROR_BUFFER_LENGTH = 32;

        /* at most 4 bytes per substitution character (part of .cnv file format! see UConverterStaticData) */
        static final int MAX_SUBCHAR_LEN = 4;

        /* at most 8 bytes per character in toUBytes[] (UTF-8 uses up to 6) */
        static final int MAX_CHAR_LEN = 8;

        /* converter options bits */
        static final int OPTION_VERSION     = 0xf;
        static final int OPTION_SWAP_LFNL   = 0x10;
        static final int OPTION_MAC   = 0x20; //agljport:comment added for Mac ISCII encodings

        static final String OPTION_SWAP_LFNL_STRING = ",swaplfnl";

        /** values for the unicodeMask */
        static final int HAS_SUPPLEMENTARY = 1;
        static final int HAS_SURROGATES =   2;
        // end bld.h

        // begin cnv.h
        /* this is used in fromUnicode DBCS tables as an "unassigned" marker */
        static final int missingCharMarker = 0xFFFF;
        /**
         *
         * @author ram
         */
        static interface UConverterResetChoice {
            static final int RESET_BOTH = 0;
            static final int RESET_TO_UNICODE = RESET_BOTH + 1;
            static final int RESET_FROM_UNICODE = RESET_TO_UNICODE + 1;
        }

        // begin utf16.h
        /**
         * The maximum number of 16-bit code units per Unicode code point (U+0000..U+10ffff).
         */
        static final int U16_MAX_LENGTH = 2;
        // end utf16.h

        // begin err.h
        /**
         * FROM_U, TO_U context options for sub callback
         */
        static byte[] SUB_STOP_ON_ILLEGAL = {'i'};

        /**
         * FROM_U, TO_U context options for skip callback
         */
        static byte[] SKIP_STOP_ON_ILLEGAL = {'i'};

        /**
         * The process condition code to be used with the callbacks.
         * Codes which are greater than IRREGULAR should be
         * passed on to any chained callbacks.
         */
        static interface UConverterCallbackReason {
            static final int UNASSIGNED = 0;  /**< The code point is unassigned.
             The error code U_INVALID_CHAR_FOUND will be set. */
            static final int ILLEGAL = 1;     /**< The code point is illegal. For example,
             \\x81\\x2E is illegal in SJIS because \\x2E
             is not a valid trail byte for the \\x81
             lead byte.
             Also, starting with Unicode 3.0.1, non-shortest byte sequences
             in UTF-8 (like \\xC1\\xA1 instead of \\x61 for U+0061)
             are also illegal, not just irregular.
             The error code U_ILLEGAL_CHAR_FOUND will be set. */
            static final int IRREGULAR = 2;   /**< The codepoint is not a regular sequence in
             the encoding. For example, \\xED\\xA0\\x80..\\xED\\xBF\\xBF
             are irregular UTF-8 byte sequences for single surrogate
             code points.
             The error code U_INVALID_CHAR_FOUND will be set. */
            static final int RESET = 3;       /**< The callback is called with this reason when a
             'reset' has occurred. Callback should reset all
             state. */
            static final int CLOSE = 4;        /**< Called when the converter is closed. The
             callback should release any allocated memory.*/
            static final int CLONE = 5;         /**< Called when safeClone() is called on the
             converter. the pointer available as the
             'context' is an alias to the original converters'
             context pointer. If the context must be owned
             by the new converter, the callback must clone
             the data and call setFromUCallback
             (or setToUCallback) with the correct pointer.
             */
        }
        //end err.h


        static final String DATA_TYPE = "cnv";
        static final int CNV_DATA_BUFFER_SIZE = 25000;
        static final int SIZE_OF_UCONVERTER_SHARED_DATA = 100;

        static final int MAXIMUM_UCS2 =            0x0000FFFF;
        static final int MAXIMUM_UTF =             0x0010FFFF;
        //static final int MAXIMUM_UCS4 =            0x7FFFFFFF;
        static final int HALF_SHIFT =              10;
        static final int HALF_BASE =               0x0010000;
        static final int HALF_MASK =               0x3FF;
        static final int SURROGATE_HIGH_START =    0xD800;
        static final int SURROGATE_HIGH_END =      0xDBFF;
        static final int SURROGATE_LOW_START =     0xDC00;
        static final int SURROGATE_LOW_END =       0xDFFF;

        /* -SURROGATE_LOW_START + HALF_BASE */
        static final int SURROGATE_LOW_BASE =      9216;
    }

    public static class Surrogate {

        private Surrogate() { }

        // TODO: Deprecate/remove the following redundant definitions
        public static final char MIN_HIGH = Character.MIN_HIGH_SURROGATE;
        public static final char MAX_HIGH = Character.MAX_HIGH_SURROGATE;
        public static final char MIN_LOW  = Character.MIN_LOW_SURROGATE;
        public static final char MAX_LOW  = Character.MAX_LOW_SURROGATE;
        public static final char MIN      = Character.MIN_SURROGATE;
        public static final char MAX      = Character.MAX_SURROGATE;
        public static final int UCS4_MIN  = Character.MIN_SUPPLEMENTARY_CODE_POINT;
        public static final int UCS4_MAX  = Character.MAX_CODE_POINT;

        /**
         * Tells whether or not the given value is in the high surrogate range.
         * Use of {@link Character#isHighSurrogate} is generally preferred.
         */
        public static boolean isHigh(int c) {
            return (MIN_HIGH <= c) && (c <= MAX_HIGH);
        }

        /**
         * Tells whether or not the given value is in the low surrogate range.
         * Use of {@link Character#isLowSurrogate} is generally preferred.
         */
        public static boolean isLow(int c) {
            return (MIN_LOW <= c) && (c <= MAX_LOW);
        }

        /**
         * Tells whether or not the given value is in the surrogate range.
         * Use of {@link Character#isSurrogate} is generally preferred.
         */
        public static boolean is(int c) {
            return (MIN <= c) && (c <= MAX);
        }

        /**
         * Tells whether or not the given UCS-4 character must be represented as a
         * surrogate pair in UTF-16.
         * Use of {@link Character#isSupplementaryCodePoint} is generally preferred.
         */
        public static boolean neededFor(int uc) {
            return Character.isSupplementaryCodePoint(uc);
        }

        /**
         * Returns the high UTF-16 surrogate for the given supplementary UCS-4 character.
         * Use of {@link Character#highSurrogate} is generally preferred.
         */
        public static char high(int uc) {
            assert Character.isSupplementaryCodePoint(uc);
            return Character.highSurrogate(uc);
        }

        /**
         * Returns the low UTF-16 surrogate for the given supplementary UCS-4 character.
         * Use of {@link Character#lowSurrogate} is generally preferred.
         */
        public static char low(int uc) {
            assert Character.isSupplementaryCodePoint(uc);
            return Character.lowSurrogate(uc);
        }

        /**
         * Converts the given surrogate pair into a 32-bit UCS-4 character.
         * Use of {@link Character#toCodePoint} is generally preferred.
         */
        public static int toUCS4(char c, char d) {
            assert Character.isHighSurrogate(c) && Character.isLowSurrogate(d);
            return Character.toCodePoint(c, d);
        }

        /**
         * Surrogate parsing support.  Charset implementations may use instances of
         * this class to handle the details of parsing UTF-16 surrogate pairs.
         */
        public static class Parser {

            public Parser() { }

            private int character;          // UCS-4
            private CoderResult error = CoderResult.UNDERFLOW;
            private boolean isPair;

            /**
             * Returns the UCS-4 character previously parsed.
             */
            public int character() {
                assert (error == null);
                return character;
            }

            /**
             * Tells whether or not the previously-parsed UCS-4 character was
             * originally represented by a surrogate pair.
             */
            public boolean isPair() {
                assert (error == null);
                return isPair;
            }

            /**
             * Returns the number of UTF-16 characters consumed by the previous
             * parse.
             */
            public int increment() {
                assert (error == null);
                return isPair ? 2 : 1;
            }

            /**
             * If the previous parse operation detected an error, return the object
             * describing that error.
             */
            public CoderResult error() {
                assert (error != null);
                return error;
            }

            /**
             * Returns an unmappable-input result object, with the appropriate
             * input length, for the previously-parsed character.
             */
            public CoderResult unmappableResult() {
                assert (error == null);
                return CoderResult.unmappableForLength(isPair ? 2 : 1);
            }

            /**
             * Parses a UCS-4 character from the given source buffer, handling
             * surrogates.
             *
             * @param  c    The first character
             * @param  in   The source buffer, from which one more character
             *              will be consumed if c is a high surrogate
             *
             * @return  Either a parsed UCS-4 character, in which case the isPair()
             *          and increment() methods will return meaningful values, or
             *          -1, in which case error() will return a descriptive result
             *          object
             */
            public int parse(char c, CharBuffer in) {
                if (Character.isHighSurrogate(c)) {
                    if (!in.hasRemaining()) {
                        error = CoderResult.UNDERFLOW;
                        return -1;
                    }
                    char d = in.get();
                    if (Character.isLowSurrogate(d)) {
                        character = Character.toCodePoint(c, d);
                        isPair = true;
                        error = null;
                        return character;
                    }
                    error = CoderResult.malformedForLength(1);
                    return -1;
                }
                if (Character.isLowSurrogate(c)) {
                    error = CoderResult.malformedForLength(1);
                    return -1;
                }
                character = c;
                isPair = false;
                error = null;
                return character;
            }

            /**
             * Parses a UCS-4 character from the given source buffer, handling
             * surrogates.
             *
             * @param  c    The first character
             * @param  ia   The input array, from which one more character
             *              will be consumed if c is a high surrogate
             * @param  ip   The input index
             * @param  il   The input limit
             *
             * @return  Either a parsed UCS-4 character, in which case the isPair()
             *          and increment() methods will return meaningful values, or
             *          -1, in which case error() will return a descriptive result
             *          object
             */
            public int parse(char c, char[] ia, int ip, int il) {
                assert (ia[ip] == c);
                if (Character.isHighSurrogate(c)) {
                    if (il - ip < 2) {
                        error = CoderResult.UNDERFLOW;
                        return -1;
                    }
                    char d = ia[ip + 1];
                    if (Character.isLowSurrogate(d)) {
                        character = Character.toCodePoint(c, d);
                        isPair = true;
                        error = null;
                        return character;
                    }
                    error = CoderResult.malformedForLength(1);
                    return -1;
                }
                if (Character.isLowSurrogate(c)) {
                    error = CoderResult.malformedForLength(1);
                    return -1;
                }
                character = c;
                isPair = false;
                error = null;
                return character;
            }

        }

        /**
         * Surrogate generation support.  Charset implementations may use instances
         * of this class to handle the details of generating UTF-16 surrogate
         * pairs.
         */
        public static class Generator {

            public Generator() { }

            private CoderResult error = CoderResult.OVERFLOW;

            /**
             * If the previous generation operation detected an error, return the
             * object describing that error.
             */
            public CoderResult error() {
                assert error != null;
                return error;
            }

            /**
             * Generates one or two UTF-16 characters to represent the given UCS-4
             * character.
             *
             * @param  uc   The UCS-4 character
             * @param  len  The number of input bytes from which the UCS-4 value
             *              was constructed (used when creating result objects)
             * @param  dst  The destination buffer, to which one or two UTF-16
             *              characters will be written
             *
             * @return  Either a positive count of the number of UTF-16 characters
             *          written to the destination buffer, or -1, in which case
             *          error() will return a descriptive result object
             */
            public int generate(int uc, int len, CharBuffer dst) {
                if (Character.isBmpCodePoint(uc)) {
                    char c = (char) uc;
                    if (Character.isSurrogate(c)) {
                        error = CoderResult.malformedForLength(len);
                        return -1;
                    }
                    if (dst.remaining() < 1) {
                        error = CoderResult.OVERFLOW;
                        return -1;
                    }
                    dst.put(c);
                    error = null;
                    return 1;
                } else if (Character.isValidCodePoint(uc)) {
                    if (dst.remaining() < 2) {
                        error = CoderResult.OVERFLOW;
                        return -1;
                    }
                    dst.put(Character.highSurrogate(uc));
                    dst.put(Character.lowSurrogate(uc));
                    error = null;
                    return 2;
                } else {
                    error = CoderResult.unmappableForLength(len);
                    return -1;
                }
            }

            /**
             * Generates one or two UTF-16 characters to represent the given UCS-4
             * character.
             *
             * @param  uc   The UCS-4 character
             * @param  len  The number of input bytes from which the UCS-4 value
             *              was constructed (used when creating result objects)
             * @param  da   The destination array, to which one or two UTF-16
             *              characters will be written
             * @param  dp   The destination position
             * @param  dl   The destination limit
             *
             * @return  Either a positive count of the number of UTF-16 characters
             *          written to the destination buffer, or -1, in which case
             *          error() will return a descriptive result object
             */
            public int generate(int uc, int len, char[] da, int dp, int dl) {
                if (Character.isBmpCodePoint(uc)) {
                    char c = (char) uc;
                    if (Character.isSurrogate(c)) {
                        error = CoderResult.malformedForLength(len);
                        return -1;
                    }
                    if (dl - dp < 1) {
                        error = CoderResult.OVERFLOW;
                        return -1;
                    }
                    da[dp] = c;
                    error = null;
                    return 1;
                } else if (Character.isValidCodePoint(uc)) {
                    if (dl - dp < 2) {
                        error = CoderResult.OVERFLOW;
                        return -1;
                    }
                    da[dp] = Character.highSurrogate(uc);
                    da[dp + 1] = Character.lowSurrogate(uc);
                    error = null;
                    return 2;
                } else {
                    error = CoderResult.unmappableForLength(len);
                    return -1;
                }
            }
        }

    }
}
