package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;

public class RegexMatchInfo
{
    String matchedString;
    int startindex;
    int endIndex;
    int groupCount;


    public RegexMatchInfo(String matchedString, int startindex, int endIndex, int groupCount )
    {
        this.matchedString = matchedString;
        this.startindex = startindex;
        this.endIndex = endIndex;
        this.groupCount = groupCount;
    }


    public RegexMatchInfo( String matchedString )
    {
        this.matchedString = matchedString;

    }


    public int getGroupCount()
    {
        return groupCount;
    }


    public void setGroupCount( int groupCount )
    {
        this.groupCount = groupCount;
    }


    public String getMatchedString()
    {
        return matchedString;
    }


    public void setMatchedString( String matchedString )
    {
        this.matchedString = matchedString;
    }


    public int getStartindex()
    {
        return startindex;
    }


    public void setStartindex( int startindex )
    {
        this.startindex = startindex;
    }


    public int getEndIndex()
    {
        return endIndex;
    }


    public void setEndIndex( int endIndex )
    {
        this.endIndex = endIndex;
    }


    @Override
    public boolean equals(Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        RegexMatchInfo that = (RegexMatchInfo) o;

        return matchedString != null ? matchedString.equals( that.matchedString ) : that.matchedString == null;
    }


    @Override
    public int hashCode()
    {
        return matchedString != null ? matchedString.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return matchedString;
    }
}
