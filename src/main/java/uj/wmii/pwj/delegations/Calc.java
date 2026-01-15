package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {
        ZonedDateTime startTime = ZonedDateTime.parse(start, FORMATTER);
        ZonedDateTime endTime = ZonedDateTime.parse(end, FORMATTER);

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        Duration duration = Duration.between(startTime, endTime);
        long totalMinutes = duration.toMinutes();

        long fullDays = totalMinutes / 1440;
        long remainingMinutes = totalMinutes % 1440;

        BigDecimal totalAmount = dailyRate.multiply(BigDecimal.valueOf(fullDays));

        if (remainingMinutes > 0) {
            if (remainingMinutes <= 480) {
                totalAmount = totalAmount.add(dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
            } else if (remainingMinutes <= 720) {
                totalAmount = totalAmount.add(dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
            } else {
                totalAmount = totalAmount.add(dailyRate);
            }
        }

        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
