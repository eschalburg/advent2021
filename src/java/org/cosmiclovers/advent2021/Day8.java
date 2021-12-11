package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.util.ElementScanner6;

public class Day8 implements Assignment {
    private static int SEGA = 0;
    private static int SEGB = 1;
    private static int SEGC = 2;
    private static int SEGD = 3;
    private static int SEGE = 4;
    private static int SEGF = 5;
    private static int SEGG = 6;

    @Override
    public void go(String[] args) {
        part1(args[1]);
        part2(args[1]);
    }
    
    public void part1(String filename) {
        boolean[] segments = new boolean[7];

        /** 
         * Deceptively easy, just count unique lengths in the output values.
         * 
         * 1 - 2 digits, 4 - 4 digits, 7 - 3 digits, 8 - 7 digits
         * e.g., ab = 1, abcd = 4, abe = 7, abcdefg = 8
         */

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] ioParts = line.split("\\|");
                System.out.format("Split input: %s%n", Arrays.toString(ioParts));

                //Second part is all we care about - in the test file the second part is on the next line.
                String output = "";
                if (ioParts.length == 2)
                    output = ioParts[1];
                else 
                    output = br.readLine();

                System.out.format("Output string %s%n", output);
                String[] numbers = output.trim().split("\\s");
                System.out.format("Split output: %s%n", Arrays.toString(numbers));
                for (String num : numbers) {
                    if (num.length() == 2 || num.length() == 3 || num.length() == 4 || num.length() == 7)
                        count++;
                }
            }
            
            System.out.format("%d unique digits appeared.%n", count);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Segment map is as follows
     *    a
     *    _ 
     * b|   |c
     *    d
     *    -
     * e|   |f
     *    -
     *    g
    
     */
    public void part2(String filename) {
        Map<String, String> segmentMap = new HashMap<String, String>();
        Map<Integer, String> sequenceMap = new HashMap<Integer, String>();

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            List<String> fiveDigits = new ArrayList<String>();
            List<String> sixDigits = new ArrayList<String>();

            while ((line = br.readLine()) != null) {
                String[] ioParts = line.split("\\|");

                String output;
                if (ioParts.length == 2)
                    output = ioParts[1];
                else
                    output = br.readLine();

                String[] numbers = output.trim().split("\\s");
                
                for (String num : numbers) {
                    if (num.length() == 2)
                        sequenceMap.put(1, num);
                    else if (num.length() == 4)
                        sequenceMap.put(4, num);
                    else if (num.length() == 3)
                        sequenceMap.put(7, num);
                    else if (num.length() == 7)
                        sequenceMap.put(8, num);
                    else if (num.length() == 5)
                        fiveDigits.add(num);
                    else if (num.length() == 6)
                        sixDigits.add(num);
                }
            }

            //Now we have the numbers sorted.  Do some deduction.
            // Start with the two 6 digit numbers
            String oneSequence = sequenceMap.get(1);
            Character segment1 = oneSequence.charAt(0);
            Character segment2 = oneSequence.charAt(1);
            if (sixDigits.get(0).contains(segment1.toString()) && sixDigits.get(1).contains(segment1.toString())) {
                if (sixDigits.get(0).contains(segment2.toString())) {
                    //This is 0, now we know both.
                    sequenceMap.put(0, sixDigits.get(0));
                    sequenceMap.put(6, sixDigits.get(1));
                    segmentMap.put("c", segment1.toString());
                    segmentMap.put("f", segment2.toString());
                } else {
                    //This is 6, now we know both.
                    sequenceMap.put(6, sixDigits.get(0));
                    sequenceMap.put(0, sixDigits.get(1));
                    segmentMap.put("c", segment1.toString());
                    segmentMap.put("f", segment2.toString());
                }
            }

            //Knowing those two, we can now figure out g from 3
            for (String s : fiveDigits) {
                if (s.contains(segmentMap.get("c")) && s.contains(segmentMap.get("f"))) {
                    //This is either 3 or 9, use 4 to figure out d
                    for (char c : sequenceMap.get(4).toCharArray()) {
                        //if (s.contains(((Character)c).toString()) && ) {
                        //    
                        //}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
