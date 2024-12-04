package sistemadeestagio.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Aluno {
    private String RA;
    private String nome;
    private String numeroSEI;
    private String email;
    private String empresa;
    private String nomeOrientador;
    private String emailOrientador;
    private LocalDate TCE;
    private LocalDate termino;
    private LocalDate termoAditivo;
    private String tipo;
    private String nomeSupervisor;
    private String emailSupervisor;
    private LocalDate dataObrigatorio;
    private String status;
    private String pendencia;
    private int cargaHoraria;
    private LocalDate parcial1;
    private LocalDate parcial2;
    private LocalDate parcial3;
    private LocalDate parcial4;
    private boolean relatorio1;
    private boolean relatorio2;
    private boolean relatorio3;
    private boolean relatorio4;

    public Aluno() {
        this.pendencia = "Nenhuma";
        this.status = "Ativo";
        this.termoAditivo = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroSEI() {
        return numeroSEI;
    }

    public void setNumeroSEI(String numeroSEI) {
        this.numeroSEI = numeroSEI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getNomeOrientador() {
        return nomeOrientador;
    }

    public void setNomeOrientador(String nomeOrientador) {
        this.nomeOrientador = nomeOrientador;
    }

    public String getEmailOrientador() {
        return emailOrientador;
    }

    public void setEmailOrientador(String emailOrientador) {
        this.emailOrientador = emailOrientador;
    }

    public LocalDate getTCE() {
        return TCE;
    }

    public void setTCE(LocalDate TCE) {
        this.TCE = TCE;
    }

    public LocalDate getTermino() {
        return termino;
    }

    public void setTermino(LocalDate termino) {
        this.termino = termino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getTermoAditivo() {
        return termoAditivo;
    }

    public void setTermoAditivo(LocalDate termoAditivo) {
        this.termoAditivo = termoAditivo;
    }

    public String getNomeSupervisor() {
        return nomeSupervisor;
    }

    public void setNomeSupervisor(String nomeSupervisor) {
        this.nomeSupervisor = nomeSupervisor;
    }

    public String getEmailSupervisor() {
        return emailSupervisor;
    }

    public void setEmailSupervisor(String emailSupervisor) {
        this.emailSupervisor = emailSupervisor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPendencia() {
        return pendencia;
    }

    public void setPendencia(String pendencia) {
        this.pendencia = pendencia;
    }

    public LocalDate getDataObrigatorio() {
        return dataObrigatorio;
    }

    public void setDataObrigatorio(LocalDate dataObrigatorio) {
        this.dataObrigatorio = dataObrigatorio;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public boolean isRelatorio1() {
        return relatorio1;
    }

    public void setRelatorio1(boolean relatorio1) {
        this.relatorio1 = relatorio1;
    }

    public boolean isRelatorio2() {
        return relatorio2;
    }

    public void setRelatorio2(boolean relatorio2) {
        this.relatorio2 = relatorio2;
    }

    public boolean isRelatorio3() {
        return relatorio3;
    }

    public void setRelatorio3(boolean relatorio3) {
        this.relatorio3 = relatorio3;
    }

    public boolean isRelatorio4() {
        return relatorio4;
    }

    public void setRelatorio4(boolean relatorio4) {
        this.relatorio4 = relatorio4;
    }

    public String getRA() {
        return RA;
    }

    public void setRA(String RA) {
        this.RA = RA;
    }

    public LocalDate getParcial1() {
        return parcial1;
    }

    public void setParcial1(LocalDate parcial1) {
        this.parcial1 = parcial1;
    }

    public LocalDate getParcial2() {
        return parcial2;
    }

    public void setParcial2(LocalDate parcial2) {
        this.parcial2 = parcial2;
    }

    public LocalDate getParcial3() {
        return parcial3;
    }

    public void setParcial3(LocalDate parcial3) {
        this.parcial3 = parcial3;
    }

    public LocalDate getParcial4() {
        return parcial4;
    }

    public void setParcial4(LocalDate parcial4) {
        this.parcial4 = parcial4;
    }
}
