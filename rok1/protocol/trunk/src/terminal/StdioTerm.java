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

2005-12-18	Changed package to "a_jar_stdio_terminal"
2005-12-18	Disabled typeahead by rearranging "KeyListener" 
			subscriptions.

*/

package terminal;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.lang.reflect.*;

import java.awt.Color;

public class StdioTerm implements ActionListener, KeyListener {

	private StdioTerm(String s) {
		theFrame = new JFrame();
		if (s != null)
			theFrame.setTitle(s);
		init();
	}

	private StdioTerm(JFrame f, String s) {
		theFrame = new JFrame(s);
		theFrame.setIconImage(f.getIconImage());
		parent = f;
		init();
	}

	private StdioTerm(JDialog d, String s) {
		theFrame = new JFrame(s);
//		theFrame.setIconImage(d.getIconImage());
		parent = d;
		init();
	}

	private void init() {
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theTextPane.setBackground(Color.black);
		Font f = theTextPane.getFont();
		theTextPane.setEditable(false);
		theTextPane.setCaretColor(Color.white);
		theTextPane.setCaret(new DefaultCaret());
		theTextPane.setFont(new Font("Monospaced", f.getStyle(), f.getSize()));
		theFrame.getContentPane().add(new JScrollPane(theTextPane), BorderLayout.CENTER);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout());
		clearButton.addActionListener(this);
		copyButton.addActionListener(this);
		btnPanel.add(clearButton);
		btnPanel.add(copyButton);
		theFrame.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		StyleConstants.setForeground( outTextAttr, Color.green );
		StyleConstants.setForeground( errTextAttr, Color.orange );
		StyleConstants.setForeground( inTextAttr, Color.white );
		out = new PrintStream( new OutStrmAdapter(this, outTextAttr));
		err = new PrintStream( new OutStrmAdapter(this, errTextAttr));
		in = new InStrmAdapter(this, inTextAttr);
	}

	private static void getStdioNames() {
		InputStream nameStream = me.getClass().getResourceAsStream(
				"StdioConfig.txt");
		if (nameStream == null) {
			System.err.println("No \"StdioConfig.txt\" found");
			return;
		}

		byte nameBytes[] = null;
		try {
			nameBytes = new byte[nameStream.available()];
			nameStream.read(nameBytes);
		} catch (Throwable t) {
			System.err.println("JarStdioTerminal.getStdioNames(): " + t);
			return;
		}
		String names = new String(nameBytes);
		int end = names.indexOf('\n');
		if (end == -1) {
			stdioClassName = names.trim();
		} else {
			stdioClassName = names.substring(0, end).trim();
			if (names.length() > end + 1)
				stdioTerminalTitle = names.substring(end + 1).trim();
		}
	}

	void configureDisplay() {
		if (!terminalGeometrySet) {
			terminalGeometrySet = true;
			theFrame.setSize(450, 300);
			theFrame.setLocationRelativeTo(parent);
		}
		theFrame.setVisible(true);
		theFrame.setState(Frame.NORMAL);
		theFrame.toFront();
	}
	
	void getUserInput() throws IOException {
		theTextPane.addKeyListener(this);
		configureDisplay();
		if (theCaret == null) 
			theCaret = theTextPane.getCaret();
		theTextPane.setCaretPosition(theDocument.getLength());
		theCaret.setBlinkRate(500);
		theCaret.setVisible(true);
		try {
			synchronized (theTextPane) {
				theTextPane.wait();
			}
			theCaret.setVisible(false);
			theCaret.setBlinkRate(0);
		} catch (InterruptedException ie) {
			theCaret.setVisible(false);
			theCaret.setBlinkRate(0);
			inputEofSeen = true;
			throw new EOFException();
		}
		theTextPane.removeKeyListener(this);
	}

	public void keyPressed(KeyEvent ke) {	
	}
	public void keyReleased(KeyEvent ke) {
		theTextPane.setCaretPosition(theDocument.getLength());
	}

	public void keyTyped(KeyEvent ke) {
		char c = ke.getKeyChar();
		if (c == '\u007f') {
			return;  /* Ignore the DEL key */
		} else if (c == '\b' && !inputBuffer.isEmpty()) {
			inputBuffer.remove(inputBuffer.size() - 1);
			try {
				theDocument.remove(theDocument.getLength() - 1, 1);
			} catch (Exception e) {/*BadLocationException not expected*/}
		} else {
			inputBuffer.add((byte)c);
			try {
				theDocument.insertString(theDocument.getLength(), 
						Character.toString(c), inTextAttr);
			} catch (Exception e) {/*BadLocationException not expected*/}
			if (c == '\n' || c == '\r') {
				synchronized (theTextPane) {
					theTextPane.notify();
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == copyButton) {
			theTextPane.selectAll();
			theTextPane.copy();
		} else if (src == clearButton) {
			theTextPane.setText("");
		}
	}

	public static void attach(JDialog d, String s) {
		if (me == null) {
			me = new StdioTerm(d, s);
			oldOut = System.out;
			oldErr = System.err;
			oldIn = System.in;
			System.setIn(in);
			System.setOut(out);
			System.setErr(err);
		}
	}

	public static void attach(JFrame f, String s) {
		if (me == null) {
			me = new StdioTerm(f, s);
			oldOut = System.out;
			oldErr = System.err;
			oldIn = System.in;
			System.setIn(in);
			System.setOut(out);
			System.setErr(err);
		} 
	}

	public static void attach(String title) {
		if (me == null) {
			me = new StdioTerm(title == null ? stdioTerminalTitle : title);
			oldOut = System.out;
			oldErr = System.err;
			oldIn = System.in;
			System.setIn(in);
			System.setOut(out);
			System.setErr(err);
		} 
	}

	public static void detach() {
		if (me != null) {
			System.setOut(oldOut);
			System.setErr(oldErr);
			System.setIn(oldIn);
			me = null;
			theFrame.dispose();
		}
	}

	public static void main(String args[]) {
		attach(null);
		getStdioNames();
		theFrame.setTitle(stdioTerminalTitle);
		if (stdioClassName == null)
			return;
		try {
			Class stdioClass = Class.forName(stdioClassName);
			Method main = stdioClass.getDeclaredMethod("main", 
					new Class[] {java.lang.String[].class});
			main.invoke(null, new Object[]{args});
                        System.out.flush();
                        System.out.println("Press enter to quit ...");
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        br.readLine();
			detach();
		} catch (Exception e) {
			System.err.printf("Cant invoke main() of \"%s\"\n", stdioClassName);
			if (e instanceof InvocationTargetException) {
				e.getCause().printStackTrace(System.err);
			} else
				System.err.println(e);
		}
	}

	private boolean terminalGeometrySet = false;
	private static String stdioClassName = null;
	private static String stdioTerminalTitle = "Terminal";
	boolean inputEofSeen = false;
	private static InputStream in = null, oldIn = null;
	Vector<Byte> inputBuffer = new Vector<Byte>(256);
	private Window parent = null;
	private static JFrame theFrame = null;
	private static StdioTerm me = null;
	private JTextPane theTextPane = new JTextPane();
	private Caret theCaret = null;
	Document theDocument = theTextPane.getDocument();
	private SimpleAttributeSet inTextAttr = new SimpleAttributeSet();
	private SimpleAttributeSet errTextAttr = new SimpleAttributeSet();
	private SimpleAttributeSet outTextAttr = new SimpleAttributeSet();
	private static PrintStream out = null, err = null;
	private static PrintStream oldOut = null, oldErr = null;
	private JButton copyButton = new JButton("Copy"), clearButton = new JButton("Clear");
}
