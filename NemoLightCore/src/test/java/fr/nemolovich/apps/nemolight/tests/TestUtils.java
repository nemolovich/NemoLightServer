/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nemolovich.apps.nemolight.tests;

import fr.nemolovich.apps.nemolight.utils.Utils;
import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author nemo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUtils {
    
    @Test
    public void test() {
        String f1="theFieldName";
        String res=Utils.getFieldName(f1);
        assertEquals(res, "the_field_name");
    }
}
