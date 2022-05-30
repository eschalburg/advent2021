package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day13 implements Assignment {
    List<String> instructions = new ArrayList<String>(); //To hold instructions
    List<Coordinate> coordinates = new ArrayList<Coordinate>();


    @Override
    public void go(String[] args) {

        BufferedReader br = getReader(args[1]);
        String line;

        Origami origami = new Origami();

        try {
            while ((line = br.readLine()) != null) {
                //Cache the input file in memory
                origami.fileContents.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialize paper
        Coordinate maxLengths = findMaxLength(origami);
        origami.paper = new boolean[maxLengths.getY() + 1][maxLengths.getX() + 1];
        System.out.format("X length: %d, Y length: %d%n", maxLengths.getX() + 1, maxLengths.getY() + 1);
        for (boolean[] row : origami.paper) {
            Arrays.fill(row, false);
        }
        markPaper(origami);

        //Debug, print the paper
        //for (boolean[] row : origami.paper)
        //    System.out.format("%s%n", Arrays.toString(row));

        part1(origami);
        //Debug, print the paper
        //for (boolean[] row : origami.paper)
        //    System.out.format("%s%n", Arrays.toString(row));
        part2();
    }

    private void part1(Origami origami) {
        origami.fold(instructions.get(0));

        //Count marks
        int totalMarked = 0;
        for (boolean[] row : origami.paper) {
            for (boolean square : row) {
                if (square) totalMarked++;
            }
        }

        System.out.format("%d total squares marked after fold.%n", totalMarked);
    }

    private void part2() {

    }

    // Helper methods
    private BufferedReader getReader(String filename) {
        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void markPaper(Origami origami) {
        for (Coordinate c : coordinates) {
            origami.mark(c.getY(), c.getX(), true);
        }
    }

    private Coordinate findMaxLength(Origami origami) {
        Coordinate maxes = new Coordinate(-1, -1);

        for (String s : origami.fileContents) {
            //Skip blank lines
            if (s.trim().strip().equals(""))
                continue;

            if (s.contains("fold along")) {
                //This was an instruction line
                instructions.add(s);
            } else {
                String[] indices = s.split(",");
                int xSize = Integer.parseInt(indices[0]);
                int ySize = Integer.parseInt(indices[1]);

                if (xSize > maxes.getX())
                    maxes.setX(xSize);
                if (ySize > maxes.getY())
                    maxes.setY(ySize);

                Coordinate c = new Coordinate(xSize, ySize);
                coordinates.add(c);
            }
        }

        return maxes;
    }

    private class Coordinate {
        private int x = -1;
        private int y = -1;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    class Origami {
        List<String> fileContents = new ArrayList<String>(); //To hold the input in
        boolean[][] paper; //The origami paper

        private void fold(String instruction) {
            String foldInstruction = instruction.substring(11, instruction.length());
            int pivotPoint = Integer.parseInt(foldInstruction.substring(2, foldInstruction.length()));
            boolean[][] newPaper = null;

            if (foldInstruction.contains("x")) {
                //Vertical fold
                System.out.format("Folding along x=%d%n", pivotPoint);
                newPaper = new boolean[paper.length][paper[0].length - pivotPoint];
                cleanPaper(newPaper);


                int diff = paper.length - pivotPoint - 1; // overlap size
                int copyEnd = paper.length - (2 * diff) - 1;
                int mergeCenter = copyEnd + diff;
                System.out.format("Size: %d, Diff: %d, Pivot: %d, copy end: %d, merge center: %d%n", paper.length, diff,
                        pivotPoint, copyEnd, mergeCenter);

                //Fill in piece without overlap
                for (int i = 0; i < newPaper.length; i++) { // y axis
                    for (int j = 0; j < copyEnd; j++) { // x axis
                        newPaper[i][j] = paper[i][j];
                    }
                }

                //Handle overlap
                for (int i = 0; i < newPaper.length; i++) { // y axis
                    //Middle out
                    for (int j = 1; j <= diff; j++) { // x axis
                        System.out.format("Setting %d,%d from %d,%d=%b or %d,%d=%b%n", i, mergeCenter - j,
                                i, mergeCenter - j, paper[i][mergeCenter - j], i, mergeCenter + j,
                                paper[i][mergeCenter+ 1]);
                        newPaper[i][mergeCenter - j] = paper[i][mergeCenter - j] || paper[i][mergeCenter + j];
                    }
                }
            } else {
                //Horizontal fold
                System.out.format("Folding along y=%d%n", pivotPoint);
                newPaper = new boolean[paper.length - pivotPoint - 1][paper[0].length];
                cleanPaper(newPaper);

                int diff = paper.length - pivotPoint - 1; // overlap size
                int copyEnd = paper.length - (2 * diff) - 1;
                int mergeCenter = copyEnd + diff;

                //System.out.format("Size: %d, Diff: %d, Pivot: %d, merge center: %d%n", paper.length, diff,
                //        pivotPoint, mergeCenter);
                //Fill in piece without overlap
                for (int i = 0; i < copyEnd; i++) { // y axis
                    for (int j = 0; j < newPaper[0].length; j++) { //x axis
                        newPaper[i][j] = paper[i][j];
                        //System.out.format("(Copy) Set %d,%d to %b%n", j, i, newPaper[i][j]);
                    }
                }
                //Handle overlap
                for (int i = 1; i <= diff; i++) { //y axis
                    for (int j = 0; j < newPaper[0].length; j++) { // x axis
                        //System.out.format("Y: %d, X: %d%n", i, j);
                        newPaper[mergeCenter - i][j] = paper[mergeCenter - i][j] || paper[mergeCenter + i][j];
                        if (j == 0) {
                            //System.out.format("(Merged) Set %d,%d to %b from %d,%d %b or %d,%d %b%n", j, mergeCenter-i,
                            //        newPaper[mergeCenter - i][j], j, mergeCenter-i, paper[mergeCenter - i][j],
                            //        j, mergeCenter + i, paper[mergeCenter + i][j]);
                            //System.out.format("Pivot-i: %d, Pivot+i: %d, x:%d%n", mergeCenter - i, mergeCenter + i, j);
                        }
                    }
                }
            }

            if (newPaper != null)
                this.paper = newPaper;
            else
                System.out.format("I ruined the paper.%n");
        }

        /**
         * Could me my paper, or a new one
         *
         * @param paper the paper to wipe clean
         */
        public void cleanPaper(boolean[][] paper) {
            for (boolean[] row : paper) {
                Arrays.fill(row, false);
            }
        }

        public void mark(int y, int x, boolean state) {
            paper[y][x] = state;
        }
    }
}
