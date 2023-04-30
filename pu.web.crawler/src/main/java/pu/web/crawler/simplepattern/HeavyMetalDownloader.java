package pu.web.crawler.simplepattern;

import java.io.IOException;

import nl.mediacenter.services.Range;

import pu.web.crawler.FileDownloader;

public class HeavyMetalDownloader extends AbstractSimplePatternDownloader
{
private static final String URL = "http://www.heavymetalmagazinefanpage.com/";
private static final String TARGET = "heavymetal";

private final Range range;
private final String baseFileName;


public static void main( String [] args ) throws IOException
{
	new HeavyMetalDownloader( new Range( 77, 99 ), "hmlist" ).run();
	new HeavyMetalDownloader( new Range( 00, 11 ), "hmlist" ).run();
	new HeavyMetalDownloader( new Range( 82, 86 ), "hmlistsp" ).run();
	new HeavyMetalDownloader( new Range( 89, 89 ), "hmlistsp" ).run();
	new HeavyMetalDownloader( new Range( 92, 99 ), "hmlistsp" ).run();
	new HeavyMetalDownloader( new Range( 00, 11 ), "hmlistsp" ).run();
	
	downloadFile( "hmlistcovers.html" );
	downloadFile( "hmlistcoversb.html" );
}
public HeavyMetalDownloader( Range aRange, String aBaseFileNamwe )
{
	super();
	range = aRange;
	baseFileName = aBaseFileNamwe;
}

private static void downloadFile( String filenaam ) throws IOException
{
	FileDownloader.download( URL + filenaam, TARGET + "\\" + filenaam );
}

@Override
public Range getRange()
{
	return range;
}

public String getBaseFileName()
{
	return baseFileName;
}

@Override
public String makeTarget( int aNumber )
{
	return TARGET + "/" + makeFileName( aNumber );
}

@Override
public String makeUrl( int aNumber )
{
	return URL + makeFileName( aNumber );
}
private String makeFileName( int aNumber )
{
	return baseFileName + getFormat2().format( aNumber ) + ".html";
}
}
