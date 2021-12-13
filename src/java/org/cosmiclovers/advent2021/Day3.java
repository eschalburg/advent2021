package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Day3 implements Assignment {
    @Override
    public void go(String[] args) {
        int power = calculatePower(args[0]);
        int rating = findRating(args[0]);

        System.out.format("Overall power is %d.\n", power);
        System.out.format("Safety rating is %d.\n", rating);
    }

    private int calculatePower(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

            //Initialize epsilon and gamma to correct number of bits
            line = br.readLine();
            Boolean[] epsilon = new Boolean[line.length()];
            Boolean[] gamma = new Boolean[line.length()];
            Arrays.fill(gamma, false);
            Arrays.fill(epsilon, false);

            br.mark(0);
            br.reset();

            int[] oneCounts = new int[line.length()];
            int[] zeroCounts = new int[line.length()];

            while ((line = br.readLine()) != null) {
                char[] chars = line.toCharArray();

                for (int i = 0; i < line.length(); i++) {
                    if (chars[i] == '1') {
                        oneCounts[i]++;
                    } else {
                        zeroCounts[i]++;
                    }
                }
            }

            for (int i = 0; i < oneCounts.length; i++) {
                // Bits need to be set in reverse order
                if (oneCounts[i] > zeroCounts[i]) {
                    gamma[gamma.length - 1 - i] = true;
                } else {
                    epsilon[epsilon.length - 1 - i] = true;
                }
            }

            //Convert to ints
            int epsilonInt = 0;
            int gammaInt = 0;

            for (int i = gamma.length - 1; i >= 0; i--) {
                if (epsilon[i]) {
                    epsilonInt += Math.pow(2, i);
                }

                if (gamma[i]) {
                    gammaInt += Math.pow(2, i);
                }
            }
            System.out.format("Converted epsilon: %d and gamma: %d.\n", epsilonInt, gammaInt);

            return epsilonInt * gammaInt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int findRating(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

            //Get number of bits per line
            line = br.readLine();
            int bits = line.length();
            br.mark(0);
            br.reset();

            // Cache the input entirely
            List<char[]> lines = new ArrayList<char[]>();

            int[] oneCounts = new int[line.length()];
            int[] zeroCounts = new int[line.length()];

            while ((line = br.readLine()) != null) {
                char[] chars = line.toCharArray();
                lines.add(chars);

                //While we're caching, do the counts
                for (int i = 0; i < line.length(); i++) {
                    if (chars[i] == '1') {
                        oneCounts[i]++;
                    } else {
                        zeroCounts[i]++;
                    }
                }
            }

            //Sensors
            char[] oxyLine = getOxygenRating(bits, lines, oneCounts, zeroCounts);
            char[] co2Line = getCO2Rating(bits, lines, oneCounts, zeroCounts);
            System.out.format("Oxygen line %s, CO2 line %s\n", Arrays.toString(oxyLine), Arrays.toString(co2Line));

            int oxyRating = 0;
            int co2Rating = 0;

            for (int i = 0; i < bits; i ++) {
                if (oxyLine[i]  == '1') {
                    oxyRating += Math.pow(2, (bits-1-i));
                }

                if (co2Line[i] == '1') {
                    co2Rating += Math.pow(2, bits-1-i);
                }
            }
            System.out.format("Converted oxygen rating: %d and CO2 rating: %d.\n", oxyRating, co2Rating);

            return oxyRating * co2Rating;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private char[] getOxygenRating(int bits, List<char[]> lines, int[] oneCounts, int[] zeroCounts) {

        for (int i=0;i<bits;i++) {
            //Re-initialize counters
            Arrays.fill(oneCounts, 0);
            Arrays.fill(zeroCounts,  0);

            System.out.format("Processing %d lines.\n", lines.size());

            for (int j=0; j< lines.size(); j++) {
                char[] line = lines.get(j);

                for (int k = 0; k < line.length; k++) {
                    if (line[k] == '1') {
                        oneCounts[k]++;
                    } else {
                        zeroCounts[k]++;
                    }
                }
            }

            List<char[]> oxygenLines = new ArrayList<char[]>();

            Iterator linesIter = lines.iterator();
            while (linesIter.hasNext()) {
                char[] testLine = (char[]) linesIter.next();

                System.out.format("Comparing %d to %d where 1s or equal win.\n", oneCounts[i], zeroCounts[i]);
                if (oneCounts[i] >= zeroCounts[i]) {
                    //More 1s than zeros or equal so keep those starting with 1
                    if (testLine[i] == '1')
                        oxygenLines.add(testLine);
                    //else co2Lines.add(testLine);
                } else if (zeroCounts[i] > oneCounts[i]) {
                    //More zeros than 1s so keep those starting with 0
                    if (testLine[i] == '0')
                        oxygenLines.add(testLine);
                    //else oxygenLines.add(testLine);
                }
            }

            if (oxygenLines.size() == 1) return (char[]) oxygenLines.get(0);
            else {
                System.out.format("%d lines remaining for oxygen.\n", oxygenLines.size());
                lines = oxygenLines;
            }
        }

        return null;
    }

    private char[] getCO2Rating(int bits, List<char[]> lines, int[] oneCounts, int[] zeroCounts) {

        for (int i=0;i<bits;i++) {
            //Re-initialize counters
            Arrays.fill(oneCounts, 0);
            Arrays.fill(zeroCounts,  0);

            System.out.format("Processing %d lines.\n", lines.size());

            for (int j=0; j< lines.size(); j++) {
                char[] line = lines.get(j);

                for (int k = 0; k < line.length; k++) {
                    if (line[k] == '1') {
                        oneCounts[k]++;
                    } else {
                        zeroCounts[k]++;
                    }
                }
            }

            List<char[]> co2Lines = new ArrayList<char[]>();

            Iterator linesIter = lines.iterator();
            while (linesIter.hasNext()) {
                char[] testLine = (char[]) linesIter.next();

                if (oneCounts[i] > zeroCounts[i]) {
                    //More 1s than zeros or equal so keep those starting with 0
                    if (testLine[i] == '0')
                        co2Lines.add(testLine);
                } else if (zeroCounts[i] >= oneCounts[i]) {
                    //More zeros than 1s so keep those starting with 1
                    if (testLine[i] == '1')
                        co2Lines.add(testLine);
                }

                if ((co2Lines.size() == 0) && !linesIter.hasNext()) {
                    return lines.get(lines.size()-1);
                }
            }

            if ((co2Lines.size() == 1) || lines.size() == 1) return (char[]) co2Lines.get(0);
            else {
                System.out.format("%d lines remaining for CO2.\n", co2Lines.size());
                lines = co2Lines;
            }
        }

        return null;
    }
}
