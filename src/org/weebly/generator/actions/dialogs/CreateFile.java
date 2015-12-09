package org.weebly.generator.actions.dialogs;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.weebly.generator.actions.Commons;
import org.weebly.generator.services.ConfigurationLoader;
import org.weebly.generator.services.ContentBuilder;
import org.weebly.generator.services.FileHandler;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateFile extends JDialog {
    private JComboBox componentTypeComboBox;
    private JTextField componentBaseName;
    private JComboBox moduleNameComboBox;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mainFileName;
    private JTextField testFileName;
    private JLabel warningLabel;
    private JLabel warningText;
    private JTextField componentName;
    private List<String> allItems;

    private boolean isFileNameAvailable = false;
    private boolean isModuleNameAvailable = false;

    Project currentProject;
    String currentPath;

    public CreateFile(Project project, String path, String[] componentTypes) {
        super();

        currentProject = project;
        currentPath = path;

        //populate componentType dropdown
        componentTypeComboBox.removeAllItems();
        for (String item : componentTypes) {
            componentTypeComboBox.addItem(item);
        }

        //populate module name typeahead
        ConfigurationLoader configurationLoader = ServiceManager.getService(ConfigurationLoader.class);
        allItems = configurationLoader.getModuleNameSuggestions();
        if (allItems.size() > 0) {
            moduleNameComboBox.removeAllItems();
            for (String item : allItems) {
                moduleNameComboBox.addItem(item);
            }
            isModuleNameAvailable = true;
        }

        updateOkButtonState();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        moduleNameComboBox.getEditor().getEditorComponent().addKeyListener(new KeyEventHandler());

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

        componentTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        componentBaseName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refresh();
                super.keyReleased(e);
            }
        });
    }

    private void refresh() {
        mainFileName.setText(ContentBuilder.getSrcFilename(getComponentBasename(), getSelectedComponentType()));
        testFileName.setText(ContentBuilder.getTestFilename(getComponentBasename(), getSelectedComponentType()));
        componentName.setText(ContentBuilder.getComponentName(getComponentBasename(), getSelectedComponentType()));

        boolean exists = this.checkIfFileExists(mainFileName.getText()) && this.checkIfFileExists(testFileName.getText());

        if (exists) {
            warningText.setText("File already exists!");
            warningLabel.setVisible(true);
            warningText.setVisible(true);
        } else {
            warningText.setText("");
            warningLabel.setVisible(false);
            warningText.setVisible(false);
        }

        isFileNameAvailable = !exists;
        updateOkButtonState();
    }

    //region helpers
    private String getComponentBasename() {
        return componentBaseName.getText().trim();
    }

    private String getSelectedComponentType() {
        return (String) componentTypeComboBox.getSelectedItem();
    }

    private void updateOkButtonState() {
        buttonOK.setEnabled(isModuleNameAvailable && isFileNameAvailable);
    }

    private void onOK() {
        ContentBuilder builder = ServiceManager.getService(ContentBuilder.class);
        ConfigurationLoader configurationLoader = ServiceManager.getService(ConfigurationLoader.class);

        try {
            String moduleName = (String) moduleNameComboBox.getSelectedItem();

            configurationLoader.persistModuleName(moduleName);
            ArrayList<String> createdFiles = builder.createAngularComponentsHandler(currentPath, getSelectedComponentType(), moduleName, getComponentBasename());

            for (String item : createdFiles) {
                Commons.openFileInEditor(currentProject, item);
            }

            refreshProjectTreeView();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Unknown Error", "Please report in case you think that this is a bug.");
        }
        dispose();
    }

    private void refreshProjectTreeView() {
        VirtualFile fileByIoFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(currentPath));
        if (fileByIoFile != null) {
            //open the file after writing content to it.
            fileByIoFile.refresh(false, true);
            fileByIoFile.getFileSystem().refresh(false);
        }
    }

    //endregion

    private void onCancel() {
        dispose();
    }

    public void showDialog() {
        warningLabel.setVisible(false);
        warningText.setVisible(false);

        this.componentBaseName.grabFocus();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Event Handler to handle key released event for the auto complete box.
     */
    private class KeyEventHandler extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            JTextComponent editor = ((JTextComponent) moduleNameComboBox.getEditor().getEditorComponent());
            if (editor.getText().equals("")) {
                isModuleNameAvailable = false;
                updateOkButtonState();
            } else {
                isModuleNameAvailable = true;
                updateOkButtonState();
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                moduleNameComboBox.setPopupVisible(false);
            } else if (String.valueOf(e.getKeyChar()).matches("\\w+")) {
                moduleNameComboBox.hidePopup();
                String input = editor.getText();

                List<String> matchingItems = new ArrayList<>();
                for (String item : allItems) {
                    if (item.toLowerCase().startsWith(input)) {
                        matchingItems.add(item);
                    }
                }

                if (!matchingItems.isEmpty()) {
                    moduleNameComboBox.removeAllItems();
                    Collections.sort(matchingItems);
                    for (String item : matchingItems) {
                        moduleNameComboBox.addItem(item);
                    }
                    moduleNameComboBox.setSelectedItem(moduleNameComboBox.getItemAt(0));
                    editor.setText(input
                            + ((String) moduleNameComboBox.getItemAt(0))
                            .substring(input.length()));
                    editor.setCaretPosition(input.length());
                    editor.setSelectionStart(input.length());
                    editor.setSelectionEnd(moduleNameComboBox.getSelectedItem().toString().length());
                }
                //if nothing matching display complete list
                else {
                    moduleNameComboBox.removeAllItems();
                    for (String item : allItems) {
                        moduleNameComboBox.addItem(item);
                    }
                    editor.setText(input);
                }
                moduleNameComboBox.showPopup();
            }
        }
    }


    /**
     * Checks if the main JS file and test file exists
     *
     * @param fileName the filename
     * @return true if the templates don't exist, otherwise false
     */
    public boolean checkIfFileExists(String fileName) {
        return FileHandler.fileExists(currentPath + "/" + fileName);
    }

    public void openFileInEditor(String filename) {
        Commons.openFileInEditor(currentProject, currentPath + "/" + filename);
    }

    /**
     * Displays the error dialog
     *
     * @param title       the title of the error
     * @param description the description of the error
     */
    private void showError(String title, String description) {
        new ErrorDialog(title, description).display();
    }

}
