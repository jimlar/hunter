//
// URL object for hunter
// (c) Jimmy Larsson 1998
//

import java.net.*;

public final class HunterUrl
{
  // urlCount used when saving URLs
  protected int urlCount;
  protected int urlLevel;
  protected URL url;

  public HunterUrl (URL newUrl, int level)
  {
    url = newUrl;
    urlLevel = level;
    urlCount = 0;
  }

  public int getLevel ()
  {
    return urlLevel;
  }

  public void setLevel (int level)
  {
    urlLevel = level;
  }

  public URL getURL ()
  {
    return url;
  }

  public void setURL (URL newUrl)
  {
    url = newUrl;
  }    

  public int getCount ()
  {
    return urlCount;
  }

  public void setCount (int newCount)
  {
    urlCount = newCount;
  }    

  public String toString ()
  {
    return url.toString ();
  }
}
