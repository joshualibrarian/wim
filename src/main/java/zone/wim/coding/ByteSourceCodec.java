package zone.wim.coding;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class ByteSourceCodec implements Codec {
    @Getter @Setter
    protected ByteOrder byteOrder = null;

    public ByteSourceCodec(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

//    public abstract Codec detectEncoding(ByteBuf input);
    public abstract boolean byteOrderSensitive();

}
