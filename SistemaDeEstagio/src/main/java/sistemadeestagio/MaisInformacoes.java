package sistemadeestagio;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import sistemadeestagio.Model.Aluno;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sistemadeestagio.Model.alunosSingleton;
import sistemadeestagio.Model.pendenciaSingleton;
import sistemadeestagio.Model.userNameSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MaisInformacoes implements Initializable {
    public Aluno aluno;
    public String seiAnterior;
    private View view;
    @FXML
    private DatePicker aditivoCampo;
    @FXML
    private TextField nomeCampo;
    @FXML
    private TextField numeroSEICampo;
    @FXML
    private TextField emailCampo;
    @FXML
    private TextField empresaCampo;
    @FXML
    private TextField nomeOrientadorCampo;
    @FXML
    private ChoiceBox<String> tipoCampo;
    @FXML
    private DatePicker tceCampo;
    @FXML
    private DatePicker terminoCampo;
    @FXML
    private CheckBox relatorio1;
    @FXML
    private CheckBox relatorio2;
    @FXML
    private CheckBox relatorio3;
    @FXML
    private CheckBox relatorio4;
    @FXML
    private ChoiceBox<String> statusCampo;
    @FXML
    private TextField cargaHorariaCampo;
    @FXML
    private TextField raCampo;
    @FXML
    private Label praeCampo;
    private boolean relatorioChange1 = false;
    private boolean relatorioChange2 = false;
    private boolean relatorioChange3 = false;
    private boolean relatorioChange4 = false;
    @FXML
    private TextField supervisorCampo;
    @FXML
    private TextField emailSupervisorCampo;
    @FXML
    private TextField emailOrientadorCampo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Roda depois de setAlunos ser usado
        Platform.runLater(() -> {
            userNameSingleton nameSingleton = userNameSingleton.getInstance();
            praeCampo.setText("PRAE: " + nameSingleton.getUsername());

            try {
                this.view = new View();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            seiAnterior = aluno.getNumeroSEI();
            nomeCampo.setText(aluno.getNome());
            numeroSEICampo.setText(String.valueOf(aluno.getNumeroSEI()));
            emailCampo.setText(aluno.getEmail());
            nomeOrientadorCampo.setText(aluno.getNomeOrientador());
            emailOrientadorCampo.setText(aluno.getEmailOrientador());
            supervisorCampo.setText(aluno.getNomeSupervisor());
            emailSupervisorCampo.setText(aluno.getEmailSupervisor());
            empresaCampo.setText(aluno.getEmpresa());
            tipoCampo.setValue(aluno.getTipo());
            tceCampo.setValue(aluno.getTCE());
            terminoCampo.setValue(aluno.getTermino());
            statusCampo.setValue(aluno.getStatus());
            cargaHorariaCampo.setText(String.valueOf(aluno.getCargaHoraria()));
            raCampo.setText(aluno.getRA());
            cargaHorariaCampo.setText(String.valueOf(aluno.getCargaHoraria()));

            if(aluno.getTermoAditivo() != null) {
                aditivoCampo.setValue(aluno.getTermoAditivo());
            }

            relatorio1.selectedProperty().setValue(aluno.isRelatorio1());
            relatorio2.selectedProperty().setValue(aluno.isRelatorio2());
            relatorio3.selectedProperty().setValue(aluno.isRelatorio3());
            relatorio4.selectedProperty().setValue(aluno.isRelatorio4());

            relatorio1.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    relatorioChange1 = true;
                }
            });
            relatorio2.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    relatorioChange2 = true;
                }
            });
            relatorio3.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    relatorioChange3 = true;
                }
            });
            relatorio4.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    relatorioChange4 = true;
                }
            });

            cargaHorariaCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputDigit);
            numeroSEICampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputDigit);

            nomeCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputAlpha);
            nomeOrientadorCampo.addEventFilter(KeyEvent.KEY_TYPED, this::validarInputAlpha);
        });
    }

    public void atualizar() throws SQLException {
        alunosSingleton alunos = alunosSingleton.getInstance();
        pendenciaSingleton pendSingleton = pendenciaSingleton.getInstance();
        List<Aluno> listaPendencias = pendSingleton.getAlunos();
        List<Aluno> lista = alunos.getAlunos();
        Iterator<Aluno> it = lista.iterator();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTCE = tceCampo.getValue();
        LocalDate dateTemino = terminoCampo.getValue();
        String supervisor = supervisorCampo.getText();
        String emailSupervisor = emailSupervisorCampo.getText();
        String emailOrientador = emailOrientadorCampo.getText();

        while (it.hasNext()) {
            Aluno alunoit = it.next();
            if (alunoit.getNumeroSEI().equals(seiAnterior)) {
                if (alunoit.getDataObrigatorio() == null && tipoCampo.getValue().equals("Obrigatório"))
                    alunoit.setDataObrigatorio(LocalDate.now());

                alunoit.setRA(raCampo.getText());
                alunoit.setNome(nomeCampo.getText());
                alunoit.setNumeroSEI(numeroSEICampo.getText());
                alunoit.setEmail(emailCampo.getText());
                alunoit.setEmpresa(empresaCampo.getText());
                alunoit.setTCE(dateTCE);
                alunoit.setTermino(dateTemino);
                alunoit.setTipo(tipoCampo.getValue());
                alunoit.setNomeOrientador(nomeOrientadorCampo.getText());

                alunoit.setStatus(statusCampo.getValue());
                alunoit.setRelatorio1(relatorio1.isSelected());
                alunoit.setRelatorio2(relatorio2.isSelected());
                alunoit.setRelatorio3(relatorio3.isSelected());
                alunoit.setRelatorio4(relatorio4.isSelected());
                alunoit.setCargaHoraria(Integer.parseInt(cargaHorariaCampo.getText()));

                if(aditivoCampo.getValue() != null) {
                    alunoit.setTermoAditivo(aditivoCampo.getValue());
                }

                alunos.setAlunos(lista);

                break;
            }
        }

        if(relatorioChange1 || relatorioChange2 || relatorioChange3 || relatorioChange4) {
            listaPendencias.removeIf(aluno1 -> Objects.equals(aluno1.getNumeroSEI(), numeroSEICampo.getText()));
            for (Aluno aluno : lista) {
                if (aluno.getNumeroSEI().equals(numeroSEICampo.getText())) {
                    aluno.setRelatorio1(relatorio1.isSelected());
                    aluno.setRelatorio2(relatorio2.isSelected());
                    aluno.setRelatorio3(relatorio3.isSelected());
                    aluno.setRelatorio4(relatorio4.isSelected());

                    String pendencia = this.view.checkPendencia(aluno.getParcial1(), aluno.getParcial2(),
                            aluno.getParcial3(), aluno.getParcial4(), aluno.isRelatorio1(), aluno.isRelatorio2(),
                            aluno.isRelatorio3(), aluno.isRelatorio4());
                    if(!pendencia.equals("Nenhuma")) {
                        aluno.setPendencia(pendencia);
                        listaPendencias.add(aluno);
                        pendSingleton.setAlunos(listaPendencias);
                    } else {
                        aluno.setPendencia(pendencia);
                    }
                }
            }



            alunos.setAlunos(lista);
            pendSingleton.setAlunos(listaPendencias);
        }

        this.view.updateSQL(raCampo.getText(), tipoCampo.getValue(), nomeOrientadorCampo.getText(),
                emailOrientador, supervisor, emailSupervisor,empresaCampo.getText(), aditivoCampo.getValue(),
                Integer.parseInt(cargaHorariaCampo.getText()), relatorio1.isSelected(),
                relatorio2.isSelected(), relatorio3.isSelected(), relatorio4.isSelected());

        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText("Aluno atualizado.");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    public void VoltarTelaPrincipal(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("TelaPrincipal.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void setAluno(String id) {
        alunosSingleton alunosSingleton = sistemadeestagio.Model.alunosSingleton.getInstance();
        List<Aluno> lista = alunosSingleton.getAlunos();

        for (Aluno aluno1 : lista) {
            if(aluno1.getNumeroSEI().equals(id)) {
                this.aluno = aluno1;
            }
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
}
