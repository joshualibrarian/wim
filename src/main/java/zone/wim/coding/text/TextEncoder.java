package zone.wim.coding.text;

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


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.lang.ref.WeakReference;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import zone.wim.coding.*;
import zone.wim.coding.CoderResult;

public abstract class TextEncoder extends Encoder {

    protected TextCodec textCodec;
    protected CharBuffer src;
    protected ByteBuf dst;
    protected byte[] replacement;

    // Optional array of escape characters
//    protected int[] escapeChars = null;

//    protected TextEncoder(TextCodec codec, byte[] replacement) {
//        super(codec);
//        this.textCodec = codec;
//
//        replaceWith(replacement);
//    }

    protected TextEncoder(TextCodec codec, CharBuffer src, ByteBuf dst) {
        super(codec);
        this.src = src;
        this.dst = dst;
    }
    /**
     * Returns this encoder's replacement value.
     *
     * @return  This encoder's current replacement,
     *          which is never {@code null} and is never empty
     */
    public final byte[] replacement() {
        return Arrays.copyOf(replacement, replacement.length);

    }

    public final TextEncoder replaceWith(byte[] newReplacement) {
        if (newReplacement == null)
            throw new IllegalArgumentException("Null replacement");
        int len = newReplacement.length;
        if (len == 0)
            throw new IllegalArgumentException("Empty replacement");
        if (len > textCodec.maxBytesPerChar())
            throw new IllegalArgumentException("Replacement too long");
//        if (!isLegalReplacement(newReplacement))
//            throw new IllegalArgumentException("Illegal replacement");
        this.replacement = Arrays.copyOf(newReplacement, newReplacement.length);

        implReplaceWith(this.replacement);
        return this;
    }

    /**
     * Reports a change to this encoder's replacement value.
     *
     * <p> The default implementation of this method does nothing.  This method
     * should be overridden by encoders that require notification of changes to
     * the replacement.  </p>
     *
     * @param  newReplacement    The replacement value
     */
    protected void implReplaceWith(byte[] newReplacement) {
    }



    //TODO: remove this whole bit?

    private WeakReference<TextDecoder> cachedDecoder = null;

//    /**
//     * Determine whether or not the given byte array is a legal replacement value
//     * for this encoder.
//     *
//     * <p> A replacement is legal if, and only if, it is a legal sequence of
//     * bytes in this encoder's charset; that is, it must be possible to decode
//     * the replacement into one or more sixteen-bit Unicode characters.
//     *
//     * <p> The default implementation of this method is not very efficient; it
//     * should generally be overridden to improve performance.  </p>
//     *
//     * @param  repl  The byte array to be tested
//     *
//     * @return  {@code true} if, and only if, the given byte array
//     *          is a legal replacement value for this encoder
//     */
//    public boolean isLegalReplacement(byte[] repl) {
//        WeakReference<TextDecoder> wr = cachedDecoder;
//        TextDecoder decoder = null;
//        if ((wr == null) || ((decoder = wr.get()) == null)) {
//            decoder = (TextDecoder) codec.decoder();
//            decoder.onMalformedInput(CodingErrorAction.REPORT);
//            decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
//            cachedDecoder = new WeakReference<TextDecoder>(decoder);
//        } else {
//            decoder.reset();
//        }
//        ByteBuffer bb = ByteBuffer.wrap(repl);
//        CharBuffer cb = CharBuffer.allocate((int)(bb.remaining() * textCodec.maxCharsPerByte()));
//        CoderResult cr = decoder.decode(true);
//        return !cr.isError();
//    }

    public final CoderResult encode(boolean endOfInput) {
        CodingState newState = endOfInput ? CodingState.END : CodingState.CODING;
        if ((state != CodingState.RESET) && (state != CodingState.CODING)
            && !(endOfInput && (state == CodingState.END)))
            throwIllegalStateException(state, newState);
        state = newState;

        for (;;) {

            CoderResult cr;
            try {
                cr = encodeLoop();
            } catch (BufferUnderflowException bue) {
                throw new CodingException.BufferUnderflow();
            } catch (BufferOverflowException bof) {
                throw new CodingException.BufferOverflow();
            }
            
            if (cr.isEscaped())
            	return cr;
            if (cr.isOverflow())
                return cr;

            if (cr.isUnderflow()) {
                if (endOfInput && src.hasRemaining()) {
                    cr = CoderResult.malformedForLength(src.remaining());
                    // Fall through to malformed-input case
                } else {
                    return cr;
                }
            }

            CodingErrorAction action = null;
            if (cr.isMalformed())
                action = malformedInputAction;
            else if (cr.isUnmappable())
                action = unmappableCharacterAction();
            else
                assert false : cr.toString();

            if (action == CodingErrorAction.REPORT)
                return cr;

            if (action == CodingErrorAction.REPLACE) {
                if (dst.writableBytes() < replacement.length)
                    return CoderResult.OVERFLOW;
                dst.writeBytes(replacement);
            }

            if ((action == CodingErrorAction.IGNORE)
                || (action == CodingErrorAction.REPLACE)) {
                // Skip erroneous input either way
                src.position(src.position() + cr.length());
                continue;
            }

            assert false;
        }

    }

//    private boolean canEncode(CharBuffer cb) {
//        if (state == CodingState.FLUSHED)
//            reset();
//        else if (state != CodingState.RESET)
//            throwIllegalStateException(state, CodingState.CODING);
//        CodingErrorAction ma = malformedInputAction();
//        CodingErrorAction ua = unmappableCharacterAction();
//        try {
//            onMalformedInput(CodingErrorAction.REPORT);
//            onUnmappableCharacter(CodingErrorAction.REPORT);
//            encode(cb);
//        } catch (CharacterCodingException x) {
//            return false;
//        } finally {
//            onMalformedInput(ma);
//            onUnmappableCharacter(ua);
//            reset();
//        }
//        return true;
//    }

//    /**
//     * Tells whether or not this encoder can encode the given character.
//     *
//     * <p> This method returns {@code false} if the given character is a
//     * surrogate character; such characters can be interpreted only when they
//     * are members of a pair consisting of a high surrogate followed by a low
//     * surrogate.  The {@link #canEncode(java.lang.CharSequence)
//     * canEncode(CharSequence)} method may be used to test whether or not a
//     * character sequence can be encoded.
//     *
//     * <p> This method may modify this encoder's state; it should therefore not
//     * be invoked if an <a href="#steps">encoding operation</a> is already in
//     * progress.
//     *
//     * <p> The default implementation of this method is not very efficient; it
//     * should generally be overridden to improve performance.  </p>
//     *
//     * @param   c
//     *          The given character
//     *
//     * @return  {@code true} if, and only if, this encoder can encode
//     *          the given character
//     *
//     * @throws  IllegalStateException
//     *          If an encoding operation is already in progress
//     */
//    public boolean canEncode(char c) {
//        CharBuffer cb = CharBuffer.allocate(1);
//        cb.put(c);
//        cb.flip();
//        return canEncode(cb);
//    }

//    /**
//     * Tells whether or not this encoder can encode the given character
//     * sequence.
//     *
//     * <p> If this method returns {@code false} for a particular character
//     * sequence then more information about why the sequence cannot be encoded
//     * may be obtained by performing a full <a href="#steps">encoding
//     * operation</a>.
//     *
//     * <p> This method may modify this encoder's state; it should therefore not
//     * be invoked if an encoding operation is already in progress.
//     *
//     * <p> The default implementation of this method is not very efficient; it
//     * should generally be overridden to improve performance.  </p>
//     *
//     * @param   cs
//     *          The given character sequence
//     *
//     * @return  {@code true} if, and only if, this encoder can encode
//     *          the given character without throwing any exceptions and without
//     *          performing any replacements
//     *
//     * @throws  IllegalStateException
//     *          If an encoding operation is already in progress
//     */
//    public boolean canEncode(CharSequence cs) {
//        CharBuffer cb;
//        if (cs instanceof CharBuffer)
//            cb = ((CharBuffer)cs).duplicate();
//        else
//            cb = CharBuffer.wrap(cs.toString());
//        return canEncode(cb);
//    }

}
