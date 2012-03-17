//
// My own tabbed notebook
// (c) Jimmy Larsson 1998
//

import java.awt.*;

public final class TabbedNotebook extends Panel
{
  protected CardLayout layout;
  protected Panel tabPanel, pagePanel;
  protected Tabs tabs;

  public TabbedNotebook ()
  {
    layout = new CardLayout ();
    pagePanel = new Panel (layout);
    tabs = new Tabs (this);

    setLayout(new BorderLayout());

    add("North", tabs);
    add("Center", pagePanel);
  }

  public void addPage (Panel panel, String name)
  {
      pagePanel.add(panel, name);
      tabs.addTab(name);
  }

  public void showPage (String name)
  {
    layout.show(pagePanel, name);
  }

}
