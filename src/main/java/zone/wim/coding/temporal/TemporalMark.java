package zone.wim.coding.temporal;

import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.SelfCoding;

import java.time.temporal.Temporal;

public abstract class TemporalMark implements SelfCoding {
    Temporal value;

    public static TemporalMark decode(DecodeAdapter adapter) {


        adapter.preCoded();
        return null;
    }
}
