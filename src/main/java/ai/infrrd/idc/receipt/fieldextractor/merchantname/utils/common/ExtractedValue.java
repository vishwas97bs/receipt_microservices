package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.abs;

public class ExtractedValue {
    private static final String MATCHED_VALUE = "matchedValue";
    private Object value;
    private String operation;
    private String matchedVicinity;
    private long index;
    private double confidence;
    private double individualConfidence;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private String matchedValue;
    private List<Map<String, Object>> tabularValue;


    public ExtractedValue( Object value, String operation, String matchedVicinity, long index, double confidence )
    {
        this.value = value;
        this.operation = operation;
        this.matchedVicinity = matchedVicinity;
        this.index = index;
        this.confidence = confidence;
    }


    public ExtractedValue( ExtractedValue extVal )
    {
        this.value = extVal.getValue();
        this.operation = extVal.getOperation();
        this.matchedVicinity = extVal.getMatchedVicinity();
        this.index = extVal.getIndex();
        this.confidence = extVal.getConfidence();
    }


    /*Instantiate a new extracted value*/
    public ExtractedValue()
    {
        // Empty Constructor
    }


    public ExtractedValue( Map<String, Object> data )
    {
        this.value = data.get( "value" );
        this.operation = (String) data.get( "operation" );
        this.matchedVicinity = (String) data.get( "matchedVicinity" );
        this.index = ( (Number) data.get( "index" ) ).longValue();
        this.confidence = ( (Number) data.get( "confidence" ) ).doubleValue();
        this.startX = ( (Number) data.get( "startX" ) ).intValue();
        this.startY = ( (Number) data.get( "startY" ) ).intValue();
        this.endX = ( (Number) data.get( "endX" ) ).intValue();
        this.endY = ( (Number) data.get( "endY" ) ).intValue();
        if ( data.containsKey( MATCHED_VALUE ) )
            this.matchedValue = (String) data.get( MATCHED_VALUE );
    }


    public Object getValue()
    {
        return value;
    }


    public void setValue( Object value )
    {
        this.value = value;
    }


    public String getOperation()
    {
        return operation;
    }


    public void setOperation( String operation )
    {
        this.operation = operation;
    }


    public long getIndex()
    {
        return index;
    }


    public void setIndex( long index )
    {
        this.index = index;
    }


    public double getConfidence()
    {
        return confidence;
    }


    public void setConfidence( double confidence )
    {
        this.confidence = confidence;
    }


    public double getIndividualConfidence()
    {
        return individualConfidence;
    }


    public String getMatchedVicinity()
    {
        return matchedVicinity;
    }


    public void setMatchedVicinity( String matchedVicinity )
    {
        this.matchedVicinity = matchedVicinity;
    }


    public int getStartX()
    {
        return startX;
    }


    public void setStartX( int startX )
    {
        this.startX = startX;
    }


    public int getStartY()
    {
        return startY;
    }


    public void setStartY( int startY )
    {
        this.startY = startY;
    }


    public int getEndX()
    {
        return endX;
    }


    public void setEndX( int endX )
    {
        this.endX = endX;
    }


    public int getEndY()
    {
        return endY;
    }


    public void setEndY( int endY )
    {
        this.endY = endY;
    }


    @JsonIgnore
    public Map<String, Object> getObjectToSave()
    {
        Map<String, Object> response = new HashMap<>();
        response.put( "value", value );
        response.put( "operation", operation );
        response.put( "matchedVicinity", matchedVicinity );
        response.put( "index", index );
        response.put( "confidence", confidence );
        response.put( "startX", startX );
        response.put( "startY", startY );
        response.put( "endX", endX );
        response.put( "endY", endY );
        response.put( MATCHED_VALUE, matchedValue );
        return response;
    }


    public String getMatchedValue()
    {
        return matchedValue;
    }


    public void setMatchedValue( String matchedValue )
    {
        this.matchedValue = matchedValue;
    }


    public List<Map<String, Object>> getTabularVal()
    {
        return tabularValue;
    }


    public void setTabularVal( List<Map<String, Object>> tabularVal )
    {
        this.tabularValue = tabularVal;
    }


    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        ExtractedValue value1 = (ExtractedValue) o;

        return ( abs( index - value1.index ) <= 2 ) && Objects.equals( value, value1.value )
                && Objects.equals( operation, value1.operation );
    }


    @Override
    public int hashCode()
    {
        //TODO implement hash properly
        //return Objects.hash(value, operation);
        return 1;

    }


    @JsonIgnore
    @Override
    public String toString()
    {
        StringBuilder val = new StringBuilder( "{ \"Value\":\"" );
        val.append( this.value );
        val.append( "\", \"Confidence\" : " );
        val.append( this.confidence );
        val.append( ", \"MatchedValue\" : \"" );
        val.append( this.matchedValue );
        val.append( "\", MatchedVicinity\" : \"" );
        val.append( this.matchedVicinity );
        val.append( "\", \"Operation\" : \"" );
        val.append( this.operation );
        val.append( "\", \"Index\" : \"" );
        val.append( this.index );
        val.append( "\" }" );
        return val.toString();

    }
}
