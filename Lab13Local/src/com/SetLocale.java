package com;

import app.LocaleExplore;

import java.text.MessageFormat;
import java.util.Locale;

public class SetLocale implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println(LocaleExplore.message("invalid"));
            return;
        }

        Locale locale = Locale.forLanguageTag(args[1]);
        LocaleExplore.setCurrentLocale(locale);

        String pattern = LocaleExplore.message("locale.set");
        System.out.println(MessageFormat.format(pattern, locale.toLanguageTag()));
    }
}