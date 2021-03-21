package com.github.sergiosagu.taxesminion.exchange;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

/**
 * Exchange resolver implementation for the BNB site (https://www.bnb.bg/).
 */
@Component
@Qualifier("bnb")
public class BnbExchangeResolver implements ExchangeResolver {

    private static final Logger LOG = LoggerFactory.getLogger(BnbExchangeResolver.class);

    private static final String EXCHANGE_URL = "https://www.bnb.bg/Statistics/StExternalSector/StExchangeRates/StERForeignCurrencies/index.htm?"
            + "downloadOper=true&"
            + "group1=second&"
            + "periodStartDays={periodStartDays}&"
            + "periodStartMonths={periodStartMonths}&"
            + "periodStartYear={periodStartYear}&"
            + "periodEndDays={periodEndDays}&"
            + "periodEndMonths={periodEndMonths}&"
            + "periodEndYear={periodEndYear}&"
            + "valutes={valutes}&"
            + "search=true&"
            + "showChart=false&"
            + "showChartButton=true&"
            + "type=XML";

    private final RestTemplate restTemplate;

    public BnbExchangeResolver(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Override
    public Pair<Float, LocalDate> getExchange(String currency, LocalDate date) {
        LOG.info("Getting 'BGN' exchange rate for 1 '{}' on '{}'...", currency, date);

        LocalDate startDate = date;
        LocalDate endDate = startDate.plusDays(7);

        Map<String, Object> vars = new HashMap<>();
        vars.put("periodStartDays", startDate.getDayOfMonth());
        vars.put("periodStartMonths", startDate.getMonthValue());
        vars.put("periodStartYear", startDate.getYear());
        vars.put("periodEndDays", endDate.getDayOfMonth());
        vars.put("periodEndMonths", endDate.getMonthValue());
        vars.put("periodEndYear", endDate.getYear());
        vars.put("valutes", currency);

        String response = restTemplate.getForObject(EXCHANGE_URL, String.class, vars);

        if (!response.contains("<?xml version=\"1.0\"?>")) {
            LOG.error("Invalid response! Non-XML?");
            throw new RuntimeException("Invalid response! Non-XML?");
        }

        LOG.debug("Response:\n{}", response);

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(response.getBytes(Charset.defaultCharset())));

            XPath xpath = XPathFactory.newInstance().newXPath();

            String row1CurrentDate = (String) xpath.evaluate("(//ROW)[1]/CURR_DATE", doc, XPathConstants.STRING);
            if (!"Date".equals(row1CurrentDate)) {
                LOG.error("Invalid response! Unexpected first row!");
                throw new RuntimeException("Invalid response! Unexpected first row!");
            }

            Double exchangeRate = (Double) xpath.evaluate("(//ROW)[2]/RATE", doc, XPathConstants.NUMBER);

            String dateString = (String) xpath.evaluate("(//ROW)[2]/CURR_DATE", doc, XPathConstants.STRING);
            LocalDate exchangeDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            
            LOG.info("The exchange rate was '{}' on '{}'.", exchangeRate, exchangeDate);
            
            return Pair.of(exchangeRate.floatValue(), exchangeDate);

        } catch (Exception e) {
            LOG.error("Invalid response! Unexpected XML format!", e);
            throw new RuntimeException("Invalid response! Unexpected XML format!", e);
        }
    }

}
