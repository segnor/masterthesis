/**
 * Created by Sascha P. Lorenz on 14/12/14.
 *  The LogAnalyzer takes in an apache access log file and
 * computes some statistics on them.
 *
 * Example command to run:
 * %  ${YOUR_SPARK_HOME}/bin/spark-submit
 *     --class "cLogsAnalyzer"
 *     --master local[4]
 *     target/log-analyzer-1.0.jar
 *     ../../data/apache.accesslog
 */

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;


import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Calendar;


public class SparkLogAnalyzer {

    static Logger logger = Logger.getRootLogger();

    private static Function2<Long, Long, Long> SUM_REDUCER = (a, b) -> a + b;

    private static class ValueComparator<K, V>
            implements Comparator<Tuple2<K, V>>, Serializable {
        private Comparator<V> comparator;

        public ValueComparator(Comparator<V> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
            return comparator.compare(o1._2(), o2._2());
        }
    }

    public static void main(String[] args) {

        Long a, b;

        // Log4j-Konfiguration laden
        PropertyConfigurator.configure("log4j.properties");
        logger.setLevel(Level.INFO);
        logger.warn("DEBUG: TESTSTART __________________________________________________________");
        a= System.currentTimeMillis();

        logger.warn("Current timestamp: " + a.toString());

        // Create a Spark Context on local prestarted Spark Master
        //SparkConf conf = new SparkConf().setMaster("spark://Saschas-MacBook-Pro.local:7077").setAppName("Log Analyzer");
        // Create a Spark Context on local individual Spark instance (life cycle == runtime)
        SparkConf conf = new SparkConf().setAppName("Log Analyzer");


        //SparkConf conf = new SparkConf()
        //        .setMaster("mesos://localhost:5050")
        //        .setAppName("Log Analyzer")
        //        .set("spark.executor.uri", "../../../Applications/spark-1.1.0.tar.gz");


        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the text file into Spark.
        if (args.length == 0) {
            System.out.println("Must specify an access logs file.");
            System.exit(-1);
        }
        String logFile = args[0];
        JavaRDD<String> logLines = sc.textFile(logFile);

        // Convert the text log lines to ApacheAccessLog objects and cache them
        //   since multiple transformations and actions will be called on that data.
        logger.warn("Current timestamp: " + a.toString());
        JavaRDD<ApacheAccessLog> accessLogs =
                logLines.map(ApacheAccessLog::parseFromLogLine).cache();

        // Calculate statistics based on the content size.
        // Note how the contentSizes are cached as well since multiple actions
        //   are called on that RDD.
        JavaRDD<Long> contentSizes =
                accessLogs.map(ApacheAccessLog::getContentSize).cache();
        System.out.println(String.format("Content Size Avg: %s, Min: %s, Max: %s",
                contentSizes.reduce(SUM_REDUCER) / contentSizes.count(),
                contentSizes.min(Comparator.naturalOrder()),
                contentSizes.max(Comparator.naturalOrder())));

        // Compute Response Code to Count.
        List<Tuple2<Integer, Long>> responseCodeToCount =
                accessLogs.mapToPair(log -> new Tuple2<>(log.getResponseCode(), 1L))
                        .reduceByKey(SUM_REDUCER)
                        .take(100);
        System.out.println(String.format("Response code counts: %s", responseCodeToCount));

        // Any IPAddress that has accessed the server more than 10 times.
        List<String> ipAddresses =
                accessLogs.mapToPair(log -> new Tuple2<>(log.getIpAddress(), 1L))
                        .reduceByKey(SUM_REDUCER)
                        .filter(tuple -> tuple._2() > 10)
                        .map(Tuple2::_1)
                        .take(100);
        System.out.println(String.format("IPAddresses > 10 times: %s", ipAddresses));

        // Top Endpoints.
        List<Tuple2<String, Long>> topEndpoints = accessLogs
                .mapToPair(log -> new Tuple2<>(log.getEndpoint(), 1L))
                .reduceByKey(SUM_REDUCER)
                .top(10, new ValueComparator<>(Comparator.<Long>naturalOrder()));
        System.out.println(String.format("Top Endpoints: %s", topEndpoints));

        // Stop the Spark Context before exiting.
        sc.stop();
        logger.warn("DEBUG: TESTEND __________________________________________________________");
        b= System.currentTimeMillis();
        logger.warn("Current timestamp: " + a.toString());

        logger.warn("Total execution time was: " + (b-a));
    }
}




