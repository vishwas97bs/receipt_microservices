//package ai.infrrd.idc.receipt.fieldextractor.merchantname.model;
//
//import com.mongodb.Mongo;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import org.mongodb.morphia.Datastore;
//import org.mongodb.morphia.Morphia;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import java.net.UnknownHostException;
//
//@EnableMongoRepositories
//@Configuration
//public class MongoConfiguration extends AbstractMongoConfiguration {
//    private static final Logger LOG = LoggerFactory.getLogger( MongoConfiguration.class );
//    private static MongoClient mongoClient;
//    private static Datastore datastore = null;
//
//
//    public static synchronized Datastore init()
//    {
//        if ( datastore != null )
//            return datastore;
////        ResourceLoader loader = new ResourceLoader();
//
//        final Morphia morphia = new Morphia();
//        // tells Morphia where to find classes
//        // can be called multiple times with different packages or classes
//        morphia.mapPackage( "ai.infrrd.gimlet" );
//
//        // create the Datastore connecting to the default port on the local host
//        try {
//            String mongoServer = "mongodb://18.236.191.34:27017";
//            String mongoDbName = "gimletRepoDev";
//
//            if ( mongoClient == null ) {
//                mongoClient = new MongoClient( new MongoClientURI( mongoServer ) );
//            }
//
//            LOG.debug( "Mongo server : {} collection Name : {}", mongoServer, mongoDbName );
//            datastore = morphia.createDatastore( mongoClient, mongoDbName );
//            datastore.ensureIndexes();
//        } catch ( Exception e ) {
//            LOG.error( "Caught Exception while connecting to Mongo server", e );
//        }
//        return datastore;
//    }
//
//
//    @Override
//    public String getDatabaseName()
//    {
//        return "gimletRepoDev";
//    }
//
//
//    @Override
//    @Bean
//    public MongoClient mongo() throws Exception
//    {
//        return new MongoClient(
//                new MongoClientURI( "mongodb://18.236.191.34:27017" ) );
//    }
//
//
//    @Override
//    @Bean
//    public MongoDbFactory mongoDbFactory() throws Exception
//    {
//        return new SimpleMongoDbFactory( mongo(), this.getDatabaseName() );
//    }
//
//
//    @Override
//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception
//    {
//        return new MongoTemplate( mongoDbFactory() );
//    }
//}
