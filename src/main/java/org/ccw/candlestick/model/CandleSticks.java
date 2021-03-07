package org.ccw.candlestick.model;

import lombok.Data;

import java.util.List;

@Data
public class CandleSticks {
    private String instrumentName;
    private String interval;
    private List<CandleStickDataPoint> data;

    public CandleSticks(String instrumentName, String interval, List<CandleStickDataPoint> data) {
        this.instrumentName = instrumentName;
        this.interval = interval;
        this.data = data;
    }
}
