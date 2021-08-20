package zone.wim.codec;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public abstract class Encoder extends Coder {

	protected Encoder(Codec codec) {
		this.codec = codec;
		this.state = CodingState.RESET;
	}


	/**
	 * Flushes this encoder.
	 *
	 * <p> Some encoders maintain internal state and may need to write some
	 * final bytes to the output buffer once the overall input sequence has
	 * been read.
	 *
	 * <p> Any additional output is written to the output buffer beginning at
	 * its current position.  At most {@link Buffer#remaining out.remaining()}
	 * bytes will be written.  The buffer's position will be advanced
	 * appropriately, but its mark and limit will not be modified.
	 *
	 * <p> If this method completes successfully then it returns {@link
	 * CoderResult#UNDERFLOW}.  If there is insufficient room in the output
	 * buffer then it returns {@link CoderResult#OVERFLOW}.  If this happens
	 * then this method must be invoked again, with an output buffer that has
	 * more room, in order to complete the current <a href="#steps">encoding
	 * operation</a>.
	 *
	 * <p> If this encoder has already been flushed then invoking this method
	 * has no effect.
	 *
	 * <p> This method invokes the {@link #implFlush implFlush} method to
	 * perform the actual flushing operation.  </p>
	 *
	 * @param  out
	 *         The output byte buffer
	 *
	 * @return  A coder-result object, either {@link CoderResult#UNDERFLOW} or
	 *          {@link CoderResult#OVERFLOW}
	 *
	 * @throws  IllegalStateException
	 *          If the previous step of the current encoding operation was an
	 *          invocation neither of the {@link #flush flush} method nor of
	 *          the three-argument {@link
	 *          ByteBuffer) encode} method
	 *          with a value of {@code true} for the {@code endOfInput}
	 *          parameter
	 */
	public final CoderResult flush(ByteBuffer out) {
		if (state == CodingState.END) {
			CoderResult cr = implFlush(out);
			if (cr.isUnderflow())
				state = CodingState.FLUSHED;
			return cr;
		}

		if (state != CodingState.FLUSHED)
			throwIllegalStateException(state, CodingState.FLUSHED);

		return CoderResult.UNDERFLOW; // Already flushed
	}

	/**
	 * Flushes this encoder.
	 *
	 * <p> The default implementation of this method does nothing, and always
	 * returns {@link CoderResult#UNDERFLOW}.  This method should be overridden
	 * by encoders that may need to write final bytes to the output buffer
	 * once the entire input sequence has been read. </p>
	 *
	 * @param  out
	 *         The output byte buffer
	 *
	 * @return  A coder-result object, either {@link CoderResult#UNDERFLOW} or
	 *          {@link CoderResult#OVERFLOW}
	 */
	protected CoderResult implFlush(ByteBuffer out) {
		return CoderResult.UNDERFLOW;
	}
	protected abstract CoderResult encodeLoop(Object in, ByteBuffer out);
	
}
