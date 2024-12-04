package sistemadeestagio;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class autenticacaoController implements Initializable {

    public PasswordField senhaLabel;
    public TextField usuarioLabel;
    public Button entrarButton;
    @FXML
    private VBox rootBox;
    private View view;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> rootBox.requestFocus());

        rootBox.setOnMouseClicked(event -> { rootBox.requestFocus(); });

        usuarioLabel.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    checarLogin();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        senhaLabel.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    checarLogin();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            this.view = new View();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        rootBox.setOnMouseClicked(event -> {
            if (usuarioLabel.isFocused() || senhaLabel.isFocused()) {
                rootBox.requestFocus();
            }
        });
    }

    public void checarLogin() throws SQLException, IOException {
        Dialog<String> dialog = new Dialog<>();
        String usuario = usuarioLabel.getText();
        String senha = senhaLabel.getText();

        if (!usuario.isEmpty() && !senha.isEmpty()) {
            if (view.searchUser(usuario, senha)) {
                view.setSingletons();
                irTelaPrincipal();
            } else {
                dialog.setContentText("Senha ou usuário incorreto!");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
            }
        } else {
            dialog.setContentText("Preencha usuário e senha!");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }

    public void irTelaPrincipal() throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("TelaPrincipal.fxml")));
        Stage stage = (Stage)rootBox.getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void irCadastro(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Cadastro.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
