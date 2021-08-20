package zone.wim.codec.text.unicode;

import zone.wim.codec.Decoder;
import zone.wim.codec.Encoder;

import java.nio.ByteOrder;

public class UTF_32 extends UnicodeCodec {

    public static String CANONICAL_NAME = "utf-16";
    public static String[] ALIASES = {"UTF-16", "utf16"};
    public static float AVERAGE_BYTES_PER_CHAR = 4.0f;
    public static float MAX_BYTES_PER_CHAR = 4.0f;
    public static float AVERAGE_CHARS_PER_BYTE = 0.25f;
    public static float MAX_CHARS_PER_BYTE = 0.25f;

    public String canonicalName() {
        return CANONICAL_NAME;
    }
    public String[] aliases() { return ALIASES; }
    public float averageBytesPerChar() { return AVERAGE_BYTES_PER_CHAR; }
    public float maxBytesPerChar() { return MAX_BYTES_PER_CHAR; }
    public float averageCharsPerByte() { return AVERAGE_CHARS_PER_BYTE; }
    public float maxCharsPerByte() { return MAX_CHARS_PER_BYTE; }

    public UTF_32(ByteOrder byteOrder) {
        super(byteOrder);
    }


    @Override
    public boolean byteOrderSensitive() { return true; }

    @Override
    public Decoder decoder() {
        return null;
    }

    @Override
    public Encoder encoder() {
        return null;
    }

}
