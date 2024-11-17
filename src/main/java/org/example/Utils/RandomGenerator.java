package org.example.Utils;

import java.util.Random;

public class RandomGenerator {
    public static final Random RANDOM_GENERATOR = new Random(1);
    public static boolean getRandomBoolean(){
        return RANDOM_GENERATOR.nextBoolean();
    }
    public static double getRandomDouble(){
        return RANDOM_GENERATOR.nextDouble();
    }
    public static int getRandomInt(int bounds){
        return RANDOM_GENERATOR.nextInt(bounds);
    }
    public static long getRandomLong(){
        return RANDOM_GENERATOR.nextLong();
    }


}