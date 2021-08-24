package zone.wim.coding;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SelfCodingUnit {
    String name();

    String returns();

    String[] parameters();

    String[] exceptions();
}
