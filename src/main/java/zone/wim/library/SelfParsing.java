package zone.wim.library;

public interface SelfParsing {

	// this is what it WOULD look like if this were allowed,
	// requiring at runtime that every implementer of this 
	// interface to implement this static function.

	// abstract static SelfParsing parse(WimParser parser);

	void parse(UnicodeReader parser);
}
