package zone.wim.library.store;

import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import javax.jdo.*;
import javax.jdo.identity.*;
import zone.wim.exception.*;
import zone.wim.exception.StoreException.NotFound;
import zone.wim.item.*;
import zone.wim.token.Token;

public class ItemStore {
	public ItemStore(String url) {
		configure();
	}
	
	private void configure() {
		
	}
	
	
}
