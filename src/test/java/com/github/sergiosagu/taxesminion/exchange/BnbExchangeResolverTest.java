package com.github.sergiosagu.taxesminion.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

@SpringBootTest
public class BnbExchangeResolverTest {

    @Autowired
    private BnbExchangeResolver resolver;

    @Test
    public void getValidExchange() {
        Pair<Double, LocalDate> value = resolver.getExchange("USD", LocalDate.of(2020, 1, 1));
        assertEquals(1.74737, value.getFirst());
        assertEquals(LocalDate.of(2020, 1, 2), value.getSecond());
    }

    @Test
    public void getExchangeInvalidCurrency() {
        assertThrows(RuntimeException.class, () -> {
            resolver.getExchange("bitcoin", LocalDate.of(2020, 1, 1));
        });
    }

    @Test
    public void getExchangeInvalidDate() {
        assertThrows(RuntimeException.class, () -> {
            resolver.getExchange("USD", LocalDate.of(2199, 1, 1));
        });
    }
}
