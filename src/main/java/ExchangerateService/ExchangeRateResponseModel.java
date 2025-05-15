package ExchangerateService;

import java.util.Map;

public class ExchangeRateResponseModel {

    private boolean success;
    private boolean timeseries;
    private String start_date;
    private String end_date;
    private String base;
    private Map<String, Map<String, Double>> rates;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isTimeseries() {
        return timeseries;
    }

    public void setTimeseries(boolean timeseries) {
        this.timeseries = timeseries;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }
}
