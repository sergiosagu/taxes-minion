package com.github.sergiosagu.taxesminion.document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.sergiosagu.taxesminion.exchange.ExchangeResolver;
import com.github.sergiosagu.taxesminion.model.InputData;
import com.github.sergiosagu.taxesminion.model.NapOutputData;
import com.github.sergiosagu.taxesminion.model.InputData.BuyEntry;
import com.github.sergiosagu.taxesminion.model.InputData.SellEntry;
import com.github.sergiosagu.taxesminion.model.NapOutputData.Annex5Part1Table2Entry;
import com.github.sergiosagu.taxesminion.model.NapOutputData.Annex8Part1Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class NapDeclarationBuilder {

    @Autowired
    @Qualifier("bnb")
    private ExchangeResolver exchangeResolver;

    public NapOutputData processData(InputData input) {
        NapOutputData output = new NapOutputData();
        output.setBuy(processBuyData(input.getBuy()));
        output.setSell(processSellData(input.getSell()));
        return output;
    }

    public List<Annex8Part1Entry> processBuyData(List<BuyEntry> input) {
        if (CollectionUtils.isEmpty(input)) {
            return Collections.emptyList();
        }

        List<Annex8Part1Entry> output = input.parallelStream().map(entry -> processBuyEntry(entry)).sorted()
                .collect(Collectors.toList());

        IntStream.rangeClosed(1, output.size()).forEachOrdered(i -> output.get(i - 1).setNumber(i));

        return output;
    }

    public Annex8Part1Entry processBuyEntry(BuyEntry entry) {
        String country = entry.getCountry();
        Float amount = entry.getAmount();
        Float totalBuyPriceCurrency = entry.getTotalPrice();
        String currency = entry.getCurrency();
        LocalDate buyDate = entry.getBuyDate();
        Pair<Float, LocalDate> exchange = exchangeResolver.getExchange(currency, buyDate);

        Float totalBuyPriceBgn = BigDecimal.valueOf(totalBuyPriceCurrency)
                .multiply(BigDecimal.valueOf(exchange.getFirst())).floatValue();

        return new Annex8Part1Entry("акция", country, amount, buyDate, totalBuyPriceCurrency, totalBuyPriceBgn);
    }

    public List<Annex5Part1Table2Entry> processSellData(List<SellEntry> input) {
        return null;
    }

}
