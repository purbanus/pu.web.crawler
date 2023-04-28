package pu.web.crawler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Windows1252ToUtf8Converter
{
private static final String VALUE = "á, é, í, ó, ú, ü, ñ, ¿";
public static void main( String [] args ) throws UnsupportedEncodingException, CharacterCodingException
{
	//new Windows1252ToUtf8Converter().convert1();
	//new Windows1252ToUtf8Converter().convert2();
	new Windows1252ToUtf8Converter().convert2Value();
	//new Windows1252ToUtf8Converter().convert3();
}
private void convert1()
{
	String value = VALUE;
	String retValue = "";
	String convertValue2 = "";
	ByteBuffer convertedBytes = null;
	try
	{
		CharsetEncoder encoder2 = Charset.forName( "Windows-1252" )
		    .newEncoder();
		CharsetEncoder encoder3 = Charset.forName( "UTF-8" )
		    .newEncoder();
		System.out.println( "value = " + value );

		assert encoder2.canEncode( value );
		assert encoder3.canEncode( value );

		ByteBuffer conv1Bytes = encoder2.encode( CharBuffer.wrap( value.toCharArray() ) );

		retValue = new String( conv1Bytes.array(), Charset.forName( "Windows-1252" ) );

		System.out.println( "retValue = " + retValue );

		convertedBytes = encoder3.encode( CharBuffer.wrap( retValue.toCharArray() ) );
		convertValue2 = new String( convertedBytes.array(), Charset.forName( "UTF-8" ) );
		System.out.println( "convertedValue =" + convertValue2 );
	}
	catch ( Exception e )
	{
		e.printStackTrace();
	}
}
public void convert2() throws UnsupportedEncodingException
{
	final Charset windowsCharset = Charset.forName("windows-1252");
	final Charset utfCharset = Charset.forName("UTF-16");
	final CharBuffer windowsEncoded = windowsCharset.decode(ByteBuffer.wrap(new byte[] {(byte) 0x91}));
	final byte[] utfEncoded = utfCharset.encode(windowsEncoded).array();
	System.out.println(new String(utfEncoded, utfCharset.displayName()));
}
public void convert2Value() throws UnsupportedEncodingException
{
	final Charset windowsCharset = Charset.forName("windows-1252");
	final Charset utfCharset = Charset.forName("UTF-16");
	final CharBuffer windowsEncoded = windowsCharset.decode(ByteBuffer.wrap( VALUE.getBytes() ) );
	final byte[] utfEncoded = utfCharset.encode( windowsEncoded ).array();
	System.out.println(new String(utfEncoded, utfCharset.displayName()));
}
// Dit werkt niet, allemaal vraagtekens
public void convert3() throws UnsupportedEncodingException, CharacterCodingException
{
	// Dit maakt windows-1252 van onze unicode string
	final CharsetEncoder windowsCharset = Charset.forName("windows-1252").newEncoder();
	// Dit maakt utf-16 van onze windows-1252 string
	final CharsetDecoder utfCharset = Charset.forName("UTF-16").newDecoder();
	final ByteBuffer windowsEncoded = windowsCharset.encode( CharBuffer.wrap( VALUE.toCharArray() ) );
	final CharBuffer utfEncoded = utfCharset.decode( windowsEncoded );
	System.out.println(new String( utfEncoded.array() ) );
}

}
