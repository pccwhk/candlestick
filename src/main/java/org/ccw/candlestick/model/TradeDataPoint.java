package org.ccw.candlestick.model;

import lombok.Data;
import org.ccw.candlestick.util.DateTimeUtil;


@Data
/**
 * data class to store the Trade data download from web
 */
public class TradeDataPoint implements Comparable<TradeDataPoint> {
    private double price;
    private long timestamp;

    public TradeDataPoint(double price, long timestamp) {
        this.price = price;
        this.timestamp = timestamp;

    }

    @Override
    public int compareTo(TradeDataPoint o) {
        return Long.compare(this.timestamp, o.timestamp);
    }

    @Override
    public String toString() {
        return "TradeDataPoint{" +
                "timestamp=" + DateTimeUtil.getDateTimeOfTimestamp(timestamp) +
                ", price=" + price +
                '}';
    }
}
