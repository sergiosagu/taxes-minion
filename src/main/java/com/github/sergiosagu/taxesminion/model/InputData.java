package com.github.sergiosagu.taxesminion.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class InputData {

    private List<BuyEntry> buy;
    private List<SellEntry> sell;
    
    @Data
    public static class SellEntry {
        private final String operationId;
        private final String currency;
        private final LocalDate sellDate;
        private final Float totalSellPrice;
        private final LocalDate buyDate;
        private final Float totalBuyPrice;
    }

    @Data
    public static class BuyEntry {
        private final String country;
        private final String currency;
        private final Float amount;
        private final LocalDate buyDate;
        private final Float totalPrice;
    }

}
