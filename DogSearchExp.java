//
// Class for storing search expressions
// (C) Jimmy Larsson 1998
//

public final class DogSearchExp
{
  protected String searchString;
  protected boolean caseSensitive;

  public DogSearchExp (String searchStr, boolean caseSens)
  {
    searchString = searchStr;
    caseSensitive = caseSens;
  }

  public String getSearchString ()
  {
    return searchString;
  }

  public boolean caseSensitive ()
  {
    return caseSensitive;
  }

}
