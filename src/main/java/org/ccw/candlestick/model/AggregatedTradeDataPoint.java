package org.ccw.candlestick.model;

import lombok.Data;
import org.ccw.candlestick.util.DateTimeUtil;

import java.util.*;

@Data
/**
 * this class stores the program generated Candlestick data, derived from actual trade data
 *
 */
public class AggregatedTradeDataPoint {
    private long timestamp;
    private long openTime;
    private long closeTime;
    // there is possibility that the trade data obtained are of the same time stamp value
    // we will store the price of same timestamp for reconciliation
    private List<TradeDataPoint> openPrices = new ArrayList<>();
    private List<TradeDataPoint> closePrices = new ArrayList<>();
    private double high;
    private double low;

    public AggregatedTradeDataPoint(TradeDataPoint p, long timestamp){
        this.timestamp = timestamp;
        this.high = p.getPrice();
        this.low = p.getPrice();
        this.openPrices.add(p);
        this.closePrices.add(p);
        this.openTime = p.getTimestamp();
        this.closeTime = p.getTimestamp();
    }

    public void updateData(TradeDataPoint p) {
        if (this.high < p.getPrice()) {
            this.high = p.getPrice();
        }
        if (this.low > p.getPrice()) {
            this.low = p.getPrice();
        }
        if (this.openTime > p.getTimestamp()) {
            this.openTime = p.getTimestamp();
            this.openPrices.clear();
            this.openPrices.add(p);
        }
        else if (this.openTime == p.getTimestamp()){
            getOpenPrices().add(p);
        }
        if (this.closeTime < p.getTimestamp()){
            this.closeTime = p.getTimestamp();
            this.closePrices.clear();
            this.closePrices.add(p);
        }
        else if (this.closeTime == p.getTimestamp()) {
            this.closePrices.add(p);
        }
    }

    @Override
    public String toString() {
        return "AggregatedTradeDataPoint{" +
                "timestamp=" + DateTimeUtil.getDateTimeOfTimestamp(timestamp) +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", openPrices=" + openPrices +
                ", closePrices=" + closePrices +
                ", high=" + high +
                ", low=" + low +
                '}';
    }
}
