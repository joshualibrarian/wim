package zone.wim.codec;

import zone.wim.codec.text.TextDecoder;

import java.nio.charset.CodingErrorAction;

public class Coder {
    protected Codec codec;
    protected CodingState state;

    protected CodingErrorAction malformedInputAction = CodingErrorAction.REPORT;
    protected CodingErrorAction unmappableAction = CodingErrorAction.REPORT;


    /**
     * Resets this decoder, clearing any internal state.
     *
     * <p> This method resets charset-independent state and also invokes the
     * {@link #implReset() implReset} method in order to perform any
     * charset-specific reset actions.  </p>
     *
     * @return  This decoder
     *
     */
    public final Coder reset() {
        implReset();
        state = CodingState.RESET;
        return this;
    }

    /**
     * Resets this coder, clearing any charset-specific internal state.
     *
     * <p> The default implementation of this method does nothing.  This method
     * should be overridden by decoders that maintain internal state.  </p>
     */
    protected void implReset() { }

    protected void throwIllegalStateException(CodingState from, CodingState to) {
        throw new IllegalStateException("Current state = " + from + ", new state = " + to);
    }

    /**
     * Returns this decoder's current action for malformed-input errors.
     *
     * @return The current malformed-input action, which is never {@code null}
     */
    public CodingErrorAction malformedInputAction() {
        return malformedInputAction;
    }

    /**
     * Changes this decoder's action for malformed-input errors.
     *
     * <p> This method invokes the {@link #implOnMalformedInput
     * implOnMalformedInput} method, passing the new action.  </p>
     *
     * @param  newAction  The new action; must not be {@code null}
     *
     * @return  This decoder
     *
     * @throws IllegalArgumentException
     *         If the precondition on the parameter does not hold
     */
    public final Coder onMalformedInput(CodingErrorAction newAction) {
        if (newAction == null)
            throw new IllegalArgumentException("Null action");
        malformedInputAction = newAction;
        implOnMalformedInput(newAction);
        return this;
    }

    /**
     * Reports a change to this decoder's malformed-input action.
     *
     * <p> The default implementation of this method does nothing.  This method
     * should be overridden by decoders that require notification of changes to
     * the malformed-input action.  </p>
     *
     * @param  newAction  The new action
     */
    protected void implOnMalformedInput(CodingErrorAction newAction) { }


    /**
     * Returns this decoder's current action for unmappable-character errors.
     *
     * @return The current unmappable-character action, which is never
     *         {@code null}
     */
    public CodingErrorAction unmappableCharacterAction() {
        return unmappableAction;
    }

    /**
     * Changes this decoder's action for unmappable-character errors.
     *
     * <p> This method invokes the {@link #implOnUnmappableCharacter
     * implOnUnmappableCharacter} method, passing the new action.  </p>
     *
     * @param  newAction  The new action; must not be {@code null}
     *
     * @return  This decoder
     *
     * @throws IllegalArgumentException
     *         If the precondition on the parameter does not hold
     */
    public final Coder onUnmappableCharacter(CodingErrorAction newAction)
    {
        if (newAction == null)
            throw new IllegalArgumentException("Null action");
        unmappableAction = newAction;
        implOnUnmappableCharacter(newAction);
        return this;
    }

    /**
     * Reports a change to this decoder's unmappable-character action.
     *
     * <p> The default implementation of this method does nothing.  This method
     * should be overridden by decoders that require notification of changes to
     * the unmappable-character action.  </p>
     *
     * @param  newAction  The new action
     */
    protected void implOnUnmappableCharacter(CodingErrorAction newAction) { }
}
