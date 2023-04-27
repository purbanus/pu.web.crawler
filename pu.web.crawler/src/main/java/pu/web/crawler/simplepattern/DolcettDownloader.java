package pu.web.crawler.simplepattern;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;
import pu.web.crawler.HtmlUtil;
import pu.web.crawler.TagReader;

public class DolcettDownloader
{
private static final String BASE_URL   = "http://zizki.com/dolcett/heads-or-tails/";
private static final String TARGET_DIR = "/home/purbanus/projecten/Pictures/new/dolcett/Heads-or-Tails";
private static final String REFERRER = "http://zizki.com/dolcett";
private final NumberFormat format2 = new DecimalFormat( "00" );
@SuppressWarnings( "unused" )
private final NumberFormat format4 = new DecimalFormat( "0000" );

public static void main( String [] args ) throws IOException
{
	new DolcettDownloader().run();
}

public void run() throws IOException
{
	verwerk();
}

private void verwerk() throws IOException
{
	final String urlString = BASE_URL;

	// Als het de tagreader niet lukt om een pagina te lezen kappen we
	TagReader tagReader = new TagReader( urlString );
	List<String> tags = tagReader.getAllTags();
	List<String> imgUrls = findOurImages( tags );

	int index = 0;
	for ( String url : imgUrls )
	{
		index++;

		final String target = TARGET_DIR + "/img" + format2.format( index ) + "jpg";
		FileUtil.mkDirs( target );
		try
		{
			FileDownloader.download( url, target, REFERRER );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		System.out.println( url );
	}
}

private List<String> findOurImages( List<String> aTags )
{
	// We hebben nu een pagina met 10 images.Die moeten we eruit peuteren. Er zijn twee vormen:
	// Een embedded image
	//   <img src="http://27.media.tumblr.com/tumblr_lwkbqqA5d71qd33kzo1_500.jpg" alt=""/>
	// Een link.
	//   <a href="http://www.tumblr.com/photo/1280/14732563541/1/tumblr_ljju0s4ezc1qilvnj">
	//   	<img src="http://28.media.tumblr.com/tumblr_ljju0s4ezc1qilvnjo1_500.jpg" alt=""/>
	//   </a>
	// Er staan verder geen images op een page, dus we doen
	// - alle <img> tags
	// - behalve als die rechtstreeks voorafgegaan wordt door een <a>
	// Goed:
	// <a href="http://zizki.com/ien/styles/large/public/comics/dolcett/e258561947.jpg?itok=vEWQPz06" title="" class="colorbox init-colorbox-processed cboxElement" rel="gallery-node-302-GiaJT6sytUw" url="<a href=&quot;/upgrade&quot;>Upgrade to view in full resolution</a>">
	//   <img typeof="foaf:Image" src="http://zizki.com/ien/styles/medium/public/comics/dolcett/e258561947.jpg?itok=JUqVlsZ9" alt="Heads or tails by Dolcett" title="Heads or tails by Dolcett" width="320" height="320"></a>
	
	//Fout:
	// <a href="/dolcett/club-x">
	//	 <img typeof="foaf:Image" src="http://zizki.com/ien/styles/thumbnail/public/comics/dolcett/e545274397.jpg?itok=Vib6Z3Cd" alt="Club X by Dolcett" title="Club X by Dolcett" width="110" height="160"></a>
	List<String> links = new ArrayList<String>();
	String currentAtag = null;
	for ( String tag : aTags )
	{
		String tagLC = tag.toLowerCase();
		if ( tagLC.startsWith( "<a " ) && ! tagLC.startsWith( "<a href=\"/dolcett\"" ) )
		{
			currentAtag = tag;
		}
		if ( tagLC.startsWith( "<img " ) )
		{
			if ( currentAtag != null )
			{
				links.add( HtmlUtil.extractAttribute( tag, "src" ) );
				currentAtag = null;
			}
		}
	}
	return links;
}


}
