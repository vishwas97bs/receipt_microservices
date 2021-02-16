//package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;
//
//
//import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.ResponseBody;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import retrofit2.Call;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//public class MLBasedMerchantNameExtractor implements CandidateValueExtractor
//{
//    private static final Logger LOG = LoggerFactory.getLogger( MLBasedMerchantNameExtractor.class );
//    private MLEntityClient mlEntityClient;
//    private double confidenceThreshold = 0.75;
//    String filePath = null;
//    private OcrEngineService ocrEngineService;
//    private boolean performLogoDetection; // This makes sure that GV call will happen only if no value or value below confidence threshold
//
//    @Override
//    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName )
//    {
//        performLogoDetection = true;
//        ocrEngineService = OcrEngineServiceImpl.getGVInstance();
//        String rawText = feRequest.getOriginalText();
//        JSONObject textJson = new JSONObject();
//        textJson.put( "rawText", rawText );
//        List<ExtractedValue> response = new ArrayList<>();
//
//        try {
//            mlEntityClient = new MLEntityClient(
//                new ResourceLoader().getProperties( "config.properties" ).getProperty( Constants.ML_ENTITY_SERVER ) );
//            //Checking api configuration for change in url or threshold
//            MLConfig mlConfigForField = GimletUtil.getMLConfigForField( feRequest, fieldName );
//            if ( mlConfigForField != null ) {
//                if ( mlConfigForField.getUrl() != null ) {
//                    LOG.debug( "Overwriting the default url with the url given in config for scanId {}",
//                        feRequest.getScanRequest().getScanRequestId() );
//                    mlEntityClient = new MLEntityClient( mlConfigForField.getUrl() );
//                }
//                if ( mlConfigForField.getThreshold() != null ) {
//                    LOG.debug( "Overwriting the default threshold with the url given in config for scanId {}",
//                        feRequest.getScanRequest().getScanRequestId() );
//                    confidenceThreshold = mlConfigForField.getThreshold();
//                }
//            }
//
//            //Making ml call for merchant name only if enabled in api configuration
//            if ( mlConfigForField != null && mlConfigForField.getEnable() ) {
//                response = findMerchantName( feRequest, textJson );
//            }
//        } catch ( Exception e ) {
//            LOG.error( "Exception while initializing for ML call", e );
//        }
//
//        if ( performLogoDetection ) {
//            LOG.info( "Making Logo Detection call since mlconfig is not enabled or threshold is low for scanId {}",
//                feRequest.getRequestId() );
//            response.addAll( extractLogo( feRequest ) );
//        }
//
//        return response;
//    }
//
//
//    private List<ExtractedValue> findMerchantName(FieldExtractionRequest feRequest, JSONObject textJson )
//    {
//        LOG.info( "ML based merchant name extractor for scanid : {}", feRequest.getRequestId() );
//        Call<ResponseBody> callback = mlEntityClient.mlResponseBody( textJson );
//        List<ExtractedValue> mlResponse = new ArrayList<>();
//        try {
//            retrofit2.Response<ResponseBody> response = callback.execute();
//            if ( response.isSuccessful() ) {
//
//                JSONArray responseArray = new JSONArray( new String( response.body().bytes() ) );
//                MLEventLogger.logScanIdEvent( feRequest.getRequestId(), responseArray.toString() );
//
//                LOG.info( "ML Response is Successful with candidate merchant size {}", responseArray.length() );
//                for ( int n = 0; n < responseArray.length(); n++ ) {
//                    JSONObject object = responseArray.getJSONObject( n );
//                    ExtractedValue extractedValue = new ExtractedValue();
//                    extractedValue.setValue( object.get( "merchantname" ) );
//                    extractedValue.setConfidence( getConfidence( (double) BigDecimal
//                        .valueOf( (double) object.get( "confidence" ) ).setScale( 3, RoundingMode.HALF_UP ).doubleValue() ) );
//                    extractedValue.setOperation( MerchantNameExtractorType.TRAINING_BASED );
//                    mlResponse.add( extractedValue );
//                    if ( extractedValue.getConfidence() >= confidenceThreshold )
//                        performLogoDetection = false;
//                }
//                LOG.debug( "GV call required based on ML threshold value : {}", performLogoDetection );
//            } else {
//                String errorStr = new String( response.errorBody().bytes() );
//                int errorStatus = response.code();
//                LOG.error( "Error while fetching data from ML API : {} , status {}", errorStr, errorStatus );
//            }
//        } catch ( IOException e ) {
//            LOG.error( "Error occurred while fetching data from ML", e );
//        }
//        return mlResponse;
//    }
//
//
//    private double getConfidence( double confidence )
//    {
//        if ( confidence >= confidenceThreshold )
//            return 1;
//        return confidence;
//    }
//
//
//    private List<ExtractedValue> extractLogo(FieldExtractionRequest feRequest )
//    {
//        List<ExtractedValue> extractedValueList = new ArrayList<>();
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> scanRequestObject = objectMapper.convertValue( feRequest.getRequestId(),
//                new TypeReference<Map<String, Object>>() {} );
//            List<String> features = new ArrayList<>();
//            features.add( "LOGO_DETECTION" );
//            OcrEngineResponse ocrEngineResponse = ocrEngineService
//                .processImage( new OcrEngineRequest( filePath, scanRequestObject, features ) );
//            if ( ocrEngineResponse != null ) {
//                feRequest.setLogoData( ocrEngineResponse.getMerchantLogos() );
//                extractedValueList = new GoogleVisionRestBasedMerchantNameExtractor().extractValue( feRequest,
//                    Fields.MERCHANT_NAME.toString() );
//            }
//
//        } catch ( RedisNotAvailableException e ) {
//            LOG.error( "Error while updating to Redis", e );
//        }
//        LOG.info( "Logo extracted for the scanid: {} ", feRequest.getRequestId() );
//        return extractedValueList;
//    }
//
//}
