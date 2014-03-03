package edu.muniz.universeguide.test;

import java.io.InputStream;
import java.net.URL;

public class TestURL {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		URL url = new URL("http://api.hostip.info/get_html.php?ip=187.21.228.122");
		String page = readPage(url);
	    int end = page.indexOf("(");
	    String country = page.substring(8,end-1);	
	    System.out.println(country);
	}

	private static String readPage(URL url) throws Exception {
        InputStream in = url.openStream();
	    StringBuffer sb = new StringBuffer();

	    byte [] buffer = new byte[256];

	    while(true){
	        int byteRead = in.read(buffer);
	        if(byteRead == -1)
	            break;
	        for(int i = 0; i < byteRead; i++){
	            sb.append((char)buffer[i]);
	        }
	    }
	    return sb.toString();

    }
	
}
