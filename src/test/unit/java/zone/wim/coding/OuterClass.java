package zone.wim.coding;

public class OuterClass {

    public InnerClass inner(String innerState) {
        return new InnerClass(innerState);
    }


    public static class InnerClass extends InnerAncestor {

        InnerClass(String instanceState) {
            super(instanceState);
        }
    }
}
