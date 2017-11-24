package jmri.jmrit.display.palette;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jmri.NamedBean;
import jmri.jmrit.catalog.NamedIcon;
import jmri.jmrit.display.Editor;
import jmri.jmrit.display.MemoryComboIcon;
import jmri.jmrit.display.MemoryIcon;
import jmri.jmrit.display.MemoryInputIcon;
import jmri.jmrit.display.MemorySpinnerIcon;
import jmri.jmrit.picker.PickListModel;
import jmri.util.swing.DrawSquares;
import jmri.util.swing.ImagePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryItemPanel extends TableItemPanel implements ChangeListener, ListSelectionListener {

    enum Type {
        READONLY, READWRITE, SPINNER, COMBO
    }
    JSpinner _spinner;
    private ImagePanel _iconFamilyPanel;

    public MemoryItemPanel(ItemPalette parentFrame, String type, String family, PickListModel<jmri.Memory> model, Editor editor) {
        super(parentFrame, type, family, model, editor);
    }

    @Override
    public void init() {
        if (!_initialized) {
            add(initTablePanel(_model, _editor));
            initIconFamiliesPanel();
            add(_iconFamilyPanel);
            add(makeButtonPanel(_iconFamilyPanel, _backgrounds));
            _initialized = true;
        }
    }

    protected JPanel instructions() {
        JPanel blurb = new JPanel();
        blurb.setLayout(new BoxLayout(blurb, BoxLayout.Y_AXIS));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        blurb.add(new JLabel(Bundle.getMessage("AddToPanel")));
        blurb.add(new JLabel(Bundle.getMessage("DragIconPanel")));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        blurb.add(new JLabel(Bundle.getMessage("nullMemoryIcon"),
                NamedIcon.getIconByName("resources/icons/misc/X-red.gif"),
                javax.swing.SwingConstants.TRAILING));
        blurb.add(new JLabel(Bundle.getMessage("emptyMemoryIcon")));
        blurb.add(new JLabel(Bundle.getMessage("emptyMemoryFix")));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        blurb.add(new JLabel(Bundle.getMessage("comboMemory1")));
        blurb.add(new JLabel(Bundle.getMessage("comboMemory2", Bundle.getMessage("EditItem", Bundle.getMessage("BeanNameMemory")))));
        blurb.add(Box.createVerticalStrut(ItemPalette.STRUT_SIZE));
        JPanel panel = new JPanel();
        panel.add(blurb);
        return panel;
    }

    /**
     * CENTER Panel
     */
    MemoryIcon _readMem;
    MemoryInputIcon _writeMem;
    MemorySpinnerIcon _spinMem;
    MemoryComboIcon _comboMem;

    @Override
    protected void initIconFamiliesPanel() {
        _iconFamilyPanel = new ImagePanel();
        _iconFamilyPanel.setLayout(new BoxLayout(_iconFamilyPanel, BoxLayout.Y_AXIS));
        _iconFamilyPanel.setOpaque(true);
        if (!_update) {
            _iconFamilyPanel.add(instructions());
        }

        // create array of backgrounds
        _backgrounds = new BufferedImage[5];
        _currentBackground = _editor.getTargetPanel().getBackground(); // start using Panel background color
        _backgrounds[0] = DrawSquares.getImage(_iconFamilyPanel, 20, _currentBackground, _currentBackground);
        for (int i = 1; i <= 3; i++) {
            _backgrounds[i] = DrawSquares.getImage(_iconFamilyPanel, 20, colorChoice[i - 1], colorChoice[i - 1]); // choice 0 is not in colorChoice[]
        }
        _backgrounds[4] = DrawSquares.getImage(_iconFamilyPanel, 20, Color.white, _grayColor);

        makeDragIconPanel(1);
        makeDndIconPanel(null, null);
    }

    @Override
    protected void makeDndIconPanel(java.util.HashMap<String, NamedIcon> iconMap, String displayKey) {
        if (_update) {
            return;
        }
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        c.weightx = 1.0;

        JLabel label = new JLabel(Bundle.getMessage("ReadWriteMemory"));
        label.setOpaque(false);
        panel.add(label, c);
        c.gridy = 1;
        _writeMem = new MemoryInputIcon(5, _editor);
        panel.add(makeDragIcon(_writeMem, Type.READWRITE), c);
        
        _spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JTextField field = ((JSpinner.DefaultEditor) _spinner.getEditor()).getTextField();
        field.setColumns(2);
        field.setText("5");
        _spinner.setMaximumSize(_spinner.getPreferredSize());
        _spinner.addChangeListener(this);
        c.gridy = 2;
        panel.add(_spinner, c);
        
        c.gridy = 3;
        c.anchor = java.awt.GridBagConstraints.NORTH;
        label = new JLabel(Bundle.getMessage("NumColsLabel"));
        label.setOpaque(false);
        panel.add(label, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        label = new JLabel(Bundle.getMessage("ReadMemory"));
        label.setOpaque(false);
        panel.add(label, c);
        c.gridy = 1;
        _readMem = new MemoryIcon(NamedIcon.getIconByName("resources/icons/misc/X-red.gif"), _editor);
        panel.add(makeDragIcon(_readMem, Type.READONLY), c);

        c.gridx = 2;
        c.gridy = 0;
        label = new JLabel(Bundle.getMessage("SpinnerMemory"));
        label.setOpaque(false);
        panel.add(label, c);
        c.gridy = 1;
        _spinMem = new MemorySpinnerIcon(_editor);
        panel.add(makeDragIcon(_spinMem, Type.SPINNER), c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;
        label = new JLabel(Bundle.getMessage("ComboMemory"));
        label.setOpaque(false);
        panel.add(label, c);
        c.gridy = 3;
        _comboMem = new MemoryComboIcon(_editor, null);
        panel.add(makeDragIcon(_comboMem, Type.COMBO), c);

        _dragIconPanel.add(panel);
        _dragIconPanel.invalidate();
    }

    private JPanel makeDragIcon(JComponent mem, Type type) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        JPanel comp;
        try {
            comp = getDragger(new DataFlavor(Editor.POSITIONABLE_FLAVOR), type, mem);
            comp.setOpaque(false);
            comp.setToolTipText(Bundle.getMessage("ToolTipDragIcon"));
        } catch (java.lang.ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            comp = new JPanel();
        }
        panel.add(comp);
        return panel;
    }

//    /**
//     * Create panel element containing [Set background:] drop down list.
//     * @see jmri.jmrit.catalog.PreviewDialog#setupPanel()
//     * @see DecoratorPanel
//     * @see FamilyItemPanel
//     *
//     * @return a JPanel with label and drop down
//     */
//    private JPanel makeButtonPanel() {
//        JComboBox<String> bgColorBox = new JComboBox<>();
//        bgColorBox.addItem(Bundle.getMessage("PanelBgColor")); // PanelColor key is specific for CPE, too long for combo
//        bgColorBox.addItem(Bundle.getMessage("White"));
//        bgColorBox.addItem(Bundle.getMessage("LightGray"));
//        bgColorBox.addItem(Bundle.getMessage("DarkGray"));
//        // bgColorBox.addItem(Bundle.getMessage("Checkers")); // checkers option not yet in combobox, under development
//        bgColorBox.setSelectedIndex(0); // panel bg color
//        bgColorBox.addActionListener((ActionEvent e) -> {
//            if (bgColorBox.getSelectedIndex() == 0) {
//                // use panel background color
//                _currentBackground = _editor.getTargetPanel().getBackground();
//                _squaresPanel.setVisible(false);
//                _iconFamilyPanel.setBackground(_currentBackground);
//            } else if (bgColorBox.getSelectedIndex() == 4) { // display checkers background, under development 4.9.6
//                _squaresPanel.setVisible(true);
//                log.debug("FamilyItemPanel checkers visible");
//                _iconFamilyPanel.setOpaque(false);
//            } else {
//                _currentBackground = colorChoice[bgColorBox.getSelectedIndex() - 1]; // choice 0 is not in colorChoice[]
//                _squaresPanel.setVisible(false);
//                _iconFamilyPanel.setBackground(_currentBackground);
//            }
//        });
//
//        JPanel backgroundPanel = new JPanel();
//        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
//        JPanel pp = new JPanel();
//        pp.setLayout(new FlowLayout(FlowLayout.CENTER));
//        pp.add(new JLabel(Bundle.getMessage("setBackground")));
//        pp.add(bgColorBox);
//        backgroundPanel.add(pp);
//        backgroundPanel.setMaximumSize(backgroundPanel.getPreferredSize());
//        return backgroundPanel;
//    }

    /*
     * Set column width for InputMemoryIcon.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("stateChanged: class= {}, value= {}", _spinner.getValue().getClass().getName(),
                    _spinner.getValue());
        }
        Integer nCols = (Integer) _spinner.getValue();
        _writeMem.setNumColumns(nCols.intValue());
    }

    /**
     * ListSelectionListener action from table.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (_table == null) {
            return;
        }
        int row = _table.getSelectedRow();
        log.debug("Table valueChanged: row= {}", row);
        if (row >= 0) {
            if (_updateButton != null) {
                _updateButton.setEnabled(true);
                _updateButton.setToolTipText(null);
            }
            NamedBean bean = getDeviceNamedBean();
            _readMem.setMemory(bean.getDisplayName());
            _writeMem.setMemory(bean.getDisplayName());
            _spinMem.setMemory(bean.getDisplayName());
            _comboMem.setMemory(bean.getDisplayName());
        } else {
            if (_updateButton != null) {
                _updateButton.setEnabled(false);
                _updateButton.setToolTipText(Bundle.getMessage("ToolTipPickFromTable"));
            }
            _iconFamilyPanel.remove(_dragIconPanel);
            makeDragIconPanel(1);
            makeDndIconPanel(null, null);
        }
        validate();
    }

    @Override
    protected void setEditor(Editor ed) {
        _editor = ed;
        if (_initialized) {
            _iconFamilyPanel.remove(_dragIconPanel);
            makeDragIconPanel(1);
            makeDndIconPanel(null, null);
        }
    }

    protected IconDragJComponent getDragger(DataFlavor flavor, Type type, JComponent comp) {
        return new IconDragJComponent(flavor, type, comp);
    }

    protected class IconDragJComponent extends DragJComponent {

        Type _memType;

        public IconDragJComponent(DataFlavor flavor, Type type, JComponent comp) {
            super(flavor, comp);
            _memType = type;
        }
        
        @Override
        protected boolean okToDrag() {
            NamedBean bean = getDeviceNamedBean();
            if (bean == null) {
                JOptionPane.showMessageDialog(this, Bundle.getMessage("noRowSelected"),
                        Bundle.getMessage("WarningTitle"), JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                return null;
            }
            NamedBean bean = getDeviceNamedBean();
            if (bean == null) {
                log.error("IconDragJComponent.getTransferData: NamedBean is null!");
                return null;
            }

            if (flavor.isMimeTypeEqual(Editor.POSITIONABLE_FLAVOR)) {
                switch (_memType) {
                    case READONLY:
                        MemoryIcon m = new MemoryIcon("", _editor);
                        m.setMemory(bean.getDisplayName());
                        m.setSize(m.getPreferredSize().width, m.getPreferredSize().height);
                        m.setLevel(Editor.MEMORIES);
                        return m;
                    case READWRITE:
                        int numCols = 5;
                        try {
                            ((JSpinner.DefaultEditor) _spinner.getEditor()).commitEdit();
                            SpinnerNumberModel spinModel = (SpinnerNumberModel) _spinner.getModel();
                            if (log.isDebugEnabled()) {
                                log.debug("MemoryDnD.createTransferable: spinCols= "
                                        + spinModel.getNumber().intValue());
                            }
                            numCols = spinModel.getNumber().intValue();
                        } catch (java.text.ParseException pe) {
                            log.error("MemoryDnD.createTransferable: " + pe);
                        }
                        MemoryInputIcon mi = new MemoryInputIcon(numCols, _editor);
                        mi.setMemory(bean.getDisplayName());
                        mi.setSize(mi.getPreferredSize().width, mi.getPreferredSize().height);
                        mi.setLevel(Editor.MEMORIES);
                        return mi;
                    case SPINNER:
                        MemorySpinnerIcon ms = new MemorySpinnerIcon(_editor);
                        ms.setMemory(bean.getDisplayName());
                        ms.setSize(ms.getPreferredSize().width, ms.getPreferredSize().height);
                        ms.setLevel(Editor.MEMORIES);
                        return ms;
                    case COMBO:
                        MemoryComboIcon mc = new MemoryComboIcon(_editor, null);
                        mc.setMemory(bean.getDisplayName());
                        mc.setSize(mc.getPreferredSize().width, mc.getPreferredSize().height);
                        mc.setLevel(Editor.MEMORIES);
                        return mc;
                    default:
                        // fall through
                        break;
                    }
                } else if (DataFlavor.stringFlavor.equals(flavor)) {
                    StringBuilder sb = new StringBuilder(_itemType);
                    sb.append(" icons for \"");
                    sb.append(bean.getDisplayName());
                    sb.append("\"");
                    return  sb.toString();
                }
                return null;
            }
        }

    private final static Logger log = LoggerFactory.getLogger(MemoryItemPanel.class);

}
