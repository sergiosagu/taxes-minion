package com.github.sergiosagu.taxesminion.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.sergiosagu.taxesminion.model.InputData.BuyEntry;
import com.github.sergiosagu.taxesminion.model.NapOutputData.Annex8Part1Entry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NapDeclarationBuilderTest {

    @Autowired
    private NapDeclarationBuilder builder;

    @Test
    public void processBuyEntry() {
        BuyEntry input = new BuyEntry("US", "USD", 10.0f, LocalDate.of(2020, 1, 1), 1000.0f);
        Annex8Part1Entry output = builder.processBuyEntry(input);
        assertBuyEntry("акция", input.getCountry(), input.getAmount(), input.getBuyDate(), input.getTotalPrice(),
                1747.37f, null, output);
    }

    @Test
    public void processBuyData() {
        List<BuyEntry> data = new ArrayList<>(3);
        data.add(new BuyEntry("US", "USD", 10.0f, LocalDate.of(2020, 1, 1), 1000.0f));
        data.add(new BuyEntry("US", "USD", 15.0f, LocalDate.of(2020, 11, 1), 1500.0f));
        data.add(new BuyEntry("US", "USD", 5.0f, LocalDate.of(2020, 6, 1), 500.0f));
        data.add(new BuyEntry("US", "USD", 10.0f, LocalDate.of(2020, 6, 1), 1500.0f));
        data.add(new BuyEntry("US", "USD", 20.0f, LocalDate.of(2020, 3, 1), 2000.0f));
        List<Annex8Part1Entry> output = builder.processBuyData(data);
        assertBuyEntry("акция", data.get(0).getCountry(), data.get(0).getAmount(), data.get(0).getBuyDate(),
                data.get(0).getTotalPrice(), 1747.37f, 1, output.get(0));
        assertBuyEntry("акция", data.get(4).getCountry(), data.get(4).getAmount(), data.get(4).getBuyDate(),
                data.get(4).getTotalPrice(), 3517.04f, 2, output.get(1));
        assertBuyEntry("акция", data.get(2).getCountry(), data.get(2).getAmount(), data.get(2).getBuyDate(),
                data.get(2).getTotalPrice(), 879.735f, 3, output.get(2));
        assertBuyEntry("акция", data.get(3).getCountry(), data.get(3).getAmount(), data.get(3).getBuyDate(),
                data.get(3).getTotalPrice(), 2639.205f, 4, output.get(3));
        assertBuyEntry("акция", data.get(1).getCountry(), data.get(1).getAmount(), data.get(1).getBuyDate(),
                data.get(1).getTotalPrice(), 2517.81f, 5, output.get(4));
    }

    private void assertBuyEntry(String type, String country, Float amount, LocalDate buyDate,
            Float totalBuyPriceCurrency, Float totalBuyPriceBgn, Integer number, Annex8Part1Entry entry) {
        System.out.println("output: " + entry.toString());
        assertEquals(type, entry.getType());
        assertEquals(country, entry.getCountry());
        assertEquals(amount, entry.getAmount());
        assertEquals(buyDate, entry.getBuyDate());
        assertEquals(totalBuyPriceCurrency, entry.getTotalBuyPriceCurrency());
        assertEquals(totalBuyPriceBgn, entry.getTotalBuyPriceBgn());
        assertEquals(number, entry.getNumber());
    }

}
