package org.ccw.candlestick.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

public abstract class DataLoader<T> {

    public static String GET_CANDLE_STICK = "get-candlestick";
    public static String GET_TRADES = "get-trades";
    public static String TIME_FRAME = "timeframe=";
    public static String INSTRUMENT_NAME = "instrument_name=";

    private HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    abstract String getRequestURL();

    //  http success status start with 2xx , divide by 100 to get the first
    private boolean isSuccess(int statusCode){
        return  2 == (statusCode / 100);
    }

    abstract Optional<T> deserialize(String body);

    public Optional<T> getData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getRequestURL()))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (isSuccess(response.statusCode())) {
            return deserialize(response.body());
        }
        return Optional.empty();
    }
}
