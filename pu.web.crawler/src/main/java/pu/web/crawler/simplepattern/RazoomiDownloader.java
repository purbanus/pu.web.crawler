package pu.web.crawler.simplepattern;

import java.io.IOException;

import pu.web.crawler.FileDownloader;
import pu.web.crawler.FileUtil;

public class RazoomiDownloader
{
private static final String TARGET = "/home/purbanus/Pictures/vrouwen/razooma-medium";

public static void main( String [] args )
{
	new RazoomiDownloader().run();
}

public void run()
{
	for ( int x = 177; x < 1000; x++ )
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
			//e.printStackTrace();
		}
	}
}

private void verwerk( int aX ) throws IOException
{
	final String file = "big_gphoto" + aX;
	final String URL = "http://www.razooma.net/img/usr/" + file;
	final String target = TARGET + "/" + file + ".jpeg";
	FileUtil.mkDirs( target );
	FileDownloader.download( URL, target );
	System.out.println( target );
}

}
