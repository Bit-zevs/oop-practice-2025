package org.example;


import java.util.Random;

public class Questions {
    public Integer expressionGenerator(){
        Random rnd = new Random();
        return rnd.nextInt(5);
    }
}