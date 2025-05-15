package ExchangerateService;

import com.fasterxml.jackson.databind.ObjectMapper;
import configUtil.ConfigUtil;
import telegram.CurrencyEnum;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class ExchangeRateService {
    LocalDate currentDate = LocalDate.now();
    LocalDate prevDate = LocalDate.now().minusDays(1);


    public String getExchangeRate(String baseCurrency, String resultCurrency) throws IOException, InterruptedException {
        ExchangeRateRequestBuilder builder = new ExchangeRateRequestBuilder()
                .setStartDate(prevDate)
                .setEndDate(currentDate)
                .setBaseCurrency(baseCurrency)
                .setSymbols(resultCurrency);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(builder.buildUri())
                .header("x-rapidapi-key", ConfigUtil.get("rapidapi.key"))
                .header("x-rapidapi-host", ConfigUtil.get("rapidapi.host"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        ExchangeRateResponseModel model = mapper.readValue(response.body(), ExchangeRateResponseModel.class);

        String date = currentDate.toString();
        String previousDate = prevDate.toString();
        Double usdRate = model.getRates().get(date).get("RUB");
        Double usdRatePrev = model.getRates().get(previousDate).get("RUB");
        CurrencyEnum currency = CurrencyEnum.valueOf(baseCurrency);


        return "Курс " + currency.getLabel() + " : " + usdRate + " " + resultCurrency + "\nВчерашний курс " + currency.getLabel() + " : " + usdRatePrev + " " + resultCurrency;
    }

}
