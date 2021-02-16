package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

import java.util.List;

public class LocaleDetectionEntity { private List<String> languages;
    private String ocrText;


    /**
     * default constructor
     */
    public LocaleDetectionEntity()
    {

    }


    /**
     * Initialises @{@link LocaleDetectionEntity}
     * @param languages languages
     * @param ocrText ocrText
     */
    public LocaleDetectionEntity( List<String> languages, String ocrText )
    {
        this.languages = languages;
        this.ocrText = ocrText;
    }


    public List<String> getLanguages()
    {
        return languages;
    }


    public String getOcrText()
    {
        return ocrText;
    }


    public void setLanguages( List<String> languages )
    {
        this.languages = languages;
    }


    public void setOcrText( String ocrText )
    {
        this.ocrText = ocrText;
    }


    @Override
    public String toString()
    {
        return "{" + "\"ocrText\" : " + ocrText + ", \"languages\" : " + languages + "}";
    }
}
