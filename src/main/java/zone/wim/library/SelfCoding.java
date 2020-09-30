package zone.wim.library;

import zone.wim.exception.LibraryException.NotInitialized;

public interface SelfCoding {
	// this is what it WOULD look like if this were allowed,
	// requiring at compile time that every implementer of this 
	// interface must implement this static function, which 
	// is not inherited.
	// abstract static SelfParsing decode(DecodeAdapter decodeAdapter);

//	void populate(DecodeAdapter adapter);
	void encode(EncodeAdapter adapter);
//	void decode(DecodeAdapter adapter);
}
