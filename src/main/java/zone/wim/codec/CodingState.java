package zone.wim.codec;

public enum CodingState {
    RESET, CODING, END, FLUSHED, ESCAPED;
}
