package org.weebly.generator.forms;

import org.weebly.generator.Controller;

import javax.swing.*;
import java.awt.event.*;

public class CreateFile extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mainFileName;
    private JTextField testFileName;
    private JComboBox fileType;
    private JTextField moduleName;
    private JLabel warningLabel;
    private JLabel warningtext;
    private JTextField objectName;
    private Controller controller;

    public CreateFile(Controller angularController) {
        super();
        this.controller = angularController;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        fileType.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        objectName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();
                super.keyReleased(e);
            }
        });
    }

    private void refresh() {
        mainFileName.setText(controller.getSrcFilename(getBaseName(), getObjectType()));
        testFileName.setText(controller.getTestFilename(getBaseName(), getObjectType()));

        boolean exists = controller.checkIfFileExists(mainFileName.getText()) && controller.checkIfFileExists(testFileName.getText());

        if (exists) {
            buttonOK.setEnabled(false);
            warningLabel.setVisible(true);
            warningtext.setVisible(true);
        } else {
            buttonOK.setEnabled(true);
            warningLabel.setVisible(false);
            warningtext.setVisible(false);
        }
    }

    private String getBaseName() {
        return objectName.getText().trim();
    }
    private String getObjectType() {return (String)fileType.getSelectedItem();}

    private void onOK() {
        this.controller.createHandler(getBaseName(), (String) fileType.getSelectedItem(), moduleName.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void showDialog() {
        warningLabel.setVisible(false);
        warningtext.setVisible(false);
        this.setLocationRelativeTo(null);
        this.objectName.grabFocus();
        this.pack();
        this.setVisible(true);
    }
}
