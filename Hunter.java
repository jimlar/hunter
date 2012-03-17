//
// Hunter, an internet search agent
// (c) Jimmy Larsson 1998
//

import java.net.*;
import java.util.*;

public final class Hunter
{
  public static final int MAX_DOGS = 99;
  public static final int MIN_DOGS = 2;
  public static final int DEFAULT_DOGS = 8;

  public final int noOfDogs = 15;
  protected HunterStatistics stats;
  protected UrlDB searchDB;
  protected DogSearchExp searchExp;
  protected HunterResult results;
  protected Vector dogs;
  protected Display display;

  public Hunter ()
  {
    display = new Display ("Hunter", this);
  }    


  public void startSearch (DogSearchExp srchExp, String startURL)
  {
    stats = new HunterStatistics (display);
    UrlDB searchDB = new UrlDB (stats);
    searchExp = srchExp;
    results = new HunterResult ("HunterResults", stats);

    // Add an URL to the DB, if startURL given
    if (!startURL.equalsIgnoreCase(""))
    {
	try {
	    searchDB.add (new HunterUrl (new URL (startURL), 1)); 
	} catch (MalformedURLException e1) {
	    return;
	}
    }

    // Fire up some Dogs
    Dog.noOfDogs = 0;
    dogs = new Vector ();

    for (int i = 0; i < noOfDogs; i++)
      dogs.addElement(new Dog (searchDB, searchExp, results, stats));    

    stats.setHunterStatus ("Searching...");
  }

  public void stopSearch ()
  {
      if (stats != null)
	  stats.setHunterStatus ("Stopping...");

      for (Enumeration e = dogs.elements(); e.hasMoreElements();)
      {
	  ((Dog) e.nextElement()).signalStop();
      }      
  }

  //  Generate results so far
  public void dumpResults ()
  {
    results.dumpTempResults ();
    // should launch netscape here
  }
    
  // Quit
  public void quit ()
  {
    // No nice quitting here... just brake it all off! 
    System.exit(0);
  }

  // MAIN
  public static void main (String[] args)
  {
    Hunter hunter = new Hunter ();

    // Wait until all dogs are done
    while (Dog.noOfDogs() > 0)
    {
	Thread.yield ();
    }

    return;
  }
}
