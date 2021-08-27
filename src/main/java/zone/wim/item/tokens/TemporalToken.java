package zone.wim.item.tokens;

import zone.wim.coding.DecodeAdapter;
import zone.wim.coding.EncodeAdapter;
import zone.wim.coding.SelfCoding;
import zone.wim.coding.temporal.TemporalMark;
import zone.wim.item.Item;

import java.util.List;
import java.util.Stack;

enum SupportedCodecs {
    ISO_8601,
    TEMPORENC;
}
public class TemporalToken extends TemporalMark {

    public static char TEMPORAL_CHAR = Item.TIMESTAMP_CHAR;
    public static TemporalToken decode(DecodeAdapter adapter) {
        Stack<Object> preCoded = adapter.preCoded();
            while (!preCoded.isEmpty()) {
                String format = (String)preCoded.pop();

//                if (format == Item.TIMESTAMP_CHAR) {
//

//                }
            }


    }

    @Override
    public void encode(EncodeAdapter adapter) {

    }
}
