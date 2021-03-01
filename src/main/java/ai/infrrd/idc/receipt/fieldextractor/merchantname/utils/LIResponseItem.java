package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

public class LIResponseItem {  public LIResponseItem( String value, Double score )
{
    super();
    this.value = value;
    this.score = score;
}


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( score == null ) ? 0 : score.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }


    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        LIResponseItem other = (LIResponseItem) obj;
        if ( score == null ) {
            if ( other.score != null )
                return false;
        } else if ( !score.equals( other.score ) )
            return false;
        if ( value == null ) {
            return other.value == null;
        } else return value.equals(other.value);
    }


    public LIResponseItem()
    {
        // Empty default constructor
    }

    private String value;
    private Double score;


    public Double getScore()
    {
        return score;
    }


    public void setScore( Double score )
    {
        this.score = score;
    }


    public String getValue()
    {
        return value;
    }


    public void setValue( String value )
    {
        this.value = value;
    }
}
