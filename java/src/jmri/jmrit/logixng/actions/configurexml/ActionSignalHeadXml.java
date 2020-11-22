package jmri.jmrit.logixng.actions.configurexml;

import jmri.*;
import jmri.jmrit.logixng.DigitalActionManager;
import jmri.jmrit.logixng.actions.ActionSignalHead;

import org.jdom2.Element;

/**
 * Handle XML configuration for ActionLightXml objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004, 2008, 2010
 * @author Daniel Bergqvist Copyright (C) 2019
 */
public class ActionSignalHeadXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ActionSignalHeadXml() {
    }
    
    /**
     * Default implementation for storing the contents of a SE8cSignalHead
     *
     * @param o Object to store, of type TripleLightSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ActionSignalHead p = (ActionSignalHead) o;

        Element element = new Element("action-signalhead");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));

        storeCommon(p, element);

        NamedBeanHandle<SignalHead> signalHead = p.getSignalHead();
        if (signalHead != null) {
            element.addContent(new Element("signalHead").addContent(signalHead.getName()));
        }
        
        element.addContent(new Element("operationType").addContent(p.getOperationType().name()));
        
        element.addContent(new Element("apperance").addContent(Integer.toString(p.getAppearance())));
/*        
        int apperance = p.getAppearance();
        String apperanceKey = "";
        if (p.getSignalHead() != null) {
            apperanceKey = p.getSignalHead().getBean().getAppearanceKey(apperance);
        }
        element.addContent(new Element("apperanceKey").addContent(apperanceKey));
*/
        return element;
    }
    
    @Override
    public boolean load(Element shared, Element perNode) {
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        ActionSignalHead h = new ActionSignalHead(sys, uname);

        loadCommon(h, shared);

        Element signalHeadName = shared.getChild("signalHead");
        if (signalHeadName != null) {
            SignalHead signalHead = InstanceManager.getDefault(SignalHeadManager.class).getSignalHead(signalHeadName.getTextTrim());
            if (signalHead != null) h.setSignalHead(signalHead);
            else h.removeSignalHead();
        }

        Element queryType = shared.getChild("operationType");
        if (queryType != null) {
            h.setOperationType(ActionSignalHead.OperationType.valueOf(queryType.getTextTrim()));
        }
        
        Element apperanceElement = shared.getChild("apperance");
        if (apperanceElement != null) {
            try {
                int apperance = Integer.parseInt(apperanceElement.getTextTrim());
                h.setAppearance(apperance);
            } catch (NumberFormatException e) {
                log.error("cannot parse apperance: " + apperanceElement.getTextTrim(), e);
            }
        }
/*
        Element apperanceKeyElement = shared.getChild("apperanceKey");
        if (apperanceKeyElement != null) {
            String apperanceKey = apperanceKeyElement.getTextTrim();
            if (!apperanceKey.isEmpty() && (signalHead != null)) {
                h.setAppearance(signalHead.getApperance(apperanceKey));
            }
        }
*/
        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionSignalHeadXml.class);
}
