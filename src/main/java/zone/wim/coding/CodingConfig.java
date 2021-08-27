package zone.wim.coding;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:coding.properties"})
public interface CodingConfig {
    public String defaultTextCodec();
    public char defaultTokenizingChar();
}
