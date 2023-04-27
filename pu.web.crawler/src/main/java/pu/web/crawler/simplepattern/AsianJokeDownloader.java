package pu.web.crawler.simplepattern;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;

public class AsianJokeDownloader
{
private static final String TARGET = "/home/purbanus/Pictures/vrouwen/asianjokes";
private final NumberFormat format = new DecimalFormat( "000" );

public static void main( String [] args )
{
	new AsianJokeDownloader().run();
}

public void run()
{
	for ( int x = 1; x < 999; x++ )
	{
		try
		{
			if ( x % 50 == 0 )
			{
				Thread.sleep( 10000 );
			}
			verwerk( x );
		}
		catch ( Exception e )
		{
			//System.out.println( "FOUT BIJ " + x );
			e.printStackTrace();
			return;
		}
	}
}

private void verwerk( int aX ) throws IOException
{
	final String file = "mai-sakashita-" + format.format( aX ) + ".jpg";
	final String URL = "http://www2.tokyosex.org/model/mai-sakashita-01/images/" + file;
	final String target = TARGET + "/" + file;
	FileUtil.mkDirs( target );
	FileDownloader.download( URL, target );
	System.out.println( target );
}

}
