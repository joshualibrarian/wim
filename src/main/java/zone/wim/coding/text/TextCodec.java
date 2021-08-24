package zone.wim.coding.text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;
import zone.wim.coding.Codec;
import zone.wim.coding.CodingException;
import zone.wim.coding.Decoder;

public abstract class TextCodec extends Codec {

    public static int SHIFT_OUT = '\u000E';
    public static int SHIFT_IN = '\u000F';
    public static int BYTE_ORDER_MARK = '\uFEFF';

    public static String DATE_TIME_FORMAT = "yyyymmddhhmmssZZ";

    public TextCodec(ByteOrder byteOrder) {
        super(byteOrder);
    }

    public abstract float averageCharsPerByte();
    public abstract float maxCharsPerByte();
    public abstract float averageBytesPerChar();
    public abstract float maxBytesPerChar();
    public abstract String defaultReplacement();

    @Override
    public zone.wim.coding.Decoder decoder(ByteBuffer src, Object dst) {
        return decoder(src, (CharBuffer)dst);
    }
    public abstract TextDecoder decoder(ByteBuffer src, CharBuffer dst);

    @Override
    public zone.wim.coding.Encoder encoder(Object src, ByteBuffer dst) {
        return encoder((CharBuffer)src, dst);
    }
    public abstract TextEncoder encoder(CharBuffer src, ByteBuffer dst);

    public abstract Charset charset() throws CodingException.UnsupportedCodec;

}
