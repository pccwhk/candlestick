package org.ccw.candlestick.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CandleStickDataPointTest {

    @Test
    public void testTradePointComparisonOrdering(){
        CandleStickDataPoint p1 = new CandleStickDataPoint(1, 1l, 1, 1, 1l);
        CandleStickDataPoint p2 = new CandleStickDataPoint(2, 2l, 2, 2, 2l);
        CandleStickDataPoint p3 = new CandleStickDataPoint(3, 3l, 3, 3, 3l);
        CandleStickDataPoint p4 = new CandleStickDataPoint(4, 4l, 4, 4, 4l);
        List<CandleStickDataPoint> list = new ArrayList<>();
        list.add(p2);
        list.add(p3);
        list.add(p1);
        list.add(p4);
        Collections.sort(list);
        Assertions.assertTrue( list.get(0) == p1, "CandleStickDataPoint ordering (first) not correct");
        Assertions.assertTrue( list.get(list.size() - 1) == p4, "CandleStickDataPoint ordering (last) not correct ");
    }
}
