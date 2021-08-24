package zone.wim.coding;

import org.junit.jupiter.api.Test;

public class StateTest {
    OuterClass foo;
    OuterClass bar;

    @Test
    void doStateTest() {
        foo = new OuterClass();
        OuterClass.InnerClass innerFoo = foo.inner("fooInnerState");

        bar = new OuterClass();
        OuterClass.InnerClass innerBar = foo.inner("barInnerState");

        assert (innerFoo.getInnerAncestorState()).equals("fooInnerState");
        assert (innerBar.getInnerAncestorState()).equals("barInnerState");
    }

}
