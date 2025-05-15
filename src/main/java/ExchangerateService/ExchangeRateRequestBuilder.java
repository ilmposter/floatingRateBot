package ExchangerateService;

import java.net.URI;
import java.time.LocalDate;

public class ExchangeRateRequestBuilder {

    private LocalDate startDate;
    private LocalDate endDate;
    private String baseCurrency;
    private String symbols;

    public ExchangeRateRequestBuilder setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ExchangeRateRequestBuilder setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ExchangeRateRequestBuilder setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
        return this;
    }

    public ExchangeRateRequestBuilder setSymbols(String symbols) {
        this.symbols = symbols;
        return this;
    }

    public URI buildUri() {
        String url = String.format(
                "https://currency-conversion-and-exchange-rates.p.rapidapi.com/timeseries?start_date=%s&end_date=%s&base=%s&symbols=%s",
                startDate, endDate, baseCurrency, symbols
        );
        return URI.create(url);
    }
}

