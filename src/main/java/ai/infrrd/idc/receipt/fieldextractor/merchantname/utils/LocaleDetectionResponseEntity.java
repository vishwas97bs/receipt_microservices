package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

public class LocaleDetectionResponseEntity {
    private String city;
    private String countryName;
    private String countryCode;
    private String language;
    private String localeValue;


    /**
     * default constructor
     */
    private LocaleDetectionResponseEntity()
    {}


    /**
     * Initialises @{@link LocaleDetectionResponseEntity}
     * @param city city
     * @param countryName countryName
     * @param countryCode countryCode
     * @param language language
     * @param localeValue localeValue
     */
    public LocaleDetectionResponseEntity( String city, String countryName, String countryCode, String language,
                                          String localeValue )
    {
        this.city = city;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.language = language;
        this.localeValue = localeValue;
    }


    public void setCity( String city )
    {
        this.city = city;
    }


    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }


    public void setCountryName( String countryName )
    {
        this.countryName = countryName;
    }


    public void setLanguage( String language )
    {
        this.language = language;
    }


    public void setLocaleValue( String localeValue )
    {
        this.localeValue = localeValue;
    }


    public String getCity()
    {
        return city;
    }


    public String getCountryCode()
    {
        return countryCode;
    }


    public String getCountryName()
    {
        return countryName;
    }


    public String getLanguage()
    {
        return language;
    }


    public String getLocaleValue()
    {
        return localeValue;
    }


    @Override
    public String toString()
    {
        return "{" + "\"city\" : " + city + ", \"countryName\" : " + countryName + ", \"countryCode\" : " + countryCode
                + ", \"language\" : " + language + ", \"localeValue\" : " + localeValue + "}";
    }
}
