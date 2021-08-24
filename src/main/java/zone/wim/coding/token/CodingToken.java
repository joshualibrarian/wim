package zone.wim.coding.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import zone.wim.coding.SelfCoding;

import java.nio.CharBuffer;

@Getter @Setter
public class CodingToken {

    public int[] tokenizers;
    int sourceStartIndex;
    int sourceEndIndex;
    Class<? extends SelfCoding> expectedClass;
    Object decodedObject;
    CharBuffer text;

    public CodingToken(Class<? extends SelfCoding> clazz, int[] tokenizers, int sourceStartIndex) {

    }

}