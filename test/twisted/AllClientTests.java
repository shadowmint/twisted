package twisted;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.google.gwt.junit.tools.GWTTestSuite;
import twisted.client.ComponentContainerTests;
import twisted.client.ComponentRegisterTests;

public class AllClientTests extends GWTTestSuite {
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for: twisted.client");
        suite.addTestSuite(ComponentContainerTests.class);
        suite.addTestSuite(ComponentRegisterTests.class);
        return suite;
    }
}