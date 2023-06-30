package pi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE = "sigb";
    private static final String DRIVER_CONEXAO = "com.mysql.cj.jdbc.Driver";
    private static final String USUARIO = "root";
    private static final String SENHA = "admin";
    
    private final String INSERT = "INSERT INTO cursos (nome, descricao) VALUES (?, ?)";
    private final String UPDATE = "UPDATE cursos SET nome = ?, descricao = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM cursos WHERE id = ?";
    private final String LIST = "SELECT * FROM cursos";
    
    private Connection connection;

    public CursoDAO() {
        try {
            Class.forName(DRIVER_CONEXAO);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL não encontrado");
            e.printStackTrace();
        } try {
            connection = DriverManager.getConnection(URL + DATABASE, USUARIO, SENHA);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Curso> buscarCursos() {
        List<Curso> cursos = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String query = LIST;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");

                Curso curso = new Curso(id, nome, descricao);
                cursos.add(curso);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }

    public void adicionarCurso(Curso curso) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, curso.getNome());
            statement.setString(2, curso.getDescricao());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarCurso(Curso curso) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, curso.getNome());
            statement.setString(2, curso.getDescricao());
            statement.setInt(3, curso.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirCurso(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            atualizarIdSequencial(id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarIdSequencial(int id) {
        String query = "ALTER TABLE cursos AUTO_INCREMENT = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

