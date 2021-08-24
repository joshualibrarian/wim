package zone.wim.coding.text.unicode;

public class Codepoint {
    int codepoint;

    public int asInt() {
        return codepoint;
    }

    public char[] asChars() {
        return Character.toChars(codepoint);
    }
}
