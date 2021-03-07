package org.ccw.candlestick.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Trades {
    private String instrumentName;
    private Set<TradeDataPoint> data;

    public Trades(String instrumentName, Set<TradeDataPoint> data) {
        this.instrumentName = instrumentName;
        this.data = data;
    }
}
