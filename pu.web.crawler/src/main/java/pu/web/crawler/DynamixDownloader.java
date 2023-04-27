package pu.web.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamixDownloader
{
private static final String RESTART_MODEL = "maha";
private static final String RESTART_GALLERY = "SEI";
private static final String BASE_URL = "http://www.dynamix.net/gallery/";
private static final String INDEX_PAGE = BASE_URL + "findex.html";
private static final String TARGET_DIR = "/home/purbanus/Pictures/new/japan/dynamix/";

private static class ModelAndGallery
{
public final String model;
public final String gallery;
public ModelAndGallery( String aModel, String aGallery )
{
	super();
	model = aModel;
	gallery = aGallery;
}
}

boolean negerenModel = true;
boolean negerenGallery = true;

public static void main( String [] args )
{
	new DynamixDownloader().run();
}

private void run()
{
	try
	{
		runImpl();
		// verwerkModel( "akfu-index.html" );
		// verwerkGallery( "akfu-B@Friend-index.html" );
	}
	catch ( MalformedURLException e )
	{
		e.printStackTrace();
	}
	catch ( IOException e )
	{
		e.printStackTrace();
	}
}

private void runImpl() throws MalformedURLException, IOException
{
	// Het zijn simpel alle <a> tags
	List<String> tags = readAllTagsSafely( INDEX_PAGE );
	for ( String tag : tags )
	{
		String lTag = tag.toLowerCase();
		//if ( lTag.startsWith( "<img" ) && hasAttribute( lTag, "alt" ) )
		if ( lTag.startsWith( "<a" ) && HtmlUtil.hasAttribute( lTag, "href" ) )
		{
			String indexHtml = HtmlUtil.extractAttribute( tag, "href" );
			//System.out.println( tag );
			if ( indexHtml.endsWith( "-index.html" ) )
			{
				if ( negerenModel /* && RESTART_MODEL != null */ )
				{
					String model = extractModel( indexHtml );
					if ( model.equals( RESTART_MODEL ) )
					{
						negerenModel = false;
					}
				}
				if ( ! negerenModel )
				{
					verwerkModel( indexHtml );
				}
			}
		}
	}
}

private void verwerkModel( String aModelPage ) throws MalformedURLException, IOException
{
	String model = extractModel( aModelPage );
	String url = BASE_URL + aModelPage;
	List<String> tags = readAllTagsSafely( url );
	for ( String tag : tags )
	{
		String lTag = tag.toLowerCase();
		if ( lTag.startsWith( "<a" ) && HtmlUtil.hasAttribute( lTag, "href" ) )
		{
			String href = HtmlUtil.extractAttribute( tag, "href" );
			if ( href.startsWith( model ) && ! href.contains( "index2" ) )
			{
				if ( negerenGallery /* && RESTART_GALLERY != null */ )
				{
					ModelAndGallery mag = extractModelAndGallery( href );
					if ( mag.gallery.equals( RESTART_GALLERY ) )
					{
						negerenGallery = false;
					}
				}
				if ( ! negerenGallery )
				{
					verwerkGallery( href );
				}
			}
		}
	}
	System.out.println();
}

private void verwerkGallery( String aGalleryPage ) throws MalformedURLException, IOException
{
	ModelAndGallery mag = extractModelAndGallery( aGalleryPage );
	String url = BASE_URL + aGalleryPage;
	// Het zijn alle <a> tags met een href die begint met het model
	List<String> tags = readAllTagsSafely( url );

	// Verzamel eerst de subpages
	Set<String> subPages = extractGallerySubpages( mag, tags );

	// Images verwerken
	verwerkImages( mag, tags, url );
	for ( String subPage : subPages )
	{
		url = BASE_URL + subPage;
		verwerkImages( mag, readAllTagsSafely( url ), url );
	}
}

private List<String> readAllTagsSafely( String url )
{
	try
	{
		return new TagReader( url ).getAllTags();
	}
	catch ( MalformedURLException e )
	{
		System.err.println( "Fout bij pagina " + url );
		e.printStackTrace();
	}
	catch ( IOException e )
	{
		System.err.println( "Fout bij pagina " + url );
		e.printStackTrace();
	}
	return Collections.EMPTY_LIST;
}

private Set<String> extractGallerySubpages( ModelAndGallery mag, List<String> tags )
{
	Set<String> subPages = new HashSet<String>();
	for ( String tag : tags )
	{
		String lTag = tag.toLowerCase();
		if ( lTag.startsWith( "<a" ) && HtmlUtil.hasAttribute( lTag, "href" ) )
		{
			String href = HtmlUtil.extractAttribute( tag, "href" );
			if ( href.startsWith( mag.model + "-" + mag.gallery ) )
			{
				int lastPartIndex = href.indexOf( "-index" );
				if ( lastPartIndex >= 0 && href.endsWith( ".html" ) )
				{
					// Nu alleen nog de eerste weglaten want die hebben we al gelezen
					if ( ! href.endsWith( "-index.html" ))
					{
						subPages.add( href );
					}
				}
			}
		}
	}
	return subPages;
}

private void verwerkImages( ModelAndGallery aMag, List<String> aTags, String aReferer ) throws IOException
{
	String modelGallery = aMag.model + "/" + aMag.gallery + "/";
	for ( String tag : aTags )
	{
		String lTag = tag.toLowerCase();
		if ( lTag.startsWith( "<a" ) && HtmlUtil.hasAttribute( lTag, "href" ) )
		{
			String href = HtmlUtil.extractAttribute( tag, "href" );
			if ( href.startsWith( modelGallery ) )
			{
				downloadImage( href, aReferer );
			}
		}
	}
}

private static int downloadCounter = 0;
private void downloadImage( String href, String aReferer )
{
	String url = BASE_URL + href;
	String targetName = TARGET_DIR + href;
	targetName = targetName.replace( '@', '-' );
	FileUtil.mkDirs( targetName );
	try
	{
		FileDownloader.download( url, targetName, aReferer );
	}
	catch ( IOException e )
	{
		System.err.println( "Fout bij downloaden " + url );
		e.printStackTrace();
	}
	System.out.println( targetName );
	if ( ++ downloadCounter == 50 )
	{
		sleep( 10 );
		downloadCounter = 0;
	}
}

private void sleep( int aSeconds )
{
	try
	{
		Thread.sleep( aSeconds * 1000 );
	}
	catch ( InterruptedException e )
	{
		e.printStackTrace();
	}
}

private String extractModel( String aModelPage )
{
	int minIndex = aModelPage.indexOf( "-index" );
	if ( minIndex < 0 )
	{
		System.err.println( "Model niet gevonden in URL!!" );
	}
	return aModelPage.substring( 0, minIndex );
}

// akfu-B@Friend-index.html
private ModelAndGallery extractModelAndGallery( String aGalleryPage )
{
	int leftIndex = aGalleryPage.indexOf( "-" );
	if ( leftIndex < 0 )
	{
		throw new RuntimeException( "Model niet gevonden in URL!!" );
	}
	String model = aGalleryPage.substring( 0, leftIndex );
	leftIndex++;
	int rightIndex = aGalleryPage.indexOf( "-index" );
	if ( rightIndex < 0 )
	{
		throw new RuntimeException( "Gallery niet gevonden in URL!!" );
	}
	String gallery = aGalleryPage.substring( leftIndex, rightIndex );
	return new ModelAndGallery( model, gallery );
}


}
