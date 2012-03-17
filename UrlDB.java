//
// The database for active URL:s
// (c) Jimmy Larsson 1998
//

import java.util.*;
import java.net.*;

public final class UrlDB 
{
  protected Vector urlLevels;
  protected int topUrlLevel;
  protected HunterStatistics stats;
  protected Hashtable visitedUrls;

  public UrlDB (HunterStatistics stat)
  {
    stats = stat;
    topUrlLevel = 1;
    stats.linkLevel(topUrlLevel);
    urlLevels = new Vector ();
    visitedUrls = new Hashtable ();
  }

  public int add (HunterUrl url)
  {
   synchronized (urlLevels)
   {
     if (url.getLevel() < topUrlLevel)
       return -1;

     if (visitedUrls.containsKey(url.toString()))
       return -2;  // Url already visited
     
     int relLevel = url.getLevel() - topUrlLevel;
     
     if ((relLevel + 1) > urlLevels.size())
     {
       Vector tmp_vec = new Vector ();
       tmp_vec.addElement(url);
       urlLevels.addElement(tmp_vec);
     }
     else
       ((Vector) urlLevels.elementAt(relLevel)).addElement(url);
     
     stats.addWaitingUrl (1);
     // Add URL to hashtable
     visitedUrls.put (url.toString(), url);
   
   }
  
   return 0;
  }

  public HunterUrl getNext ()
  {
    synchronized (urlLevels)
    {
      if (urlLevels.isEmpty())
	return null;
      
      Vector level = ((Vector) urlLevels.firstElement());
      
      if (level.isEmpty())
      {
	urlLevels.removeElement (level);
	return null;
      }
      
      HunterUrl tmpUrl = ((HunterUrl) level.firstElement());
      level.removeElement (tmpUrl);
      
      if (level.isEmpty())
      {
	urlLevels.removeElement (level);
	topUrlLevel++;
	stats.linkLevel(topUrlLevel);
      }
      
      stats.addWaitingUrl (-1);
      return tmpUrl;
    }
  }
      
  public int levels ()
  {
    return urlLevels.size();
  }

  public int topLevel ()
  {
    return topUrlLevel;
  }
}
