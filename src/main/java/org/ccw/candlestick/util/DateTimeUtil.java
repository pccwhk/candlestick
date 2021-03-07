package org.ccw.candlestick.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtil {
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long roundDownToNearestInstant(long inputTime, long numberOfSecond) {
        long millisecond = numberOfSecond * 1000;
        return (inputTime / millisecond) * millisecond;
    }
}
