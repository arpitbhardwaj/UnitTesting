package com.ab.unittesting.basic;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arpit Bhardwaj
 */
public class HamcrestTest {

    @Test
    public void mapShouldContainValue() {
        Map<String,Integer> map = getMap();
        //Junit assert will not be good for diagnostic
        //Assert.assertTrue(map.containsKey("B"));

        //Hamcrest gives better diagnostic messages
        MatcherAssert.assertThat(map, Matchers.hasKey("B"));
    }

    private Map<String, Integer> getMap() {

        Map<String,Integer> map = new HashMap<>();
        map.put("A",1);
        map.put("B",2);
        return map;
    }

    @Test
    public void listContainsItems() {
        List<Integer> integerList = getNumbers();

        //Assert.assertEquals(5, (int) integerList.get(6));
        MatcherAssert.assertThat(integerList, Matchers.hasItem(5));
    }

    private List<Integer> getNumbers() {
        return Arrays.asList(0,1,2,3,4,5);
    }
}
