package zone.wim.coding;

import lombok.AllArgsConstructor;
import lombok.Data;


public class InnerAncestor {
    String innerAncestorState;

    public InnerAncestor(String innerAncestorState) {
        this.innerAncestorState = innerAncestorState;
    }

    public String getInnerAncestorState() {
        return innerAncestorState;
    }

}
