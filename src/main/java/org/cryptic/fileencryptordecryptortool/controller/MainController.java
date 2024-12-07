package org.cryptic.fileencryptordecryptortool.controller;

import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.StringUtils;
import org.cryptic.fileencryptordecryptortool.model.Algorithm;
import org.cryptic.fileencryptordecryptortool.ui.controls.buttons.FocusedButton;
import org.cryptic.fileencryptordecryptortool.utils.AlgorithmJAXBReader;
import org.cryptic.fileencryptordecryptortool.utils.Encryptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;

public class MainController implements Initializable {
    @FXML
    private TextField sourceFilePathTextField;
    @FXML
    private TextField destinationFilePathTextField;
    @FXML
    private FocusedButton destinationDirectoryOpenFocusedButton;
    @FXML
    private CheckBox recursiveReplaceCheckBox;
    @FXML
    private TextField filtersTextField;
    @FXML
    private ComboBox<Algorithm> encryptionAlgorithmComboBox;
    @FXML
    private TextArea outputTextArea;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEncryptionAlgorithm();
    }

    @FXML
    protected void handleOpenSourceDirectory() {
        browseDirectory(sourceFilePathTextField);
    }

    @FXML
    protected void handleOpenDestDirectory() {
        browseDirectory(destinationFilePathTextField);
    }

    @FXML
    protected void handleReplaceAction() {
        if (recursiveReplaceCheckBox.isSelected()) {
            destinationFilePathTextField.setDisable(true);
            destinationDirectoryOpenFocusedButton.setDisable(true);
        } else {
            destinationFilePathTextField.setDisable(false);
            destinationDirectoryOpenFocusedButton.setDisable(false);
        }
    }

    @FXML
    protected void handleEncryptionAlgorithmSelection(ActionEvent event) {
//        System.out.println;
//        optionChoose.getSelectionModel()

//        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @FXML
    protected void handleEncryptButtonClick() {
        encryptAndDecrypt(true);
    }


    @FXML
    protected void handleDecryptButtonClick() {
        encryptAndDecrypt(false);
    }

    private void encryptAndDecrypt(Boolean isEncryption) {
        Path destinationDir;
        if (recursiveReplaceCheckBox.isSelected()) {
            destinationDir = Path.of(sourceFilePathTextField.getText());
        } else {
            destinationDir = Path.of(destinationFilePathTextField.getText());
        }
        Path dirPath = Paths.get(sourceFilePathTextField.getText());
        String[] extensions = Arrays.stream(filtersTextField.getText().split(","))
                .map(ext -> "." + ext.trim())
                .toArray(String[]::new);
        try {
            Files.walk(dirPath)
                    .filter(path -> isValidExtension(path, extensions))
                    .forEach(path -> {
                        try {
                            String fileContent = Arrays.toString(Files.readAllBytes(path));
                            if (isEncryption) {
                                String encryptedContent = Encryptor.encrypt(fileContent, encryptionAlgorithmComboBox.getValue().getName());
                                Path destinationPath = destinationDir.resolve(path.getFileName());
                                Files.write(destinationPath, encryptedContent.getBytes());
                                System.out.println("Encrypted file written to: " + destinationPath);
                            } else {
                                String decryptedContent = Encryptor.decrypt(fileContent, encryptionAlgorithmComboBox.getValue().getName());
                                Path destinationPath = destinationDir.resolve(path.getFileName());
                                Files.write(destinationPath, decryptedContent.getBytes());
                                System.out.println("Decrypted file written to: " + destinationPath);
                            }

                        } catch (IOException e) {
                            System.err.println("Error processing file: " + path + ", " + e.getMessage());
                        } catch (JAXBException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }

    private static boolean isValidExtension(Path path, String[] extensions) {
        String fileName = path.getFileName().toString().toLowerCase();
        return Arrays.stream(extensions)
                .anyMatch(fileName::endsWith);
    }

    private void setupEncryptionAlgorithm() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("algorithms.xml");
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found in resources: algorithms.xml");
        }
        AlgorithmJAXBReader reader = new AlgorithmJAXBReader();
        List<Algorithm> algorithms = reader.readAlgorithms(inputStream);
        if (algorithms != null) {
            encryptionAlgorithmComboBox.setItems(FXCollections.observableArrayList(algorithms));
        }
        if (encryptionAlgorithmComboBox.getItems().size() > 1) {
            encryptionAlgorithmComboBox.getSelectionModel().select(encryptionAlgorithmComboBox.getItems().size() - 1);
        }
    }

    private void browseDirectory(TextField textField) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        if (StringUtils.isNotBlank(textField.getText())) {
            try {
                Path directory = Paths.get(textField.getText());
                if (Files.exists(directory)) {
                    directoryChooser.setInitialDirectory(directory.toFile());
                }
            } catch (Exception ignore) {
                // Ignore
            }
        }
        File selectedDirectory = directoryChooser.showDialog(textField.getScene().getWindow());
        if (selectedDirectory != null) {
            textField.setText(selectedDirectory.getAbsolutePath());
        }
    }
}