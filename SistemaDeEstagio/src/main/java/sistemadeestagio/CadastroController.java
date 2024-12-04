package sistemadeestagio;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {
    @FXML
    private TextField nomeCampo;
    @FXML
    private TextField emailCampo;
    @FXML
    private TextField usuarioCampo;
    @FXML
    private TextField senhaCampo;
    @FXML
    private VBox rootBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> rootBox.requestFocus());

        rootBox.setOnMouseClicked(event -> { rootBox.requestFocus(); });

        nomeCampo.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    cadastrar();
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        emailCampo.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    cadastrar();
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        usuarioCampo.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    cadastrar();
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        senhaCampo.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                try {
                    cadastrar();
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void cadastrar() throws SQLException, ClassNotFoundException, IOException {
        View view = new View();
        Dialog<String> dialog = new Dialog<>();
        String nome = nomeCampo.getText();
        String email = emailCampo.getText();
        String usuario = usuarioCampo.getText();
        String senha = senhaCampo.getText();

        if(!nome.isEmpty() && !email.isEmpty() && !usuario.isEmpty() && !senha.isEmpty()) {
            view.insertUsuario(nome, email, usuario, senha);
            dialog.setTitle("Cadastrado com sucesso!");
            dialog.setContentText("PRAE cadastrado com sucesso!");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
            irAutenticacao();
        } else {
            dialog.setTitle("Erro no cadastro!");
            dialog.setContentText("Por favor, preencha todos os campos!");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }
    }

    public void irAutenticacao() throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Autenticacao.fxml")));
        Stage stage = (Stage)rootBox.getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
