package eproctor_v1;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import jfx.messagebox.MessageBox;

/**
 * This class is FXML Controller class. It consists exclusively of methods that
 * operate on LoginUI or return java.lang.Object. It contains polymorphic
 * algorithm that operate on collections.
 * <p>
 * The methods of this class all throw a <tt>NullPointerException</tt>
 * if the collections or class objects provided to them are null.
 *
 * @author Gong Yue
 * @author Chen Liyang
 */
public class LoginFormController implements Initializable {

    private Stage selfStage;

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    ChoiceBox choiceType;
    @FXML
    private Button buttonLogin;
    @FXML
    private Font x1;
    @FXML
    private Button buttonExit;

    @FXML
    ProgressBar bar;

    /**
     * This method handle the login event.
     * <p>
     * If login is successful then UI will switch to another.</p>
     * <p>
     * else, message box will be filled with certain information.</p>
     *
     * @param event the event to be handle
     * @throws Exception if overwrite error
     * @see LoginForm.fxml
     */
    @FXML
    private void login(ActionEvent event) throws Exception {
        buttonLogin.setDisable(true);
        
        SimpleDoubleProperty progress0 = new SimpleDoubleProperty(0);
        SimpleDoubleProperty progress1 = new SimpleDoubleProperty(0);
        SimpleDoubleProperty progress2 = new SimpleDoubleProperty(0);

        Task<Void> progressTask = new Task<Void>() {

            @Override
            protected void succeeded() {
                super.succeeded();
                try {
                    openStudentForm();
                } catch (Exception ex) {
                    Logger.getLogger(LoginFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            protected Void call() throws Exception {
                if (DatabaseInterface.isUser(choiceType.getValue().toString(), username.getText(), getMD5(password.getText(), true))) { 
                    progress0.set(1);
                    DatabaseInterface.updateLocalRecordData(progress1);
                    DatabaseInterface.updateLocalCourseData(progress2);
                } else {
                    MessageBox.show(selfStage,
                            "The username / password you entered is incorrect.\nPlease try again.",
                            "Please re-enter your username / password",
                            MessageBox.ICON_INFORMATION);
                    buttonLogin.setDisable(false);
                }

                return null;
            }
        };

        bar.progressProperty().bind(progress0.multiply(0.1).add(progress1.multiply(0.45).add(progress2.multiply(0.45))));
        new Thread(progressTask).start();
    }

    /**
     * This method handle the exit operation.
     * <p>
     * destroy objects which contains user or exam information.</p>
     * <p>
     * close UI and close the software.</p>
     *
     * @param event
     */
    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    private double dragInitialX, dragInitialY;

    /**
     * This method handles mouse click/press event
     *
     * @param me short for MouseEvent
     */
    @FXML
    private void mousePressedHandler(MouseEvent me) {
        if (me.getButton() != MouseButton.MIDDLE) {
            dragInitialX = me.getSceneX();
            dragInitialY = me.getSceneY();
        }
    }

    /**
     * This method handles mouse dragge event
     *
     * @param me short for MouseEvent
     */
    @FXML
    private void mouseDraggedHandler(MouseEvent me) {
        if (me.getButton() != MouseButton.MIDDLE) {
            selfStage.setX(me.getScreenX() - dragInitialX);
            selfStage.setY(me.getScreenY() - dragInitialY);
        }
    }

    /**
     * This method initialize url.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * This method set selfStage to stage.
     *
     * @param stage Stage
     */
    public void setStage(Stage stage) {
        selfStage = stage;
    }

    /**
     * This method open StudentForm by initializing a Stage and
     * StudentFormController.
     * <p>
     * Show student home UI after initialization finished.</p>
     *
     *
     * @throws IOException
     */
    private void openStudentForm() throws Exception {
        selfStage.close();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentForm.fxml"));
        Parent root = (Parent) loader.load();
        StudentFormController controller = (StudentFormController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setTitle("eProctor Student Client");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        controller.setStudentScene(scene);
    }

    /**
     * This method implements MD5 generating algorithm.
     *
     * @param input String for user password
     * @param getHex boolean
     * @return MD5 MD5 string respect to input String
     * @throws NoSuchAlgorithmException throws
     */
    public static String getMD5(String input, boolean getHex)
            throws NoSuchAlgorithmException {
        MessageDigest md;
        byte[] bytesOfMessage = input.getBytes(StandardCharsets.UTF_8);
        md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        if (!getHex) {
            return new String(thedigest, StandardCharsets.UTF_8);
        } else {
            return bytesToHex(thedigest);
        }
    }

    /**
     *
     * @param bytes desired bytes
     * @return hexChars the re-formated bytes as a String
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
