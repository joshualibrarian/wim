package zone.wim.token;

import java.nio.Buffer;

public class TokenBuffer extends Buffer {

	TokenBuffer(int mark, int pos, int lim, int cap) {
		super(mark, pos, lim, cap);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int arrayOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDirect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Buffer slice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Buffer duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Object base() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object array() {
		// TODO Auto-generated method stub
		return null;
	}

}
