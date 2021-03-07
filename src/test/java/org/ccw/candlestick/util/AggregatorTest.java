package org.ccw.candlestick.util;

import org.ccw.candlestick.model.AggregatedTradeDataPoint;
import org.ccw.candlestick.model.TradeDataPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AggregatorTest {
    @Test
    public void testDuplicatedOpenPriceAndClose(){
        List<TradeDataPoint> inputDataList = new ArrayList<>();
        long now = System.currentTimeMillis();
        double low = 10;
        double high = low + 10;
        TradeDataPoint lowPrice = new TradeDataPoint(low, now);
        TradeDataPoint highPrice = new TradeDataPoint(high, now);
        inputDataList.add(lowPrice);
        inputDataList.add(highPrice);
        long timeRoundDownToMinute = DateTimeUtil.roundDownToNearestInstant(now, 60);
        Map<Long, AggregatedTradeDataPoint>  map =  Aggregator.aggregateAsCandleStick(inputDataList, timeRoundDownToMinute, 60 * 1000);
        Assertions.assertTrue(map.get(timeRoundDownToMinute) != null);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getOpenPrices().size() ==  2);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getClosePrices().size() ==  2);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getLow() ==  low);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getHigh() ==  high);
    }

    @Test
    public void testDiffOpenHighLowClose() {
        List<TradeDataPoint> inputDataList = new ArrayList<>();
        long now = System.currentTimeMillis();
        double low = 10;
        double high = low + 10;
        TradeDataPoint lowPrice = new TradeDataPoint(low, now);
        TradeDataPoint highPrice = new TradeDataPoint(high, now + 1000);
        inputDataList.add(lowPrice);
        inputDataList.add(highPrice);
        long timeRoundDownToMinute = DateTimeUtil.roundDownToNearestInstant(now, 60);
        Map<Long, AggregatedTradeDataPoint>  map =  Aggregator.aggregateAsCandleStick(inputDataList, timeRoundDownToMinute, 60 * 1000);
        Assertions.assertTrue(map.get(timeRoundDownToMinute) != null);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getLow() ==  low);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getHigh() ==  high);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getClosePrices().size() == 1);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getOpenPrices().size() == 1);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getClosePrices().get(0) == highPrice);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getOpenPrices().get(0) ==  lowPrice);
    }

    @Test
    public void testSingleDataPoint() {
        List<TradeDataPoint> inputDataList = new ArrayList<>();
        long now = System.currentTimeMillis();
        double low = 10;
        TradeDataPoint lowPrice = new TradeDataPoint(low, now);
        inputDataList.add(lowPrice);
        long timeRoundDownToMinute = DateTimeUtil.roundDownToNearestInstant(now, 60);
        Map<Long, AggregatedTradeDataPoint>  map =  Aggregator.aggregateAsCandleStick(inputDataList, timeRoundDownToMinute, 60 * 1000);
        Assertions.assertTrue(map.get(timeRoundDownToMinute) != null);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getLow() ==  low);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getHigh() ==  low);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getClosePrices().size() == 1);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getClosePrices().get(0) == lowPrice);
        Assertions.assertTrue(map.get(timeRoundDownToMinute).getOpenPrices().get(0) ==  lowPrice);
    }
}
