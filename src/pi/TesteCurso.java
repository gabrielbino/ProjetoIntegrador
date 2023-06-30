package pi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TesteCurso {
    private static JFrame frame;
    private static JTable tabelaCursos;
    private static JTextField textFieldNome;
    private static JTextField textFieldDescricao;
    private static DefaultTableModel tableModel;
    private static CursoDAO cursoDAO;

    public static void main(String[] args) {
        cursoDAO = new CursoDAO();
        SwingUtilities.invokeLater(() -> new TesteCurso().criarTela());
    }
    
    public void exibirTela() {
        SwingUtilities.invokeLater(() -> {
            criarTela();
        });
    }
        
    private void excluirCurso() {
        int linhaSelecionada = tabelaCursos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um curso para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idCurso = (int) tabelaCursos.getValueAt(linhaSelecionada, 0);
        cursoDAO.excluirCurso(idCurso);
        carregarCursos();
    }
    
    public void validarListaVazia() {
        List<Curso> cursos = cursoDAO.buscarCursos();
        if (cursos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Não possui cursos cadastrados.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarCurso() {
        int linhaSelecionada = tabelaCursos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um curso para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idCurso = (int) tabelaCursos.getValueAt(linhaSelecionada, 0);
        String nome = textFieldNome.getText();
        String descricao = textFieldDescricao.getText();

        if (nome.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Curso curso = new Curso(idCurso, nome, descricao);
        cursoDAO.atualizarCurso(curso);
        limparTabelaCursos();
        carregarCursos();
    }

        private void criarTela() {
            frame = new JFrame("Cadastro de Cursos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            // Painel de cadastro
            JPanel painelCadastro = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel labelNome = new JLabel("Nome:");
            painelCadastro.add(labelNome, gbc);

            textFieldNome = new JTextField(20);
            gbc.gridx = 1;
            painelCadastro.add(textFieldNome, gbc);

            JLabel labelDescricao = new JLabel("Descrição:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            painelCadastro.add(labelDescricao, gbc);

            textFieldDescricao = new JTextField(20);
            gbc.gridx = 1;
            painelCadastro.add(textFieldDescricao, gbc);

            JButton botaoAdicionar = new JButton("Adicionar");
            botaoAdicionar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adicionarCurso();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.LINE_START;
            painelCadastro.add(botaoAdicionar, gbc);
            
            JButton botaoExcluir = new JButton("Excluir");
            botaoExcluir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    excluirCurso();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            painelCadastro.add(botaoExcluir, gbc);

            JButton botaoAtualizar = new JButton("Atualizar");
            botaoAtualizar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    atualizarCurso();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.LINE_END;
            painelCadastro.add(botaoAtualizar, gbc);
            
            JButton btnCarregarCursos = new JButton("Carregar Cursos");
            btnCarregarCursos.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    carregarCursos();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = -2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            painelCadastro.add(btnCarregarCursos, gbc);

            // Painel da tabela
            JPanel painelTabela = new JPanel(new BorderLayout());

            tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição"}, 0);
            tabelaCursos = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(tabelaCursos);
            painelTabela.add(scrollPane, BorderLayout.CENTER);

            // Painel principal
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.add(painelCadastro, BorderLayout.NORTH);
            painelPrincipal.add(painelTabela, BorderLayout.CENTER);

            frame.add(painelPrincipal);
            frame.setVisible(true);
        }

        private void adicionarCurso() {
            String nome = textFieldNome.getText();
            String descricao = textFieldDescricao.getText();

            if (nome.isEmpty() || descricao.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Curso curso = new Curso(0, nome, descricao);
            cursoDAO.adicionarCurso(curso);

            textFieldNome.setText("");
            textFieldDescricao.setText("");

            carregarCursos();
        }

        private void carregarCursos() {
            List<Curso> cursos = cursoDAO.buscarCursos();
            tableModel.setRowCount(0);
            //limparTabelaCursos();
            validarListaVazia();
            
            for (Curso curso : cursos) {
                Object[] rowData = {curso.getId(), curso.getNome(), curso.getDescricao()};
                tableModel.addRow(rowData);
            }
        }

        private void limparTabelaCursos() {
            int rowCount = tableModel.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                tableModel.removeRow(i);
            }
        }
}    
