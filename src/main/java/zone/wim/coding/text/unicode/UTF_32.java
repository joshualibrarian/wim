package zone.wim.coding.text.unicode;

import zone.wim.coding.Decoder;
import zone.wim.coding.Encoder;
import zone.wim.coding.text.TextDecoder;
import zone.wim.coding.text.TextEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

public class UTF_32 extends UnicodeCodec {

    public static String CANONICAL_NAME = "UTF-32";
    public static String[] ALIASES = {"utf32", "utf-32"};
    public static float AVERAGE_BYTES_PER_CHAR = 4.0f;
    public static float MAX_BYTES_PER_CHAR = 4.0f;
    public static float AVERAGE_CHARS_PER_BYTE = 0.25f;
    public static float MAX_CHARS_PER_BYTE = 0.25f;

    public String canonicalName() { return CANONICAL_NAME; }
    public String[] aliases() { return ALIASES; }
    public float averageBytesPerChar() { return AVERAGE_BYTES_PER_CHAR; }
    public float maxBytesPerChar() { return MAX_BYTES_PER_CHAR; }
    public float averageCharsPerByte() { return AVERAGE_CHARS_PER_BYTE; }
    public float maxCharsPerByte() { return MAX_CHARS_PER_BYTE; }

    public boolean byteOrderSensitive() { return true; }

    public UTF_32(ByteOrder byteOrder) {
        super(byteOrder);
    }

    @Override
    public TextDecoder decoder(ByteBuffer src, CharBuffer dst) {
        return null;
    }

    @Override
    public TextEncoder encoder(CharBuffer src, ByteBuffer dst) {
        return null;
    }

}
