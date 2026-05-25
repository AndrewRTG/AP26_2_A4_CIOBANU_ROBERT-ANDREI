package com;

import app.LocaleExplore;

import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

public class DisplayLocales implements Command {
    @Override
    public void execute(String[] args) {
        Locale currentLocale = LocaleExplore.getCurrentLocale();
        System.out.println(LocaleExplore.message("locales"));

        Locale[] locales = Locale.getAvailableLocales();

        Arrays.sort(locales, (l1, l2) -> {
            Collator collator = Collator.getInstance(currentLocale);
            return collator.compare(l1.toLanguageTag(), l2.toLanguageTag());
        });

        for (Locale locale : locales) {
            if (!locale.toLanguageTag().equals("und")) {
                String country = locale.getDisplayCountry(currentLocale);
                String language = locale.getDisplayLanguage(currentLocale);
                String tag = locale.toLanguageTag();

                if (!country.isBlank() || !language.isBlank()) {
                    System.out.println(tag + " - " + language + " " + country);
                }
            }
        }
    }
}