package com.github.sergiosagu.taxesminion.exchange;

import java.time.LocalDate;

import org.springframework.data.util.Pair;

/**
 * Interface for exchange resolvers.
 */
public interface ExchangeResolver {

    /**
     * Retrieves the exchange rate in BGN for 1 unit of the provided currency on the
     * provided date. If no exchange rate was available on the provided date (e.g.
     * national or official holiday) then the first date after that with available
     * exchange rate will be used.
     * 
     * @param currency Three-letter ISO currency code, e.g. USD or EUR.
     * @param date     Expected date of the exchange rate.
     * @return Pair with the exchange rate and the actual date of that exchange
     *         value.
     */
    Pair<Float, LocalDate> getExchange(String currency, LocalDate date);

}
