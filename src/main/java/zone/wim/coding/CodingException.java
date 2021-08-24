package zone.wim.coding;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.CharacterCodingException;

public class CodingException {

    public static class UnsupportedCodec extends Exception {
        public UnsupportedCodec(String codecName) {
            super(codecName);
        }
    }

    public static class IllegalName extends Exception {
        public IllegalName(String name) { super(name); }
    }

    public static class BufferUnderflow extends BufferUnderflowException {
        public BufferUnderflow() { super(); }
    }

    public static class BufferOverflow extends BufferOverflowException {
        public BufferOverflow() { super(); }
    }

    public static class MalformedInput extends CharacterCodingException {
        Object input;
        public MalformedInput(Object input) {
            super();
            this.input = input;
        }
    }

    public static class Unmappable extends CharacterCodingException {
        String value;
        public Unmappable(String value) {
            super();
            this.value = value;
        }
    }
    public static class Escaped extends CharacterCodingException {
        Object escapedCodepoint;
        public Escaped(Object escapedCodepoint) {
            super();
            this.escapedCodepoint = escapedCodepoint;

        }

    }

    public static class InvalidData extends CharacterCodingException {
        String message;
        public InvalidData(String message) {
            super();
            this.message = message;
        }
    }
}
