package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.processing.Filer;

public class Day10 implements Assignment {
    List<String> incompleteChunks = new ArrayList<String>();
    Map<String, String> bracketMatch = new HashMap<String, String>();
    String openers = "([{<";
    String closers = ">}])";

    @Override
    public void go(String[] args) {
        bracketMatch.put("(", ")");
        bracketMatch.put("[", "]");
        bracketMatch.put("{", "}");
        bracketMatch.put("<", ">");

        part1(args[1]);
        part2(args[1]);
    }

    private void part1(String filename) {
        List<String> errors = new ArrayList<String>();
        List<String> corruptedChunks = new ArrayList<String>();
        List<String> goodChunks = new ArrayList<String>();

        Stack openings = new Stack();

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                //System.out.format("Chunk %s%n", line);
                String[] operands = line.split("");
                //System.out.format("Split line: %s%n", Arrays.toString(operands));

                boolean error = false;
                for (int i = 0; i<operands.length; i++) {
                    String operand = operands[i];
                    if (openers.contains(operand)) {
                        //System.out.format("Pushing %s%n", operand);
                        openings.push(operand);
                    } else if (closers.contains(operand)) {
                        String peek = (String) openings.peek();
                        if (operand.equals(bracketMatch.get(peek))) {
                            //Cool, valid, pop
                            //System.out.format("Popping %s%n", peek);
                            openings.pop();
                        } else {
                            //System.out.format("%s encountered but needed %s%n", operand, bracketMatch.get(openings.peek()));
                            corruptedChunks.add(line);
                            errors.add(operand);
                            error = true;
                            break;
                        }
                    } else {
                        //Illegal character
                        corruptedChunks.add(line);
                        error = true;
                        break;
                    }
                }
                if (!error && openings.isEmpty())
                    goodChunks.add(line);
                else if (!error) {
                    incompleteChunks.add(line);
                }
            }

            System.out.format("%d good, %d incomplete and %d corrupted chunks.%n", goodChunks.size(), 
                incompleteChunks.size(), corruptedChunks.size());
            //Tally points
            int score = 0;
            for (int i = 0; i<errors.size(); i++) {
                switch(errors.get(i)) {
                    case ")":
                        score += 3;
                        break;
                    case "]":
                        score += 57;
                        break;
                    case "}":
                        score += 1197;
                        break;
                    case ">":
                        score += 25137;
                        break;
                    default:
                        break;
                }
            }

            System.out.format("Part 1 score: %d%n", score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void part2(String filename) {
        //Part1 already set up the incomplete chunks, should be easy
        List<String> completions = new ArrayList<String>();        
        Stack ops = new Stack();

        for (String chunk : incompleteChunks) {
            //System.out.format("Chunk: %s%n", chunk);
            String[] operands = chunk.split("");
            //System.out.format("Operands: %s%n", Arrays.toString(operands));

            for (int i = 0; i<operands.length; i++) {
                String operand = operands[i];

                if (openers.contains(operand)) {
                    //System.out.format("Pushing %s%n", operand);
                    ops.push(operand);
                } else if (closers.contains(operand)) {
                    String peek = (String) ops.peek();
                    if (operand.equals(bracketMatch.get(peek))) {
                        //Cool, valid, pop
                        //System.out.format("Popping %s%n", peek);
                        ops.pop();
                    }
                }
            }

            //Complete the chunk
            String completionString = "";

            while (!ops.isEmpty()) {
                String peek = (String) ops.peek();
                String match = bracketMatch.get(peek);
                //System.out.format("Matching %s with %s%n", peek, match);
                completionString = completionString.concat(match);
                //System.out.format("Current completion string: %s%n", completionString);
                ops.pop();
            }
            //System.out.format("Adding completion string %s for %s%n", completionString, chunk);
            completions.add(completionString);
        }
        
        List<BigInteger> scores = new ArrayList<BigInteger>();

        BigInteger biOne = new BigInteger("1");
        BigInteger biTwo = new BigInteger("2");
        BigInteger biThree = new BigInteger("3");
        BigInteger biFour = new BigInteger("4");
        BigInteger biFive = new BigInteger("5");

        for (String completion: completions) {
            BigInteger score = new BigInteger("0");
            String[] operands = completion.split("");
            for (String s : operands) {
                score = score.multiply(biFive);
                if (s.equals(")")) 
                    score = score.add(biOne);
                else if (s.equals("]"))
                    score = score.add(biTwo);
                else if (s.equals("}"))
                    score = score.add(biThree);
                else if (s.equals(">"))
                    score = score.add(biFour);
            }
            //System.out.format("Adding score %d%n", score);
            scores.add(score);
        }

        //Get the middle score
        Collections.sort(scores);
        //System.out.format("Sorted list: %s%n", scores);
        int middle = Math.round(scores.size()/2);
        System.out.format("Part 2 score: %d.%n", scores.get(middle));
    }

}
