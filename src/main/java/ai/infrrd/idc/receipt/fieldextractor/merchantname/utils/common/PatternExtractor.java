package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternExtractor
{
    private static final Logger LOGGER = LoggerFactory.getLogger( PatternExtractor.class );

    private Pattern pattern;
    private String regEx;
    private int timeOutInSeconds;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
    static ExecutorService executorService = Executors.newFixedThreadPool( 10 );


    public PatternExtractor( String regEx )
    {
        this.regEx = regEx;
        this.pattern = Pattern.compile( regEx );
        timeOutInSeconds = 5;
    }


    public List<RegexMatchInfo> matchedPatterns(String text )
    {
        return matchedPatterns( text, 0 );
    }


    public List<RegexMatchInfo> matchedPatterns(String text, Integer groupNumber )
    {
        LOGGER.trace( "Extracting pattern from text {}", text );
        List<RegexMatchInfo> matchedPatterns = new ArrayList<>();
        Matcher matcher = pattern.matcher( new InterruptibleCharSequence( text ) );
        Future<List<RegexMatchInfo>> future = executorService.submit( () -> {
            while ( matcher.find() ) {
                if ( groupNumber != null ) {
                    matchedPatterns.add( new RegexMatchInfo( matcher.group( groupNumber ), matcher.start(), matcher.end(),
                        matcher.groupCount() ) );
                } else {
                    matchedPatterns
                        .add( new RegexMatchInfo( matcher.group( 0 ), matcher.start(), matcher.end(), matcher.groupCount() ) );
                }
            }
        }, matchedPatterns );
        try {
            return future.get( timeOutInSeconds, TimeUnit.SECONDS );
        } catch ( InterruptedException | ExecutionException | TimeoutException | StackOverflowError e ) {
            future.cancel( true );
            text = processInputString( text );
            LOGGER.warn( "Pattern did not complete after {} seconds", timeOutInSeconds );
            LOGGER.warn( "Time: {}", DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ) );
            LOGGER.warn( "Expression: {} \nText: {} \nCalling Method: {}", regEx, text,
                Thread.currentThread().getStackTrace()[2] );
            return new ArrayList<>();
        }
    }


    private String processInputString(String input )
    {
        StringBuilder output = new StringBuilder();
        String[] lines = input.split( "\\\\r\\\\n|\\r\\n|\n" );
        if ( lines.length == 1 || input.isEmpty() ) {
            return input;
        }
        for ( String line : lines ) {
            output.append( line );
            output.append( "\\r\\n" );
        }
        return output.toString();
    }


    public List<RegexMatchInfo> matchedPatterns(String text, String groupName )
    {
        LOGGER.trace( "Extracting pattern from text {}", text );
        List<RegexMatchInfo> matchedPatterns = new ArrayList<>();
        Matcher matcher = pattern.matcher( new InterruptibleCharSequence( text ) );
        Future<List<RegexMatchInfo>> future = executorService.submit( () -> {
            while ( matcher.find() ) {
                if ( groupName != null ) {
                    matchedPatterns.add( new RegexMatchInfo( matcher.group( groupName ), matcher.start(), matcher.end(),
                        matcher.groupCount() ) );
                } else {
                    matchedPatterns
                        .add( new RegexMatchInfo( matcher.group( 0 ), matcher.start(), matcher.end(), matcher.groupCount() ) );
                }
            }
        }, matchedPatterns );
        try {
            return future.get( timeOutInSeconds, TimeUnit.SECONDS );
        } catch ( InterruptedException | ExecutionException | TimeoutException e ) {
            future.cancel( true );
            text = processInputString( text );
            LOGGER.warn( "Pattern did not complete after {} seconds", timeOutInSeconds );
            LOGGER.warn( "Time: {}", DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ) );
            LOGGER.warn( "Expression: {} \nText: {} \nCalling Method: {}", regEx, text,
                Thread.currentThread().getStackTrace()[2] );
            return new ArrayList<>();
        }
    }


    public boolean isMatchedPatterns( String text )
    {
        LOGGER.trace( "Extracting pattern from text {}", text );
        List<RegexMatchInfo> matchedPatterns = new ArrayList<>();
        Matcher matcher = pattern.matcher( new InterruptibleCharSequence( text ) );
        Future<Boolean> future = executorService.submit( () -> matcher.find() );
        try {
            return future.get( timeOutInSeconds, TimeUnit.SECONDS );
        } catch ( InterruptedException | ExecutionException | TimeoutException e ) {
            future.cancel( true );
            text = processInputString( text );
            LOGGER.warn( "Pattern did not complete after {} seconds", timeOutInSeconds );
            LOGGER.warn( "Time: {}", DATE_FORMAT.format( new Date( System.currentTimeMillis() ) ) );
            LOGGER.warn( "Expression: {} \nText: {} \nCalling Method: {}", regEx, text,
                Thread.currentThread().getStackTrace()[2] );
            return false;
        }
    }
}
