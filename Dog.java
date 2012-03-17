//
// This is the class for all the search agents, The Dogs...
// (c) Jimmy Larsson
//

import java.net.*;
import java.util.*;
import java.io.*;

public final class Dog extends Thread
{
  // Class variables
  public static int noOfDogs = 0;
  protected static int activeDogs = 0;
  protected static Vector dogPack = new Vector ();
  protected static ThreadGroup dogThreads = new ThreadGroup ("Dogpack");

  // Instance variables
  protected DogConnection connection;
  protected UrlDB dataBase;
  protected DogSearchExp searchExp;
  protected HunterResult result;
  protected HunterStatistics stats;
  protected boolean done;
  protected boolean idle;
  protected int dogNo;
  protected DogConnectionContent content = new DogConnectionContent();
  protected boolean stopping;

  // Constructor
  public Dog (UrlDB db, DogSearchExp searchExp, HunterResult res, HunterStatistics stat)
  {
    super(dogThreads, "Dog " + (++noOfDogs));
    dogNo = noOfDogs;
    activeDogs++; //Start with dogs active
    content.set (null, null, null, dogNo);  // Set dog number in content object
    done = false;
    idle = false;
    connection = new DogConnection (stat, content);
    dataBase = db;
    result = res;
    stats = stat;
    stopping = false;
    dogPack.addElement(this);
    this.setPriority(Thread.MIN_PRIORITY);
    this.searchExp = searchExp;
    this.start();
  }
  
  // Class methods
  public static int noOfDogs ()
  {
    return noOfDogs;
  }

  public static ThreadGroup getThreads()
  {
    return dogThreads;
  }

  // Instance methods
  public int dogNo ()
  {
    return dogNo;
  }


  public void signalStop ()
  {
      stopping = true;
  }

  // Thread code...
  public void run ()
  {
    HunterUrl curUrl = dataBase.getNext();

    while (!stopping)
    {
      while (curUrl != null && !stopping)
      {
	if (idle)
	{
	  idle = false;
	  activeDogs++;

	}

	try {
	  stats.setStatus (dogNo, "connecting to: " + curUrl.toString());
	  connection.connect(curUrl);	  
	} catch (IOException e1)
	{
	  stats.setStatus (dogNo, "could not connect to: " + curUrl.toString() +", " +e1.getMessage());
	  stats.addBadUrl ();
	}

	if (connection.connected ())
	{
	  // DEBUGGING INFO
	  stats.setStatus (dogNo, "searching: " +connection.getURL().toString());

	  int occurs = connection.search(searchExp);
	  if (occurs > 0)
	  {
	    result.add(curUrl, occurs);
	  }
	  else
	  {
	    stats.addFailedUrl (); 
	  }

	  Vector newUrls = connection.getLinks ();
	
	  for (Enumeration e = newUrls.elements(); e.hasMoreElements();)
	    dataBase.add(new HunterUrl ((URL) e.nextElement(), curUrl.getLevel() + 1));
	}
	else
	  stats.addBadUrl ();
	
	curUrl = dataBase.getNext();  
      }

      if (!idle)
      {
	idle = true;
	activeDogs--;
	
	stats.setStatus (dogNo,"sleeping");
      }

      try {
	// Sleep 10 milliseconds
	this.sleep (10);
      } catch (InterruptedException e1) {}

      // If no other dog is active (out of URLs), we die
      if (activeDogs == 0)
	break;

      // See if a new Url has come up
      curUrl = dataBase.getNext();  
    }

    stats.setHunterStatus ("done");
    done = true;
    noOfDogs--;
    this.stop ();
  }

  public boolean idle ()
  {
    return idle;
  }

  // Are we done?
  public boolean done ()
  {
    return done;
  }

  // A main for testing a single Dog
  //public static void main (String[] args) throws MalformedURLException
  //{
  //  HunterStatistics stats = new HunterStatistics (); 
  //  UrlDB testDB = new UrlDB (stats);
  //  DogSearchExp searchExp = new DogSearchExp (args[0], false);
  //  HunterResult results = new HunterResult ("DogTestResults.html", stats);


    // Add an URL to the DB
  //  testDB.add (new HunterUrl (new URL ("http://www.nada.kth.se/~d96-abe"), 1)); 
    // Fire up a Dog
  //  Dog testDog = new Dog (testDB, searchExp, results, stats);
    
    // Wait until it's done
  //  while (!testDog.done()) {}

  //  results.close ();
  //}
}

