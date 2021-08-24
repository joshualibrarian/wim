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


import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.charset.CodingErrorAction;

import lombok.Getter;
import lombok.Setter;
import zone.wim.coding.CoderResult;
import zone.wim.coding.CodingException;
import zone.wim.coding.CodingState;
import zone.wim.coding.Decoder;

public abstract class TextDecoder extends Decoder {

    protected TextCodec textCodec;    // convenience to minimize casting
    protected ByteBuffer src;
    protected CharBuffer dst;
    protected String replacement;
    protected int [] escapes;
    @Getter @Setter
    protected int countdown = -1;

    protected TextDecoder(TextCodec codec, ByteBuffer src, CharBuffer dst) {
        super(codec);
        this.src = src;
        this.dst = dst;
        replacement(textCodec.defaultReplacement());
    }

    protected boolean isEscape(int value) {
        for (int e : escapes) {
            if (value == e) {
                return true;
            }
        }
        return false;
    }

    protected boolean countdownDecrementAndIsReached() {
        if (countdown > -1) {
            if(countdown >= 0) {
                countdown--;
            }
            if (countdown == -1) {
                return true;
            }
        }
        return false;
    }

    public final TextDecoder replacement(String newReplacement) {
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

    public final CoderResult decode(boolean endOfInput) {
        ByteBuffer in = src;

        CodingState newState = endOfInput ? CodingState.END : CodingState.CODING;
        if ((state != CodingState.RESET) && (state != CodingState.CODING)
            && !(endOfInput && (state == CodingState.END)))
            throwIllegalStateException(state, newState);
        state = newState;

        for (;;) {
            CoderResult cr;
            try {
                cr = decodeLoop();
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
                if (dst.remaining() < replacement.length())
                    return CoderResult.OVERFLOW;
                dst.put(replacement);
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
     *          #decode(boolean) decode} method
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

}
