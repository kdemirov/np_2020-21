package DMOJ.FoodLines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FoodLines {
    public static int shortest(int[] lines, int n) {
        int min = 0;
        for (int i = 1; i < n; i++) {
            if (lines[i] < lines[min]) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] firstLine;
        String[] secondLine;
        int[] lines = new int[100];
        firstLine = scanner.nextLine().split("\\s");

        secondLine = scanner.nextLine().split("\\s");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);

        for (int i = 0; i < n; i++) {
            lines[i] = Integer.parseInt(secondLine[i]);
        }
        for (int i = 0; i < m; i++) {
            int min = shortest(lines, n);
            System.out.println(lines[min]);
            lines[min]++;
        }

    }

}