package zone.wim.coding;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * There are two fundamental points that this library makes, the reason it exists instead just using the existing and broadly deployed `Charset` libraries in the core of Java.  Well, firstly is the issue of escapes.
 *
 * The way that the `Charset` scheme is arranged sends a buffer of bytes to encoded, and there is no way to interrupt this process.  If one wanted to say, escape the current encoding by using a control character and switch to a different kind of encoding, this is utterly not possible in the current setup, even if you re-implement the encoder and decoder implementations to include checks, there is no program flow to allow it in the `CoderResult`, and it would have to be interpreted as an error.
 *
 * Secondly, `Charset` descends from `Object`, even though it is really only one broad category of possible codecs, and it would be nice to have a broader framework capable of integrating various types of codecs (text, image, spacial, temporal, etc) together more usefully.
 *
 */
public interface Codec {

	public abstract String canonicalName();
	public abstract String[] aliases();

	public abstract Decoder decoder(ByteBuf src, Object dst);
	public abstract Encoder encoder(Object src, ByteBuf dst);

	public abstract Codec detectEncoding(Object source);
	//TODO: all the caching and instance managing thing here

}