/*
 * Created on 14-dec-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pu.web.crawler.tryout;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nl.mediacenter.services.FileHelper;

import pu.web.crawler.UriGetter;


/** A simple HTML Link Checker. 
 * Need a Properties file to set depth, URLs to check. etc.
 * Responses not adequate; need to check at least for 404-type errors!
 * When all that is (said and) done, display in a Tree instead of a TextArea.
 * Then use Color coding to indicate errors.
 * <p>
 * Further, it needs to use Swing and Threads properly (see
 * Java Swing, section on "MultiThreading Issues with Swing".
 * As it stands, the GUI thread is locked up until the complete
 * checking is completed, which could take a long time.
 * <p>
 * TODO: parse using an XML parser, though this would fail for most web
 * sites today (and how much longer do you think we must maintain
 * so much ad-hoc code for parsing the almost-regular kludge known as HTML?)
 *
 * @author Ian Darwin, Darwin Open Systems, www.darwinsys.com.
 * @version $Id: LinkChecker.java,v 1.1 2006/10/26 19:02:23 purbanus Exp $
 */
public class LinkChecker extends JFrame
{
	/** The "global" activation flag: set false to halt. */
	protected boolean done = false;

	/** The textfield for the starting URL.
	 * Should have a Properties file and a JComboBox instead.
	 */
	protected JTextField textFldURL;
	protected JButton checkButton;
	protected JButton saveButton;
	protected JButton killButton;
	protected JTextArea textWindow;
	protected int indent = 0;

	public static void main(String[] args)
	{
		final String START = "http://localhost";
		LinkChecker lc = new LinkChecker();
		lc.textFldURL.setText( START );
		lc.setVisible(true);
	}

/** Construct a LinkChecker */
public LinkChecker()
{
	super("LinkChecker");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Container cp = getContentPane();
	cp.setLayout(new BorderLayout());
	JPanel p = new JPanel();
	p.setLayout(new FlowLayout());
	p.add(new JLabel("URL"));
	p.add(textFldURL = new JTextField(30));
	p.add(checkButton = new JButton("Check URL"));

	// Make a single action listener for both the text field (when
	// you hit return) and the explicit "Check URL" button.
	ActionListener starter = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			done = false;
			checkButton.setEnabled(false);
			killButton.setEnabled(true);
			Thread t = new Thread()
			{
				@Override
				public void run()
				{
					textWindow.setText("Checking...");
					checkOut(textFldURL.getText());
					textWindow.append("-- All done --");
				}
			};
			t.start();
		}
	};
	textFldURL.addActionListener(starter);
	checkButton.addActionListener(starter);
	p.add(killButton = new JButton("Stop"));
	killButton.setEnabled(false); // until startChecking is called.
	killButton.addActionListener(new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			done = true;
			checkButton.setEnabled(true);
			killButton.setEnabled(false);
		}
	});
	p.add(saveButton = new JButton("Save Log"));
	saveButton.addActionListener(new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				String log = textWindow.getText();
				String fileName = "linkchecker.log";
				//FileIO.stringToFile(log, fileName);
				FileHelper.writeFile( fileName, log );
				JOptionPane.showMessageDialog(
					LinkChecker.this,
					"File saved as " + fileName,
					"Done",
					JOptionPane.INFORMATION_MESSAGE);
			}
			catch (IOException ex)
			{
				JOptionPane.showMessageDialog(
					LinkChecker.this,
					"IOError",
					ex.toString(),
					JOptionPane.ERROR_MESSAGE);
			}
		}
	});
	// Now lay out the main GUI - URL & buttons on top, text larger
	cp.add("North", p);
	textWindow = new JTextArea(80, 40);
	cp.add("Center", new JScrollPane(textWindow));
	//UtilGUI.maximize(this);
	maximize( this );
}

/** Maximize a window, the hard way. */
public static void maximize(Window w) {
//	Dimension us = w.getSize();
	Dimension them = Toolkit.getDefaultToolkit().getScreenSize();
	w.setBounds(0,0, them.width, them.height);
}

/** Start checking, given a URL by name.
 * Calls checkLink to check each link.
 */
public void checkOut(String rootURLString)
{
	URL rootURL = null;
	UriGetter urlGetter = null;

	if (done)
		return;
	if (rootURLString == null)
	{
		textWindow.append("checkOut(null) isn't very useful");
		return;
	}

	// Open the root URL for reading. May be a filename or a real URL.
	try
	{
		try
		{
			rootURL = new URL(rootURLString);
		}
		catch (MalformedURLException e)
		{
			// Neat Trick: if not a valid URL, try again as a file.
			rootURL = new File(rootURLString).toURL();
		}
		// Either way, now try to open it.
		urlGetter = new UriGetter(rootURL);
	}
	catch (FileNotFoundException e)
	{
		textWindow.append("Can't open file " + rootURLString + "\n");
		return;
	}
	catch (IOException e)
	{
		textWindow.append("openStream " + rootURLString + " " + e + "\n");
		return;
	}

	// If we're still here, the root URL given is OK.
	// Next we make up a "directory" URL from it.
	String rootURLdirString;
	if (rootURLString.endsWith("/") || rootURLString.endsWith("\\"))
	{
		rootURLdirString = rootURLString;
	}
	else
	{
		rootURLdirString = rootURLString.substring(0, rootURLString.lastIndexOf('/')); // XXX or \
	}

	try
	{
		List<String> urlTags = urlGetter.getUris();
		Iterator<String> urlIterator = urlTags.iterator();
		while (urlIterator.hasNext())
		{
			if (done)
				return;
			String tag = urlIterator.next();
			//Debug.println("TAG", tag);
			System.out.println( "TAG" + tag );

			String href = extractHREF(tag);

			for (int j = 0; j < indent; j++)
				textWindow.append("\t");
			textWindow.append(href + " -- ");

			// Can't really validate these!
			if (href.startsWith("mailto:"))
			{
				textWindow.append(href + " -- not checking\n");
				continue;
			}

			if (href.startsWith("..") || href.startsWith("#"))
			{
				textWindow.append(href + " -- not checking\n");
				// nothing doing!
				continue;
			}

			URL hrefURL = new URL(rootURL, href);

			// TRY THE URL.
			// (don't combine previous textWindow.append with this one,
			// since this one can throw an exception)
			textWindow.append(checkLink(hrefURL));

			// There should be an option to control whether to
			// "try the url" first and then see if off-site, or
			// vice versa, for the case when checking a site you're
			// working on on your notebook on a train in the Rockies
			// with no web access available.

			// Now see if the URL is off-site.
			if (!hrefURL.getHost().equals(rootURL.getHost()))
			{
				textWindow.append("-- OFFSITE -- not following\n");
				continue;
			}
			textWindow.append("\n");

			// If HTML, check it recursively. No point checking
			// PHP, CGI, JSP, etc., since these usually need forms input.
			// If a directory, assume HTML or something under it will work.
			if (href.endsWith(".htm") || href.endsWith(".html") || href.endsWith("/"))
			{
				++indent;
				if (href.indexOf(':') != -1)
					checkOut(href); // RECURSE
				else
				{
					String newRef = rootURLdirString + '/' + href;
					checkOut(newRef); // RECURSE
				}
				--indent;
			}
		}
		urlGetter.close();
	}
	catch (IOException e)
	{
		System.err.println("Error: (" + e + ")");
	}
}

/** Check one link, given its DocumentBase and the tag */
public String checkLink(URL linkURL)
{

	try
	{
		// Open it; if the open fails we'll likely throw an exception
		URLConnection luf = linkURL.openConnection();
		if (linkURL.getProtocol().equals("http"))
		{
			HttpURLConnection huf = (HttpURLConnection) luf;
			String s = huf.getResponseCode() + " " + huf.getResponseMessage();
			if (huf.getResponseCode() == -1)
				return "Server error: bad HTTP response";
			return s;
		}
		else if (linkURL.getProtocol().equals("file"))
		{
			try ( InputStream is = luf.getInputStream(); )
			{	
				// If that didn't throw an exception, the file is probably OK
				return "(File)";
			}
		}
		else
			return "(non-HTTP)";
	}
	catch (SocketException e)
	{
		return "DEAD: " + e.toString();
	}
	catch (IOException e)
	{
		return "DEAD";
	}
}

/** Read one tag. Adapted from code by Elliott Rusty Harold */
public String readTag(BufferedReader is)
{
	StringBuffer theTag = new StringBuffer("<");
	int i = '<';

	try
	{
		while (i != '>' && (i = is.read()) != -1)
			theTag.append((char) i);
	}
	catch (IOException e)
	{
		System.err.println("IO Error: " + e);
	}
	catch (Exception e)
	{
		System.err.println(e);
	}

	return theTag.toString();
}

/** Extract the URL from <sometag attrs HREF="http://foo/bar" attrs ...> 
 * We presume that the HREF is correctly quoted!!!!!
 * TODO: Handle Applets.
 */
public String extractHREF(String tag) throws MalformedURLException
{
	String caseTag = tag.toLowerCase(), attrib;
	int p1, p2, p3, p4;

	if (caseTag.startsWith("<a "))
		attrib = "href"; // A
	else
		attrib = "src"; // image, frame
	p1 = caseTag.indexOf(attrib);
	if (p1 < 0)
	{
		throw new MalformedURLException("Can't find " + attrib + " in " + tag);
	}
	p2 = tag.indexOf("=", p1);

	// This fails to handle unquoted href, which some dinosaurs insist
	// on using, saying the parser can sort it out. Phhhhhhhht!!!!
	p3 = tag.indexOf("\"", p2);
	p4 = tag.indexOf("\"", p3 + 1);
	if (p3 < 0 || p4 < 0)
	{
		throw new MalformedURLException("Invalid " + attrib + " in " + tag);
	}
	String href = tag.substring(p3 + 1, p4);
	return href;
}
}
