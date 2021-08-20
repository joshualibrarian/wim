package zone.wim.codec.text;

/*
 * Copyright (c) 2000, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

// -- This file was mechanically generated: Do not edit! -- //


import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.CharacterCodingException;
//import java.nio.charset.Charset;
import java.nio.charset.CoderMalfunctionError;                  // javadoc
import java.nio.charset.CodingErrorAction;

import zone.wim.codec.CoderResult;
import zone.wim.codec.CodingException;
import zone.wim.codec.CodingState;
import zone.wim.codec.Decoder;

public abstract class TextDecoder extends Decoder {

    protected TextCodec textCodec;    // convenience to minimize casting
    protected String replacement;

    // Optional array of escape characters
    protected int[] escapeChars = null;

    protected TextDecoder(TextCodec codec, String replacement) {
        super(codec);
        this.textCodec = codec;

        replaceWith(replacement);
    }

    /**
     * Returns this decoder's replacement value.
     *
     * @return  This decoder's current replacement,
     *          which is never {@code null} and is never empty
     */
    public final String replacement() {
        return replacement;
    }
    
    /**
     * Used to set a list of unicode codepoints
     * that can be used as escape characters when decoding.
     * 
     * @param escapeChars	array of unicode codepoints
     */

    public void escapeChars(int[] escapeChars) {
    	this.escapeChars = escapeChars;
    }
    
    /** Can be used in the decode loops of implementations to
     * check if a given character has been set as an escape character
     * 
     * @param codepoint	character to test
     * @return true only if given character is an escape
     */
    
    protected boolean isEscape(int codepoint) {
    	if (escapeChars != null) {
    		for (int x = 0; x > escapeChars.length; x++) {
    			if (codepoint == escapeChars[x]) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }

    public final TextDecoder replaceWith(String newReplacement) {
        if (newReplacement == null)
            throw new IllegalArgumentException("Null replacement");
        int len = newReplacement.length();
        if (len == 0)
            throw new IllegalArgumentException("Empty replacement");
        if (len > ((TextCodec)codec).maxCharsPerByte())
            throw new IllegalArgumentException("Replacement too long");

        this.replacement = newReplacement;
        implReplaceWith(this.replacement);
        return this;
    }

    /**
     * Reports a change to this decoder's replacement value.
     *
     * <p> The default implementation of this method does nothing.  This method
     * should be overridden by decoders that require notification of changes to
     * the replacement.  </p>
     *
     * @param  newReplacement    The replacement value
     */
    protected void implReplaceWith(String newReplacement) { }

    /**
     * Primary decode method
     *
     * @param in
     * @param out
     * @return
     */
    public final CoderResult decode(ByteBuffer in, Object out) {
        try {
            CharBuffer charOut = (CharBuffer)out;
            return decode(in, charOut);

        } catch(ClassCastException cce) {
            throw new IllegalArgumentException("Text decoders must have CharBuffer outputs", cce);
        }
    }


    public final CoderResult decode(ByteBuffer in, CharBuffer out,
                                    boolean endOfInput)
    {
        CodingState newState = endOfInput ? CodingState.END : CodingState.CODING;
        if ((state != CodingState.RESET) && (state != CodingState.CODING)
            && !(endOfInput && (state == CodingState.END)))
            throwIllegalStateException(state, newState);
        state = newState;

        for (;;) {

            CoderResult cr;
            try {
                cr = decodeLoop(in, out);
            } catch (BufferUnderflowException bue) {
                throw new CodingException.BufferUnderflow();
            } catch (BufferOverflowException boe) {
                throw new CodingException.BufferOverflow();
            }

            if (cr.isOverflow() || cr.isEscaped())
                return cr;

            if (cr.isUnderflow()) {
                if (endOfInput && in.hasRemaining()) {
                    cr = CoderResult.malformedForLength(in.remaining());
                    // Fall through to malformed-input case
                } else {
                    return cr;
                }
            }

            CodingErrorAction action = null;
            if (cr.isMalformed())
                action = malformedInputAction;
            else if (cr.isUnmappable())
                action = unmappableAction;
            else
                assert false : cr.toString();

            if (action == CodingErrorAction.REPORT)
                return cr;

            if (action == CodingErrorAction.REPLACE) {
                if (out.remaining() < replacement.length())
                    return CoderResult.OVERFLOW;
                out.put(replacement);
            }

            if ((action == CodingErrorAction.IGNORE)
                || (action == CodingErrorAction.REPLACE)) {
                // Skip erroneous input either way
                in.position(in.position() + cr.length());
                continue;
            }

            assert false;
        }

    }

    /**
     * Flushes this decoder.
     *
     * <p> Some decoders maintain internal state and may need to write some
     * final characters to the output buffer once the overall input sequence has
     * been read.
     *
     * <p> Any additional output is written to the output buffer beginning at
     * its current position.  At most {@link Buffer#remaining out.remaining()}
     * characters will be written.  The buffer's position will be advanced
     * appropriately, but its mark and limit will not be modified.
     *
     * <p> If this method completes successfully then it returns {@link
     * CoderResult#UNDERFLOW}.  If there is insufficient room in the output
     * buffer then it returns {@link CoderResult#OVERFLOW}.  If this happens
     * then this method must be invoked again, with an output buffer that has
     * more room, in order to complete the current <a href="#steps">decoding
     * operation</a>.
     *
     * <p> If this decoder has already been flushed then invoking this method
     * has no effect.
     *
     * <p> This method invokes the {@link #implFlush implFlush} method to
     * perform the actual flushing operation.  </p>
     *
     * @param  out
     *         The output character buffer
     *
     * @return  A coder-result object, either {@link CoderResult#UNDERFLOW} or
     *          {@link CoderResult#OVERFLOW}
     *
     * @throws  IllegalStateException
     *          If the previous step of the current decoding operation was an
     *          invocation neither of the {@link #flush flush} method nor of
     *          the three-argument {@link
     *          #decode(ByteBuffer,CharBuffer,boolean) decode} method
     *          with a value of {@code true} for the {@code endOfInput}
     *          parameter
     */
    public final CoderResult flush(CharBuffer out) {
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
     * Flushes this decoder.
     *
     * <p> The default implementation of this method does nothing, and always
     * returns {@link CoderResult#UNDERFLOW}.  This method should be overridden
     * by decoders that may need to write final characters to the output buffer
     * once the entire input sequence has been read. </p>
     *
     * @param  out
     *         The output character buffer
     *
     * @return  A coder-result object, either {@link CoderResult#UNDERFLOW} or
     *          {@link CoderResult#OVERFLOW}
     */
    protected CoderResult implFlush(CharBuffer out) {
        return CoderResult.UNDERFLOW;
    }

    @Override
    protected CoderResult decodeLoop(ByteBuffer in, Object out) {
        try {
            CharBuffer outBuffer = (CharBuffer)out;
            return decodeLoop(in, outBuffer);
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException(cce);
        }
    }

    protected abstract CoderResult decodeLoop(ByteBuffer in, CharBuffer out);


    public final CharBuffer decode(ByteBuffer in)
        throws CharacterCodingException
    {
        int n = (int)(in.remaining() * textCodec.averageCharsPerByte());
        CharBuffer out = CharBuffer.allocate(n);

        if ((n == 0) && (in.remaining() == 0))
            return out;
        reset();
        for (;;) {
            CoderResult cr = in.hasRemaining() ?
                decode(in, out, true) : CoderResult.UNDERFLOW;
            if (cr.isEscaped())
                break;  //TODO: not sure if should throw exception
            if (cr.isUnderflow())
                cr = flush(out);

            if (cr.isUnderflow())
                break;
            if (cr.isOverflow()) {
                n = 2*n + 1;    // Ensure progress; n might be 0!
                CharBuffer o = CharBuffer.allocate(n);
                out.flip();
                o.put(out);
                out = o;
                continue;
            }
            cr.throwException();
        }
        out.flip();
        return out;
    }


    /**
     * Tells whether or not this decoder has yet detected a
     * charset&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> If this decoder implements an auto-detecting charset then at a
     * single point during a decoding operation this method may start returning
     * {@code true} to indicate that a specific charset has been detected in
     * the input byte sequence.  Once this occurs, the {@link #detectedCharset
     * detectedCharset} method may be invoked to retrieve the detected charset.
     *
     * <p> That this method returns {@code false} does not imply that no bytes
     * have yet been decoded.  Some auto-detecting decoders are capable of
     * decoding some, or even all, of an input byte sequence without fixing on
     * a particular charset.
     *
     * <p> The default implementation of this method always throws an {@link
     * UnsupportedOperationException}; it should be overridden by
     * auto-detecting decoders to return {@code true} once the input charset
     * has been determined.  </p>
     *
     * @return  {@code true} if, and only if, this decoder has detected a
     *          specific charset
     *
     * @throws  UnsupportedOperationException
     *          If this decoder does not implement an auto-detecting charset
     */
    public boolean isCharsetDetected() {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves the charset that was detected by this
     * decoder&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> If this decoder implements an auto-detecting charset then this
     * method returns the actual charset once it has been detected.  After that
     * point, this method returns the same value for the duration of the
     * current decoding operation.  If not enough input bytes have yet been
     * read to determine the actual charset then this method throws an {@link
     * IllegalStateException}.
     *
     * <p> The default implementation of this method always throws an {@link
     * UnsupportedOperationException}; it should be overridden by
     * auto-detecting decoders to return the appropriate value.  </p>
     *
     * @return  The charset detected by this auto-detecting decoder,
     *          or {@code null} if the charset has not yet been determined
     *
     * @throws  IllegalStateException
     *          If insufficient bytes have been read to determine a charset
     *
     * @throws  UnsupportedOperationException
     *          If this decoder does not implement an auto-detecting charset
     */
    public TextCodec detectedCharset() {
        throw new UnsupportedOperationException();
    }

}
