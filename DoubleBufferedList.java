//
// This class is for avoiding flicker in the status field
// (c) Jimmy Larsson 1998
//

 
import java.awt.*;


public final class DoubleBufferedList extends List 
{
    
  Image buffer;

  public DoubleBufferedList (int showLines)
  {
    super(showLines);
    buffer = null;
  }

  public void invalidate() 
  {
    super.invalidate();
    buffer = null;
  }

  public void update(Graphics g) 
  {
    paint(g);
  }

  public void paint(Graphics g) 
  {
    if(buffer == null) 
      buffer = createImage(getSize().width, getSize().height);
      
    Graphics bg = buffer.getGraphics();
    bg.setClip(0,0,getSize().width, getSize().height);
    super.paint(bg);
    g.drawImage(buffer, 0, 0, null);
    //bg.dispose();
  }
}

