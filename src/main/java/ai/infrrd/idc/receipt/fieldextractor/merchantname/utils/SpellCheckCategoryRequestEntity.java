package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

import java.util.List;

public class SpellCheckCategoryRequestEntity {
    private String ocrText;
    private List<String > possibleLanguages;


    public SpellCheckCategoryRequestEntity(String ocrText, List<String> possibleLanguages)
    {
        this.ocrText = ocrText;
        this.possibleLanguages = possibleLanguages;
    }

    public String getOcrText()
    {
        return ocrText;
    }


    public void setOcrText( String ocrText )
    {
        this.ocrText = ocrText;
    }


    public List<String> getPossibleLanguages()
    {
        return possibleLanguages;
    }


    public void setPossibleLanguages( List<String> possibleLanguages )
    {
        this.possibleLanguages = possibleLanguages;
    }
}

