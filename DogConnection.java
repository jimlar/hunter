//
// Class for connecting to webservers and downloading/searching pages...
// (C) Jimmy Larsson 1998
//

import java.net.*; 
import java.io.*;
import java.util.*;

public final class DogConnection
{
  protected URL connectedUrl;
  protected URLConnection connection;
  protected BufferedReader input = null;
  protected DogConnectionContent content;
  protected HunterStatistics stats;
  protected DogSearcher searcher;
  protected DogLinkFilter linkFilter;
  protected boolean connected;
  protected int dogNo;

  public DogConnection (HunterStatistics stat, DogConnectionContent cont)
  {
    content = cont;
    dogNo = content.getDogNo();
    stats = stat;
    searcher = new DogSearcher(content);
    linkFilter = new DogLinkFilter (stats, content);
    connected = false;
  }

  // Connect, if already connected, disconnect first!
  public void connect (HunterUrl url) throws IOException
  {
    connected = false;
    connection = url.getURL().openConnection();
    connection.connect();
    connectedUrl = connection.getURL();
   
    // Get the stream
    InputStream inStream = connection.getInputStream();
    InputStreamReader inStreamReader = new InputStreamReader (inStream);
    input = new BufferedReader (inStreamReader);

    int contentLength = connection.getContentLength();
    String cont;

    if (contentLength > -1)  // Content length is known, good... =)
    {
      stats.setStatus(dogNo, "receiving " + contentLength + " bytes from " + connectedUrl.toString());

      char[] cbuf = new char[contentLength];
      input.read (cbuf, 0, contentLength);

      cont = new String (cbuf);
    }
    else                         // Content length unknown, read in 1Kbyte chunks
    {
      stats.setStatus(dogNo, "receiving [unknown] bytes from " + connectedUrl.toString());

      char[] cbuf = new char[1024];
      int pos = 0;
      cont = new String ("");
     
      while (pos > -1)
      {
	pos = input.read(cbuf, 0, 1024);
	cont = cont.concat (new String (cbuf));
      }
    }

    content.set (cont, connection.getContentType(), connection.getHeaderField("title"), content.getDogNo());

    input.close();       // Close reader
    // Close streams
    if(connection.getDoInput())
      inStream.close();          
    if (connection.getDoOutput())
      connection.getOutputStream().close();

    connected = true;
  }

  public boolean connected ()
  {
    return connected;
  }

  public Vector getLinks ()
  {
    return linkFilter.getLinks (connectedUrl);
  }

  public URL getURL ()
  {
    return connectedUrl;
  }

  public int search (DogSearchExp searchExp)
  {
    return searcher.search (searchExp);
  }
}
  
