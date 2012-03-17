//
// Class to search a connected url with a search expression
// (c) Jimmy Larsson 1998
//

public final class DogSearcher
{
  protected DogConnectionContent content;

  public DogSearcher (DogConnectionContent cont)
  {
    content = cont;
  }

  // Return number of occurances of the search expression
  public int search (DogSearchExp searchExp)
  {
    int startIndex;
    int occurs = 0;
    String searchString;
    String searchContent;
    
    if (searchExp.caseSensitive())
    {
      searchString = searchExp.getSearchString();
      searchContent = content.getData();
    }
    else
    {
      searchString = searchExp.getSearchString().toLowerCase();
      searchContent = content.getDataLowerCase();
    }

    startIndex = searchContent.indexOf (searchString,0);
      
    while (startIndex >= 0)
    {
      occurs++;
      startIndex = searchContent.indexOf (searchString,startIndex + 1);
    }
        
    // FOR DEBUGGING
    //System.out.println ("search, occurs: " + occurs);
    
    return occurs;
  }

}
