package zone.wim.codec.text;

//import java.nio.charset.Charset;

import java.nio.ByteOrder;

import zone.wim.codec.Codec;

public abstract class TextCodec extends Codec {

    public static String DATE_TIME_FORMAT = "yyyymmddhhmmssZZ";

    public TextCodec(ByteOrder byteOrder) {
        super(byteOrder);
    }

    public abstract float averageCharsPerByte();
    public abstract float maxCharsPerByte();
    public abstract float averageBytesPerChar();
    public abstract float maxBytesPerChar();

}
