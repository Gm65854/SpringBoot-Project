package com.webproject.blog.service.serviceimplementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webproject.blog.client.OpenExchangeRatesClient;
import com.webproject.blog.models.ExchangeRates;
import com.webproject.blog.service.ServiceInterface.ExchangeRatesService;


// Service to work with openexchangerates.org
@Service
public class OpenExchangeRatesServiceImpl implements ExchangeRatesService {

    private ExchangeRates prevRates;
    private ExchangeRates currentRates;

    private OpenExchangeRatesClient openExchangeRatesClient;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public OpenExchangeRatesServiceImpl(
            OpenExchangeRatesClient openExchangeRatesClient,
            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat
    ) {
        this.openExchangeRatesClient = openExchangeRatesClient;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    /**
     * returning list of available currencies 
     *
     * @return
     */

    @Override
    public List<String> getCharCodes() {
        List<String> result = null;
        if (this.currentRates.getRates() != null) {
            result = new ArrayList<>(this.currentRates.getRates().keySet());
        }
        return result;
    }

   /**
     * checks and updates rates,
     * compares and returns its results,
     * if absent then returns 101
     *
     * @param charCode
     * @return
     */

    @Override
    public int getKeyForTag(String charCode) {
        this.refreshRates();
        Double prevCoefficient = this.getCoefficient(this.prevRates, charCode);
        Double currentCoefficient = this.getCoefficient(this.currentRates, charCode);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }


// checking and updating rates

    @Override
    public void refreshRates() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }

/**
* updating rates, checking time up to hour thx to openexchangerates.org
*
* @param time
*/


private void refreshCurrentRates(long time) {
    if (
            this.currentRates == null ||
                    !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                            .equals(timeFormat.format(time))
    ) {
        this.currentRates = openExchangeRatesClient.getLatestRates(this.appId);
    }
}

    /**
     * Update of yesterday's rates.
     * The time is checked up to a day.
     * Also, so that with each request one does not has to contact an external service
     * to update rates - the date of the current prevRates is checked
     * with YYYY-MM-DD cast to the string form of the current date and one day less from the current date,
     * bc when requesting to openexchangerates.org//historical/* indicating
     * yesterday's date may return
     * courses with a date equal to the current one.
     *
     * @param time
     */

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = openExchangeRatesClient.getHistoricalRates(newPrevDate, appId);
        }
    }

    /**
     * Formula to calc coef relative to established one rate
     * (Default_Base / Our_Base) * Target
     * null if non-esistent
     * rounding up to 4 numbers after comma
     *
     * @param rates
     * @param charCode
     */
    
    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map = null;
        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(charCode);
            appBaseRate = map.get(this.base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal(
                    (defaultBaseRate / appBaseRate) * targetRate
            )
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }

}