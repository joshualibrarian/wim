package zone.wim.coding;

@FunctionalInterface
public interface EscapeHandler {
    public CoderResult shouldEscape(Object lastDecoded);
}
