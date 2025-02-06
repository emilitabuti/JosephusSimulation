import EstruturaListaDuplamenteLigadaCircular.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface implements ActionListener{
    //painel para apresentar os indivíduos
    JPanel painel = new JPanel();
    JFrame janela;
    //array de labels para apresentar as imagens
    JLabel labels[];
    //label para apresentar o texto de indivíduo que sobreviveu e morreu
    JLabel textoStatus;

    //textField para as entradas
    JTextField qntdI,qntdPasso,qntdTempo;
    int qntdIndiv, passo;
    float tempo;
    int status;

    //lista para armazenar os indivíduos
    IListaDuplamenteLigadaCircular l = new ListaDuplamenteLigadaCircular();

    //Componentes para os botoes
    Container _pBotoes = null;   // container dos botoes 
    JButton _jbOK = null;
    JButton _jbSair = null;
    JButton _jbIniciar = null;
    JButton _jbConfigurar = null;
    JButton _jbSobre = null;

    /**
     * Interface Construtor
     *
     */
    public Interface(){
        janela = new JFrame("Josephus");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        
        // Carregar um ícone
        ImageIcon icon = new ImageIcon("imagens/hehe.png");
        // Definir o ícone para a janela
        janela.setIconImage(icon.getImage());

        //Inicializacao dos componentes
        painel = new JPanel();
        _jbOK = new JButton("Carregar");
        _jbSair = new JButton("Sair");
        _jbIniciar = new JButton("Jogar");
        _jbConfigurar = new JButton("Novo Jogo");
        _jbSobre = new JButton("Sobre o jogo");

        qntdI = new JTextField(10);
        qntdPasso = new JTextField(10);
        qntdTempo = new JTextField(10);

        //Configuração do layout para o JFrame principal
        janela.setLayout(new BorderLayout());

        //Adiciona inputs no panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));//FlowLayout para alinhamento horizontal
        inputPanel.add(new JLabel("Quantidade de indivíduos:"));
        inputPanel.add(qntdI);
        inputPanel.add(new JLabel("Passo:"));
        inputPanel.add(qntdPasso);
        inputPanel.add(new JLabel("Tempo(segundos):"));
        inputPanel.add(qntdTempo);
        inputPanel.add(_jbOK);

        // Adiciona o painel de entrada ao NORTE do JFrame principal
        janela.add(inputPanel, BorderLayout.NORTH);

        // Adiciona KeyListener para mudar o foco com Enter
        qntdI.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    qntdPasso.requestFocus();
                }
            }
        });
        qntdPasso.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    qntdTempo.requestFocus();
                }
            }
        });
        // Adiciona KeyListener para apertar o botão _jbOK com Enter
        qntdTempo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    _jbOK.doClick();
                }
            }
        });
        
        // Adicionar botões e textos no panel
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new FlowLayout());
        
        botoesPanel.add(_jbIniciar);
        botoesPanel.add(_jbConfigurar);
        botoesPanel.add(_jbSair);
        botoesPanel.add(_jbSobre);
        
        textoStatus = new JLabel();
        botoesPanel.add(textoStatus);
        
        //Adiciona panel com botões ao SUL do JFrame principal
        janela.add(botoesPanel, BorderLayout.SOUTH);

        // Adiciona panel para exibir indivíduos
        janela.add(painel, BorderLayout.CENTER);

        //desabilita os botões 
        _jbConfigurar.setEnabled(false);
        _jbIniciar.setEnabled(false);
        
        // Define o tamanho do JFrame e torna visível
        janela.setSize(1000,600);
        janela.setLocationRelativeTo(null); // Centraliza a janela
        janela.setVisible(true);

        // Adiciona listeners
        _jbOK.addActionListener(this);
        _jbSair.addActionListener(this);
        _jbIniciar.addActionListener(this);
        _jbConfigurar.addActionListener(this);
        _jbSobre.addActionListener(this);
    }

    /**
     * Trata eventos de opcoes de menu
     * @param e comando correspondente a opcao
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _jbOK){
            try {
                String qntd1 = qntdI.getText();
                String passo1 = qntdPasso.getText();
                String tempo1 = qntdTempo.getText();
                espacoVazio(qntd1, passo1, tempo1); //exceção para verificar se os espaços estão vazios
                verificarString(qntd1, passo1, tempo1); //exceção para verificar se ha letras nas entradas
                qntdIndiv =Integer.parseInt(qntdI.getText());
                indivInvalido(qntdIndiv);

                passo = Integer.parseInt(qntdPasso.getText());
                passoInvalido(passo);

                tempo = Float.parseFloat(qntdTempo.getText().replace(',', '.'));
                tempoInvalido(tempo);

                Individuo elem;
                l = new ListaDuplamenteLigadaCircular(); // Reiniciar a lista para nova configuração
                labels = new JLabel[qntdIndiv]; // define o tamanho da label

                //preenche a lista com "Vivos"
                for (int i = 0; i < qntdIndiv; i++) {
                    elem = new Individuo(i + 1, "Vivo");
                    l.inserirFim(elem);
                }
                mostrarLista();

                qntdI.setEditable(false);
                qntdPasso.setEditable(false);
                qntdTempo.setEditable(false);
                _jbConfigurar.setEnabled(true);
                _jbIniciar.setEnabled(true);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(janela, ex.getMessage());
            }
        }  
        else if (e.getSource() == _jbIniciar) {
            if(_jbConfigurar.isEnabled()){ 
                disableAllButtons();
                new Thread(this::simulacao).start(); //chama o método simulação
                _jbIniciar.setEnabled(false);
            }
        } 
        else if (e.getSource() == _jbConfigurar) {
            // Toggle para tornar o campo de texto editável ou não editável
            qntdI.setEditable(!qntdI.isEditable());
            qntdPasso.setEditable(!qntdPasso.isEditable());
            qntdTempo.setEditable(!qntdTempo.isEditable());
            
            painel.removeAll();
            painel.revalidate();
            painel.repaint();
            
            textoStatus.setText("");//Limpa a caixa de texto textoStatus
            qntdI.setText(""); // Limpa a caixa de texto qntdI
            qntdPasso.setText(""); // Limpa a caixa de texto qntdPasso
            qntdTempo.setText(""); // Limpa a caixa de texto qntdTempo
            
            qntdI.setEditable(true);
            qntdPasso.setEditable(true);
            qntdTempo.setEditable(true);
            
            _jbIniciar.setEnabled(false);
            _jbConfigurar.setEnabled(false);
        }
        else if (e.getSource() == _jbSair) {
            System.exit(0);
        }
        else if (e.getSource() == _jbSobre) {
            JOptionPane.showMessageDialog(janela, "A história do Josephus:\n"+ 
                                                "Durante a guerra judaica contra os romanos, Flávio Josefo, um historiador judeu antigo, juntamente com 40 de seus companheiros ficaram presos\nem "+
                                                "uma caverna. Preferindo o suicídio coletivo à rendição, eles se posicionaram em círculo e decidiram matar a cada terceiro homem até que\nrestasse"+ 
                                                " apenas um, que então tiraria sua própria vida.\n\n"+
                                                "O jogo Josephus consiste em:\nDado um número n de pessoas em um círculo e um passo k, onde a cada"+
                                                " k-ésima pessoa é removida até que reste apenas uma pessoa, qual é a\nposição dessa última pessoa que sobrevive?");
        }
    }

    /**
     * Método espacoVazio - Serve para verificar se o usuario não deixou em branco alguma informação.
     *
     * @param x Quantidade de Individuos.
     * @param y Quantidade de passos.
     * @param z Quantidade de tempo.
     */
    public void espacoVazio(String x,String y,String z) throws Excecoes {
        if((x == null || x.equals("")) && (y == null || y.equals("")) && (z == null || z.equals(""))){
            throw new Excecoes("Atenção! Voce nao adicionou individuos, passo e tempo.");
        }
        else if((x == null || x.equals("")) && (y == null || y.equals(""))){
            throw new Excecoes("Atenção! Voce nao adicionou individuos e passo.");
        }
        else if((z == null || z.equals("")) && (y == null || y.equals(""))){
            throw new Excecoes("Atenção! Voce nao adicionou passo e tempo.");
        }
        else if((z == null || z.equals("")) && (x == null || x.equals(""))){
            throw new Excecoes("Atenção! Voce nao adicionou individuo e tempo.");
        }
        else if(x == null || x.equals("")){
            throw new Excecoes("Atenção!: Voce nao adicionou individuo.");
        }
        else if(y == null || y.equals("")){
            throw new Excecoes("Atenção! Voce nao adicionou passo.");
        }
        else if(z == null || z.equals("")){
            throw new Excecoes("Atenção! Voce nao adicionou tempo.");
        }
    }

    /**
     * Método verificarString - Verifica se o usuário inseriu 
     * apenas números nas informações pedidas. Com execeção do tempo,
     * que é permitida a entrada de vírgula ou ponto.
     *
     *  @param x Quantidade de Individuos.
     *  @param y Quantidade de passos.
     *  @param z Quantidade de tempo.
     */
    public void verificarString(String x, String y, String z) throws Excecoes{
        if ((x == null || !x.matches("\\d+")) || (y == null || !y.matches("\\d+")) || (z == null || !z.matches("[\\d.,]+"))) {
            throw new Excecoes("Atenção! Voce deve apenas utilizar números.");
        }
    }

    /**
     * Método indivInvalido - Verifica se o usuário colocou um número de individuos na faixa ]2,117].
     *
     * @param x Quantidade de Individuos.
     */
    public void indivInvalido(int x) throws Excecoes{
        if (x < 3){
            throw new Excecoes("Atenção! Valor inválido, o número de indivíduos deve ser maior que 2.");
        }
        else if(x > 120){
            throw new Excecoes("Atenção! Valor inválido, o número de indivíduos deve ser no máximo 120.");
        }
    }

    /**
     * Método passoInvalido - Verifica se o usuário colocou um número de passos na faixa [2,quantidadeDeIndividuos].
     *
     * @param x Quantidade de passos.
     */
    public void passoInvalido(int x) throws Excecoes{
        if (x < 2){
            throw new Excecoes("Atenção! Valor inválido, o passo deve ser maior que 1.");
        }
        else if (x >= qntdIndiv){
            throw new Excecoes("Atenção! Valor inválido, o passo deve ser menor que o número de indivíduos.");
        }
    }
    
    /**
     * Método verificarString - Verifica se o usuário inseriu 
     * apenas números nas informações pedidas. Com execeção do tempo,
     * que é permitida a entrada de vírgula ou ponto.
     *
     *  @param x Quantidade de Individuos.
     *  @param y Quantidade de passos.
     *  @param z Quantidade de tempo.
     */
    public void tempoInvalido(float x) throws Excecoes{
        if (x > 120) {
            throw new Excecoes("Atenção! O tempo limite é de 2 minutos.");
        }
    }

    /**
     * Método simulacao - Metódo onde a lógica do josephus se aplica.
     *
     */
    public void simulacao(){
        Individuo conteudo;
        No noAtual = null;
        No prox = null;
        int cont = 1;
        int qntd = qntdIndiv;
        int sobr;
        ImageIcon imagem;

        if(qntd > 1){
            noAtual = l.getInicio();

            while(qntd > 1){
                while(cont < passo){ //loop para avançar de indivíduo de acordo com o passo
                    noAtual = noAtual.getProximo();
                    conteudo = (Individuo)noAtual.getConteudo();
                    if(conteudo.getStatus() != "Morto")
                        cont++;
                }

                prox = noAtual.getProximo();
                conteudo = (Individuo)prox.getConteudo();
                if(conteudo.getStatus() == "Morto"){ //se o indivíduo ja estiver morto passa para o próximo No
                    do{
                        prox = prox.getProximo();
                        conteudo = (Individuo)prox.getConteudo();
                    }while(conteudo.getStatus() == "Morto");
                }

                conteudo = (Individuo)noAtual.getConteudo();
                conteudo.setStatus("Morto");
                status = conteudo.getIdentificador();
                textoStatus.setText("<html>Indivíduo morto: <span style='color: red;'>" + status + "</span></html>"); // Atualiza o textoStatus

                noAtual = prox;
                cont = 1;
                qntd--;
                mostrarLista();

                try { //delay escolhido pelo usuário
                    Thread.sleep((long)(tempo*1000));
                } catch (InterruptedException e) {
                }
            }
            sobr = sobrevivente();// atualizar a imagem para sobrevivente
            imagem = new ImageIcon("imagens/homer.png");
            labels[sobr - 1].setIcon(imagem);
            
            status = sobr;
            textoStatus.setText("<html>Indivíduo que sobreviveu: <span style='color: green;'>" + status + "</span></html>");
        }
        enableAllButtons();
        _jbIniciar.setEnabled(false);
    }

    /**
     * Método mostrarLista - Mostra a lista sempre atualizada.
     *
     */
    public void mostrarLista() {
        painel.removeAll();

        No noAtual = l.getInicio();
        Individuo conteudo;
        int tam = 0;
        ImageIcon imagem;
        
        while (noAtual!= null && tam < labels.length) {
            conteudo = (Individuo) noAtual.getConteudo();
            if (conteudo.getStatus().equals("Vivo")) {//se o status estiver como "Vivo" a imagem sera vivo.png
                imagem = new ImageIcon("imagens/homer.png"); 
            } else {
                imagem = new ImageIcon("imagens/fantasma.png"); //se o status estiver como "Morto" a imagem sera fantasma.png
            }
            labels[tam] = new JLabel(imagem);//adiciona a imagem no labels
            painel.add(labels[tam]);//adiciona o label no painel
            noAtual = noAtual.getProximo();
            tam++;
        }

        painel.revalidate();
        painel.repaint();
    }

    /**
     * Método sobrevivente - Metódo que procura qual seria o sobrevivente.
     *
     * @return O valor do individuo no array.
     */
    public int sobrevivente(){
        Individuo conteudo;
        No noVivo = l.getInicio();
        No noSobrevivente = null;
        int tam = 0;
        int ident = 0;

        while(noVivo != null && tam < qntdIndiv){
            conteudo = (Individuo)noVivo.getConteudo();
            if(conteudo.getStatus() == "Vivo")//se o status for "Vivo", pega o identificador do Individuo correspondent
                ident = conteudo.getIdentificador();
            noVivo = noVivo.getProximo();
            tam++;
        }
        return ident;
    }

    /**
     * Método disableAllButtons - Desabilita os botões.
     *
     */
    private void disableAllButtons() {
        _jbOK.setEnabled(false);
        _jbIniciar.setEnabled(false);
        _jbConfigurar.setEnabled(false);
    }

    /**
     * Método disableAllButtons - Habilita os botões.
     *
     */
    private void enableAllButtons() {
        _jbOK.setEnabled(true);
        _jbIniciar.setEnabled(true);
        _jbConfigurar.setEnabled(true);
    }
}
