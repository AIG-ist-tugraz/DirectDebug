/* DirectDebug: Automated Testing and Debugging of Feature Models
 *
 * Copyright (C) 2020-2021  AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiagLib.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Generate pseudo-random floating point values, with an approximately Gaussian
 * (normal) distribution.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class RandomGaussian {

    double mean;
    double variance;
    int min;
    int max;
    private Random fRandom = new Random();

    public static void main(String... aArgs) {
//		RandomGaussian gaussian = new RandomGaussian(3,1,2,4);
//		RandomGaussian gaussian = new RandomGaussian(5,1,3,7);
        RandomGaussian gaussian = new RandomGaussian(7,2,2,12);
        Map<Integer, Integer> valueCount = new HashMap<Integer, Integer>();
        for (int idx = 1; idx <= 1000; ++idx) {
            int val = (int) gaussian.getGaussian();
            Integer valForNum = valueCount.get(val);
            if (valForNum == null) {
                valueCount.put(val, 1);
            }
            else {
                valueCount.put(val, valForNum +1);
            }
        }
        System.out.println(valueCount);
    }

    /**
     * Creates a random number
     * @param mean mean
     * @param variance variance of distribution
     * @param min minbound
     * @param max maxbound
     */
    public RandomGaussian(double mean, double variance, int min, int max) {
        this.mean = mean;
        this.variance = variance;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets an integer-converted value
     * @return a random value
     */
    public double getGaussian() {
        double value = this.mean + fRandom.nextGaussian() * this.variance;
        int result = (int)Math.round(value);
        if (result < min) result = min;
        if (result > max) result = max;
        return result;
    }
}
