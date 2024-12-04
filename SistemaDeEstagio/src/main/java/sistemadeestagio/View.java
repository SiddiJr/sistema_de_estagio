package sistemadeestagio;

import sistemadeestagio.Model.Aluno;
import sistemadeestagio.Model.alunosSingleton;
import sistemadeestagio.Model.pendenciaSingleton;
import sistemadeestagio.Model.userNameSingleton;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class View {
    private Connection connection;
    private Statement statement;

    public View() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String userBD = ""; // Inserir o usuário do banco de dados mysql
        String passwordBD = ""; //Inserir a senha do banco de dados escolhido
        String urlBD = ""; // Inserir a url do banco de dados e o banco de dados a ser utilizado. Ex: "localhost/dainf"

        connection = DriverManager.getConnection("jdbc:mysql://" + urlBD, userBD, passwordBD);
        statement = connection.createStatement();
    }

    public void enviaEmail(String emailAluno, String nomeAluno,
                           String nomeOrientador, String emailOrientador) {
        String emailFrom = ""; // Inserir email que será usado para o envio do email.
        String senhaFrom = ""; // Inserir senha de aplicativo do email escolhido.
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", 587);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, senhaFrom);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailFrom, "PRAE"));

            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(emailOrientador, nomeOrientador));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAluno, nomeAluno));
            msg.setSubject("RELATÓRIOS PARCIAIS EM ATRASO");
            msg.setText("Prezados aluno e orientador,\n" +
                    "\n" +
                    "Os relatórios parciais de estágio estão em atraso. A periodicidade dos relatórios é de 6 em 6 meses a contar da data de início do estágio. Aditivos ao Termo de Compromisso de Estágio não modificam a data de envio destes. Também o Relatório Final de estágio obrigatório não dispensa a apresentação dos relatórios parciais.\n" +
                    "\n" +
                    "Faça download dos documentos atualizados no site da UTFPR. Estes devem ser assinados pelo estagiário e pelo seu supervisor na empresa e enviados no formato PDF pelo estagiário ao seu professor orientador com cópia para o PRAE (estagio-bsi-ct@utfpr.edu.br). O professor orientador assina somente depois no SEI (sistema eletrônico da UTFPR).\n" +
                    "\n" +
                    "1) Relatório Parcial de Estágio - Estagiário: https://www.utfpr.edu.br/estagios/imagens/modelos-de-documentos/relatorio-parcial-de-estagio_estagiario_docx\n" +
                    "\n" +
                    "2) Relatório Parcial de Estágio - Supervisor: https://www.utfpr.edu.br/estagios/imagens/modelos-de-documentos/relatorio-parcial-de-estagio_supervisor_docx\n" +
                    "\n" +
                    "Atenciosamente,\n" +
                    "Luiz Augusto Pelisson.\n" +
                    "PRAE BSI CT");
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean searchUser(String user, String password) throws SQLException {
        user = "'" + user + "'";
        password = "'" + password + "'";
        String selectSQL = "SELECT * FROM usuarios WHERE username=" + user + " and " + "senha = " + password + ";";
        ResultSet result = statement.executeQuery(selectSQL);

        boolean hasRow = result.isBeforeFirst();

        if(hasRow) {
            if(result.next()) {
                userNameSingleton nameSingleton = userNameSingleton.getInstance();
                nameSingleton.setUsername(result.getString("nome"));
            }
        }

        return hasRow;
    }

    public ResultSet getAlunos() throws SQLException {
        String selectSQL = "SELECT documentos.NomeSupervisor, documentos.EmailSupervisor, " +
                "documentos.EmailOrientador, documentos.cargaHoraria, documentos.empresa, " +
                "documentos.SEI, documentos.TCE, documentos.termino, documentos.tipo, " +
                "documentos.aditivo, documentos.relatorio1, documentos.relatorio2, documentos.relatorio3, " +
                "documentos.relatorio4, documentos.parcial1, documentos.parcial2, " +
                "documentos.parcial3, documentos.parcial4, documentos.orientador, " +
                "alunos.RA, alunos.Nome, alunos.Email " +
                "FROM documentos INNER JOIN alunos ON documentos.raAluno = alunos.RA";
        return statement.executeQuery(selectSQL);
    }

    public void updateSQL(String RA, String Tipo, String Orientador, String emailOrientador,
                          String nomeSupervisor, String emailSupervisor, String Empresa,
                          LocalDate termoAditivo, int cargaHoraria, boolean relatorio1,
                          boolean relatorio2, boolean relatorio3, boolean relatorio4) throws SQLException {
        RA = "'" + RA + "'";
        Tipo = "'" + Tipo + "'";
        Orientador = "'" + Orientador + "'";
        emailOrientador = "'" + emailOrientador + "'";
        Empresa = "'" + Empresa + "'";
        nomeSupervisor = "'" + nomeSupervisor + "'";
        emailSupervisor = "'" + emailSupervisor + "'";
        String documentosInsert;

        if(termoAditivo != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter.format(termoAditivo);
            String strAditivo = "'" + termoAditivo + "'";
            documentosInsert = String.format("UPDATE documentos SET tipo = %s, orientador = %s, EmailOrientador = %s, NomeSupervisor = %s, EmailSupervisor = %s, empresa = %s, aditivo = %s, cargaHoraria = %d, relatorio1 = %b, relatorio2 = %b, relatorio3 = %b, relatorio4 = %b WHERE raAluno = %s;", Tipo, Orientador, emailOrientador, nomeSupervisor, emailSupervisor, Empresa, strAditivo, cargaHoraria, relatorio1, relatorio2, relatorio3, relatorio4, RA);
        } else {
            documentosInsert = String.format("UPDATE documentos SET tipo = %s, orientador = %s, EmailOrientador = %s, NomeSupervisor = %s, EmailSupervisor = %s, empresa = %s, aditivo = %s, cargaHoraria = %d, relatorio1 = %b, relatorio2 = %b, relatorio3 = %b, relatorio4 = %b WHERE raAluno = %s;", Tipo, Orientador, emailOrientador, nomeSupervisor, emailSupervisor, Empresa, "NULL", cargaHoraria, relatorio1, relatorio2, relatorio3, relatorio4, RA);
        }
        statement.executeUpdate(documentosInsert);
    }

    public void insertSQL(String RA, String NumeroSEI,
                          String Nome, String email, String Tipo,
                          LocalDate TCE, LocalDate Termino, String Orientador, String emailOrientador,
                          String nomeSupervisor, String emailSupervisor, String Empresa, int cargaHoraria,
                          LocalDate parcial1, LocalDate parcial2, LocalDate parcial3, LocalDate parcial4) throws SQLException {
        RA = "'" + RA + "'";
        NumeroSEI = "'" + NumeroSEI + "'";
        Nome = "'" + Nome + "'";
        email = "'" + email + "'";
        Tipo = "'" + Tipo + "'";
        Orientador = "'" + Orientador + "'";
        emailOrientador = "'" + emailOrientador + "'";
        Empresa = "'" + Empresa + "'";
        nomeSupervisor = "'" + nomeSupervisor + "'";
        emailSupervisor = "'" + emailSupervisor + "'";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter.format(TCE);
        formatter.format(Termino);
        formatter.format(parcial1);
        formatter.format(parcial2);
        formatter.format(parcial3);
        formatter.format(parcial4);
        String strParcial1 = "'" + parcial1 + "'";
        String strParcial2 = "'" + parcial2 + "'";
        String strParcial3 = "'" + parcial3 + "'";
        String strParcial4 = "'" + parcial4 + "'";
        String strTCE = "'" + TCE + "'";
        String strTermino = "'" + Termino + "'";

        String alunosInsert = "INSERT INTO alunos (RA, Nome, Email) VALUES (" + RA + "," + Nome + "," + email + ");";
        String documentosInsert = String.format("INSERT INTO documentos (SEI, TCE, termino, tipo, parcial1, parcial2, parcial3, parcial4, raAluno, orientador, empresa, cargaHoraria, NomeSupervisor, EmailSupervisor, EmailOrientador) " +
                "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %d, %s, %s, %s);", NumeroSEI, strTCE, strTermino, Tipo, strParcial1, strParcial2, strParcial3, strParcial4, RA, Orientador, Empresa, cargaHoraria, nomeSupervisor, emailSupervisor, emailOrientador);

        statement.executeUpdate(alunosInsert);
        statement.executeUpdate(documentosInsert);
    }

    public void deleteSQL(String RA) throws SQLException {
        String deleteDocumentosQuery = "DELETE FROM documentos WHERE raAluno = ?";
        String deleteAlunosQuery = "DELETE FROM alunos WHERE RA = ?";

        try (PreparedStatement stmt1 = connection.prepareStatement(deleteDocumentosQuery);
             PreparedStatement stmt2 = connection.prepareStatement(deleteAlunosQuery)) {

            stmt1.setString(1, RA);
            stmt1.executeUpdate();

            stmt2.setString(1, RA);
            stmt2.executeUpdate();
        }
    }

    public void setSingletons() throws SQLException {
        pendenciaSingleton pendenciaSing = pendenciaSingleton.getInstance();
        alunosSingleton alunoSing = alunosSingleton.getInstance();
        ResultSet query = getAlunos();
        List<Aluno> alunoList = new ArrayList<>();
        List<Aluno> alunoPendencia = new ArrayList<>();

        while(query.next()) {
            Aluno aluno = new Aluno();
            String RA = query.getString("RA");
            String nome = query.getString("Nome");
            String email = query.getString("Email");
            String SEI = query.getString("SEI");
            int cargaHoraria = query.getInt("cargaHoraria");
            boolean relatorio1 = query.getBoolean("relatorio1");
            boolean relatorio2 = query.getBoolean("relatorio2");
            boolean relatorio3 = query.getBoolean("relatorio3");
            boolean relatorio4 = query.getBoolean("relatorio4");
            LocalDate TCE = null;
            LocalDate parcial1 = null;
            LocalDate parcial2 = null;
            LocalDate parcial3 = null;
            LocalDate parcial4 = null;
            LocalDate termino = null;
            LocalDate aditivo = null;
            String tipo = query.getString("tipo");
            if(query.getDate("aditivo") != null) {
                aditivo = query.getDate("aditivo").toLocalDate();
                aluno.setTermoAditivo(aditivo);
            }
            String orientador = query.getString("orientador");
            String emailOrientador = query.getString("EmailOrientador");
            String supervisor = query.getString("NomeSupervisor");
            String emailSupervisor = query.getString("EmailSupervisor");
            String empresa = query.getString("empresa");

            aluno.setTipo(tipo);
            aluno.setTermino(termino);
            aluno.setEmailOrientador(emailOrientador);
            aluno.setNomeSupervisor(supervisor);
            aluno.setEmailSupervisor(emailSupervisor);
            aluno.setNomeOrientador(orientador);
            aluno.setRA(RA);
            aluno.setNome(nome);
            aluno.setEmail(email);
            aluno.setNumeroSEI(SEI);
            aluno.setEmpresa(empresa);
            aluno.setCargaHoraria(cargaHoraria);
            aluno.setRelatorio1(relatorio1);
            aluno.setRelatorio2(relatorio2);
            aluno.setRelatorio3(relatorio3);
            aluno.setRelatorio4(relatorio4);

            if (query.getDate("TCE") != null) {
                TCE = query.getDate("TCE").toLocalDate();
                aluno.setTCE(TCE);
            }

            if (query.getDate("termino") != null) {
                termino = query.getDate("termino").toLocalDate();
                aluno.setTermino(termino);
            }

            if (query.getDate("parcial1") != null) {
                parcial1 = query.getDate("parcial1").toLocalDate();
                aluno.setParcial1(parcial1);
            }

            parcial2 = query.getDate("parcial2").toLocalDate();

            if (query.getDate("parcial2") != null) {
                parcial2 = query.getDate("parcial2").toLocalDate();
                aluno.setParcial2(parcial2);
            }

            if (query.getDate("parcial3") != null) {
                parcial3 = query.getDate("parcial3").toLocalDate();
                aluno.setParcial3(parcial3);
            }

            if (query.getDate("parcial4") != null) {
                parcial4 = query.getDate("parcial4").toLocalDate();
                aluno.setParcial4(parcial4);
            }

            String pendencia = checkPendencia(parcial1, parcial2, parcial3, parcial4, relatorio1, relatorio2, relatorio3, relatorio4);
            if(!pendencia.equals("Nenhuma")) {
                aluno.setPendencia(pendencia);
                alunoPendencia.add(aluno);
            }
            alunoList.add(aluno);
        }

        alunoSing.setAlunos(alunoList);
        pendenciaSing.setAlunos(alunoPendencia);
    }

    public String checkPendencia(LocalDate parcial1, LocalDate parcial2, LocalDate parcial3, LocalDate parcial4, boolean relatorio1, boolean relatorio2, boolean relatorio3, boolean relatorio4) {
        LocalDate now = LocalDate.now();

        if(!relatorio1 && parcial1.getDayOfMonth() < now.getDayOfMonth() &&
                parcial1.getMonthValue() <= now.getMonthValue() &&
                parcial1.getYear() <= now.getYear()) {
            return "Relatório 1";
        } else if(!relatorio2 && parcial2.getDayOfMonth() < now.getDayOfMonth() &&
                parcial2.getMonthValue() <= now.getMonthValue() &&
                parcial2.getYear() <= now.getYear()) {
            return "Relatório 2";
        } else if(!relatorio3 && parcial3.getDayOfMonth() < now.getDayOfMonth() &&
                parcial3.getMonthValue() <= now.getMonthValue() &&
                parcial3.getYear() <= now.getYear()) {
            return "Relatório 3";
        } else if (!relatorio4 && parcial4.getDayOfMonth() < now.getDayOfMonth() &&
                parcial4.getMonthValue() <= now.getMonthValue() &&
                parcial4.getYear() <= now.getYear()) {
            return "Relatório 4";
        }

        return "Nenhuma";
    }

    public void insertUsuario(String nome, String email, String usuario, String senha) throws SQLException {
        nome = "'" + nome + "'";
        email = "'" + email + "'";
        usuario = "'" + usuario + "'";
        senha = "'" + senha + "'";

        String query = String.format("INSERT INTO usuarios (username, email, senha, nome) VALUES (%s, %s, %s, %s);", usuario, email, senha, nome);
        statement.executeUpdate(query);
    }
}
