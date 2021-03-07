package org.ccw.candlestick.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class TradeDataPointTest {

    @Test
    public void testTradePointComparisonOrdering(){
        TradeDataPoint p1 = new TradeDataPoint(1, 1l);
        TradeDataPoint p2 = new TradeDataPoint(2, 2l);
        TradeDataPoint p3 = new TradeDataPoint(3, 3l);
        ArrayList<TradeDataPoint> list = new ArrayList<>();
        list.add(p2);
        list.add(p3);
        list.add(p1);
        Collections.sort(list);
        Assertions.assertTrue( list.get(0) == p1, "TradePoint ordering (first) not correct");
        Assertions.assertTrue( list.get(list.size() - 1 ) == p3, "TradePoint ordering (last) not correct ");
    }
}
