package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Day5 implements Assignment {
    private int[][] map = new int[999][999];

    @Override
    public void go(String[] args) {
        String filename = args[1];

        part1(filename);
        part2(filename);
    }

    private void part1(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] coords = line.split("\\s[-][>]\\s");

                for (int i = 0; i<coords.length;i+=2) {
                    String coord1 = coords[i];
                    String coord2 = coords[i+1];

                    int comma = coord1.indexOf(",");
                    int x1 = Integer.parseInt(coord1.substring(0,comma));
                    int y1 = Integer.parseInt(coord1.substring(comma+1, coord1.length()));
                    comma = coord2.indexOf(",");
                    int x2 = Integer.parseInt(coord2.substring(0,comma));
                    int y2 = Integer.parseInt(coord2.substring(comma+1, coord2.length()));

                    //System.out.format("X1: %d, Y1: %d, X2: %d, Y2: %d%n", x1, y1, x2, y2);

                    //Test for horizontal vs vertical
                    if ((x1 == x2) && (y1 < y2)) {
                        for (int z = y1; z <= y2; z++) {
                            //System.out.format("Increment %d,%d%n", x1, z);
                            map[x1][z]++;
                        }
                    } else if ((x1 == x2) && (y1 > y2)) {
                        for (int z = y2; z <= y1; z++) {
                            //System.out.format("Increment %d,%d%n", x1, z);
                            map[x1][z]++;
                        }
                    } else if ((y1 == y2) && (x1 < x2)) {
                        for (int z = x1; z <= x2; z++) {
                            //System.out.format("Increment %d,%d%n", z, y1);
                            map[z][y1]++;
                        }
                    } else if ((y1 == y2) && (x1 > x2)) {
                        for (int z = x2; z <= x1; z++) {
                            //System.out.format("Increment %d,%d%n", z, y1);
                            map[z][y1]++;
                        }
                    }
                }
            }

            //Debug
            
            /**
            for (int i = 0; i<10; i++) {
                for (int j = 0; j<10; j++) {
                    if (map[i][j] == 0)
                      System.out.format("-");
                    else
                      System.out.format("%d", map[i][j]);
                }
                System.out.format("%n");
            }
            */
            
            
            //Find points >= 2
            int count = 0;
            for (int i = 0; i<999; i++) {
                for (int j = 0; j<999; j++) {
                    if (map[i][j] >= 2)
                        count++;
                }
            }

            System.out.format("%d points overlap%n", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void part2(String filename) {    
        //Re-init map
        map = new int[999][999];

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] coords = line.split("\\s[-][>]\\s");

                for (int i = 0; i<coords.length;i+=2) {
                    String coord1 = coords[i];
                    String coord2 = coords[i+1];

                    int comma = coord1.indexOf(",");
                    int x1 = Integer.parseInt(coord1.substring(0,comma));
                    int y1 = Integer.parseInt(coord1.substring(comma+1, coord1.length()));
                    comma = coord2.indexOf(",");
                    int x2 = Integer.parseInt(coord2.substring(0,comma));
                    int y2 = Integer.parseInt(coord2.substring(comma+1, coord2.length()));

                    //System.out.format("X1: %d, Y1: %d, X2: %d, Y2: %d%n", x1, y1, x2, y2);

                    //Test for horizontal vs vertical
                    if ((x1 == x2) && (y1 < y2)) {
                        for (int z = y1; z <= y2; z++) {
                            //System.out.format("Increment %d,%d%n", x1, z);
                            map[x1][z]++;
                        }
                    } else if ((x1 == x2) && (y1 > y2)) {
                        for (int z = y2; z <= y1; z++) {
                            //System.out.format("Increment %d,%d%n", x1, z);
                            map[x1][z]++;
                        }
                    } else if ((y1 == y2) && (x1 < x2)) {
                        for (int z = x1; z <= x2; z++) {
                            //System.out.format("Increment %d,%d%n", z, y1);
                            map[z][y1]++;
                        }
                    } else if ((y1 == y2) && (x1 > x2)) {
                        for (int z = x2; z <= x1; z++) {
                            //System.out.format("Increment %d,%d%n", z, y1);
                            map[z][y1]++;
                        }
                    //Now account for diagonals as well
                    } else if ((x1 > x2) && (y1 > y2)) {
                        //Move from lower left to upper right
                        int q = y2;
                        for (int z = x2; z <= x1; z++) {
                            //System.out.format("Increment %d,%d%n", z, q);
                            map[z][q]++;
                            q++;
                        }
                    } else if ((x1 > x2) && (y1 < y2)) {
                        //Move from lower right to upper left
                        int q = y2;
                        for (int z = x2; z<= x1; z++) {                            
                            //System.out.format("Increment %d,%d%n", z, q);
                            map[z][q]++;
                            q--;
                        }
                    } else if ((x1 < x2) && (y1 > y2)) {
                        //Move from upper left to lower right
                        int q = y1;
                        for (int z = x1; z <= x2; z++) {
                            //System.out.format("Increment %d,%d%n", z, q);
                            map[z][q]++;
                            q--;
                        }
                    } else if ((x1 < x2) && (y1 < y2)) {
                        //Move from lower left to upper right
                        int q = y1;
                        for (int z = x1; z <= x2; z++) {
                            //System.out.format("Increment %d,%d%n", z, q);
                            map[z][q]++;
                            q++;
                        }
                    }
                }
            }

            //Debug
            /**
            for (int i = 0; i<10; i++) {
                for (int j = 0; j<10; j++) {
                    if (map[i][j] == 0)
                      System.out.format("-");
                    else
                      System.out.format("%d", map[i][j]);
                }
                System.out.format("%n");
            }
            */
            
            //Find points >= 2
            int count = 0;
            for (int i = 0; i<999; i++) {
                for (int j = 0; j<999; j++) {
                    if (map[i][j] >= 2)
                        count++;
                }
            }

            System.out.format("%d points overlap%n", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
