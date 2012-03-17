//
// This is the result class, that saves all results in HTML-format
// (c) Jimmy Larsson 1997 
//
//


import java.util.*;
import java.io.*;
import java.net.*;

public final class HunterResult
{
  protected RandomAccessFile outFile;
  protected String outFileName;
  protected int error;
  protected HunterStatistics stats;
  protected boolean addBlock;
  protected Thread blockedThread;

  // fileName shuold be without the ".html" extension
  public HunterResult (String fileName, HunterStatistics stat)
  {
    outFileName = fileName;
    stats = stat;
    addBlock = false;
    blockedThread = null;

    error = 0;
    // Sets error var on errors
    // Open file
      try 
      {
	outFile = new RandomAccessFile (fileName + ".html", "rw");
      } catch (IOException e1)
      {
	outFile = null;
	error = 1;
      }
      if (error == 0)
      {
	this.writeHeader(outFile);
      }
  }

  public int error ()
  {
    return error;
  }

  public void close ()
  {
    // Finish up
    if (outFile != null)
    {
      writeFooter(outFile);
      try {
	outFile.close ();
      } catch (IOException e1) {}
      outFile = null;
    }
  }

  public synchronized void add (HunterUrl url, int count)
  {
    if (addBlock)
    {
      blockedThread = Thread.currentThread ();
      blockedThread.suspend();
    }

    if (outFile != null)
    {
      stats.addSuccessedUrl ();
      url.setCount (count);
      try {
	outFile.writeUTF ("<A HREF=\"" + url.toString() + "\">" + url.toString() + "</A><BR>\n");
      } catch (IOException e1) {}
    }
  }

  // REturns the name of the temporary file
  public String dumpTempResults ()
  {
    // Block everybody trying to add new results until we're done
    addBlock = true;
    RandomAccessFile tmpFile;

    String tmpFileName = outFileName + "_tmp.html";
    try {
      tmpFile = new RandomAccessFile (tmpFileName, "rw");
    }catch (IOException e1)
    {
      addBlock = false;
      if (blockedThread != null)
      {
	blockedThread.resume();
	blockedThread = null;
      }
      return null;
    }

    try {
      outFile.getFD().sync();
      long pos = outFile.getFilePointer ();
      outFile.seek(0);
      String tmpLine;

      while ((tmpLine = outFile.readUTF()) != null)
      {
	tmpFile.writeUTF(tmpLine + "\n");
      }

      writeFooter (tmpFile);
      tmpFile.getFD().sync();
      tmpFile.close();

      outFile.seek(pos);
    } catch (IOException e1)
    {
      addBlock = false;
      if (blockedThread != null)
      {
	blockedThread.resume();
	blockedThread = null;
      }
      try {
	tmpFile.close();
      }catch (IOException e2) {}

      return null;
    }

    addBlock = false;
    if (blockedThread != null)
    {
      blockedThread.resume();
      blockedThread = null;
    }

    return tmpFileName;
  }  

  // Protected methods
  protected void finalize ()
  {
    this.close ();
  }

  protected void writeHeader (RandomAccessFile file)
  {
    if (file != null)
    {
      try {
	file.writeUTF ("<HTML><HEAD>\n");
	file.writeUTF ("<TITLE>Hunter output</TITLE></HEAD>\n");
	file.writeUTF ("<BODY BGCOLOR=\"#c3c3a3\" LINK=\"#0000ff\" VLINK=\"#551a8b\" ALINK=\"#ff8000\">\n");
	file.writeUTF ("<FONT SIZE=+3>Hunt results:</FONT>\n");
	file.writeUTF ("<HR ALIGN=LEFT SIZE=\"1\">\n");
      } catch (IOException e1) {}
    }
  }

  protected void writeFooter (RandomAccessFile file)
  {
    if (file != null)
    {
      try {
	file.writeUTF ("<HR ALIGN=LEFT SIZE=\"1\">\n");
	file.writeUTF ("<FONT SIZE=-1>" + stats.getProcessedUrls()+" URL's processed, "+stats.getBadUrls()+" bad, " + stats.getSuccessedUrls()+" successful, "+stats.getFailedUrls()+" failed. </FONT>\n");
	file.writeUTF ("<HR ALIGN=LEFT SIZE=\"1\">\n");
	file.writeUTF ("<I><FONT SIZE=-1>(C) Jimmy Larsson 1998</FONT></I>\n");
	file.writeUTF ("</BODY></HTML>\n");
      } catch (IOException e1) {}
    }
  }

  // Main, just for testing...
  public static void main (String args[])
  {
    HunterStatistics testStat = new HunterStatistics (null);
    HunterResult testRes = new HunterResult ("HunterResult-test.html", testStat);
    
    try {
      testRes.add(new HunterUrl (new URL ("http", "emma.jimnet.se", 80, "/howto"), 1), 1);
      testRes.add(new HunterUrl (new URL ("http", "emma.jimnet.se", 80, "/howto/mini"), 1), 1);
      testRes.add(new HunterUrl (new URL ("http", "emma.jimnet.se", 80, "/howto/other-formats"), 1), 1);

      testRes.close ();
      System.out.print ("Output written to 'HunterResult-test.html'...\n");
    } catch (MalformedURLException e1)
    {
      System.out.print ("HunterResult-test: Bad URL!");
    }
  }

}
