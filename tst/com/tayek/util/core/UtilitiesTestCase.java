package com.tayek.util.core;
import org.junit.*;
import org.junit.rules.TestRule;
import com.tayek.util.junit.BasicTestWatcher;
import static com.tayek.util.io.Print.*;
public class UtilitiesTestCase {
    @Rule public TestRule watcher=new BasicTestWatcher(getClass());

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {}
    @After public void tearDown() throws Exception {}
    @Test public void test() {
        for(int i=0;i<=3;i++)
            p(i+": "+Stacks.method(i));
    }
}


