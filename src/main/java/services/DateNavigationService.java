package services;

import constants.Constants;

import java.time.LocalDate;

public class DateNavigationService {
    private LocalDate currentDate;

    public DateNavigationService() {
        this.currentDate = LocalDate.now();
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate newDate) {
        this.currentDate = newDate;
    }

    public LocalDate previousDay() {
        currentDate = currentDate.minusDays(1);
        return currentDate;
    }

    public LocalDate nextDay() {
        currentDate = currentDate.plusDays(1);
        return currentDate;
    }

    public String getFormattedDate() {
        return currentDate.format(Constants.NAVIGATION_DATE_FORMATTER);
    }
}
