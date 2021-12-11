package org.cosmiclovers.advent2021;

import java.util.Calendar;

import static java.util.Calendar.getInstance;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2)
            displayUsage();


        String className = args[0];

        try {
            Assignment a = (Assignment) Class.forName(className).getDeclaredConstructor().newInstance();
            System.out.format("%s instantiated, executing.%n", className);
            a.go(args);
        } catch (Exception e) {
            System.out.format("Could not load class %s, exiting.%n", className);
            e.printStackTrace();
        }
    }

    private static void displayUsage() {
        System.out.format("Usage org.cosmiclovers.advent2021 <class name> <input file>%n");
        System.out.format(" e.g., org.cosmiclovers.advent2021 org.cosmiclovers.advent2021.Day1 day1/test.txt");
        System.exit(0);
    }
}
