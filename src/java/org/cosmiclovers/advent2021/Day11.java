package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class Day11 implements Assignment {
    int[][] map = new int[10][10];
    int flashers = 0;

    @Override
    public void go(String[] args) {
        part1(args[1], Integer.parseInt(args[2]));
        part2(args[1]);
    }

    private void part1(String filename, int turns) {
        fillMap(filename);

        processFlashingTurns(turns);

        System.out.format("After %d rounds there were %d flashes.%n", turns, flashers);
    }

    private void processFlashingTurns(int turns) {
        System.out.format("Running for %d turns.%n", turns);

        //Run for specified number of rounds
        for (int z = 0; z<turns; z++) {
            //Increment energy levels
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    map[i][j]++;
                }
            }

            //Process flashers
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                    processBlinking(x, y);
                }
            }

            //Lower energy for flashers
            for (int i = 0; i < map[0].length; i++) {
                for (int j = 0; j < 10; j++) {
                    if (map[i][j] > 9)
                        map[i][j] = 0;
                }
            }

            System.out.format("Map after round %d:%n", z);
            for (int i = 0; i<10; i++)
                System.out.format("%s%n", Arrays.toString(map[i]));

        }
    }

    private void part2(String filename) {
        fillMap(filename);
    }

    /**
     * Helper methods
     */
    private void fillMap(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int j = 0;
            while ((line = br.readLine()) != null) {
                String[] levels = line.split("");

                for (int i = 0; i<10; i++) {
                    int level = Integer.parseInt(levels[i]);
                    map[j][i] = level;
                }
                j++;
            }

            System.out.format("Map:%n");
            for (int i = 0; i<10; i++)
                System.out.format("%s%n", Arrays.toString(map[i]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processBlinking(int x, int y) {
        if (map[x][y] == 10) {
            System.out.format("Processing blink for (%d,%d)  with value %d%n", x, y, map[x][y]);
            flashers++;
            if (x == 0 && y == 0) {
                //Top left corner
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x, y+1);
                map[x + 1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1, y);
                map[x + 1][y + 1]++;
                if (map[x+1][y+1] == 10)
                    processBlinking(x+1, y+1);
            } else if (x == 9 && y == 9) {
                //Bottom right corner
                map[x - 1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1, y);
                map[x][y - 1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x - 1][y - 1]++;
                if (map[x-1][y-1] == 10)
                    processBlinking(x-1,y-1);
            } else if (x == 0 && y == 9) {
                //Bottom left corner
                map[x + 1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1,y);
                map[x][y - 1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x + 1][y - 1]++;
                if (map[x+1][y-1] == 10)
                    processBlinking(x+1,y-1);
            } else if (x == 9 && y == 0) {
                //Top right corner
                map[x - 1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1,y);
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x,y+1);
                map[x - 1][y + 1]++;
                if (map[x-1][y+1] == 10)
                    processBlinking(x-1,y+1);
            } else if (x ==0) {
                //Left side
                map[x][y - 1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x, y+1);
                map[x + 1][y - 1]++;
                if (map[x+1][y-1] == 10)
                    processBlinking(x+1,y-1);
                map[x + 1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1,y);
                map[x + 1][y + 1]++;
                if (map[x+1][y+1] == 10)
                    processBlinking(x+1,y+1);
            } else if (x == 9) {
                //Right side
                map[x - 1][y - 1]++;
                if (map[x-1][y-1] == 10)
                    processBlinking(x-1,y-1);
                map[x - 1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1,y);
                map[x - 1][y + 1]++;
                if (map[x-1][y+1] == 10)
                    processBlinking(x-1, y+1);
                map[x][y - 1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x, y+1);
            } else if (y == 0) {
                //Top row
                map[x - 1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1,y);
                map[x + 1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1,y);
                map[x - 1][y + 1]++;
                if (map[x-1][y+1] == 10)
                    processBlinking(x-1,y+1);
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x, y+1);
                map[x + 1][y + 1]++;
                if (map[x+1][y+1] == 10)
                    processBlinking(x+1, y+1);
            } else if (y == 9) {
                //Bottom row
                map[x-1][y-1]++;
                if (map[x-1][y-1] == 10)
                    processBlinking(x-1,y-1);
                map[x][y-1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x+1][y-1]++;
                if (map[x+1][y-1] == 10)
                    processBlinking(x+1, y-1);
                map[x-1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1, y);
                map[x+1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1,y);
            } else {
                //Everyone else
                map[x - 1][y]++;
                if (map[x-1][y] == 10)
                    processBlinking(x-1,y);
                map[x - 1][y - 1]++;
                if (map[x-1][y-1] == 10)
                    processBlinking(x-1,y-1);
                map[x - 1][y + 1]++;
                if (map[x-1][y+1] == 10)
                    processBlinking(x-1,y+1);
                map[x][y - 1]++;
                if (map[x][y-1] == 10)
                    processBlinking(x, y-1);
                map[x][y + 1]++;
                if (map[x][y+1] == 10)
                    processBlinking(x, y+1);
                map[x + 1][y - 1]++;
                if (map[x+1][y-1] == 10)
                    processBlinking(x+1, y-1);
                map[x + 1][y]++;
                if (map[x+1][y] == 10)
                    processBlinking(x+1,y);
                map[x + 1][y + 1]++;
                if (map[x+1][y+1] == 10)
                    processBlinking(x+1,y+1);
            }
        }
    }
}
