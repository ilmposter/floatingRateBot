import ExchangerateService.ExchangeRateService;
import GraphService.TradingViewScreenshotService;
import telegram.CurrencyEnum;

import java.io.IOException;
import java.util.Objects;

public class ExchangerateServiceTest {
    ExchangeRateService exchangerateService = new ExchangeRateService();

    public static void main(String[] args) throws IOException, InterruptedException {
        ExchangeRateService exchangerateService = new ExchangeRateService();
        TradingViewScreenshotService tradingViewScreenshotService = new TradingViewScreenshotService();
        tradingViewScreenshotService.captureChart("USD");

//        CurrencyEnum.fromCommand("USD");

//        System.out.println(exchangerateService.getExchangeRate("RUB", "USD"));
    }
}
