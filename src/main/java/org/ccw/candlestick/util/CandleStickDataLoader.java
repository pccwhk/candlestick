package org.ccw.candlestick.util;

import com.google.gson.*;
import org.ccw.candlestick.model.CandleStickDataPoint;
import org.ccw.candlestick.model.CandleSticks;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

public class CandleStickDataLoader extends DataLoader<CandleSticks> implements JsonDeserializer<CandleSticks> {

    private String baseUrl;
    private String instrument;
    private String timeFrame;

    @Override
    public CandleSticks deserialize
            (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jObject = jElement.getAsJsonObject();
        String instrument = jObject.get("result").getAsJsonObject().getAsJsonPrimitive("instrument_name").getAsString();
        String interval = jObject.get("result").getAsJsonObject().getAsJsonPrimitive("interval").getAsString();
        JsonArray array = jObject.get("result").getAsJsonObject().getAsJsonArray("data");
        var list = new ArrayList<CandleStickDataPoint>();
        Iterator<JsonElement> itr = array.iterator();
        itr.forEachRemaining(x -> {
            CandleStickDataPoint p = new CandleStickDataPoint(
                    x.getAsJsonObject().get("o").getAsDouble(),
                    x.getAsJsonObject().get("h").getAsDouble(),
                    x.getAsJsonObject().get("l").getAsDouble(),
                    x.getAsJsonObject().get("c").getAsDouble(),
                    x.getAsJsonObject().get("t").getAsLong());
            list.add(p);
        });
        Collections.sort(list);
        return new CandleSticks(instrument, interval, list);
    }

    @Override
    public String getRequestURL() {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(GET_CANDLE_STICK).append('?');
        sb.append(INSTRUMENT_NAME).append(instrument).append("&");
        sb.append(TIME_FRAME).append(timeFrame);
        return sb.toString();
    }

    @Override
    Optional<CandleSticks> deserialize(String body) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(CandleSticks.class, this);
        return Optional.of(gsonBuilder.create().fromJson(body, CandleSticks.class));
    }

    public CandleStickDataLoader(String baseUrl, String instrument, String timeFrame) {
        this.baseUrl = baseUrl;
        this.instrument = instrument;
        this.timeFrame = timeFrame;
    }
}
