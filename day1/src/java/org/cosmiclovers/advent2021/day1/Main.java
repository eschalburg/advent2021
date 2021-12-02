package org.cosmiclovers.advent2021.day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        int increases = countIncreases("day1/input.txt");
        System.out.format("There were %d individual depth increases.\n", increases);

        increases = countWindowIncreases("day1/input.txt");
        System.out.format("There were %d window based increases.\n", increases);
    }

    /**
     * Part 1
     *
     * @param filename
     * @return
     */
    private static int countIncreases(String filename) {
        int lastDepth =-1;
        int increases = 0;

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

            while ((line = br.readLine()) != null) {
                int curDepth = Integer.parseInt(line);

                System.out.println("Last depth: " + lastDepth + ", current depth: " + curDepth);
                if (curDepth > lastDepth) {
                    System.out.println(curDepth + " > " + lastDepth);
                    increases++;
                } else {
                    System.out.println(curDepth + " <= " + lastDepth);
                }

                lastDepth = curDepth;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return increases - 1;
    }

    /**
     * Part 2
     */
    private static int countWindowIncreases(String filename) {
        int lastDepth =-1;
        int increases = 0;
        int[] window = new int[3];
        int sum1, sum2 = -1;

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

            //Initialize the first windows
            for (int i = 0; i< 3; i++) {
                line = br.readLine();
                int curDepth = Integer.parseInt(line);
                window[i] = curDepth;
            }

            line = br.readLine();
            int curDepth = Integer.parseInt(line);
            sum1 = window[0] + window[1] + window[2];
            sum2 = window[1] + window[2] + curDepth;


            System.out.format("Window 1 %d, %d, %d, Window 2 %d, %d, %d\n", window[0], window[1], window[2],
                    window[1], window[2], curDepth);

            if (sum2 > sum1) {
                System.out.format("%d > %d\n", sum2, sum1);
                increases++;
            }

            while ((line = br.readLine()) != null) {
                //Move the window
                window[0] = window[1];
                window[1] = window[2];
                window[2] = lastDepth;
                curDepth = Integer.parseInt(line);

                sum1 = window[0] + window[1] + window[2];
                sum2 = window[1] + window[2] + curDepth;

                if (sum2 > sum1) {
                    System.out.format("%d > %d\n", sum2, sum1);
                    increases++;
                }
                lastDepth = curDepth;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return increases;
    }
}