package zone.wim.coding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.*;
import java.util.*;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import zone.wim.coding.text.TextCodec;
import zone.wim.coding.text.TextDecoder;
import zone.wim.coding.token.CodingToken;
import zone.wim.library.Library;
import zone.wim.coding.CodingException.*;

public class DecodeAdapter {

	public static final int SHIFT_OUT = '\u000E';
	public static final int SHIFT_IN = '\u000F';

	@Setter @Getter public int[] tokenizers;
	@Setter @Getter private Codec defaultCodec;
	@Getter @Setter private int defaultCharBufferSize = 1024;
	@Getter @Setter private int encodingEscapeChar = SHIFT_OUT;
	@Getter @Setter private int encodingResumeChar = SHIFT_IN;

	private Stack<Decoder> decoderStack;
    private Stack<Expecting> expectingStack;
	private Stack<SelfCoding> foundStack;
	private Stack<CodingToken> tokenStack;

	private ByteBuf sourceBytes;
	private CharBuffer activeCharBuffer;
	private List<String> tokens;
	
	private CharSequence currentToken;
	private CharSequence currentChar;

	private int currentCodepoint = -1;
	private int startIndexOfCurrentToken = 0;	//TODO: correct for BOM
	private int endIndexOfCurrentToken = -1;

	public DecodeAdapter(ByteBuf in) {
		this(in, Library.local().textEncoding());
	}

	public DecodeAdapter(ByteBuf in, Codec defaultOverride) {
		defaultCodec = defaultOverride;
		sourceBytes = in;
		foundStack = new Stack<>();
		expectingStack = new Stack<>();
		decoderStack = new Stack<>();

		activeCharBuffer =  CharBuffer.allocate(defaultCharBufferSize);

		Codec detectedCodec = defaultCodec.detectEncoding(in);
		if (detectedCodec instanceof TextCodec) {
			decoderStack.add(detectedCodec.decoder(in.nioBuffer(), activeCharBuffer));

		} else {
//			throw new UnsupportedCodec(detectedCodec.canonicalName());
		}

	}

	/**
	 *
	 * @param length number of text codepoints to encode
	 * @return
	 */
	public String readStringOfLength(int length) {
		TextDecoder decoder = (TextDecoder)decoderStack.peek();
		decoder.countdown(length);
		decoder.decode(false);
		return activeCharBuffer.get();
	}

	public int readSingleCodepoint() {
		TextDecoder decoder = (TextDecoder)decoderStack.peek();
		decoder.countdown(1);
		decoder.decode(false);
		return activeCharBuffer.get();
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected, int[] endingWith) {



		Expecting expecting = new Expecting(sourceBytes.readerIndex(), expected, endingWith);
		expectingStack.push(expecting);



//		CodingToken token = new CodingToken()

		SelfCoding result = null;
		try {
			Method decodeMethod = expected.getMethod("decode",
					new Class[] {DecodeAdapter.class});
			result = (SelfCoding)decodeMethod.invoke(this);
		} catch (NoSuchMethodException nsme) {
			//TODO: throw new proper exception here
			nsme.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void mark() {

	}

	@Data
	@AllArgsConstructor
	private class Expecting {
		int sourceIndex;
		Class<? extends SelfCoding> expectingClass;
		int[] escapes;
	}

	
}