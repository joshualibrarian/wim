package zone.wim.codec.text.unicode;

import zone.wim.codec.text.TextCodec;
import java.nio.ByteOrder;

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

    protected UnicodeCodec() { super(null); }
    protected UnicodeCodec(ByteOrder byteOrder) {
        super(byteOrder);
    }

}
