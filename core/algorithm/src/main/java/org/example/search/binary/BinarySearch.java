package org.example.search.binary;

import java.util.Optional;

public class BinarySearch {

    public static Optional<Integer> binarySearch(int[] list, int item) {
        int low = 0;
        int high = list.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int guess = list[mid];
            if (guess == item) {
                return Optional.of(mid);
            }
            if (guess < item) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return Optional.empty();
    }
}

class Main {
    public static void main(String[] args) {
        Optional<Integer> item = BinarySearch.binarySearch(new int[]{5, 8, 9, 12, 13}, 5);
        System.out.println(item + "item was");

    }
}