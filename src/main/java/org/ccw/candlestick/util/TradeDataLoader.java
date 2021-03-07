package org.ccw.candlestick.util;

import com.google.gson.*;
import org.ccw.candlestick.model.TradeDataPoint;
import org.ccw.candlestick.model.Trades;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class TradeDataLoader extends DataLoader<Trades> implements JsonDeserializer<Trades> {
    private String baseUrl;
    private String instrument;

    public TradeDataLoader(String baseUrl, String instrument) {
        this.baseUrl = baseUrl;
        this.instrument = instrument;
    }

    @Override
    public Trades deserialize
            (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jObject = jElement.getAsJsonObject();
        String instrument = jObject.get("result").getAsJsonObject().getAsJsonPrimitive("instrument_name").getAsString();
        JsonArray array = jObject.get("result").getAsJsonObject().getAsJsonArray("data");
        var data = new HashSet<TradeDataPoint>();
        Iterator<JsonElement> itr = array.iterator();
        itr.forEachRemaining(x -> {
            TradeDataPoint p = new TradeDataPoint(
                    x.getAsJsonObject().get("p").getAsDouble(),
                    x.getAsJsonObject().get("t").getAsLong());
            data.add(p);
        });
        //Collections.sort(arrayList);
        return new Trades(instrument, data);
    }

    public String getRequestURL() {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(GET_TRADES);
        if (instrument != null ||  instrument.length() > 0)
            sb.append('?').append(INSTRUMENT_NAME).append(instrument);
        return sb.toString();
    }

    @Override
    Optional<Trades> deserialize(String body) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Trades.class, this);
        return Optional.of(gsonBuilder.create().fromJson(body, Trades.class));
    }
}
