package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

import java.util.List;

public class WordLookupRequestEntity {
    private String type;
    private List<String> items;


    /**
     * default constructor
     */
    public WordLookupRequestEntity()
    {
        // do nothing
    }


    public WordLookupRequestEntity( String type, List<String> items )
    {
        this.type = type;
        this.items = items;
    }


    public void setItems( List<String> items )
    {
        this.items = items;
    }


    public void setType( String type )
    {
        this.type = type;
    }


    public String getType()
    {
        return type;
    }


    public List<String> getItems()
    {
        return items;
    }


    @Override
    public String toString()
    {
        return "{" + " \"type\" : " + type + ", \"items\" : " + items + '}';
    }
}
