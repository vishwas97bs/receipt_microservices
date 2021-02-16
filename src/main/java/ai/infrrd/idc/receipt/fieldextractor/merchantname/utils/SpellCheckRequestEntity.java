package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

import java.util.List;

public class SpellCheckRequestEntity {
    private static final Double DEFAULT_ACCURACY = 0.7;
    private static final Integer DEFAULT_LIMIT = 5;

    private Double accuracy = DEFAULT_ACCURACY;
    private Integer limit = DEFAULT_LIMIT;
    private String type;
    private List<String> items;
    private Double ocrConfidence = 75.0;


    /**
     * default constructor
     */
    public SpellCheckRequestEntity()
    {
        // do nothing
    }


    /**
     * Initialises @{@link SpellCheckRequestEntity}
     * @param accuracy accuracy
     * @param limit limit
     * @param type type
     * @param items items
     * @param ocrConfidence
     */
    public SpellCheckRequestEntity( Double accuracy, Integer limit, String type, List<String> items, Double ocrConfidence )
    {
        if(ocrConfidence != null) {
            this.ocrConfidence = ocrConfidence;
        }
        if ( accuracy != null ) {
            this.accuracy = accuracy;
        }

        if ( limit != null ) {
            this.limit = limit;
        }
        this.type = type;
        this.items = items;
    }


    public SpellCheckRequestEntity( String type, List<String> lines, Double ocrConfidence )
    {
        this.type = type;
        this.items = lines;
        if (ocrConfidence != null){
            this.ocrConfidence = null;
        }
    }


    public void setAccuracy( Double accuracy )
    {
        if ( accuracy != null )
            this.accuracy = accuracy;
    }


    public void setItems( List<String> items )
    {
        this.items = items;
    }


    public void setLimit( Integer limit )
    {
        if ( limit != null )
            this.limit = limit;
    }


    public void setType( String type )
    {
        this.type = type;
    }


    public Double getAccuracy()
    {
        return accuracy;
    }


    public Integer getLimit()
    {
        return limit;
    }


    public String getType()
    {
        return type;
    }


    public List<String> getItems()
    {
        return items;
    }


    public void setOcrConfidence( Double ocrConfidence )
    {
        this.ocrConfidence = ocrConfidence;
    }


    public Double getOcrConfidence()
    {
        return ocrConfidence;
    }


    @Override
    public String toString()
    {
        return "{" + "\"accuracy\" : " + accuracy + ", \"limit\" : " + limit + ", \"type\" : " + type + ", \"items\" : " + items
                + '}';
    }
}
