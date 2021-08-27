package zone.wim.coding;

public class SelfCodingException extends Exception {
    public static class IncorrectlyCoded extends SelfCodingException {
        public IncorrectlyCoded(String message) {

        }
        public IncorrectlyCoded(Object expected, Object encountered) {

        }
    }

}
