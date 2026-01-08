package com.tayek.utilities;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.TestRule;
import com.tayek.MyTestWatcher;
import static com.tayek.io.IO.*;
public class UtilitiesTestCase {
    @Rule public TestRule watcher=new MyTestWatcher();

    @BeforeClass public static void setUpBeforeClass() throws Exception {}
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {}
    @After public void tearDown() throws Exception {}
    @Test public void test() {
        for(int i=0;i<=3;i++)
            p(i+": "+Utility.method(i));
    }
}
