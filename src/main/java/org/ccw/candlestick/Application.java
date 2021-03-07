package org.ccw.candlestick;

import org.ccw.candlestick.model.*;
import org.ccw.candlestick.util.Aggregator;
import org.ccw.candlestick.util.CandleStickDataLoader;
import org.ccw.candlestick.util.TradeDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.*;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Aggregator.class);

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static final String baseUrl = "https://api.crypto.com/v2/public/";

//    public static final String cancelStickDataUrl = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=BTC_USDT&timeframe=1m";
//    public static final String tradeDataUrl = "https://api.crypto.com/v2/public/get-trades?instrument_name=BTC_USDT";

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length < 2) {
            logger.error("usage: java -jar CandleStick-1.0-SNAPSHOT.jar [instrument] [number in minute (1, 5)]");
            logger.error("usage: java -jar CandleStick-1.0-SNAPSHOT.jar BTC_USDT 1");
            System.exit(1);
        }

        if (!"1".equals(args[1]) && !"5".equals(args[1])){
            logger.error("Valid time frame is 1 or 5 only");
            System.exit(1);
        }

        String instrument = args[0];
        String timeFrame = args[1] + "m";

        logger.info("starting to get trade data {} {}", instrument, timeFrame);

        int multiplier = 1;
        int intervalInSeconds = 60  ;
        long secondToMilliseconds = 1000l;

        Set<TradeDataPoint> set = new HashSet<>();
        TradeDataLoader tradeDataLoader = new TradeDataLoader(baseUrl, instrument);

        // keep loading data for least equal to the time duration of one candle stick
        long endTime = System.currentTimeMillis() + (intervalInSeconds * (multiplier+1)) * secondToMilliseconds;
        do {
            if (tradeDataLoader.getData().isPresent()) {
                set.addAll(tradeDataLoader.getData().get().getData());
            }
            Thread.sleep(100);
        } while ( System.currentTimeMillis() < endTime );

        List<TradeDataPoint> allTrades = new ArrayList<>(set);
        Collections.sort(allTrades);

        CandleStickDataLoader candleStickDataLoader = new CandleStickDataLoader(baseUrl, instrument , timeFrame);
        Optional<CandleSticks> candlestickOpt = candleStickDataLoader.getData();
        Map<Long, AggregatedTradeDataPoint> aggregatedCandleSticks = null;
        if (candlestickOpt.isPresent()) {
            CandleSticks candleSticks = candlestickOpt.get();
            if (candleSticks.getData().size() > 0 ) {
                logger.info("received candle stick data total count = " + candleSticks.getData().size());

                if (allTrades.size() > 0 ) {
                    Trades trades = tradeDataLoader.getData().get();
                    TradeDataPoint firstTrade = allTrades.get(0);
                    TradeDataPoint lastTrade = allTrades.get(trades.getData().size() - 1);
                    logger.info("first trade data is {} ", firstTrade);
                    logger.info("last trade data is {}",lastTrade);
                    //logger.info(trades.toString());
                    for (CandleStickDataPoint dataPoint : candleSticks.getData()) {
                        if (dataPoint.getTimestamp() < firstTrade.getTimestamp() || dataPoint.getTimestamp() > lastTrade.getTimestamp()) {
                            //logger.info("skipping the candle stick validation (due to not enough trade data) for " + stick);
                            continue;
                        }
                        else {
                            //
                            if (aggregatedCandleSticks == null) {
                                aggregatedCandleSticks = Aggregator.aggregateAsCandleStick(allTrades, dataPoint.getTimestamp(),
                                        multiplier * intervalInSeconds * secondToMilliseconds);
                            }
                            if (Aggregator.validateCandleStick(aggregatedCandleSticks, dataPoint)){
                                logger.info("validate result: Pass.");
                            }
                            else {
                                logger.info("validate result: data not match");
                            }
                        }
                    }


                }
            }
        }


    }

}