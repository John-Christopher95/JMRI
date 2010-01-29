package jmri.jmrit.display.configurexml;

import jmri.jmrit.display.Editor;
import jmri.jmrit.display.ReporterIcon;

import org.jdom.Element;

/**
 * Handle configuration for display.ReporterIcon objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004
 * @version $Revision: 1.15 $
 */
public class ReporterIconXml extends PositionableLabelXml {

    public ReporterIconXml() {
    }

    /**
     * Default implementation for storing the contents of a
     * ReporterIcon
     * @param o Object to store, of type ReporterIcon
     * @return Element containing the complete info
     */
    public Element store(Object o) {

        ReporterIcon p = (ReporterIcon)o;

        Element element = new Element("reportericon");

        // include contents
        element.setAttribute("reporter", p.getReporter().getSystemName());
        storeCommonAttributes(p, element);

        storeTextInfo(p, element);
        
        element.setAttribute("class", "jmri.jmrit.display.configurexml.ReporterIconXml");

        return element;
    }


    public boolean load(Element element) {
        log.error("Invalid method called");
        return false;
    }

    /**
     * Create a PositionableLabel, then add to a target JLayeredPane
     * @param element Top level Element to unpack.
     * @param o  PanelEditor as an Object
     */
    public void load(Element element, Object o) {
        Editor ed = (Editor)o;
		ReporterIcon l = new ReporterIcon(ed);

        loadTextInfo(l, element);

        l.setReporter(jmri.InstanceManager.reporterManagerInstance().getReporter(
            element.getAttribute("reporter").getValue()));

        loadCommonAttributes(l, Editor.REPORTERS, element);

        l.setSize(l.getPreferredSize().width, l.getPreferredSize().height);
        ed.putItem(l);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReporterIconXml.class.getName());
}