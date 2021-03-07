package org.ccw.candlestick.util;

import org.ccw.candlestick.model.CandleSticks;
import org.ccw.candlestick.model.TradeDataPoint;
import org.ccw.candlestick.model.Trades;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class DataLoaderTest {

    @Test
    public void loadCandleStickTest(){
        CandleStickDataLoader dataLoader = new CandleStickDataLoader("", "", "");
        String testJsonStr = """
                    {
                  "code":0,
                  "method":"public/get-candlestick",
                  "result":{
                    "instrument_name":"BTC_USDT",
                    "interval":"5m",
                    "data":[
                      {"t":1596944700000,"o":11752.38,"h":11754.77,"l":11746.65,"c":11753.64,"v":3.694583},
                      {"t":1596945000000,"o":11753.63,"h":11754.77,"l":11739.83,"c":11746.17,"v":2.073019},
                      {"t":1596945300000,"o":11746.16,"h":11753.24,"l":11738.1,"c":11740.65,"v":0.867247}
                    ]
                  }
                }
                """;
        Optional<CandleSticks> candleSticksOptional = dataLoader.deserialize(testJsonStr);
        Assertions.assertTrue(candleSticksOptional.isPresent());
        Assertions.assertTrue(candleSticksOptional.get().getData().size() == 3, "Candle stick Data Loader should contain 3entries");
        Assertions.assertTrue(candleSticksOptional.get().getData().get(2).getTimestamp() == 1596945300000l  );
        Assertions.assertTrue(candleSticksOptional.get().getData().get(2).getHigh() == 11753.24  );
        Assertions.assertTrue(candleSticksOptional.get().getData().get(2).getLow() == 11738.1  );
        Assertions.assertTrue(candleSticksOptional.get().getData().get(2).getOpen() == 11746.16  );
        Assertions.assertTrue(candleSticksOptional.get().getData().get(2).getClose() == 11740.65  );
    }

    @Test
    public void loadTradeDataTest(){
        TradeDataLoader dataLoader = new TradeDataLoader("", "");
        String testJsonStr = """
{"code":0,"method":"public/get-trades",
"result":{"instrument_name":"BTC_USDT","data":[
{"dataTime":1615106962410,"d":1250579130188425632,"s":"SELL","p":50474.12,"q":0.028575,"t":1615106962409,"i":"BTC_USDT"},
{"dataTime":1615106958950,"d":1250579014092727232,"s":"SELL","p":50463.71,"q":0.000512,"t":1615106958949,"i":"BTC_USDT"},
{"dataTime":1615106957571,"d":1250578967818329632, "s":"BUY","p":50457.47,"q":0.002000,"t":1615106957570,"i":"BTC_USDT"},
{"dataTime":1615106955202,"d":1250578888317567584,"s":"SELL","p":50443.72,"q":0.014286,"t":1615106955200,"i":"BTC_USDT"}]}}
                """;
        Optional<Trades> trades =  dataLoader.deserialize(testJsonStr);
        Assertions.assertTrue(trades.isPresent());
        Assertions.assertTrue(trades.get().getData().size() == 4, "Trade Data Loader should contain 4 entries");
        ArrayList<TradeDataPoint> list = new ArrayList<>();
        list.addAll(trades.get().getData());
        Collections.sort(list);
        Assertions.assertTrue(list.get(2).getTimestamp() == 1615106958949l  );
        Assertions.assertTrue(list.get(2).getPrice() == 50463.71  );
    }
}
