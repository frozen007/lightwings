/**
 * Project ZmyRepo
 * by zhao-mingyu at 2008-7-1
 *
 */
package org.zmy.util;

import java.util.Random;

/**
 * Algorithm
 * 
 */
public class Algorithm {

    public static int[][] getRandomMetrics(int size) {
        int[][] result = new int[size][size];
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = rand.nextInt();
            }
        }

        return result;
    }

    public static long[][] multiplyMetrics(int[][] a, int[][] b, int size) {
        long[][] result = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = 0;
                for (int k = 0; k < size; k++) {
                    result[i][j] = result[i][j] + a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }
}
