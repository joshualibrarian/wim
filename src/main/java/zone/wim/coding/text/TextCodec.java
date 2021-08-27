package zone.wim.coding.text;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import zone.wim.coding.ByteSourceCodec;
import zone.wim.coding.CodingException;

public abstract class TextCodec extends ByteSourceCodec {

    public static int SHIFT_OUT = '\u000E';
    public static int SHIFT_IN = '\u000F';
    public static int BYTE_ORDER_MARK = '\uFEFF';

    public TextCodec(ByteOrder byteOrder) {
        super(byteOrder);
    }

    public abstract float averageCharsPerByte();
    public abstract float maxCharsPerByte();
    public abstract float averageBytesPerChar();
    public abstract float maxBytesPerChar();
    public abstract String defaultReplacement();

    @Override
    public TextDecoder decoder(ByteBuf src, Object dst) {
        return decoder(src, (CharBuffer)dst);
    }
    public abstract TextDecoder decoder(ByteBuf src, CharBuffer dst);

    @Override
    public TextEncoder encoder(Object src, ByteBuf dst) {
        return encoder((CharBuffer)src, dst);
    }
    public abstract TextEncoder encoder(CharBuffer src, ByteBuf dst);

    public abstract Charset charset() throws CodingException.UnsupportedCodec;

}
