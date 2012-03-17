//
// This is where I store contents of a connected URL
// (c) Jimmy Larsson 1998
//

public final class DogConnectionContent
{
  protected String contentData;
  protected String contentDataLowerCase;
  protected String contentType;
  protected String contentTitle;
  protected int dogNo;

  public DogConnectionContent ()
  {
    this(null, null, null, 0);
  }

  public DogConnectionContent (String data, String type, String title, int dogNo)
  {
    set (data, type, title, dogNo);
  }

  public void set (String data, String type, String title, int dogNo)
  {
    contentData = data;
    if (data != null)
      contentDataLowerCase = data.toLowerCase();
    contentTitle = title;
    this.dogNo = dogNo;
  }

  public String getData ()
  {
    return contentData;
  }

  public String getDataLowerCase ()
  {
    return contentDataLowerCase;
  }

  public String getType ()
  {
    return contentType;
  }

  public String getTitle ()
  {
    return contentTitle;
  }

  public int getDogNo ()
  {
    return dogNo;
  }
}
