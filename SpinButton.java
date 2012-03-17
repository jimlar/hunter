//
// This is the tabs for my spinbutton
// (c) Jimmy Larsson 1998
//

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;


public final class SpinButton extends Canvas implements MouseListener
{
  protected final int BUTTON_INSET_X = 2;
  protected final int BUTTON_INSET_Y = 2;
  protected final int TEXT_INSET = 3;

  protected final int BUTTON2_STEP = 10; 

  protected Image buffer = null;
  protected int maxValue;
  protected int minValue;
  protected int value;
  protected Color background = null;
  protected Rectangle upRect;
  protected Rectangle downRect;
  protected boolean fixedSize;
  protected int buttonWidth;
  protected int buttonHeight;
  protected Image arrowUp;
  protected Image arrowDown;

  // Create a Spinbutton with variable size
  public SpinButton (int min, int max, int start)
  {
      maxValue = max;
      minValue = min;
      value = start;
      if (value > maxValue)
	  value = maxValue;
      if (value < minValue)
	  value = minValue;

      fixedSize = false;

// This is crap.....  should implement preferredSize and minimumSize!!!!!!!
      setSize (300,100);


      addMouseListener(this);

      // Create button images
      int w = 7;
      int h = 4;
      int upPixels[] = {0,0,0,100,0,0,0, 0,0,100,100,100,0,0, 0,100,100,100,100,100,0, 100,100,1,1,1,1,1};
      arrowUp = createImage(new MemoryImageSource (w, h, upPixels, 0, w));

      int downPixels[] = {1,1,1,1,1,1,1, 0,1,1,1,1,1,0, 0,0,1,1,1,0,0, 0,0,0,1,0,0,0};
      arrowDown = createImage(new MemoryImageSource (w, h, downPixels, 0, w));
  }
  
  // Create a spinbutton with fixed size
  public SpinButton (int min, int max, int start, int width, int height)
  {
      this(min, max, start);
      fixedSize = true;
      buttonWidth = width;
      buttonHeight = height;
  }

  public int getValue ()
  {
      return value;
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
	tr.addImage(arrowUp,2);
	tr.addImage(arrowDown,3);
    	try {
	  tr.waitForID(1);
	  tr.waitForID(2);
	  tr.waitForID(3);
	}catch(InterruptedException exp){}
    }

    if (background == null)
	background = getBackground();

    // Paint everything into an invisible buffer first
    Graphics g = buffer.getGraphics();
    super.paint(g);

    int middle = getSize().height / 2;
    int stringWidth = g.getFontMetrics().stringWidth(String.valueOf(value)) + TEXT_INSET;
    int stringX = 0;
    Polygon upArrow = new Polygon ();
    Polygon downArrow = new Polygon ();

    if (fixedSize)
    {
	int xUp = BUTTON_INSET_X;
	int yUp = middle - buttonHeight - 1;
	int widthUp = buttonWidth;
	int heightUp = buttonHeight;
	int xDown = xUp;
	int yDown = middle + 1;
	int widthDown = widthUp;
	int heightDown = heightUp;

	upRect = new Rectangle (xUp, yUp, widthUp, heightUp);
	downRect = new Rectangle (xDown, yDown, widthDown, heightDown);
	stringX = buttonWidth + TEXT_INSET + BUTTON_INSET_X;
    } else
    {
	int rectWidth = getSize().width - BUTTON_INSET_X - stringWidth - TEXT_INSET;
	upRect = new Rectangle (BUTTON_INSET_X, BUTTON_INSET_Y, rectWidth, middle - 1 - BUTTON_INSET_Y);
	downRect = new Rectangle (BUTTON_INSET_X, middle + 1, rectWidth, middle - BUTTON_INSET_Y - 1);
	stringX = rectWidth + BUTTON_INSET_X + TEXT_INSET;
    }
    g.setColor(background);
    g.draw3DRect(upRect.x, upRect.y, upRect.width, upRect.height, true);
    g.draw3DRect(downRect.x, downRect.y, downRect.width, downRect.height, true);
    g.setColor(Color.black);
    g.drawString(String.valueOf(value), stringX, middle + g.getFontMetrics().getAscent() / 2);
  
    // Draw the arrows
    int XMiddle = upRect.x + upRect.width/2;
    int upYMiddle = upRect.y + upRect.height/2;
    int downYMiddle = downRect.y + downRect.height/2;

    upArrow.addPoint(XMiddle, upYMiddle - 2);
    upArrow.addPoint(XMiddle + 4, upYMiddle + 2);
    upArrow.addPoint(XMiddle - 4, upYMiddle + 2);

    downArrow.addPoint(XMiddle, downYMiddle + 2);
    downArrow.addPoint(XMiddle + 4, downYMiddle - 2);
    downArrow.addPoint(XMiddle - 4, downYMiddle - 2);
                                  
    g.fillPolygon(upArrow);
    g.fillPolygon(downArrow);
                                
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
    int button2 = e.getModifiers() & InputEvent.BUTTON2_MASK;

    if (button2 == 0)
    {
	if (upRect.contains(p) && value < maxValue)
	    value++;
	
	if (downRect.contains(p) && value > minValue)
	    value--;

    } else
    {
	if (upRect.contains(p) && value < maxValue)
	    if (maxValue - value > BUTTON2_STEP)
	    {
		value = value + BUTTON2_STEP;
	    } else
	    {
		value = value + (maxValue - value);
	    }
	
	if (downRect.contains(p) && value > minValue)
	    if (value - minValue > BUTTON2_STEP)
	    {
		value = value - BUTTON2_STEP;
	    } else
	    {
		value = value - (value - minValue);
	    }
    }

    invalidate();
    repaint();
  }

  // A main for testing the spinbutton
  public static void main (String[] args)
  {
    Frame f = new Frame("Spinbutton-test");
    f.setLayout(new BorderLayout());
    SpinButton sb = new SpinButton(0,99,5, 12, 12);
   
    f.add("Center",sb);
    f.setSize(400,80);
    //f.pack();
    f.show();
  }
}
