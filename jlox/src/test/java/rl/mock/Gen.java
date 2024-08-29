package rl.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Gen {

    public static void main(String[] args) {
        gen2();
        String[] t = new String[100];
        for (int i = 0; i < t.length; ++i) {
            t[i] = "'T" + i + "'";
        }
        System.out.println(Arrays.toString(t));
    }

    static void gen2() {
        int[] p0 = new int[] {45, 40, 36, 31, 27, 24, 20, 17, 15, 13, 11, 10, 10, 10, 10, 11, 13, 15, 18, 21, 25, 28, 33, 37, 41, 46, 50, 54, 59, 62, 66, 69, 72, 75, 77, 79, 81, 80, 79, 79, 77, 75, 73, 70, 67, 63, 60, 55, 51, 47, 42, 38, 34, 29, 26, 22, 19, 16, 14, 12, 10, 10, 10, 10, 11, 12, 14, 17, 20, 23, 27, 30, 35, 39, 43, 48, 52, 56, 61, 64, 68, 71, 74, 76, 78, 79, 79, 79, 79, 78, 76, 74, 72, 69, 65, 62, 58, 53, 49, 20};

        int[] p1 = new int[100];
        int[] p2 = new int[100];
        for (int i = 0; i < p1.length; ++i) {
            p0[i] += 10;
            p1[i] = Integer.max(Integer.min(30, 80 - p0[i]), 0);
            p2[i] = Integer.max(80 - p0[i] - p1[i], 0);
        }

        System.out.println(Arrays.toString(p0));
        System.out.println(Arrays.toString(p1));
        System.out.println(Arrays.toString(p2));
    }


    static void gen(int total, int n, int pn) {
        List<int[]> list = new ArrayList<>(pn);
        for (int i = 0; i < pn; ++i) {
            list.add(new int[n]);
        }
        for (int i = 0; i < n; i++) {
            int value = ThreadLocalRandom.current().nextInt(total / 2);
            list.get(0)[i] = value;
            int left = total - value;
            for (int k = 1; k < pn - 1; k++) {
                value = ThreadLocalRandom.current().nextInt(left / 2);
                list.get(k)[i] = value;
                left -= value;
            }
            list.get(pn - 1)[i] = left;
        }

        list.stream().map(Arrays::toString).forEach(System.out::println);
    }
}
