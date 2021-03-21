package com.github.sergiosagu.taxesminion.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class NapOutputData {
    
    private List<Annex5Part1Table2Entry> sell;
    private List<Annex8Part1Entry> buy;

    @Data
    public static class Annex5Part1Table2Entry {
        private final Integer number;
        private final Integer code;
        private final LocalDate sellDate;
        private final Float totalSellPriceBgn;
        private final Float totalBuyPriceBgn;
        private final Float gain;
        private final Float loss;
        private final String payerId;
        private final String payerName;
        private final String operationId;
    }

    @Data
    public static class Annex8Part1Entry implements Comparable {
        private final String type;
        private final String country;
        private final Float amount;
        private final LocalDate buyDate;
        private final Float totalBuyPriceCurrency;
        private final Float totalBuyPriceBgn;
        private Integer number;
        
        @Override
        public int compareTo(Object o) {
            return buyDate.compareTo(((Annex8Part1Entry) o).buyDate);
        }
    }

}
