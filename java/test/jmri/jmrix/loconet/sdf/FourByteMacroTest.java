package jmri.jmrix.loconet.sdf;

import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class FourByteMacroTest {

    @Test
    public void testCTor() {
        FourByteMacro t = new FourByteMacro(1,2,3,4);
        Assert.assertNotNull("exists",t);
    }

    @BeforeEach
    public void setUp() {
        JUnitUtil.setUp();
    }

    @AfterEach
    public void tearDown() {
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(FourByteMacroTest.class);

}
