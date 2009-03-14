/*

    A Standard I/O Terminal For JAR Files
    https://a-jar-stdio-terminal.dev.java.net/

Copyright (c) 2005 Sanjay Dasgupta (s4dg@yahoo.com)

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
OTHER DEALINGS IN THE SOFTWARE.
*/

/*		CHANGE HISTORY

	2005-12-18 Changed package to "a_jar_stdio_terminal"

*/

package terminal;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

public class InStrmAdapter extends InputStream {
	public InStrmAdapter(StdioTerm owner, SimpleAttributeSet textAttr) {
		this.owner = owner;
		theDocument = owner.theDocument;
		this.textAttr = textAttr;
	}
	public int read() throws IOException {
		if (owner.inputEofSeen)
			return -1;
		if (owner.inputBuffer.isEmpty()) {
			owner.getUserInput();
		}
		return owner.inputBuffer.remove(0);
	}
	public int read(byte b[], int off, int len) throws IOException {
		if (owner.inputEofSeen)
			return -1;
		if (owner.inputBuffer.isEmpty()) {
			owner.getUserInput();
		}
		int dataLength = Math.min(len, owner.inputBuffer.size());
		for (int i = 0; i < dataLength; ++i)
			b[off + i] = owner.inputBuffer.remove(0);
		return dataLength;
	}
	private byte tempChar[] = new byte[1];
	private Document theDocument;
	private SimpleAttributeSet textAttr;
	private StdioTerm owner;
}
