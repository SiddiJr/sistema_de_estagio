package sistemadeestagio;

import javafx.application.Platform;
import sistemadeestagio.Model.Aluno;
import sistemadeestagio.Model.alunosSingleton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import sistemadeestagio.Model.pendenciaSingleton;
import sistemadeestagio.Model.userNameSingleton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TelaPrincipalController implements Initializable {
    private List<Aluno> listaAlunos;
    @FXML
    private Label praeNome;
    @FXML
    private VBox rootBox;
    @FXML
    private TextField filtrarField;
    @FXML
    private ChoiceBox opcoes;
    @FXML
    private TableView<Aluno> tabela;
    @FXML
    private TableColumn<Aluno, String> numeroSEICol;
    @FXML
    private TableColumn<Aluno, String> nomeCol;
    @FXML
    private TableColumn<Aluno, String> emailCol;
    @FXML
    private TableColumn<Aluno, LocalDate> TCECol;
    @FXML
    private TableColumn<Aluno, LocalDate> terminoCol;
    @FXML
    private TableColumn<Aluno, String> tipoCol;
    @FXML
    private TableColumn<Aluno, LocalDate> termoAditivoCol;
    @FXML
    private TableColumn<Aluno, String> pendenciaCol;
    @FXML
    private TableColumn<Aluno, String> statusCol;
    private List<Aluno> alunosPendente;
    private View view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pendenciaSingleton pendenciaSingleton = sistemadeestagio.Model.pendenciaSingleton.getInstance();
        alunosPendente = pendenciaSingleton.getAlunos();

        userNameSingleton nameSingleton = userNameSingleton.getInstance();
        praeNome.setText("PRAE: " + nameSingleton.getUsername());

        try {
            this.view = new View();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        //alunosPendente = pendenciaSingleton.getInstance().getAlunos();
        this.listaAlunos = alunosSingleton.getInstance().getAlunos();

        //Tira foco do textField do filtrar
        rootBox.setOnMouseClicked(event -> {
            if (filtrarField.isFocused()) {
                rootBox.requestFocus();
            }
        });

        //usa o método filtrar quando enter é apertado
        filtrarField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                filtrar();
            }
        });

        nomeCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNome()));
        numeroSEICol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNumeroSEI()));
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TCECol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTCE()));
        tipoCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTipo()));
        terminoCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTermino()));
        termoAditivoCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTermoAditivo()));
        statusCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getStatus()));
        pendenciaCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPendencia()));

        //alinhamento das células
        setCellAlignmentString(nomeCol);
        setCellAlignmentString(emailCol);
        setCellAlignmentString(statusCol);

        numeroSEICol.setCellFactory(col -> {
            return new TableCell<Aluno, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item));
                    }
                    setTextAlignment(TextAlignment.CENTER);
                    setStyle("-fx-alignment: center;");
                }
            };
        });

        //colocar o formato da data dd/MM/yyyy
        setCellDateFormat(terminoCol);
        setCellDateFormat(termoAditivoCol);
        setCellDateFormat(TCECol);

        tabela.setItems(FXCollections.observableList(listaAlunos));

        //coloca cor nas linhas dependendo das pendencias
        pendenciaCol.setCellFactory(col -> {
            return new TableCell<Aluno, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    TableRow<Aluno> linhaAtual = getTableRow();
                    Aluno aluno = linhaAtual != null ? linhaAtual.getItem() : null;

                    if (empty || item == null || aluno == null) {
                        setText(null);
                        setStyle("");

                        if (linhaAtual != null) {
                            linhaAtual.setStyle(""); // Reset the row style for empty rows
                        }
                    } else {
                        setText(item);
                        setTextAlignment(TextAlignment.CENTER);
                        setStyle("-fx-alignment: center;");

                        if (!isEmpty()) {
                            if (!aluno.getPendencia().equals("Nenhuma")) {
                                linhaAtual.setStyle("-fx-background-color: #f1c21b; -fx-selection-bar: #f1c21b; -fx-selection-bar-non-focused: #f1c21b; -fx-font-size: 14px;");
                            } else {
                                linhaAtual.setStyle("-fx-background-color: #29D15B; -fx-selection-bar: #29D15B; -fx-selection-bar-non-focused: #29D15B; -fx-font-size: 14px;");
                            }

                            if (aluno.getStatus().equals("Encerrado")) {
                                linhaAtual.setStyle("-fx-text-fill: white; -fx-background-color: #ee0000; -fx-font-size: 14px;");
                            }
                        } else {
                            linhaAtual.setStyle("");
                        }
                    }
                }
            };
        });

        //adiciona tooltip quando passa o mouse sobre o tipo
        tipoCol.setCellFactory(column -> {
            return new TableCell<Aluno, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        setTextAlignment(TextAlignment.CENTER);
                        setStyle("-fx-alignment: center;");

                        Aluno aluno = getTableRow().getItem();

                        if (aluno != null && aluno.getDataObrigatorio() != null) {
                            Tooltip tooltip = new Tooltip("Tornou obrigatório em: " + aluno.getDataObrigatorio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tooltip.setStyle("-fx-font-size: 12px;");
                            setTooltip(tooltip);
                        }
                    }
                }
            };
        });

        //cria um menu context quando clica com o botão direito em uma lina
        tabela.setRowFactory(new Callback<TableView<Aluno>, TableRow<Aluno>>() {
            @Override
            public TableRow<Aluno> call(TableView<Aluno> tableView) {
                TableRow<Aluno> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem removerMenuItem = new MenuItem("Remover");
                MenuItem maisInfoMenuItem = new MenuItem("Mais Informações");

                //item "mais informações" do context menu
                maisInfoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //muda de tela quando é selecionado "mais informações"
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MaisInformacoes.fxml"));
                        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                        try {
                            Parent root = fxmlLoader.load();
                            MaisInformacoes maisInformacoes = fxmlLoader.getController();
                            maisInformacoes.setAluno(row.getItem().getNumeroSEI());
                            Stage stage = (Stage)row.getScene().getWindow();
                            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

                //item "remover" do context menu
                removerMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            deletarAluno(row.getItem());
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                //adiciona itens no context menu
                contextMenu.getItems().add(maisInfoMenuItem);
                contextMenu.getItems().add(removerMenuItem);

                //cria context menu apenas em linhas não vazias
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    private void setCellDateFormat(TableColumn<Aluno, LocalDate> column) {
        column.setCellFactory(col -> new TableCell<Aluno, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }

                setTextAlignment(TextAlignment.CENTER);
                setStyle("-fx-alignment: center;");
            }
        });
    }

    private void setCellAlignmentString(TableColumn<Aluno, String> column) {
        column.setCellFactory(col -> new TableCell<Aluno, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
                setTextAlignment(TextAlignment.CENTER);
                setStyle("-fx-alignment: center;");
            }
        });
    }

    public void deslogar(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação - Sair");
        //alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Deseja sair?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Autenticacao.fxml")));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            stage.setScene(scene);
            stage.show();
        }
    }

    //filtra elementos para pegar alunos especificos
    public void filtrar() {
        String choiceBox = (String) opcoes.getValue();
        String filtrarValue = filtrarField.getText();

        ObservableList<Aluno> listaFiltrada = FXCollections.observableArrayList();

        if (filtrarValue.isEmpty()) {
            listaFiltrada.addAll(listaAlunos);
        } else {
            for (Aluno item : listaAlunos) {
                if (choiceBox.equals("Nome") && item.getNome().trim().equalsIgnoreCase(filtrarValue)) {
                    listaFiltrada.add(item);
                } else if (choiceBox.equals("NumeroSEI") && item.getNumeroSEI().equals(filtrarValue)) {
                    listaFiltrada.add(item);
                } else if (choiceBox.equals("Empresa") && item.getEmpresa().trim().equalsIgnoreCase(filtrarValue)) {
                    listaFiltrada.add(item);
                } else if (choiceBox.equals("Professor Orientador") && item.getNomeOrientador().trim().equalsIgnoreCase(filtrarValue)) {
                    listaFiltrada.add(item);
                }
            }
        }

        tabela.setItems(listaFiltrada);
        filtrarField.clear();
    }

    public void deletarAluno(Aluno aluno) throws SQLException, ClassNotFoundException {
        Iterator<Aluno> it = this.listaAlunos.iterator();

        while (it.hasNext()) {
            Aluno next = it.next();
            if (next.getRA().equals(aluno.getRA())) {
                this.view.deleteSQL(aluno.getRA());
                it.remove();
                tabela.getItems().remove(aluno);
                tabela.refresh();
                break;
            }
        }

        alunosSingleton.getInstance().setAlunos(listaAlunos);
    }

    public void enviarEmailPendencia() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                int i = 0;
                HashMap<String, String> pendentes = new HashMap<>();
                if(alunosPendente != null) {
                    for (Aluno aluno : alunosPendente) {
                        view.enviaEmail(aluno.getEmail(), aluno.getNome(),
                                aluno.getNomeOrientador(), aluno.getEmailOrientador());
                        i++;
                    }
                }

                int emailCount = i;

                Platform.runLater(() -> {
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setContentText(String.format("E-mail enviado para %d alunos com pendências.", emailCount));
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.show();
                });

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void TelaNovoAluno(ActionEvent event) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("NovoAluno.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}