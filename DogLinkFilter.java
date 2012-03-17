//
// Linkfilter, used to get all links from a homepage
// (c) Jimmy Larsson
//

import java.util.*;
import java.net.*;


public final class DogLinkFilter
{
  protected DogConnectionContent content;
  protected URL connectedUrl;
  protected HunterStatistics stats;

  public DogLinkFilter (HunterStatistics stat, DogConnectionContent cont)
  {
    stats = stat;
    content = cont;
  }

  public void setURL (URL url)
  {
    connectedUrl = url;
  }

  // Filter out all links and return them in a Vector
  public Vector getLinks (URL url)
  {
    setURL (url);
    return getLinks();
  }

  // Filter out all links and return them in a Vector
  public Vector getLinks ()
  {
    String newUrl;
    Vector returnVector = new Vector ();
    int startIndex;
    int endIndex;
    URL tempURL;

    startIndex = endIndex = 0;
      
    while (startIndex >= 0)
    {
      // Catches both "<a" for normal links and "<area" for image maps
      startIndex = content.getDataLowerCase().indexOf ("<a",startIndex);

      if (startIndex >= 0)
	startIndex = content.getDataLowerCase().indexOf ("href",startIndex) ;
      
      if (startIndex >= 0)
      {
	startIndex = content.getDataLowerCase().indexOf ("\"",startIndex) ;
	endIndex = content.getDataLowerCase().indexOf ("\"", startIndex + 1);
      }
      if (startIndex >= 0 && endIndex >= 0)
      {    
	newUrl = content.getData().substring (startIndex + 1, endIndex);
	// Remove trailing dots
	if (newUrl.endsWith("./") || newUrl.endsWith("."))
	    newUrl = newUrl.substring (0, newUrl.lastIndexOf("."));

	// Remove trailing #index, type of things... =)
	if (newUrl.lastIndexOf("#") > -1)
	    newUrl = newUrl.substring (0, newUrl.lastIndexOf("#"));

	try {
	  tempURL = new URL (newUrl);
	  returnVector.addElement (tempURL);
	} catch (MalformedURLException e1)
	{
	  // No absolute link, try relative link...
	  try {
	    String trimmedUrl = connectedUrl.toString();

	    // The connected url is a file
	    if (!trimmedUrl.endsWith("/") && (trimmedUrl.toLowerCase().indexOf(".htm") > -1))
	      trimmedUrl = trimmedUrl.substring (0, trimmedUrl.lastIndexOf("/") + 1);
	      
	    // The connected Url i a catalog, add a slash if needed
	    if (!trimmedUrl.endsWith("/") && (trimmedUrl.toLowerCase().indexOf(".htm") < 0))
	      trimmedUrl = trimmedUrl.concat ("/");

	    tempURL = new URL (trimmedUrl + newUrl);
	    returnVector.addElement (tempURL);
	  } catch (MalformedURLException e2)
	  {
	    stats.addBadUrl ();
	  }
	}
      }
    }
    
    // FOR DEBUGGING
    //System.out.println ("getLinks: " + returnVector.toString ());
    
    return returnVector;
  }
}
