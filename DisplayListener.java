//
// The display listener, here all events are dispatched
// (c) Jimmy Larsson 1998
//

import java.awt.event.*;

public final class DisplayListener implements ActionListener, ItemListener
{
  protected Hunter hunter;
  protected Display display;

  public DisplayListener (Hunter h, Display d)
  {
    hunter = h;
    display = d;
  }

  // This catches:
  //       List double-click
  //       Button-click
  //       Textfield editing finished
  //       Menu choices

  public void actionPerformed (ActionEvent e)
  {
    String action = e.getActionCommand();

    if (action.equalsIgnoreCase("search"))
    {
      // SEARCH clicked

      // Add case sensitivity here
      DogSearchExp searchExp = new DogSearchExp (display.getSearchWords(), false);
      hunter.startSearch(searchExp, display.getStartURL());



    } else if (action.equalsIgnoreCase("results"))
    {
      // RESULTS clicked

      hunter.dumpResults ();


    } else if (action.equalsIgnoreCase("stop"))
    {
      // STOP clicked

      hunter.stopSearch();

    } else if (action.equalsIgnoreCase("quit"))
      // gee, the user wants to quit =)
    
      hunter.quit ();
  }

  // This catches:
  //       Checkbox
  //       CheckboxMenuItem
  //       Choice
  //       List (item selected)

  public void itemStateChanged (ItemEvent e)
  {
  }
}
