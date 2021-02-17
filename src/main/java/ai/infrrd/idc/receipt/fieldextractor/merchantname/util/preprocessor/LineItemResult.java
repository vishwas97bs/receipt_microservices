package ai.infrrd.idc.receipt.fieldextractor.merchantname.util.preprocessor;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.exception.LineItemCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class LineItemResult {
    private Integer startIdx;
    private Integer endIdx;
    private List<String> theLines;
    private String operation;


    public Integer getStartIdx()
    {
        return startIdx;
    }


    public void setStartIdx( Integer startIdx )
    {
        this.startIdx = startIdx;
    }


    public Integer getEndIdx()
    {
        return endIdx;
    }


    public void setEndIdx( Integer endIdx )
    {
        this.endIdx = endIdx;
    }


    public List<String> getTheLines()
    {
        return theLines;
    }


    public void setTheLines( List<String> theLines )
    {
        this.theLines = theLines;
    }


    public Integer getListItemICount()
    {
        return this.getTheLines() != null ? this.getTheLines().size() : 0;
    }


    @Override
    public String toString()
    {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString( this );
        } catch ( JsonProcessingException e ) {
            throw new LineItemCreationException( "Error creating JSON object", e );
        }
    }


    public String getOperation()
    {
        return operation;
    }


    public void setOperation( String operation )
    {
        this.operation = operation;
    }
}
