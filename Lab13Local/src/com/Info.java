package com;

import app.LocaleExplore;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public class Info implements Command {
    @Override
    public void execute(String[] args) {
        Locale locale;

        if (args.length >= 2) {
            locale = Locale.forLanguageTag(args[1]);
        } else {
            locale = LocaleExplore.getCurrentLocale();
        }

        Locale displayLocale = LocaleExplore.getCurrentLocale();

        String pattern = LocaleExplore.message("info");
        System.out.println(MessageFormat.format(pattern, locale.toLanguageTag()));

        System.out.println(LocaleExplore.message("country") + ": " + locale.getDisplayCountry(displayLocale));
        System.out.println(LocaleExplore.message("language") + ": " + locale.getDisplayLanguage(displayLocale));

        try {
            Currency currency = Currency.getInstance(locale);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
            System.out.println(LocaleExplore.message("currency") + ": " + currency.getCurrencyCode() + " (" + currency.getDisplayName(displayLocale) + ")");
            System.out.println("Example: " + currencyFormat.format(12345.67));
        } catch (Exception e) {
            System.out.println(LocaleExplore.message("currency") + ": -");
        }

        DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);

        String[] weekdays = Arrays.stream(symbols.getWeekdays())
                .filter(day -> day != null && !day.isBlank())
                .toArray(String[]::new);

        String[] months = Arrays.stream(symbols.getMonths())
                .filter(month -> month != null && !month.isBlank())
                .toArray(String[]::new);

        System.out.println(LocaleExplore.message("week.days") + ": " + String.join(", ", weekdays));
        System.out.println(LocaleExplore.message("months") + ": " + String.join(", ", months));

        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(locale);

        System.out.println(LocaleExplore.message("today") + ": " + LocalDate.now().format(formatter));
    }
}