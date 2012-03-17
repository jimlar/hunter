//
// HunterStatistics, this class is for progress measurement
// (C) Jimmy Larsson 1998
//

public final class HunterStatistics 
{
  protected long badUrls = 0;              // Urls that could not be connected to
  protected long waitingUrls = 0;         // Urls waiting to be searched
  protected long processedUrls = 0;    // Urls done
  protected long successedUrls = 0;   // Urls searched and matched
  protected long failedUrls = 0;          // Urls searched but not containing search expression
  protected Display display;
  protected long linkTreeLevel = 0;

  public HunterStatistics (Display d)
  {
    display = d;
  }

  public void setStatus (int dogNo, String message)
  {
    display.setStatus(dogNo, "Dog " + dogNo +": " + message);
  }

  public void setHunterStatus (String message)
  {
    display.setStatus(0, "Hunter: " + message);
  }

  public synchronized void addBadUrl ()
  {
    addProcessedUrl ();
    badUrls++;
    display.setStatsBad(badUrls);
  }

  public synchronized void addWaitingUrl (int value)
  {
    if (value > 0)
      for (int i = 1; i <= value; i++)
	addProcessedUrl ();
    if (value < 0)
      for (int i = -1; i >= value; i--)
	addProcessedUrl ();

    waitingUrls += value;
    display.setStatsWaiting (waitingUrls);
  }

  public synchronized void addProcessedUrl ()
  {
    processedUrls++;
    display.setStatsProcessed(processedUrls);
  }

  public void addSuccessedUrl ()
  {
    successedUrls++;
  }

  public void addFailedUrl ()
  {
    failedUrls++;
  }

  public long getBadUrls ()
  {
    return badUrls;
  }

  public long getWaitingUrls ()
  {
    return waitingUrls;
  }

  public long getProcessedUrls ()
  {
    return processedUrls;
  }

  public long getSuccessedUrls ()
  {
    return successedUrls;
  }

  public long getFailedUrls ()
  {
    return failedUrls;
  }

  public synchronized void linkLevel (long level)
  {
    linkTreeLevel = level;
    display.setStatsLinkLevel(level);
  }

}
