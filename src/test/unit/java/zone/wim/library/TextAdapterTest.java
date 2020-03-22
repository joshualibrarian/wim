package zone.wim.library;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import com.ibm.icu.charset.*;
import com.ibm.icu.
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.*;

import com.ibm.icu.lang.UCharacter;

public class TextAdapterTest {
	
	@Nested
	class UCharacterTests {
//		ByteBuffer buffer;
		
		@BeforeEach
//		void initBuffer() {
//			byte[] bytes = "🖖".getBytes(StandardCharsets.UTF_16BE);
//			buffer = ByteBuffer.wrap(bytes);
//			
//		}
		
		@Test
		void testPlainChar() {
			byte[] bytes = "abc".getBytes(StandardCharsets.UTF_16BE);
			System.out.println("bytes: " + Arrays.toString(bytes));
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			System.out.println("buffer capacity: " + buffer.capacity());
			System.out.println("buffer array: " + Arrays.toString(buffer.array()));
			char c = buffer.getChar();
			char s = buffer.getChar();
			System.out.println(c);
			System.out.println(s);
//			int codePoint = UCharacter.toCodePoint(c,  (Character) null);
			CharsetDecoder decoder = Charset.forName("UTF-16BE").newDecoder();
			decoder.
			System.out.println("codepoint: " + Integer.toString(codePoint, 16));
			System.out.println(UCharacter.toChars(codePoint));
			assert(codePoint == 0x0061);
		}
		
		@Test
		void testEmoji() {
			byte[] bytes = "🖖".getBytes(StandardCharsets.UTF_16LE);
			System.out.println("bytes: " + Arrays.toString(bytes));
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			System.out.println("buffer capacity: " + buffer.capacity());
			System.out.println("buffer array: " + Arrays.toString(buffer.array()));
			char c = buffer.getChar();
			char s = buffer.getChar();
			System.out.println(c);
			System.out.println(s);
			int codePoint = UCharacter.toCodePoint(c,  s);
			System.out.println("codepoint: " + Integer.toString(codePoint, 16));
			System.out.println(UCharacter.toChars(codePoint));
			assert(codePoint == 0x1F596);
		}
	}
	
}
