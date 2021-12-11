package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class Day7 implements Assignment {
    private int[][] map;

    @Override
    public void go(String[] args) {
        String filename = args[1];
        
        part1(filename);
        part2(filename);
    }

    public void part1(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int[] positions;

            //Read in input
            line = br.readLine();
            String[] inputs = line.split(",");
            positions = new int[inputs.length];

            // Set initial positions
            for (int i = 0; i<inputs.length; i++) {
                positions[i] = Integer.parseInt(inputs[i]);
            }

            //Figure out least fuel cost
            int lowestCost = 9999999;
            int position = -1;
            int min = Arrays.stream(positions).min().getAsInt();
            int max = Arrays.stream(positions).max().getAsInt();

            for (int j = min; j <= max; j++) {
              int cost = 0;
              for (int i=0; i<positions.length; i++) {
                if (positions[i] <= j)
                  cost += j-positions[i];
                else
                  cost += positions[i]-j;
              }
              if (cost < lowestCost) {
                lowestCost = cost;
                position = j;
              }
              System.out.format("Current cost: %d, current lowest cost: %d%n", cost, lowestCost);
            }

            System.out.format("Lowest cost postion is %d with cost %d.", position, lowestCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public void part2(String filename) {
      try {
        File f = new File(filename);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int[] positions;

        //Read in input
        line = br.readLine();
        String[] inputs = line.split(",");
        positions = new int[inputs.length];

        // Set initial positions
        for (int i = 0; i<inputs.length; i++) {
            positions[i] = Integer.parseInt(inputs[i]);
        }

        //Figure out least fuel cost
        int lowestCost = Integer.MAX_VALUE;
        int position = -1;
        int min = Arrays.stream(positions).min().getAsInt();
        int max = Arrays.stream(positions).max().getAsInt();

        for (int j = min; j <= max; j++) {
          int cost = 0;
          for (int i=0; i<positions.length; i++) {
            
            //Debug this
            int sigma = 0;
            if (positions[i] <= j) {
              sigma += j-positions[i];
            } else {
              sigma += positions[i]-j;
            }

            //System.out.format("For position %d and location %d, sigma is %d%n", positions[i], j, sigma);
            // Calculate summation
            for (int s = 1; s<=sigma; s++) {
              cost += s;
            }
          }
          if (cost < lowestCost) {
            lowestCost = cost;
            position = j;
          }
          System.out.format("Current cost: %d, current lowest cost: %d%n", cost, lowestCost);
        }

        System.out.format("Lowest cost postion is %d with cost %d%n", position, lowestCost);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
