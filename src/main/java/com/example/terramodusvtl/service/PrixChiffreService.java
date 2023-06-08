package com.example.terramodusvtl.service;
import java.text.NumberFormat;
import java.util.Locale;

public class PrixChiffreService {

    public static String convertToCapitalFrenchLetters(long number) {
        // Create a Locale for French
        Locale frenchLocale = new Locale("fr", "FR");

        // Create a NumberFormat instance with French Locale
        NumberFormat formatter = NumberFormat.getInstance(frenchLocale);

        // Set the formatting style to spell out the number
        formatter.setParseIntegerOnly(true);
        formatter.setMaximumFractionDigits(0);

        // Format the number to get the spelled out representation
        String spelledOut = formatter.format(number);

        // Convert the spelled out representation to capital letters
        String capitalLetters = spelledOut.toUpperCase(frenchLocale);

        return capitalLetters;
    }
}
