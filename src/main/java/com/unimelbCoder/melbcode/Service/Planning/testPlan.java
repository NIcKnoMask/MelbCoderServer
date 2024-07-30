package com.unimelbCoder.melbcode.Service.Planning;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class testPlan {


    public static void main(String[] args) {
        Map<Integer, double[]> locationMap = new HashMap<>();

        locationMap.put(1, new double[]{37, -122});
        locationMap.put(5, new double[]{34, -118});
        locationMap.put(7, new double[]{40, -74});
        locationMap.put(44, new double[]{35, -77});
        locationMap.put(124, new double[]{41, -94});
        locationMap.put(3, new double[]{44, -85});

        List<Integer> res = PlanningAlgorithms.greedySearch(locationMap);
        for(int l: res){
            System.out.print(l + ", ");
        }
    }
}
