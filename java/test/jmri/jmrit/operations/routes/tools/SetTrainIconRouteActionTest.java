package jmri.jmrit.operations.routes.tools;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import jmri.jmrit.operations.OperationsTestCase;
import jmri.util.JUnitUtil;
import jmri.util.JmriJFrame;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class SetTrainIconRouteActionTest extends OperationsTestCase {

    @Test
    public void testCTor() {
        SetTrainIconRouteAction t = new SetTrainIconRouteAction("Test");
        Assert.assertNotNull("exists",t);
    }
    
    @Test
    public void testAction() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        SetTrainIconRouteAction a = new SetTrainIconRouteAction("Test");
        Assert.assertNotNull("exists", a);
        
        a.actionPerformed(new ActionEvent(this, 0, null));
        
        JmriJFrame f = JmriJFrame.getFrame(Bundle.getMessage("MenuSetTrainIcon"));
        Assert.assertNotNull("frame exists", f);
        JUnitUtil.dispose(f);
    }

    // private final static Logger log = LoggerFactory.getLogger(SetTrainIconRouteActionTest.class);

}
