// This is the display class for my searcher project
// (c) Jimmy Larsson 1997
 
import java.awt.*;

public class Display extends Frame 
{
  protected DisplayListener listener;

  protected MenuBar menubar;                                // the menubar
  protected Menu file, help;                                     // menu panes
  protected MenuItem fileConfig, fileQuit, helpAbout, helpSearchTips;  // menu items
  protected Button search, newSearch, results, stop; // buttons
  protected TextField searchField, urlField;              // One line of text input
  protected DoubleBufferedList statusArea;              // A list
    // Sub-containers for all this stuff.
  protected Panel fieldPanel, statusPanel, tabPage1, tabPage2, tabPage3, buttonpanel;
  protected TabbedNotebook tabNotebook;

  protected Hunter hunter;
  // The layout manager for each of the containers.
  protected GridBagLayout gridbag = new GridBagLayout();

  // Labels for the statistics page  
  protected Label statProcessed, statWaiting, statBad, statTreeLevel;

  // Spinbuttons for advanced tab-page
  protected SpinButton numberOfDogs;
 
  public Display(String title, Hunter h) 
  {
    super(title);
    
    hunter = h;

    // Create listener
    listener = new DisplayListener (hunter, this);

    // Create TabbedNotebook
    tabNotebook = new TabbedNotebook ();

    // Create the menubar.  Tell the frame about it.
    menubar = new MenuBar();
    this.setMenuBar(menubar);

    // Create the file menu.  Add two items to it.  Add to menubar.
    file = new Menu("File");

    fileConfig = new MenuItem("Configuration");
    fileConfig.addActionListener(listener);
    fileConfig.setActionCommand("configuration");
    file.add(fileConfig);

    fileQuit = new MenuItem("Quit");
    fileQuit.addActionListener(listener);
    fileQuit.setActionCommand("quit");
    file.addSeparator();
    file.add(fileQuit);

    menubar.add(file);

    // Create Help menu; add an item; add to menubar
    help = new Menu("Help");

    helpAbout = new MenuItem("About");
    helpAbout.addActionListener(listener);
    helpAbout.setActionCommand("about");
    help.add(helpAbout);
    help.addSeparator();

    helpSearchTips = new MenuItem("Search Tips");
    helpSearchTips.addActionListener(listener);
    helpSearchTips.setActionCommand("searchtips");
    help.add(helpSearchTips);

    menubar.add(help);

    // Display the help menu in a special reserved place.
    menubar.setHelpMenu(help);
    
    // SEARCH PAGE OF THE TABBED NOTEBOOK
    /////////////////////////////////////////////////////////////////////
    // Create pushbuttons
    search = new Button("Search");
    search.addActionListener(listener);
    search.setActionCommand("search");

    newSearch = new Button("Clear all");
    newSearch.addActionListener(listener);
    newSearch.setActionCommand("clearall");

    stop = new Button("Stop");
    stop.addActionListener(listener);
    stop.setActionCommand("stop");

    results = new Button("Results");
    results.addActionListener(listener);
    results.setActionCommand("results");
    
    // Create textfields
    searchField = new TextField();
    searchField.addActionListener(listener);

    urlField = new TextField();
    urlField.addActionListener(listener);

    String text = new String ("Hunter: \n");

    statusArea = new DoubleBufferedList(15);
    statusArea.add ("Hunter version 0.03 (c) Jimmy Larsson 1998");
    
    // Create status lines for all dogs
    for (int i = 1; i <= hunter.noOfDogs; i++)
    {
      statusArea.add("Dog " + i + ": ");
    }

    // Create a panel to hold fieldPanel and buttonpanel
    tabPage1 = new Panel (gridbag);

    // Create a Panel to contain all the components along the
    // top-left part of the window.  Use a GridBagLayout for it.
    fieldPanel = new Panel();
    fieldPanel.setLayout(gridbag);
    
    // Use several versions of the constrain() convenience method
    // to add components to the panel and to specify their 
    // GridBagConstraints values.
    
    // Place textfields
    constrain(fieldPanel, new Label("Search words:"), 0, 0, 1, 1);
    constrain(fieldPanel, searchField, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL,
	      GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
    
    constrain(fieldPanel, new Label("Start URL:"), 0, 2, 1, 1);
    constrain(fieldPanel, urlField, 0, 3, 1, 1, GridBagConstraints.HORIZONTAL,
	      GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);

    // Create a panel for the bottom textarea.
    // Use a GridBagLayout, and arrange items with constrain(), as above.
    statusPanel = new Panel();
    statusPanel.setLayout(gridbag);
    
    // Place textarea
    constrain(statusPanel, new Label("Status:"), 0, 0, 1, 1);
    constrain(statusPanel, statusArea, 0, 1, 1, 3, GridBagConstraints.BOTH,
              GridBagConstraints.NORTH, 1.0, 1.0, 0, 0, 0, 0);
    
    // Do the same for the buttons along the top-right part of the window.
    buttonpanel = new Panel();
    buttonpanel.setLayout(gridbag);
    
    // Place the buttons
    constrain(buttonpanel, search, 0, 0, 1, 1, GridBagConstraints.BOTH,
              GridBagConstraints.NORTHEAST, 0.0, 0.25, 0, 0, 0, 0);
    constrain(buttonpanel, newSearch, 0, 1, 1, 1, GridBagConstraints.BOTH,
              GridBagConstraints.NORTHEAST, 0.0, 0.25, 0, 0, 0, 0);
    constrain(buttonpanel, stop, 0, 2, 1, 1, GridBagConstraints.BOTH,
              GridBagConstraints.NORTHEAST, 0.0, 0.25, 0, 0, 0, 0);
    constrain(buttonpanel, results, 0, 3, 1, 1, GridBagConstraints.BOTH,
              GridBagConstraints.NORTHEAST, 0.0, 0.25, 0, 0, 0, 0);


    // Add fieldPanel and buttonpanel to tabPage1
    constrain(tabPage1, fieldPanel, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 
              GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
    constrain(tabPage1, buttonpanel, 1, 0, 1, 1, GridBagConstraints.VERTICAL,
              GridBagConstraints.NORTHEAST, 0.0, 0.0, 5, 0, 5, 5);
    

    // ADVANCED PAGE OF THE TABBED NOTEBOOK
    ///////////////////////////////////////////////////////////////////////////
    tabPage2 = new Panel (new GridBagLayout());

    numberOfDogs = new SpinButton (Hunter.MIN_DOGS, Hunter.MAX_DOGS, Hunter.DEFAULT_DOGS, 16, 16);

    constrain(tabPage2, numberOfDogs, 0, 0, 1, 1, GridBagConstraints.NONE,
	      GridBagConstraints.NORTHEAST, 0.0, 0.0, 0, 0, 0, 0);

    // STATISTICS PAGE OF THE TABBED NOTEBOOK
    ///////////////////////////////////////////////////////////////////////////
    tabPage3 = new Panel (gridbag);

    statProcessed = new Label ("0 processed", Label.LEFT);
    statWaiting = new Label ("0 waiting", Label.LEFT);
    statBad = new Label ("0 bad", Label.LEFT);
    statTreeLevel = new Label ("Linktree level 0", Label.LEFT);  

    constrain(tabPage3, statProcessed, 0, 1, 10, 1);
    constrain(tabPage3, statWaiting, 0, 2, 10, 1);
    constrain(tabPage3, statBad, 0, 3, 10, 1);
    constrain(tabPage3, statTreeLevel, 0, 4, 10, 1);


    // Add pages to Notebook
    tabNotebook.addPage(tabPage1, "Search");
    tabNotebook.addPage(tabPage2, "Advanced");
    tabNotebook.addPage(tabPage3, "Statistics");


    // Finally, use a GridBagLayout to arrange the panels themselves
    this.setLayout(gridbag);
    
    // And add the notebook and statuspanel to the toplevel window
    constrain(this, tabNotebook, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 
              GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
    constrain(this, statusPanel, 0, 1, 1, 1, GridBagConstraints.BOTH,
              GridBagConstraints.CENTER, 1.0, 1.0, 5, 2, 2, 2);

    
    // We cannot be resized
    //this.setResizable(false);
    
    // pack and show
    this.setSize(400,400);
    this.show();
  }
  
  public void constrain(Container container, Component component, 
	    int grid_x, int grid_y, int grid_width, int grid_height,
                  int fill, int anchor, double weight_x, double weight_y,
                  int top, int left, int bottom, int right)
  {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = grid_x; c.gridy = grid_y;
    c.gridwidth = grid_width; c.gridheight = grid_height;
    c.fill = fill; c.anchor = anchor;
    c.weightx = weight_x; c.weighty = weight_y;
    if (top+bottom+left+right > 0)
      c.insets = new Insets(top, left, bottom, right);
    
    ((GridBagLayout)container.getLayout()).setConstraints(component, c);
    container.add(component);
  }
    
  public void constrain(Container container, Component component, 
	       int grid_x, int grid_y, int grid_width, int grid_height) 
  {
    constrain(container, component, grid_x, grid_y, 
              grid_width, grid_height, GridBagConstraints.NONE, 
              GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
  }
    
  public void constrain(Container container, Component component, 
                  int grid_x, int grid_y, int grid_width, int grid_height,
                  int top, int left, int bottom, int right) 
  {
    constrain(container, component, grid_x, grid_y, 
              grid_width, grid_height, GridBagConstraints.NONE, 
              GridBagConstraints.NORTHWEST, 
              0.0, 0.0, top, left, bottom, right);
  }
    

  public String getSearchWords ()
  {
    return searchField.getText ();
  }

  public String getStartURL ()
  {
    return urlField.getText ();
  }

  public void setStatus (int line, String message)
  {
    // No need for synchronized, replaceItem is already synchronized...
    statusArea.replaceItem (message, line);
  }

  public void setStatsProcessed (long processed)
  {
      statProcessed.setText (processed + " processed");
  }

  public void setStatsWaiting (long waiting)
  {
    statWaiting.setText (waiting + " waiting");
  }

  public void setStatsBad (long bad)
  {
    statBad.setText (bad + " bad");
  }

  public void setStatsLinkLevel (long level)
  {
    statTreeLevel.setText("Linktree level " + level);
  }
}




