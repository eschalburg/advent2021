package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;

public class Day9 implements Assignment {
    @Override
    public void go(String[] args) {
        part1(args[1]);
        part2(args[1]);
    }

    private void part1(String filename) {
        List<Integer[]> map = new ArrayList<Integer[]>();

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int y = 0;
            int maxy = 0;
            while ((line = br.readLine()) != null) {
                String[] digits = line.split("");
                System.out.format("Current split line %s%n", Arrays.toString(digits));

                Integer[] row = new Integer[digits.length];
                for (int x = 0; x<digits.length; x++) {
                    row[x] = Integer.parseInt(digits[x]);
                }
                map.add(row);
                maxy=y;
                y++;
            }

            Map<Coordinate, Integer> lowRiskLevels = getRiskLevels(map, maxy);

            int sum = 0;
            Iterator it = lowRiskLevels.keySet().iterator();
            while (it.hasNext()) {
                Coordinate key = (Coordinate)it.next();
                int s = lowRiskLevels.get(key);
                sum += s;
            }
            System.out.format("Sum of low risk levels is %d%n", sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void part2(String filename) {
        List<Integer[]> map = new ArrayList<Integer[]>();

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int y = 0;
            int maxy = 0;
            while ((line = br.readLine()) != null) {
                String[] digits = line.split("");
                System.out.format("Current split line %s%n", Arrays.toString(digits));

                Integer[] row = new Integer[digits.length];
                for (int x = 0; x<digits.length; x++) {
                    row[x] = Integer.parseInt(digits[x]);
                }
                map.add(row);
                maxy=y;
                y++;
            }

            Map<Coordinate, Integer> lowRiskLevels = getRiskLevels(map, maxy);

            //Now find the basins around each low point
            List<Basin> basins = new ArrayList<Basin>();
            Set<Coordinate> keys = lowRiskLevels.keySet();
            Iterator it = keys.iterator();

            while (it.hasNext()) {
                Coordinate point = (Coordinate) it.next();
                Basin basin = new Basin(point);
                
                int x = point.getX();
                y = point.getY();

                //Go right and down first
                for (int i = x; i<map.get(0).length-1;i++) {
                    for (int j = y; j<maxy; j++) {
                        if (map.get(j)[i] == 9)
                            break;
                        else {
                            Coordinate coord = new Coordinate(i, j);
                            System.out.format("Adding coordinate %d,%d to basin for %d,%d%n", i, j, x, y);
                            basin.addCoordinate(coord);
                        }
                    }
                }
                //Now left and up
                if (x != 0) {
                    if (y != 0) {
                        for (int i = x; i>=0; i--) {
                            for (int j=y; j>0; j--) {
                                if (map.get(j)[i] == 9)
                                    break;
                                else {
                                    Coordinate coord = new Coordinate(i, j);
                                    System.out.format("Adding coordinate %d,%d to basin for %d,%d%n", i, j, x, y);
                                    basin.addCoordinate(coord);
                                }
                            }
                        }
                    } else {
                        //Check left but only in the first row
                        for (int i = x; i>=0; i--) {
                            if (map.get(0)[i] == 9)
                                break;
                            else {
                                Coordinate coord = new Coordinate(i, 0);
                                System.out.format("Adding coordinate %d,%d to basin for %d,%d%n", i, 0, x, y);
                                basin.addCoordinate(coord);
                            }
                        }
                    }
                } else {
                    //Check up but only in the first column
                    for (int j=y; j >= 0; j--) {
                        if (map.get(j)[0] == 9)
                            break;
                        else {
                            Coordinate coord = new Coordinate(0, j);
                            System.out.format("Adding coordinate %d,%d to basin for %d,%d%n", 0, j, x, y);
                            basin.addCoordinate(coord);
                        }
                    }
                }
                basins.add(basin);
            }

            //Output calculation
            int[] biggestBasins = new int[3];
            Arrays.fill(biggestBasins, 0);
            for (Basin basin : basins) {
                int size = basin.getCoordinates().size();
                if (size > biggestBasins[0]) 
                    biggestBasins[0] = size;
                else if (size > biggestBasins[1])
                    biggestBasins[1] = size;
                else if (size > biggestBasins[2])
                    biggestBasins[2] = size;
            }
            int product = biggestBasins[0]*biggestBasins[1]*biggestBasins[2];
            System.out.format("Product of biggest basins is %d%n", product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Coordinate, Integer> getRiskLevels(List<Integer[]> map, int maxy) {
        int y;
        //List<Integer> lowRiskLevels = new ArrayList<Integer>();
        Map<Coordinate, Integer> lowPointMap = new HashMap<Coordinate, Integer>();

        for (int x = 0; x<map.get(0).length;x++) {
            for (y = 0; y<=maxy;y++) {
                //System.out.format("Checking X: %d, Y: %d with value %d%n", x, y, map.get(y)[x]);
                if (x == 0  && y == 0) {
                    //Only consider the digits to the right and below
                    if (map.get(y)[x] < map.get(y)[x+1] && map.get(y)[x] < map.get(y+1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (x == 0 && y == maxy) {
                    //Only consider the digits to the right and above
                    if (map.get(y)[x] < map.get(y)[x+1] && map.get(y)[x] < map.get(y-1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (x == map.get(0).length-1 && y == 0) {
                    //Only consider the digits to the left and down
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y+1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (x == map.get(0).length-1 && y == maxy) {
                    //Only consider the digits to the left and up
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y-1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (x == 0) {
                    //Leftmost row inner
                    if (map.get(y)[x] < map.get(y)[x+1] && map.get(y)[x] < map.get(y+1)[x] && map.get(y)[x] < map.get(y-1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (x == map.get(0).length - 1) {
                    //Rightmost row inner 
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y+1)[x] && map.get(y)[x] < map.get(y-1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (y == 0) {
                    //Topmost row inner
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y)[x+1] && map.get(y)[x] < map.get(y+1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else if (y == maxy) {
                    //Bottommost row inner
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y)[x+1] && map.get(y)[x] < map.get(y-1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                } else {
                    //Everybody else
                    if (map.get(y)[x] < map.get(y)[x-1] && map.get(y)[x] < map.get(y)[x+1] && 
                        map.get(y)[x] < map.get(y-1)[x] && map.get(y)[x] < map.get(y+1)[x]) {
                        System.out.format("Adding %d+1 to low risk levels.%n", map.get(y)[x]);
                        //lowRiskLevels.add(map.get(y)[x]+1);
                        lowPointMap.put(new Coordinate(x, y), map.get(y)[x]+1);
                    }
                }
            }
        }
        return lowPointMap;
    }

    /**
     * Helper classes
     */
    private class Basin {
        private Coordinate lowPoint;
        private List<Coordinate> coordinates;

        public Basin(Coordinate origin) {
            this.lowPoint = origin;
            this.coordinates = new ArrayList<Coordinate>();
            //Obviously the origin point is part of the basin
            //this.coordinates.add(origin);
        }

        public List<Coordinate> getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        public void addCoordinate(Coordinate coord) {
            this.coordinates.add(coord);
        }
    }

    private class Coordinate {
        int x;
        int y;

        public Coordinate( int x, int y ) {
            this.x = x;
            this.y = y;
        }

        protected Coordinate() {
            this.x = 0;
            this.y = 0;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
