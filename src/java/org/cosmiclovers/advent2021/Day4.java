package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Day4 implements Assignment {
    List<Transporter> winners = new ArrayList<Transporter>();
    int lastWinningBall = -1;

    @Override
    public void go(String[] args) {
        Transporter lastWinner = playBingo(args[0]);
        System.out.format("Winning board prize is %s\n", winners.get(0).getPrize());


        Transporter lastWin = winners.get(winners.size()-1);
        System.out.format("Last winning board:\n");
        System.out.format("%s\n", Arrays.toString(lastWin.getInts()[0]));
        System.out.format("%s\n", Arrays.toString(lastWin.getInts()[1]));
        System.out.format("%s\n", Arrays.toString(lastWin.getInts()[2]));
        System.out.format("%s\n", Arrays.toString(lastWin.getInts()[3]));
        System.out.format("%s\n", Arrays.toString(lastWin.getInts()[4]));
        System.out.format("Last winning board stamps:\n");
        System.out.format("%s\n", Arrays.toString(lastWin.getBools()[0]));
        System.out.format("%s\n", Arrays.toString(lastWin.getBools()[1]));
        System.out.format("%s\n", Arrays.toString(lastWin.getBools()[2]));
        System.out.format("%s\n", Arrays.toString(lastWin.getBools()[3]));
        System.out.format("%s\n", Arrays.toString(lastWin.getBools()[4]));
        System.out.format("Last winner would win %d.\n", winners.get(winners.size()-1).getPrize());
    }

    private Transporter playBingo(String filename) {
        List<Integer> balls = new ArrayList<Integer>();

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

            //Process pending ball drawing
            line = br.readLine();
            System.out.format("Balls: %s\n", line);
            String[] bingoMachine = line.split(",");
            List<Integer> bingoInts = new ArrayList<Integer>();
            for (String s : bingoMachine) {
                bingoInts.add(Integer.parseInt(s));
            }
            balls.addAll(bingoInts);

            //Set up all the boards
            List<Integer[][]> boards = new ArrayList<Integer[][]>();

            // Parallel boards to capture "stamps"
            List<Boolean[][]>  filledBoards = new ArrayList<Boolean[][]>();

            //Read in all the boards
            while (((line = br.readLine()) != null)) {
                line = line.strip();

                if (!line.isEmpty()) {
                    // Read in the whole board
                    String line2 = br.readLine();
                    String line3 = br.readLine();
                    String line4 = br.readLine();
                    String line5 = br.readLine();

                    List<String> lines = new ArrayList<String>();
                    lines.add(line);
                    lines.add(line2);
                    lines.add(line3);
                    lines.add(line4);
                    lines.add(line5);

                    Integer[][] board = new Integer[5][5];

                    int rowNum = 0;
                    for (String l : lines) {
                        l = l.strip();
                        String[] row = l.split("\\s+");
                        for (int colNum = 0; colNum < row.length; colNum++) {
                            board[rowNum][colNum] = Integer.parseInt(row[colNum]);
                        }
                        System.out.format("Adding board row %s.\n", Arrays.toString(board[rowNum]));
                        rowNum++;
                    }
                    boards.add(board);

                    //Initialize the parallel board  with no stamps
                    Boolean[][] filledBoard = new Boolean[5][5];
                    for (int i = 0; i < filledBoard.length; i++) {
                        Arrays.fill(filledBoard[i], false);
                    }
                    filledBoards.add(filledBoard);
                }
            }

            for (Integer ball : balls) {
                System.out.format("Processing ball %d.\n", ball);
                Transporter check = checkBoards(ball, balls, boards, filledBoards);

                //if (check != null)
                //    return check;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Transporter checkBoards(int ball, List<Integer> balls, List<Integer[][]> boards, List<Boolean[][]> filledBoards) {
        for (int i = 0; i < boards.size(); i++) {
            Integer[][] board = boards.get(i);
            Boolean[][] filledBoard = filledBoards.get(i);

            for (int row = 0; row < 5; row ++) {
                for (int col = 0; col < 5; col++) {
                    if (board[row][col] == ball) {
                        filledBoard[row][col] = true;
                        if (checkBoard(filledBoard)) {
                            //This board just won
                            System.out.format("Winning board!\n");

                            //Calculate prize
                            int prize = 0;
                            for (int p = 0; p < board[0].length; p++) {
                                for (int pr = 0; pr < board[0].length; pr++) {
                                    if (!filledBoard[p][pr]) {
                                        prize += board[p][pr];
                                    }
                                }
                            }
                            prize *= ball;
                            Transporter winner = new Transporter(board, filledBoard, prize);
                            winners.add(winner);
                            return winner;
                        }
                    }
                }
            }
        }

        System.out.println("No one won.");
        return null;
    }

    private boolean checkBoard(Boolean[][] board) {
        System.out.format("Checking board: %s\n%s\n%s\n%s\n%s\n", Arrays.toString(board[0]), Arrays.toString(board[1]), Arrays.toString(board[2]),
                Arrays.toString(board[3]), Arrays.toString(board[4]));
        for (int row = 0; row < 5; row++) {
            if (board[row][0] && board[row][1] && board[row][2] && board[row][3] && board[row][4]) {
                // this is a winning row
                System.out.format("Board wins on row %s\n", row);
                return true;
            }
        }

        for (int col = 0; col < 5; col++) {
            if (board[0][col] && board[1][col] && board[2][col] && board[3][col] && board[4][col]) {
                //this is a winning column
                System.out.format("Board wins on column %d\n", col);
                return true;
            }
        }

        return false;
    }

    private int sumWin(Transporter t) {
        Boolean[][]filledBoard = t.getBools();
        Integer[][] board = t.getInts();

        //Find the winning row or column
        for (int row = 0; row < 5; row++) {
            if (filledBoard[row][0] && filledBoard[row][1] && filledBoard[row][2] && filledBoard[row][3] && filledBoard[row][4]) {
                // this is the winning row
                return board[row][0] + board[row][1] + board[row][2] + board[row][3] + board[row][4];
            }
        }
        for (int col = 0; col < 5; col++) {
            if (filledBoard[0][col] && filledBoard[1][col] && filledBoard[2][col] && filledBoard[3][col] && filledBoard[4][col]) {
                //this is the winning column
                return board[0][col] + board[1][col] + board[2][col] + board[3][col] + board[4][col];
            }
        }

        return -1;
    }

    private class Transporter {
        private Integer[][] ints;
        private Boolean[][] bools;
        private int prize = 0;

        public Transporter(Integer[][] ints, Boolean[][] bools, int prize) {
            this.ints = ints;
            this.bools = bools;
            this.prize = prize;
        }

        public Transporter(Integer[][] ints, Boolean[][] bools) {
            this.ints = ints;
            this.bools = bools;
        }

        public Integer[][] getInts() {
            return ints;
        }

        public void setInts(Integer[][] ints) {
            this.ints = ints;
        }

        public Boolean[][] getBools() {
            return bools;
        }

        public void setBools(Boolean[][] bools) {
            this.bools = bools;
        }

        public int getPrize() {
            return prize;
        }

        public void setPrize(int prize) {
            this.prize = prize;
        }
    }
}
