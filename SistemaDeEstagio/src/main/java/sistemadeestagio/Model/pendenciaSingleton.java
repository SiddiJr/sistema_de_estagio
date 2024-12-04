package sistemadeestagio.Model;

import java.util.List;

public final class pendenciaSingleton {
    private List<Aluno> aluno;
    private final static pendenciaSingleton INSTANCE = new pendenciaSingleton();

    public static pendenciaSingleton getInstance() {
        return INSTANCE;
    }

    public List<Aluno> getAlunos() {
        return this.aluno;
    }

    public void setAlunos(List<Aluno> pendencias) {
        this.aluno = pendencias;
    }
}
