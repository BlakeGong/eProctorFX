package eproctor_v1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author CLY
 */
public class FrameFormController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView settingImageView;
    @FXML
    private ImageView aboutImageView;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private Pane contentPane;

    private Stage selfStage;
    private AnchorPane studentView, settingView;
    private StackPane aboutView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void openStudentForm() {
        if (studentView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentForm.fxml"));
            try {
                studentView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        contentPane.getChildren().setAll(studentView);
    }

    @FXML
    private void openSettingForm() throws Exception {
        if (settingView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingForm.fxml"));
            try {
                settingView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            SettingFormController settingFormController = loader.getController();
        }
        contentPane.getChildren().setAll(settingView);
    }

    @FXML
    private void openAboutForm() {
        if (aboutView == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutFormStackPane.fxml"));
            try {
                aboutView = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            AboutFormController aboutFormController = loader.getController();
            aboutFormController.setFrameFormController(this);
            aboutFormController.setBackground();
        }
        contentPane.getChildren().setAll(aboutView);
    }

    @FXML
    private void logout() throws Exception {
        System.out.println("logout");
        selfStage.close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
        Parent root = (Parent) loader.load();
        LoginFormController controller = (LoginFormController) loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setTitle("eProctor");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void setBackground() {
        mainPane.setStyle("-fx-background-image: url(\"/images/studentHome.png\");"); // not working
        System.out.println("mainPane style: " + mainPane.getStyle());
    }

    public void setSelfStage(Stage selfStage) {
        this.selfStage = selfStage;
    }
}
