package sistemadeestagio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sistemadeestagio.Model.Aluno;
import sistemadeestagio.Model.alunosSingleton;
import sistemadeestagio.Model.pendenciaSingleton;
import sistemadeestagio.Model.userNameSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class NovoAlunoController implements Initializable {
    private View view;
    @FXML
    private TextField raCampo;
    @FXML
    private TextField numeroSEICampo;
    @FXML
    private TextField nomeCampo;
    @FXML
    private TextField emailCampo;
    @FXML
    private TextField empresaCampo;
    @FXML
    private TextField orientadorCampo;
    @FXML
    private ChoiceBox tipoCampo;
    @FXML
    private DatePicker tceCampo;
    @FXML
    private DatePicker terminoCampo;
    @FXML
    private TextField cargaHorariaCampo;
    @FXML
    private Label praeNome;
    @FXML
    private TextField supervisorCampo;
    @FXML
    private TextField emailSupervisorCampo;
    @FXML
    private TextField emailOrientadorCampo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameSingleton nameSingleton = userNameSingleton.getInstance();
        praeNome.setText("PRAE: " + nameSingleton.getUsername());

        try {
            this.view = new View();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cargaHorariaCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputDigit);
        numeroSEICampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputDigit);

        nomeCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputAlpha);
        orientadorCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputAlpha);

        tceCampo.setEditable(false);
        terminoCampo.setEditable(false);
    }

    public void obterInformacoes() throws SQLException {
        Dialog<String> dialog = new Dialog<>();
        String numeroSEI = numeroSEICampo.getText();
        String nome = nomeCampo.getText();
        String email = emailCampo.getText();
        String empresa = empresaCampo.getText();
        String orientador = orientadorCampo.getText();
        String emailOrientador = emailOrientadorCampo.getText();
        String supervisor = supervisorCampo.getText();
        String supervisorEmail = emailSupervisorCampo.getText();
        String tipo = (String) tipoCampo.getValue();
        String cargaHoraria = cargaHorariaCampo.getText();
        LocalDate dateTCE = tceCampo.getValue();
        LocalDate dateTemino = terminoCampo.getValue();
        String RA = raCampo.getText();

        if(!numeroSEI.isEmpty() && !nome.isEmpty() && !email.isEmpty() && !empresa.isEmpty() &&
                !orientador.isEmpty() && !tipo.isEmpty() && dateTCE != null && dateTemino != null &&
                !RA.isEmpty() && !cargaHoraria.isEmpty()) {
            if(dateTCE.isBefore(dateTemino)) {
                Aluno aluno = new Aluno();
                alunosSingleton alunosSing = alunosSingleton.getInstance();
                pendenciaSingleton pendSingleton = pendenciaSingleton.getInstance();
                List<Aluno> pendencias = pendSingleton.getAlunos();
                aluno.setRA(RA);
                aluno.setNome(nome);
                aluno.setNumeroSEI(numeroSEI);
                aluno.setEmail(email);
                aluno.setEmpresa(empresa);
                aluno.setTCE(dateTCE);
                aluno.setTermino(dateTemino);
                aluno.setTipo(tipo);
                aluno.setNomeOrientador(orientador);
                aluno.setEmailOrientador(emailOrientador);
                aluno.setNomeSupervisor(supervisor);
                aluno.setEmailSupervisor(supervisorEmail);
                aluno.setCargaHoraria(Integer.parseInt(cargaHoraria));
                if(tipo.equals("Obrigatório")) aluno.setDataObrigatorio(LocalDate.now());
                LocalDate parcial1 = dateTCE.plusMonths(6);
                LocalDate parcial2 = dateTCE.plusMonths(12);
                LocalDate parcial3 = dateTCE.plusMonths(18);
                LocalDate parcial4 = dateTCE.plusMonths(24);

                String pendencia = this.view.checkPendencia(parcial1, parcial2, parcial3, parcial4, aluno.isRelatorio1(),
                        aluno.isRelatorio2(), aluno.isRelatorio3(), aluno.isRelatorio4());

                aluno.setPendencia(pendencia);
                pendencias.add(aluno);
                pendSingleton.setAlunos(pendencias);

                List<Aluno> lista = alunosSing.getAlunos();
                lista.add(aluno);
                alunosSing.setAlunos(lista);

                this.view.insertSQL(RA, numeroSEI, nome, email, tipo, dateTCE, dateTemino, orientador, emailOrientador, supervisor, supervisorEmail, empresa, Integer.parseInt(cargaHoraria),parcial1, parcial2, parcial3, parcial4);

                dialog.setContentText("Aluno adicionado.");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
                numeroSEICampo.clear();
                nomeCampo.clear();
                orientadorCampo.clear();
                emailCampo.clear();
                empresaCampo.clear();
                tceCampo.getEditor().clear();
                terminoCampo.getEditor().clear();
                cargaHorariaCampo.clear();
                raCampo.clear();
            } else {
                dialog.setContentText("A data TCE deve ser antes da data de término.");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.show();
            }
        } else {
            dialog.setContentText("Todos os campos devem ser preenchidos!");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.show();
        }
    }

    public void VoltarTelaPrincipal(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("TelaPrincipal.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void deslogar(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação - Sair");
        //alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Deseja sair?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Autenticacao.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            stage.setScene(scene);
            stage.show();
        }
    }

    public void validarInputDigit(KeyEvent keyEvent) {
        String input = keyEvent.getCharacter();
        TextField textField = (TextField) keyEvent.getSource();
        String currentText = textField.getText();
        Node source = (Node) keyEvent.getSource();

        if (input.matches("\\d")) {
            long digitCount = currentText.chars().filter(Character::isDigit).count();

            if (Objects.equals(source.getId(), "numeroSEICampo") && digitCount >= 17) {
                keyEvent.consume();
            } else if (Objects.equals(source.getId(), "cargaHorariaCampo") && digitCount >= 3) {
                keyEvent.consume();
            }
        } else {
            keyEvent.consume();
        }
    }

    public void validarInputAlpha(KeyEvent keyEvent) {
        String input = keyEvent.getCharacter();
        if (!input.matches("[a-zA-Z ]")) {
            keyEvent.consume();
        }
    }
}
