/*
 * Created on 14-dec-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pu.web.crawler.tryout;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import nl.mediacenter.services.xml.JDom2Printer;

/**
 * Haalt de links uit een html pagina
 * Dit werkt niet; De Xerces DOM parser lust geen HTML 4 (waarschijnlijk wel xhtml, maar ja.
 */
public class HtmlLinkExtractor
{

/**
 * 
 */
public HtmlLinkExtractor()
{
	super();
	// TODO Auto-generated constructor stub
}

public static void main(String[] args)
{
	try
	{
		new HtmlLinkExtractor().verwerk();
	}
	catch (FactoryConfigurationError e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (SAXException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (ParserConfigurationException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (RuntimeException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private static void verwerk() throws FactoryConfigurationError, SAXException, IOException, ParserConfigurationException, RuntimeException
{
	String URI = "pu/xml/HTML Voorbeeld.html";
	
//	DocumentBuilderFactory parserFactory = DocumentBuilderFactory.newInstance();
//	//parserFactory.setCoalescing( isCoalescing() ); // default=false
//	//parserFactory.setExpandEntityReferences( isExpandEntityReferences() ); // default=true
//	//parserFactory.setIgnoringComments( true ); // default=false
//	//parserFactory.setIgnoringElementContentWhitespace( true ); // default=false
//	//parserFactory.setNamespaceAware( isNamespaceAware() ); // default=false
//	//parserFactory.setValidating( true ); // default=false
//	
//	Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( URI );
//	if ( document == null )
//	{
//		throw new RuntimeException( "Doc is null!!" );
//	}
	
	// @@NOG Dit werkt nog niet, hij kan die URI niet resolven
	new JDom2Printer().parseAndPrint( URI );
}
}
