package org.ccw.candlestick.model;

import lombok.Data;
import org.ccw.candlestick.util.DateTimeUtil;

@Data
/**
 * data class to stored the download data for CandleStick
 */
public class CandleStickDataPoint implements Comparable<CandleStickDataPoint> {
    private double open;
    private double high;
    private double low;
    private double close;
    private long timestamp;

    public CandleStickDataPoint(double open, double high, double low, double close, long timestamp) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.timestamp = timestamp;
    }


    @Override
    public int compareTo(CandleStickDataPoint o) {
        return Long.compare(this.timestamp, o.timestamp) ;
    }

    @Override
    public String toString() {
        return "CandleStickDataPoint{" +
                "timestamp=" + DateTimeUtil.getDateTimeOfTimestamp(timestamp) +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                '}';
    }
}
