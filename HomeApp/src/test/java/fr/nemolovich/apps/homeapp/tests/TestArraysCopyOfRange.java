/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.homeapp.tests;

import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Nemolovich
 */
public class TestArraysCopyOfRange {

    private static final String[] arr1 = {"0", "1", "2"};
    private static final String[] arr2 = {"1", "2"};
    private static final String[] arr3 = {"2"};
    private static final String[] arr4 = {};

    @Test
    public void test() {
        String[] tmp = Arrays.copyOfRange(arr1, 1, arr1.length);
        assertArrayEquals(arr2, tmp);
        tmp = Arrays.copyOfRange(arr2, 1, arr2.length);
        assertArrayEquals(arr3, tmp);
        tmp = Arrays.copyOfRange(arr3, 1, arr3.length);
        assertArrayEquals(arr4, tmp);
    }
}
