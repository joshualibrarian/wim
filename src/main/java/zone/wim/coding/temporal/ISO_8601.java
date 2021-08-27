package zone.wim.coding.temporal;

import io.netty.buffer.ByteBuf;
import zone.wim.coding.Codec;
import zone.wim.coding.Decoder;
import zone.wim.coding.Encoder;
import zone.wim.coding.text.TextSourceCodec;

public class ISO_8601 extends TextSourceCodec implements TemporalCodec {

    @Override
    public String canonicalName() {
        return "TemporEnc";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public Decoder decoder(ByteBuf src, Object dst) {
        return null;
    }

    @Override
    public Encoder encoder(Object src, ByteBuf dst) {
        return null;
    }

    @Override
    public Codec detectEncoding(Object source) {
        return null;
    }
}
