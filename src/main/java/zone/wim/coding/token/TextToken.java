package zone.wim.coding.token;

import zone.wim.coding.SelfCoding;

import java.nio.CharBuffer;

public class TextToken extends CodingToken {




    public TextToken(int[] escapeCodepoints, int byteSourceStartIndex, int byteSourceEndIndex, Class<? extends SelfCoding> expectedClass, Object decodedObject, CharBuffer chars) {
        super(escapeCodepoints, byteSourceStartIndex, byteSourceEndIndex, expectedClass, decodedObject, chars);
    }


}
