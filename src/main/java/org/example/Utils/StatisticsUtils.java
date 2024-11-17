package org.example.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsUtils {

    public static double[] removeOutliersAndCalculateStatistics(List<Long> data) {


        List<Long> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        int size = sortedData.size();
        long q1 = sortedData.get(size / 4);
        long q3 = sortedData.get(3 * size / 4);
        long iqr = q3 - q1;

        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        List<Long> filteredData = new ArrayList<>();
        for (long value : sortedData) {
            if (value >= lowerBound && value <= upperBound) {
                filteredData.add(value);
            }
        }

        double mean = calculateMean(filteredData);
        double stdDev = calculateStandardDeviation(filteredData, mean);

        return new double[]{mean, stdDev};

    }

    private static double calculateMean(List<Long> data) {
        long sum = 0;
        for (long value : data) {
            sum += value;
        }
        return sum / (double) data.size();
    }

    private static double calculateStandardDeviation(List<Long> data, double mean) {
        double sum = 0;
        for (long value : data) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / data.size());
    }
}
