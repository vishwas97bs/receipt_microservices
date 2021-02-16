package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckException;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WordLookupClient {
    WordLookupClientInterface wordLookupClientInterface = null;


    /**
     * Constructor for {@link WordLookupClient}
     * @param apiUrl    api url
     */
    public WordLookupClient( String apiUrl )
    {
        this.wordLookupClientInterface = new Retrofit.Builder().baseUrl( apiUrl )
                .addConverterFactory( GsonConverterFactory.create() )
                .client( new OkHttpClient().newBuilder().readTimeout( 5, TimeUnit.SECONDS ).build() ).build()
                .create( WordLookupClientInterface.class );
    }


    /**
     * Hits gimlet SpellCheck API
     * @param wordLookupRequestEntity spellCheckRequestEntity
     * @return response
     */
    public Map<String, Map<String, Object>> wordLookAPI(WordLookupRequestEntity wordLookupRequestEntity )
            throws SpellCheckException
    {
        try {
            retrofit2.Response<Map<String, Map<String, Object>>> response = this.wordLookupClientInterface
                    .spellCheckAPI( wordLookupRequestEntity ).execute();
            if ( response.isSuccessful() ) {
                return response.body();
            } else {
                throw new SpellCheckException( String.format( "Error while fetching entries from SpellCheck API: %s",
                        new String( response.errorBody().bytes() ) ) );
            }
        } catch ( IOException e ) {
            throw new SpellCheckException( "Error occurred while connecting to SpellCheck service", e );
        }
    }


    /**
     * private interface for Gimlet SpellCheck
     */
    private interface WordLookupClientInterface
    {
        @POST( "/gimlet-spellcheck/v3/validate-words")
        Call<Map<String, Map<String, Object>>> spellCheckAPI(@Body WordLookupRequestEntity wordLookupRequestEntity );
    }
}
