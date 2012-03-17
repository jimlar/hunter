//
// This is the tabs for my tabbed notebook
// (c) Jimmy Larsson 1998
//

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public final class Tabs extends Canvas implements MouseListener
{
  protected final int TOP_TABINSET = 5;
  protected final int LEFT_TABINSET = 2;
  protected final int RIGHT_TABINSET = 2;
  protected final int BOTTOM_TABINSET = 0;
  protected final int BETWEEN_TABINSET = 5;
  protected final int TEXT_TABINSET = 3;

  protected int noOfTabs;
  protected int selectedTab;
  protected Vector tabNames;
  protected Vector tabRectangles;
  protected FontMetrics fm;
  protected int fontHeight;
  protected int fontBaseLine;
  protected TabbedNotebook noteBook;
  protected Color background = null;
  
  protected Image buffer;

  public Tabs (TabbedNotebook nb)
  {
    noteBook = nb;
    tabNames = new Vector ();
    tabRectangles = new Vector ();
    noOfTabs = 0;
    selectedTab = 1;
    addMouseListener (this);

    setSize(400,30);
  }

  public synchronized void addTab (String name)
  {
    noOfTabs++;
    tabNames.addElement(name);

  }

  public void update(Graphics g)
  {
      paint(g);
  }

  public void paint (Graphics realG)
  {
   
    if (buffer == null)
    {
    	buffer = createImage(getSize().width, getSize().height);
    	MediaTracker tr = new MediaTracker(this);
    	tr.addImage(buffer,1);
    	try {
	  tr.waitForID(1);
	}catch(InterruptedException exp){}
    }

    // Paint everything into an invisible buffer first
    Graphics g = buffer.getGraphics();

    if (fm == null)
    {
      fm = g.getFontMetrics();
      fontHeight = fm.getHeight();
      fontBaseLine = TOP_TABINSET + fontHeight + TEXT_TABINSET - fm.getDescent() - 1;
    }

    // Get background color
    if (background == null)
      background = getBackground();

    if (tabNames.size() != tabRectangles.size())
    {
      int nextXPos = LEFT_TABINSET;
      for (Enumeration eNames = tabNames.elements(); eNames.hasMoreElements();)
      {
	String curTab = (String) eNames.nextElement();
	int strWidth = fm.stringWidth(curTab);
    
	tabRectangles.addElement(new Rectangle (nextXPos, TOP_TABINSET, strWidth + 2*TEXT_TABINSET, 2*TEXT_TABINSET + fontHeight));
    
	nextXPos = nextXPos + strWidth + 2*TEXT_TABINSET + BETWEEN_TABINSET;
      }
      setSize(getSize().width, 2*TEXT_TABINSET + TOP_TABINSET + fontHeight + 1);
    }

    int i = 1;
    Rectangle rSelected = null;
    Enumeration eRects = tabRectangles.elements();
    g.setColor(background);

    for (Enumeration eNames = tabNames.elements(); eNames.hasMoreElements();)
    {
      String curTab = (String) eNames.nextElement();
      Rectangle r = (Rectangle) eRects.nextElement();
      
      g.draw3DRect (r.x, r.y, r.width, r.height, true); 
      g.setColor(Color.black);
      g.drawString(curTab, r.x + TEXT_TABINSET, fontBaseLine);
      if (i == selectedTab)
	rSelected = r;
      
      i++;
      g.setColor(background);
    }
    
    g.setColor(background.brighter());
    g.drawLine(0, TOP_TABINSET+2*TEXT_TABINSET+fontHeight, getSize().width, TOP_TABINSET+2*TEXT_TABINSET+fontHeight);
    g.setColor(background);
    if (rSelected != null)
      g.drawLine(rSelected.x + 1, rSelected.y + rSelected.height, rSelected.x + rSelected.width - 1, rSelected.y + rSelected.height);
    
    realG.drawImage(buffer, 0, 0, null);
    g.dispose();
    buffer = null;

  }


  // The Mouse listener interface
  public void mouseEntered (MouseEvent e) {}
  public void mouseExited (MouseEvent e) {}
  public void mousePressed (MouseEvent e) {}
  public void mouseReleased (MouseEvent e) {}

  public void mouseClicked (MouseEvent e)
  {
    Point p = e.getPoint();
    int i = 1;
    int newSelected = selectedTab;

    for (Enumeration eRect = tabRectangles.elements(); eRect.hasMoreElements(); i++)
    {
      if (((Rectangle) eRect.nextElement()).contains(p))
	newSelected = i;
    }
    if (newSelected != selectedTab)
    {
      selectedTab = newSelected;
      
      repaint();
      
      if (noteBook != null)
	noteBook.showPage((String) tabNames.elementAt(selectedTab - 1));

    }
  }

  // A main for testing the tabs
  public static void main (String[] args)
  {
    Frame f = new Frame("Tabs-test");
    f.setLayout(new BorderLayout());
    Tabs tabs = new Tabs(null);
   
    tabs.addTab("Testg");
    tabs.addTab("The worlds slowest tabs");
    f.add("Center",tabs);
    f.setSize(400,400);
    f.pack();
    f.show();
  }
}
