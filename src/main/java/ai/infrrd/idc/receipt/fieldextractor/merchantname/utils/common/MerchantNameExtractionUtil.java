package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;


import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.NGram;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.ConfidenceValueCollection;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.Constants;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.UtilConditions;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MerchantNameExtractionUtil
{
    private static DBCollection STOPWORDS_COLLECTION = null;
    private static Set<String> STOPWORDS = null;
    public static final boolean ENABLE_SEQUENTIAL_LOOKUP = true;
    public static MerchantNameExtractionUtil merchantNameExtractionUtil = null;

    public static void initializeMongo( String mongoUrl )
    {
        if ( STOPWORDS_COLLECTION == null ) {
            STOPWORDS_COLLECTION = MongoConnector.getDB( mongoUrl ).getCollection( Constants.STOP_WORDS_COLLECTION );
        }

        STOPWORDS = loadStopwords();
    }


    /**
     * Loads up the merchant name stopwords from the database
     *
     * @return Set of merchant name stopwords
     */
    private static Set<String> loadStopwords()
    {
        DBObject merchantStopwords = STOPWORDS_COLLECTION.findOne( new BasicDBObject( "_id", "merchant" ) );
        if ( merchantStopwords != null ) {
            return new HashSet<>( lowercase( (List<String>) merchantStopwords.get( "stopwords" ) ) );
        }
        return new HashSet<>();
    }


    private boolean isStopword(String word)
    {
        for ( String stopword : STOPWORDS ) {
            if ( stopword.length() <= UtilConditions.MIN_LENGTH_LEVENSHTEIN && stopword.equals( word ) ) {
                return true;
            } else if ( stopword.length() > UtilConditions.MIN_LENGTH_LEVENSHTEIN
                && ( LevenstheinDistance.getLevenshteinDistance( word, stopword,
                    (int) Math.ceil( stopword.length() * UtilConditions.THRESHOLD_LEVENSHTEIN ) ) >= 0 )
                && word.length() == stopword.length() ) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the given name matches the criteria for guessed names
     *
     * @param name Name to check
     * @return true if name matches criteria, false otherwise
     */
    public boolean isPossibleWebsite( String name )
    {
        // Check if text is at least 3 characters long
        if ( name.trim().length() < 2 ) {
            return false;
        }
        // Check if the entire phrase is a stopword
        if ( isStopword( name ) ) {
            return false;
        }
        // Check if at least 50% of the name is letters
        return !(100 * countLetters(name) / (double) name.length() < 50);
    }


    /**
     * Checks if the given name matches the criteria for guessed names
     *
     * @param name Name to check
     * @return true if name matches criteria, false otherwise
     */
    private boolean isPossibleName(String name)
    {
        // Check if text is at least 3 characters long
        if ( name.trim().length() < 3 ) {
            return false;
        }
        // Check if the entire phrase is a stopword
        if ( isStopword( name ) ) {
            return false;
        }
        // Check if at least 50% of the name is letters
        if ( 100 * countLetters( name ) / (double) name.length() < 50 ) {
            return false;
        }
        // Check if avg characters in word is more then 2
        String[] potentialWordParts = name.split( " " );
        double avgWrdLen = 0;
        for ( String part : potentialWordParts ) {
            avgWrdLen += part.length();
        }
        avgWrdLen = avgWrdLen / potentialWordParts.length;
        return !(avgWrdLen <= 2);
    }


    public List<String> clean( List<String> originalList )
    {
        List<String> finalList = new ArrayList<>();
        for ( String str : originalList ) {
            finalList.add( cleanUp( str ) );
        }
        return finalList;
    }


    private String cleanUp(String str)
    {
        String regex = "[^\\w\\p{Lo}\\p{IsHan}&#\\*]+";

        if ( RegexUtils.checkIfStringContainsRegexPattern( str, "\\p{InCyrillic}" ) ) {
            regex = "[^\\w\\p{Lo}\\p{IsHan}\\p{InCyrillic}\"&#\\*]+";
        }
        return str.toLowerCase().replaceAll( regex, " " ).trim();
    }


    public List<String> lightClean( List<String> originalList )
    {
        List<String> finalList = new ArrayList<>();
        for ( String str : originalList ) {
            String val = lightCleanUp( str );
            if ( val.length() > 0 )
                finalList.add( val );
        }
        return finalList;
    }


    private String lightCleanUp(String str)
    {
        String regex = "[^\\w.\\-'’ü&!@()]+";
        if ( RegexUtils.checkIfStringContainsRegexPattern( str, "\\p{InCyrillic}" ) ) {
            regex = "[^\\w\\p{InCyrillic}.\\-\"'’ü&!@()]+";
        }
        return str.replaceAll( regex, " " ).trim();
    }


    public  List<String> filter( List<String> originalList )
    {
        List<String> finalList = new ArrayList<>();
        for ( String str : originalList ) {
            if ( !filtered( str ) ) {
                finalList.add( str );
            }
        }
        return finalList;
    }


    private boolean filtered(String str)
    {
        return str.trim().length() < UtilConditions.THRESHOLD_LENGTH;
    }


    public List<String> reorder( List<String> originalList )
    {
        List<String> finalList = new ArrayList<>();
        int size = originalList.size();
        for ( int i = 0; i < size; i++ ) {
            if ( i % 2 == 0 ) {
                finalList.add( originalList.get( i / 2 ) );
            } else {
                finalList.add( originalList.get( ( size - 1 ) - ( i / 2 ) ) );
            }
        }
        return finalList;
    }


    public List<String> nGrammify( String str, String separator, int length )
    {
        List<String> output = new ArrayList<>();
        String[] strParts = str.split( " " );

        for ( int n = 1; n <= length; n++ ) {
            for ( int i = 0; i < strParts.length - n + 1; i++ ) {
                StringBuilder sb = new StringBuilder();
                for ( int j = 0; j < n; j++ ) {
                    if ( sb.length() > 0 ) {
                        sb.append( separator );
                    }
                    sb.append( strParts[i + j] );
                }
                output.add( sb.toString() );
            }
        }
        return output;
    }


    public String getCleanText( String str )
    {
        String output = null;
        String[] strParts = str.split( " " );
        int startIndx = 0;
        int endIndx = strParts.length;
        for ( int i = 0; i < endIndx && isStopword( strParts[i] ); i++ ) {
            startIndx = i + 1;
        }

        for ( int i = strParts.length - 1; i > startIndx && isStopword( strParts[i] ); i-- ) {
            endIndx = i;
        }
        StringBuilder sb = new StringBuilder();
        for ( int i = startIndx; i < endIndx; i++ ) {
            sb.append( strParts[i] );
            sb.append( " " );
        }
        if ( sb.length() != strParts.length )
            if ( sb.length() > 0 ) {
                output = sb.toString().trim();
            }


        return output;
    }


    /**
     * Returns all the n grams of the specified size and lesser till size of 1 after removing the stop words and white spaces
     * @param lines sentence
     * @return n-gram List
     */
    public List<NGram> getNGrams( List<String[]> lines, int maxLength, int minLength )
    {
        List<NGram> response = new ArrayList<>();

        double lineConfidence = ConfidenceValueCollection.EXISTING_MERCHANT_LIST_EXTRACTOR;
        Map<Integer, HashSet<String>> lineWiseNGrams = new HashMap<>();
        for ( int i = 0; i < lines.size(); i++ ) {
            HashSet<String> existenceCheck;
            if ( lineWiseNGrams.containsKey( i ) )
                existenceCheck = lineWiseNGrams.get( i );
            else {
                existenceCheck = new HashSet<>();
            }
            lineWiseNGrams.put( i, existenceCheck );
            boolean rev = (i % 2 > 0) && !ENABLE_SEQUENTIAL_LOOKUP;

            int nGramLength = maxLength;
            while ( nGramLength >= minLength ) {
                int pos = 0;
                List<Integer> lineIndexes = new ArrayList<>();
                String[] tempList;
                if ( rev ) {
                    int lineIndx = ( lines.size() - 1 ) - ( i / 2 );
                    tempList = lines.get( lineIndx );
                    if ( !lineIndexes.contains( lineIndx ) )
                        lineIndexes.add( lineIndx );
                } else {
                    int lineIndx = ENABLE_SEQUENTIAL_LOOKUP ? i : i / 2;
                    tempList = lines.get( lineIndx );
                    if ( !lineIndexes.contains( lineIndx ) )
                        lineIndexes.add( lineIndx );
                }
                StringBuilder nGramVal = new StringBuilder();
                boolean completed = true;
                int nextLine = 1;
                for ( int ngramPos = 0; ngramPos < nGramLength; ngramPos++ ) {
                    if ( pos >= tempList.length ) {
                        pos = 0;
                        if ( !rev && (ENABLE_SEQUENTIAL_LOOKUP ? i : i / 2 ) + nextLine < lines.size() ) {
                            int lineIndx = (ENABLE_SEQUENTIAL_LOOKUP ? i : i / 2 ) + nextLine;
                            nextLine++;
                            tempList = lines.get( lineIndx );
                            if ( !lineIndexes.contains( lineIndx ) )
                                lineIndexes.add( lineIndx );
                        } else if ( rev && ( ( lines.size() - 1 ) - ( i / 2 ) + nextLine ) < lines.size() ) {
                            int lineIndx = ( lines.size() - 1 ) - ( i / 2 ) + nextLine;
                            nextLine++;
                            tempList = lines.get( lineIndx );
                            if ( !lineIndexes.contains( lineIndx ) )
                                lineIndexes.add( lineIndx );
                        } else {
                            completed = false;
                            break;
                        }
                    }
                    nGramVal.append( tempList[pos] + " " );
                    pos++;
                }
                if ( completed ) {
                    String possibleName = extractPotentialName( nGramVal.toString().trim() );
                    boolean alreadyExists = false;
                    for ( Integer lineNos : lineIndexes ) {
                        HashSet<String> existenceChk = lineWiseNGrams.get( lineNos );
                        if ( existenceChk == null ) {
                            existenceChk = new HashSet<>();
                            lineWiseNGrams.put( lineNos, existenceChk );
                        }
                        if ( existenceChk.contains( possibleName ) )
                            alreadyExists = true;
                    }
                    if ( possibleName != null && !alreadyExists ) {
                        NGram ngram = new NGram( possibleName, nGramLength, lineConfidence, 0.0 );
                        ngram.setMatchedVal( nGramVal.toString() );
                        int lineIndx;
                        if ( rev ) {
                            lineIndx = ( lines.size() - 1 ) - ( i / 2 );
                        } else {
                            lineIndx = ENABLE_SEQUENTIAL_LOOKUP ? i : i / 2;
                        }

                        ngram.setLineIndex( lineIndx );
                        response.add( ngram );
                        for ( Integer lineNos : lineIndexes ) {
                            HashSet<String> existenceChk = lineWiseNGrams.get( lineNos );
                            existenceChk.add( possibleName );
                        }
                    }
                }

                nGramLength--;
            }

            lineConfidence = lineConfidence * UtilConditions.SCORE_DECAY;
        }

        return response;
    }


    private int countLetters(String str)
    {
        int count = 0;
        for ( Character c : str.toCharArray() ) {
            if ( Character.isLetter( c ) ) {
                count++;
            }
        }
        return count;
    }


    private static List<String> lowercase(List<String> originalList)
    {
        List<String> lowercaseList = new ArrayList<>();
        for ( String str : originalList ) {
            lowercaseList.add( str.toLowerCase() );
        }
        return lowercaseList;
    }


    /**
     * Combines a string array into a single string
     *
     * @param strParts   Broken string to combine
     * @param separator  Separator to combine with
     * @param startIndex Start index to combine from
     * @param stopIndex  Stop index to combine until (inclusive)
     * @return Combined string
     */
    private String mkString(String[] strParts, String separator, int startIndex, int stopIndex)
    {
        StringBuilder sb = new StringBuilder();
        for ( int i = startIndex; i <= stopIndex; i++ ) {
            if ( sb.length() > 0 )
                sb.append( separator );
            sb.append( strParts[i] );
        }
        return stripExtraCharacters( sb.toString() );
    }


    private static String stripExtraCharacters(String value)
    {
        if ( value.isEmpty() )
            return value;
        int length = value.length();
        while ( ( value.charAt( length - 1 ) == '-' || value.charAt( length - 1 ) == '/' ) && length > 0 ) {
            length--;
        }
        int index = 0;
        while ( ( value.charAt( index ) == '-' || value.charAt( index ) == '/' ) && index < length ) {
            index++;
        }
        if ( index > 0 || length < value.length() )
            return value.substring( index, length );
        return value;
    }


    public static double getConfidence( String name, double base, double gVScore )
    {
        double score = base;
        if ( name.toLowerCase().contains( "gmail" ) || name.toLowerCase().contains( "google" )
            || name.toLowerCase().contains( "yahoo" ) ) {
            score = .1;
        }
        score = score + ( gVScore / 10 );

        return score;
    }


    /**
     * Breaks up a line into constituent words and tries to identify the largest
     * string that can form a potential merchant name
     *
     * @param line Line to extract the name from
     * @return Potential merchant name in the line
     */
    public String extractPotentialName( String line )
    {
        String[] lineParts = line.split( " " );
        int startIndex = 0;
        int stopIndex = lineParts.length - 1;
        for ( ; startIndex <= stopIndex; startIndex++ ) {
            if ( !isStopword( lineParts[startIndex].toLowerCase() )
                && countLetters( lineParts[startIndex].toLowerCase() ) != 0 )
                break;
        }
        for ( ; stopIndex >= startIndex; stopIndex-- ) {
            if ( !isStopword( lineParts[stopIndex].toLowerCase() ) && countLetters( lineParts[stopIndex].toLowerCase() ) != 0 )
                break;
        }
        String potentialName = mkString( lineParts, " ", startIndex, stopIndex );
        // Check if the name meets the criteria for guessed names, use website
        // domain for increased confidence
        if ( isPossibleName( potentialName ) ) {
            return potentialName;
        }
        return null;
    }


    /**
     * Tokenize each string in the list
     * @return Tokenized list for string
     */
    public List<String[]> tokenizeText( List<String> lines )
    {
        List<String[]> response = new ArrayList<>();
        for ( String line : lines ) {
            String[] parts = line.trim().split( "[\\s,:\"\\\\]" );
            List<String> tokens = new ArrayList<>();
            for ( String part : parts ) {
                if ( !part.isEmpty() ) {
                    tokens.add( part );
                }
            }
            Object[] objectList = tokens.toArray();
            String[] res = Arrays.copyOf( objectList, objectList.length, String[].class );
            if ( res.length > 0 )
                response.add( res );
        }
        return response;
    }
}
