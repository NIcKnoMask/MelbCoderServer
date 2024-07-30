package com.unimelbCoder.melbcode.Service.Planning;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanningAlgorithms {
    private static final double EARTH_RADIUS = 6371.0;

    public static List<Integer> greedySearch(Map<Integer, double[]> pos){
        List<Integer> res = new ArrayList<>();
        int n = pos.size();
        double[][] map = new double[n][n];

        // construct map
        int idx1 = 0;
        Map<Integer, Integer> idxToLocId = new HashMap<>(); // idx: locationId

        for(int k1: pos.keySet()){
            idxToLocId.put(idx1, k1);
            int idx2 = 0;
            for(int k2: pos.keySet()){
                map[idx1][idx2] = 0;
                if(k1 != k2){
                    map[idx1][idx2] = distanceCalculation(pos.get(k1), pos.get(k2));
                }
                idx2++;
            }
            idx1++;
        }

        // greedy dfs
        boolean[] visited = new boolean[n];
        int curr = 0;
        while(true){
            visited[curr] = true;
            int minP = Integer.MAX_VALUE, minN = 0;
            boolean flag = false;
            for(int j = 0; j < n; j++){
                if(visited[j]) continue;
                if(map[curr][j] < minP){
                    minN = j;
                    flag = true;
                }
            }
            if(flag){
                res.add(idxToLocId.get(curr));
                curr = minN;
            }else{
                break;
            }
        }

        return res;
    }

    public static List<Integer> TSP(Map<Integer, double[]> pos){
        List<Integer> res = new ArrayList<>();
        int n = pos.size();
        double[][] map = new double[n][n];

        // construct map
        int idx1 = 0;
        Map<Integer, Integer> idxToLocId = new HashMap<>(); // idx: locationId

        for(int k1: pos.keySet()){
            idxToLocId.put(idx1, k1);
            int idx2 = 0;
            for(int k2: pos.keySet()){
                map[idx1][idx2] = 0;
                if(k1 != k2){
                    map[idx1][idx2] = distanceCalculation(pos.get(k1), pos.get(k2));
                }
                idx2++;
            }
            idx1++;
        }

        //
        double[][] dp = new double[n][1 << (n - 1)];
        int[][] parent = new int[n][1 << (n - 1)];
        for(int i = 0; i < n; i++) {
            dp[i][0] = map[i][0];
        }

        for (int p = 1; p < 1 << (n - 1); p++) {  //遍历所有集合
            for (int i = 0; i < n; i++) {  //选定一个起点城市
                dp[i][p] = Integer.MAX_VALUE >> 1;
                if(isSelf(i, p)) {  //当然起点城市不能包含在P中
                    continue;
                }
                for (int k = 1; k < n; k++) {  //依次枚举子问题,选取城市k为子问题的起点
                    if(visit(k, p)) {  //判断城市k是否在集合p中
                        int op = unmark(p, k);  //将起点k对应的位标为0
                        double cost = dp[k][op] + map[i][k];
                        if (cost < dp[i][p]) {
                            dp[i][p] = cost;
                            parent[i][p] = k;
                        }
                    }
                }
            }
        }

        boolean[] visited = new boolean[n];
        int lastNode = 0;
        int mask = (1 << (n - 1)) - 1;
        for (int i = n - 1; i > 0; i--) {
            res.add(idxToLocId.get(lastNode));
            visited[lastNode] = true;
            lastNode = parent[lastNode][mask];
            mask ^= (1 << (lastNode - 1));
        }
        res.add(idxToLocId.get(lastNode));
        visited[lastNode] = true;

        // 将未访问过的节点加入路径中
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                res.add(idxToLocId.get(i));
            }
        }
        return res;
    }

    private static boolean isSelf(int city, int p) {  //对城市0统一返回false
        return (p & (1 << (city - 1))) != 0;
    }

    private static boolean visit(int city, int p) {
        return isSelf(city, p);
    }

    private static int unmark(int p, int city) {
        return (p ^ (1 << (city - 1))); //异或，使得city - 1这个位置变为0
    }

    private static double distanceCalculation(double[] p1, double[] p2){
        double diffLat = p1[0] - p2[0];
        double diffLng = p1[1] - p2[1];
        double a = Math.pow(Math.sin(diffLat / 2), 2) + Math.cos(p1[0]) * Math.cos(p1[1]) * Math.pow(Math.sin(diffLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
