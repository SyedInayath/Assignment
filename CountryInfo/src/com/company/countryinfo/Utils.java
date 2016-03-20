package com.company.countryinfo;

import java.io.InputStream;
import java.io.OutputStream;

/* 
 * Class storing constant and utility functions
 * 
 * */
public class Utils {
	public static final String URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";
	/* The below constant will go as strings and can be changed based on language change */
	public static final String NO_NETWORK_MSG1 = "Connect to wifi or quit";
	public static final String CONNECT_NETWORK= "Connect to WIFI";
	public static final String QUIT = "Quit";
	public static final String WIFI = "WIFI";
	public static final String MOBILE = "MOBILE";

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try  {
            byte[] bytes=new byte[buffer_size];
            for(;;) {
              //Read byte from input stream

              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;

              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
