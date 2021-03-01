package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;


import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Class for Creating spellCheck Client
 * Created by ritesh on 8/6/17.
 */
public class SpellCheckClient implements InitializingBean
{
    SpellCheckClientInterface spellCheckClientInterface = null;

    @Value( "${spellcheck-server}")
    String apiUrl;

    /**
     * Constructor for {@link SpellCheckClient}
     */
//    public SpellCheckClient()
//    {
//        this.spellCheckClientInterface = new Retrofit.Builder().baseUrl( apiUrl )
//            .addConverterFactory( GsonConverterFactory.create() )
//            .client( new OkHttpClient().newBuilder().readTimeout( 5, TimeUnit.SECONDS ).build() ).build()
//            .create( SpellCheckClientInterface.class );
//    }


    /**
     * Hits gimlet SpellCheck API
     * @param spellCheckRequestEntity spellCheckRequestEntity
     * @return response
     */
    public Map<String, List<Map<String, Object>>> spellCheckAPI(SpellCheckRequestEntity spellCheckRequestEntity )
        throws SpellCheckException
    {
        try {
            retrofit2.Response<Map<String, List<Map<String, Object>>>> response = this.spellCheckClientInterface
                .spellCheckAPI( spellCheckRequestEntity ).execute();
            if ( response.isSuccessful() ) {
                return response.body();
            } else {
                throw new SpellCheckException( String.format( "Error while fetching entries from SpellCheck API: %s",
                    new String(response.errorBody() != null ? response.errorBody().bytes() : new byte[0]) ) );
            }
        } catch ( IOException e ) {
            throw new SpellCheckException( "Error occurred while connecting to SpellCheck service", e );
        }
    }


    /**
     * Hits gimlet SpellCheck API
     * @param spellCheckRequestEntity spellCheckRequestEntity
     * @return response
     */
    public List<Object> categorizationAPI(SpellCheckCategoryRequestEntity spellCheckRequestEntity ) throws SpellCheckException
    {
        try {
            retrofit2.Response<List<Object>> response = this.spellCheckClientInterface
                .categorizationAPI( spellCheckRequestEntity ).execute();
            if ( response.isSuccessful() ) {
                return response.body();
            } else {
                throw new SpellCheckException( String.format( "Error while fetching entries from SpellCheck API: %s",
                    new String(response.errorBody() != null ? response.errorBody().bytes() : new byte[0]) ) );
            }
        } catch ( IOException e ) {
            throw new SpellCheckException( "Error occurred while connecting to SpellCheck service", e );
        }
    }


    /**
     * Hits gimlet Locale detection API
     * @param localeDetectionEntity localeRequestEntity
     * @return response
     */
    public List<LocaleDetectionResponseEntity> localeDetectionAPI(LocaleDetectionEntity localeDetectionEntity )
        throws SpellCheckException
    {
        try {
            retrofit2.Response<List<LocaleDetectionResponseEntity>> response = this.spellCheckClientInterface
                .localeDetectionAPI( localeDetectionEntity ).execute();
            if ( response.isSuccessful() ) {
                return response.body();
            } else {
                throw new SpellCheckException( String.format( "Error while fetching entries from SpellCheck API: %s",
                    new String(response.errorBody() != null ? response.errorBody().bytes() : new byte[0]) ) );
            }
        } catch ( IOException e ) {
            throw new SpellCheckException( "Error occurred while connecting to SpellCheck Locale detection service", e );
        }
    }


    /**
     * Hits gimlet Locale detection API
     * @return response
     */
    public LineItemCheckResponse liCheckAPI( LineItemCheckRequest lineItemCheckRequest ) throws SpellCheckException
    {
        try {
            retrofit2.Response<LineItemCheckResponse> response = this.spellCheckClientInterface
                .liCheckAPI( lineItemCheckRequest ).execute();
            if ( response.isSuccessful() ) {
                return response.body();
            } else {
                throw new SpellCheckException( String.format( "Error while fetching entries from SpellCheck API: %s",
                    new String(response.errorBody() != null ? response.errorBody().bytes() : new byte[0]) ) );
            }
        } catch ( IOException e ) {
            throw new SpellCheckException( "Error occurred while connecting to SpellCheck Locale detection service", e );
        }
    }

    @Override
    public void afterPropertiesSet() {
        this.spellCheckClientInterface = new Retrofit.Builder().baseUrl( apiUrl )
            .addConverterFactory( GsonConverterFactory.create() )
            .client( new OkHttpClient().newBuilder().readTimeout( 5, TimeUnit.SECONDS ).build() ).build()
            .create( SpellCheckClientInterface.class );
    }


    /**
     * private interface for Gimlet SpellCheck
     */
    private interface SpellCheckClientInterface
    {
        @POST ( "/gimlet-spellcheck/v3/suggestions")
        Call<Map<String, List<Map<String, Object>>>> spellCheckAPI(@Body SpellCheckRequestEntity spellCheckRequestEntity);


        @POST ( "/gimlet-spellcheck/getReceiptCategories")
        Call<List<Object>> categorizationAPI(@Body SpellCheckCategoryRequestEntity spellCheckRequestEntity);


        @POST ( "/gimlet-spellcheck/findCities")
        Call<List<LocaleDetectionResponseEntity>> localeDetectionAPI(@Body LocaleDetectionEntity localeDetectionEntity);


        @POST ( "/gimlet-spellcheck/text-lookup/line-item/suggestions")
        Call<LineItemCheckResponse> liCheckAPI(@Body LineItemCheckRequest lineItemCheckRequest);
    }
}