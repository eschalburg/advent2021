package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 implements Assignment {
    private List<Fish> school;
    private BigInteger[] ageCounts;

    public Day6() {
        this.school = new ArrayList<Fish>();
    }

    public void go(String[] args) {
        this.ageCounts = new BigInteger[9];
        Arrays.fill(this.ageCounts, new BigInteger("0"));

        String filename = args[1];
        Integer days = Integer.parseInt(args[2]);
        initSchool(filename);

        gameOfLife(days);

        BigInteger fishCount = new BigInteger("0");
        for (int i = 0; i<ageCounts.length;i++)
            fishCount = fishCount.add(ageCounts[i]);

        System.out.format("%d fish in the school.%n", fishCount);
    }

    private void initSchool(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                System.out.format("Input: %s%n", line);
                String[] fishCycles = line.split(",");

                for (String cycle : fishCycles) {
                    System.out.format("Cycle: %s%n", cycle);
                    //Fish fish = new Fish(Integer.parseInt(cycle));
                    //school.add(fish);
                    int age = Integer.parseInt(cycle);
                    this.ageCounts[age] = this.ageCounts[age].add(new BigInteger("1"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gameOfLife(int days) {
        for (int i = 0; i<days; i++) {

            // The clever OOP approach was not so clever
            
            /** 
            List<Fish> kids = new ArrayList<Fish>();

            for (Fish f : school) {
                f.passTime();
                if (f.getCycleDays() == -1) {
                    f.setCycleDays(6);
                    Fish baby = f.spawn();
                    kids.add(baby);
                }
            }
            
            school.addAll(kids);
            */

            /**
             * Debug
            System.out.format("At end of day (i+1): ");
            for (Fish f : school) {
                System.out.format("%d,", f.getCycleDays());
            }
            System.out.format("%n");
             */
        
            BigInteger babies = new BigInteger("0");
            for (int j = 0; j<ageCounts.length; j++) {
                if (j == 0) {
                    //We will use this to add new 8s and 6s
                    babies = babies.add(ageCounts[j]);
                } else {
                    ageCounts[j-1] = ageCounts[j];
                }
            }
            ageCounts[8] = babies;
            ageCounts[6] = ageCounts[6].add(babies);

            BigInteger fishCount = new BigInteger("0");
            for (int k = 0; k<ageCounts.length;k++)
                fishCount = ageCounts[k].add(new BigInteger("1"));

            System.out.format("On day %d there are %d fish.%n", i, fishCount);
        }
    }

    private class Fish {
        private int cycleDays;

        public Fish() {
            //Default to 9 day cycle
            this.cycleDays = 8;
        }

        public Fish(int cycleDays) {
            this.cycleDays = cycleDays;
        }

        public void passTime() {
            cycleDays--;
        }

        public int getCycleDays() {
            return this.cycleDays;
        }

        public void setCycleDays(int cycleDays) {
            this.cycleDays = cycleDays;
        }

        public Fish spawn() {
            return new Fish();
        }
    }
}