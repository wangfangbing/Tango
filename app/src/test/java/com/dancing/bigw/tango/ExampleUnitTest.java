package com.dancing.bigw.tango;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        System.out.println("1 " + new IM().getViewType());
        System.out.println("2 " + new IM().getViewType());
        System.out.println("3 " + new IM().getViewType());
    }

    @Test
    public void testClass() {
        List<IM> class1 = new ArrayList<>();
        List<String> class2 = new ArrayList<>();
        System.out.println("eq " + (class1.getClass() == class2.getClass()));
        System.out.println(class1.getClass());
    }

    private static class IM {
        public final int mViewType = hashCode();

        public int getViewType() {
            return mViewType;
        }
    }
}