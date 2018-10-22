package com.ill.sample;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubList {

    public static void main(String args[]) {

        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);

        int elem_index =  (intList.size() - 1) / 5;


        System.out.println("Size: " + intList.size() );
        System.out.println("Elem index: " + elem_index);


     /*  List<List<Integer>> lstArr = new ArrayList<>(
               intList.stream().collect(Collectors.partitioningBy(
                       s -> intList.indexOf(s) > elem_index)).values()  );*/

        List<List<Integer>> lstArr = Lists.partition(intList, elem_index );

        lstArr.forEach(System.out::println);
    }
}
