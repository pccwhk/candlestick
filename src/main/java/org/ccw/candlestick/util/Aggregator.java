package org.ccw.candlestick.util;

import org.ccw.candlestick.model.AggregatedTradeDataPoint;
import org.ccw.candlestick.model.CandleStickDataPoint;
import org.ccw.candlestick.model.CandleSticks;
import org.ccw.candlestick.model.TradeDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class Aggregator {

    private static Logger logger = LoggerFactory.getLogger(Aggregator.class);

    private static boolean checkPrice(final List<TradeDataPoint> tradeDataPoints, final double price) {
        for (var tradeDataPoint : tradeDataPoints) {
            if (price == tradeDataPoint.getPrice()){
                return true;
            }
        }
        return false;
    }

    public static boolean validateCandleStick(final Map<Long, AggregatedTradeDataPoint> aggregatedData, final CandleStickDataPoint candleStick) {
        logger.info("validating  {} now" , candleStick);
        boolean result = true;
        if (aggregatedData.get(candleStick.getTimestamp()) == null){
            logger.info("Cannot validate the candle stick data due to not enough information");
            result = false;
        }
        else {
            AggregatedTradeDataPoint aggregatedTradeDataPoint = aggregatedData.get(candleStick.getTimestamp());
            if (candleStick.getHigh() != aggregatedTradeDataPoint.getHigh() ){
                logger.info("Candlestick high is {} while the aggregated data is {}", candleStick.getHigh(), aggregatedTradeDataPoint.getHigh());
                result = false;
            }
            if (candleStick.getLow() != aggregatedTradeDataPoint.getLow() ){
                logger.info("Candlestick low is {} while the aggregated data is {}", candleStick.getLow(), aggregatedTradeDataPoint.getLow());
                result = false;
            }
            // this is to check if there are more than one trade price of the same timestamp for open
            if (!checkPrice(aggregatedTradeDataPoint.getOpenPrices(), candleStick.getOpen())){
                logger.info("Candlestick open is {} while the aggregated data is {}", candleStick.getOpen(), aggregatedTradeDataPoint.getOpenPrices());
                result = false;
            }
            // this is to check if there are more than one trade price of the same timestamp for close
            if (!checkPrice(aggregatedTradeDataPoint.getClosePrices(), candleStick.getClose())){
                logger.info("Candlestick close is {} while the aggregated data is {}", candleStick.getHigh(), aggregatedTradeDataPoint.getClosePrices());
                result = false;
            }
        }
        return result;
    }

    public static Map<Long, AggregatedTradeDataPoint> aggregateAsCandleStick(List<TradeDataPoint> inputList, final long startTime, long interval){
        if (inputList == null || inputList.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, AggregatedTradeDataPoint> map = new HashMap<>();
        long currentStartTime = startTime - interval ;
        logger.info("Aggregation first start time is {}" , DateTimeUtil.getDateTimeOfTimestamp(currentStartTime));
        long currentEndTime = startTime ;


        for (TradeDataPoint p : inputList){
            long currentTimestamp = p.getTimestamp();
            if (currentTimestamp < currentStartTime) {
                // skip for data that are smaller than current start time
                continue;
            }
            else if (currentTimestamp > currentEndTime ){
                //  trade data is now in next interval
                do {
                    currentEndTime = currentEndTime + interval;
                } while (currentTimestamp > currentEndTime);
                currentStartTime = currentEndTime - interval;
            }
            AggregatedTradeDataPoint data = map.get(currentStartTime);
            if (data == null) {
                data = new AggregatedTradeDataPoint(p, currentStartTime);
                map.put(currentStartTime, data);
            } else {
                data.updateData(p);
            }
        }
        return map;
    }
}
