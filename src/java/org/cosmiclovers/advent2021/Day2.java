package org.cosmiclovers.advent2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Day2 implements Assignment {
    public void go(String[] args) {
        int result = driveAround(args[0]);
        int aimResult = driveAroundAndAim(args[0]);

        System.out.format("Driving around result is %d\n", result);
        System.out.format("Driving around and aiming is %d\n", aimResult);
    }

    private int driveAround(String filename) {
        // [x, y,  z]
        int[] map = {0, 0, 0};

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;
            String[] parts;
            String command;
            int distance;

            while ((line=br.readLine()) != null) {
                parts = line.split(" ");
                command = parts[0];
                distance = Integer.parseInt(parts[1]);

                //System.out.format("Command %s, distance %d\n", command, distance);
                switch (command) {
                    case "forward":
                        map[0]+=distance;
                        break;
                    case "down":
                        map[2]+=distance;
                        break;
                    case "up":
                        map[2]-=distance;
                        break;
                }
            }

            return map[0] * map[2];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int driveAroundAndAim(String filename) {
        // [x, aim,  z]
        int[] map = {0, 0, 0};

        try {
            File f = new File(filename);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;
            String[] parts;
            String command;
            int distance;

            while ((line=br.readLine()) != null) {
                parts = line.split(" ");
                command = parts[0];
                distance = Integer.parseInt(parts[1]);

                //System.out.format("Command %s, distance %d\n", command, distance);
                switch (command) {
                    case "forward":
                        map[0]+=distance;
                        map[2]+=map[1]*distance;
                        break;
                    case "down":
                        map[1]+=distance;
                        break;
                    case "up":
                        map[1]-=distance;
                        break;
                }
                //System.out.format("X: %d, Aim: %d, Z: %d\n", map[0], map[1], map[2]);
            }

            return map[0] * map[2];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
