package sistemadeestagio.Model;

import java.util.ArrayList;
import java.util.List;

public final class alunosSingleton {
    private List<Aluno> aluno = new ArrayList<>();
    private final static alunosSingleton INSTANCE = new alunosSingleton();

    public static alunosSingleton getInstance() {
        return INSTANCE;
    }

    public List<Aluno> getAlunos() {
        return this.aluno;
    }

    public void setAlunos(List<Aluno> aluno) {
        this.aluno = aluno;
    }
}
