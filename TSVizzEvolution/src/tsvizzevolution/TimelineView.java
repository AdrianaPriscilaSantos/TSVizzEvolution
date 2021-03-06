package tsvizzevolution;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import static org.graphstream.algorithm.Toolkit.randomNode;
import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

public class TimelineView extends JFrame {
    private JButton btnChooseFileSearch1;
    private JButton btnChooseFileSearch2;
    private JButton btnVisualizeTimeline;
    private JButton btnSearchMethod;
    private JButton btnSearchMethod2;
    private JComboBox<String> cbTimeline;
    private JComboBox<String> cbSelectMethod;

    private JLabel lblTimeline;
    private JLabel lblCsv1;
    private JLabel lblCsv2;
    private JLabel lblVisualization;
    private JLabel lblSelectTheCsvMethod;
    private JLabel lblSelectTheSecond;
	private JLabel lblVisualizeTimeline;
	private JLabel lblSelectMethod;
    private JPanel pnlGraph;
    private JPanel pnlTimeline;
	private JPanel pnlVisualization ;
	private JPanel pnlMethod;
	private JPanel pnlProgress;
	private JPanel pnlSelectMethod;
	public JProgressBar progress;
	public JPanel classe;
	public JPanel metodo;
	public JPanel contentPane;
	
	public JFrame frame;

    private JTextField txtFilePathDefault1; 
    private JTextField txtFilePathDefault2;
    private JTextField txtFilePathMethod; 
    private JTextField txtFilePathMethod2;

    private static final String VIRGULA = ";";
    private static String nomeDoArquivo;



	public Thread progressoT = new Thread() {
		@Override
		public void run(){
			progress.setValue(0);				
			for (int i = 0; i <= 50; i++) {
				progress.setValue(i);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	};

	public static int converteInteiro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public TimelineView() throws IOException {

		setTitle("TSVizzEvolution - Two Versions");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 715, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);

		// Para abrir no centro da Tela
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
        initComponents();
        pnlVisualization.setVisible(true);
        pnlTimeline.setVisible(true);
        btnVisualizeTimeline.setVisible(true);
        pnlMethod.setVisible(false);
		lblVisualizeTimeline.setVisible(true);
		pnlSelectMethod.setVisible(false);
		btnVisualizeTimeline.setEnabled(false);
        
        cbTimeline.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
            	if (event.getItem().equals("Methods")) {
                    pnlMethod.setVisible(true);
                    pnlSelectMethod.setVisible(false);
                } else {
                    pnlMethod.setVisible(false);
                    pnlSelectMethod.setVisible(false);
                }
            }
        });
    }

    private void btnChooseFileSearch1ActionPerformed(ActionEvent evt) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(TimelineView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtFilePathDefault1.setText(file.getPath());
            //txtFilePathDefault1.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\resultado_evolution1.csv");
            nomeDoArquivo = file.getName();
        }
    }
   
    private void btnChooseFileSearch2ActionPerformed(ActionEvent evt) throws IOException {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(TimelineView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtFilePathDefault2.setText(file.getPath());
            //txtFilePathDefault2.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\resultado_evolution1.csv");
            nomeDoArquivo = file.getName();
            btnGerarUploadActionPerformed(evt);
        }
    }

    private void btnSearchMethodActionPerformed(ActionEvent evt) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(TimelineView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtFilePathMethod.setText(file.getPath());
            //txtFilePathMethod.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\commons-io_result_byclasstest_testsmells.csv");
            nomeDoArquivo = file.getName();
        }
    }

    private void btnSearchMethod2ActionPerformed(ActionEvent evt) throws IOException {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(TimelineView.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtFilePathMethod2.setText(file.getPath());
            //txtFilePathMethod2.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\commons-io_result_byclasstest_testsmells.csv");
            nomeDoArquivo = file.getName();
            List<ClassMethod> l = retorna_lista_classe_metodo();
            List<String> list = new ArrayList<>();
            for(ClassMethod obj: l){
                if(obj.metodos.size() != 0){
                    list.add(obj.classe);
                }
            }
            Collections.sort(list);
            for(String s: list){
                //cbClass.addItem(s);
            }
        }
    }

    
    private void btnGerarTimelineActionPerformed(ActionEvent evt) {
    	dispose();
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension( 800 + Configurations.adicionalBorda, Configurations.alturaPainel ));
        frame.setMaximumSize(frame.getPreferredSize());
        frame.setMinimumSize(frame.getPreferredSize());
		frame.setTitle("TSVizzEvolution");
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLocation(100, 100);

		
        JPanel painel = new JPanel();
        painel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painel.setBackground(Configurations.corPainel); //seta a cor de fundo
        painel.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaPainel, Configurations.larguraBorda)); // seta a borda
        painel.setPreferredSize(new Dimension( 700, 300 ));
        painel.setMaximumSize(painel.getPreferredSize());
        painel.setMinimumSize(painel.getPreferredSize());
        frame.getContentPane().add(painel);
        
		
        int tamanho = 0;
        try {
            String selecionado = (String) cbTimeline.getSelectedItem();
            tamanho = criaRetangulos(painel, selecionado, txtFilePathDefault1.getText(), txtFilePathDefault2.getText(), tamanho);

        } catch (Exception e){
            e.printStackTrace();
        }
    painel.setPreferredSize(new Dimension(tamanho*204, 300 ));
    
	JScrollPane jScrollPane = new JScrollPane(painel);
	jScrollPane.setHorizontalScrollBarPolicy(jScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	//jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	frame.getContentPane().add(jScrollPane);
	}

	private int criaRetangulos(JPanel painel, String filtro, String fileName1, String fileName2, int tam){
		
        JLabel versao1 = new JLabel ("V1");
        versao1.setFont(new Font("Tahoma", Font.PLAIN, 22));
		painel.add(versao1);
		
		JPanel pacote = new JPanel();
        pacote.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pacote.setBackground(Configurations.corPacote); //seta a cor de fundo
        pacote.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaPacote, Configurations.larguraBorda)); // seta a borda
        ToolTipManager.sharedInstance().setInitialDelay(500);//aparecerá logo que passe 0,5 segundos
        painel.add(pacote);

        JLabel versao2 = new JLabel ("V2");
        versao2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		painel.add(versao2);
		
        JPanel pacote2 = new JPanel();
        pacote2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pacote2.setBackground(Configurations.corPacote); //seta a cor de fundo
        pacote2.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaPacote, Configurations.larguraBorda)); // seta a borda
        ToolTipManager.sharedInstance().setInitialDelay(500);//aparecerá logo que passe 0,5 segundos
        
        painel.add(pacote2);
       
        //txtFilePathMethod.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\all_report_by_testsmells.csv");
        List<ClassMethod> l1 = CriaListaDeMetodos(txtFilePathMethod.getText());
        List<ClassMethod> l2 = CriaListaDeMetodos(txtFilePathMethod2.getText());
        List<Data> dados1 = retornaDados(fileName1, filtro);
        List<Data> dados2 = retornaDados(fileName2, filtro);
        arrumaDados(dados1, dados2);
        int tamanho = constroiBlocos(dados1, filtro, pacote, l1);
        tamanho = constroiBlocos(dados2, filtro, pacote2, l2);
        
        LegendTimeline t = new LegendTimeline();
		LegendTimeline.main(null);
		
        return tamanho;
      
    }

    private int constroiBlocos(List<Data> dados, String filtro, JPanel pacote, List<ClassMethod> l){
        List<String> analisados = new ArrayList<>();
        if (filtro.equals("Project")){
            for (int i = 0; i < dados.size(); i++){
                String projeto = dados.get(i).projeto;
                if (!foiAnalisado(analisados, projeto)){
                    classe = new JPanel();
                    classe.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    classe.setBackground(Configurations.corClasse); //seta a cor de fundo
                    classe.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaClasse, Configurations.larguraBorda)); // seta a borda
                    classe.setPreferredSize(new Dimension(204, Configurations.alturaClasse));
                    String novo_nome_segundo_vertice = projeto.substring(0, projeto.length()-2);
                    String html_classe = "<html><p><font color=\"#000000\" " + "size=\"4\" face=\"Arial\"><b> "+ filtro+": <body></b>" + novo_nome_segundo_vertice +"</font></p></html>";
                    classe.setToolTipText(html_classe);
                    pacote.add(classe);
                    JPanel espaco3 = new JPanel();
                    espaco3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    espaco3.setBackground(Color.WHITE); //seta a cor de fundo
                    espaco3.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaPainel, Configurations.larguraBorda)); // seta a borda
                    ToolTipManager.sharedInstance().setInitialDelay(500);//aparecerá logo que passe 0,5 segundos
                    espaco3.setPreferredSize(new Dimension(800, 10 ));
                    
                    pacote.add(espaco3);
                    for (int j = i; j < dados.size(); j++){
                        if (dados.get(j).projeto.equals(projeto)){
                            JPanel metodo = new JPanel();
                            metodo.setBackground(dados.get(j).cor);
                            metodo.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaMetodo, Configurations.larguraBorda)); // seta a borda
                            metodo.setPreferredSize(new Dimension (Configurations.larguraMetodo, Configurations.alturaMetodo)); // seta o tamanho
                            metodo.setMaximumSize(metodo.getPreferredSize());
                            metodo.setMinimumSize(metodo.getPreferredSize());
                            String novo_nome_primeiro_vertice = dados.get(j).nome.substring(0, dados.get(j).nome.length()-2);
                            String html_metodo = "<html><p><font color=\"#000000\" " + "size=\"4\" face=\"Arial\"><b> Test Smells: <body></b>" + novo_nome_primeiro_vertice + " <br>  "+ "<HtMl><b>Occurrence: <body></b>" + dados.get(j).valor + "</font></p></html>";
                            metodo.setToolTipText(html_metodo);
                            classe.add(metodo);
                        }
                    }
                    analisados.add(projeto);
                }
            }
        }else{
            for (int i = 0; i < dados.size(); i++){
                String classe_analisada = dados.get(i).classe;
                if (!foiAnalisado(analisados, classe_analisada)){
                    classe = new JPanel();
                    classe.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    classe.setBackground(Configurations.corClasse); //seta a cor de fundo
                    classe.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaClasse, Configurations.larguraBorda)); // seta a borda
                    classe.setPreferredSize(new Dimension(204, Configurations.alturaClasse));
                    String novo_nome_segundo_vertice = classe_analisada.substring(0, classe_analisada.length()-2);
                    String html_classe = "<html><p><font color=\"#000000\" " + "size=\"4\" face=\"Arial\"><b> Test Class: <body></b>" + novo_nome_segundo_vertice +"</font></p></html>";
                    classe.setToolTipText(html_classe);
                    pacote.add(classe);
                    for (int j = i; j < dados.size(); j++){
                        if (dados.get(j).classe.equals(classe_analisada)){
                            JPanel metodo = new JPanel();
                            metodo.setBackground(dados.get(j).cor);
                            metodo.setBorder(BorderFactory.createLineBorder((Color) Configurations.bordaMetodo, Configurations.larguraBorda)); // seta a borda
                            metodo.setPreferredSize(new Dimension (Configurations.larguraMetodo, Configurations.alturaMetodo)); // seta o tamanho
                            metodo.setMaximumSize(metodo.getPreferredSize());
                            metodo.setMinimumSize(metodo.getPreferredSize());
                            String novo_nome_primeiro_vertice = dados.get(j).nome.substring(0, dados.get(j).nome.length()-2);
                            String html_metodo = "<html><p><font color=\"#000000\" " + "size=\"4\" face=\"Arial\"><b> Test Smells: <body></b>" + novo_nome_primeiro_vertice + " <br>  "+ "<HtMl><b>Occurrence: <body></b>" + dados.get(j).valor +" <br>  "+  "<HtMl><b>Test Class: <body></b>" + novo_nome_segundo_vertice + "</font></p></html>";
                            if(filtro.equals("Methods")){
                                for(ClassMethod obj: l){
                                    if(obj.testSmell.equals(novo_nome_primeiro_vertice) && obj.classe.equals(novo_nome_segundo_vertice)){
                                        html_metodo = "<html><p><font color=\"#000000\" " + "size=\"4\" face=\"Arial\"><b> Test Smells: <body></b>" + novo_nome_primeiro_vertice + " <br>  "+ "<HtMl><b>Occurrence: <body></b>" + dados.get(j).valor +" <br>  " +  "<HtMl><b>Test Class: <body></b>" + novo_nome_segundo_vertice +  "<br> <HtMl><b>Metodos: <body></b>" + obj.metodos +"</font></p></html>";
                                    }
                                }
                            }
                            metodo.setToolTipText(html_metodo);
                            classe.add(metodo);
                        }
                    }
                    analisados.add(classe_analisada);
                }
            }
        }
        return analisados.size();
    }

    private boolean foiAnalisado(List<String> analisados, String classe){
        for (String analisado : analisados) {
            if (analisado.equals(classe)) {
                return true;
            }
        }
        return false;
    }


    private List<ClassMethod> CriaListaDeMetodos(String path){
        List<ClassMethod> listaMetodosClasse1 = new ArrayList<>();
        try {
            List listaMetodos = new ArrayList();
            String linha = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(VIRGULA);
                listaMetodos.add(dados);
            }

            for (int i = 0; i < listaMetodos.size(); i++) {
                boolean tem = false;
                String[] dado_linha = (String[]) listaMetodos.get(i);
                for (ClassMethod obj : listaMetodosClasse1) {
                    if (dado_linha[1].equals(obj.classe) && dado_linha[7].equals(obj.testSmell)) {
                        tem = true;
                    }
                }
                if (tem == false) {
                    listaMetodosClasse1.add(new ClassMethod(dado_linha[1], dado_linha[7]));
                }
            }
            for (ClassMethod obj : listaMetodosClasse1) {
                for (int i = 0; i < listaMetodos.size(); i++) {
                    String[] dado_linha = (String[]) listaMetodos.get(i);
                    if (obj.classe.equals(dado_linha[1]) && obj.testSmell.equals(dado_linha[7])) {
                        String begin;
                        String end;
                        try {
                            begin = dado_linha[9];
                        }catch (Exception e){
                            begin = "0";
                        }
                        try {
                            end = dado_linha[10];
                        }catch (Exception e){
                            end = "0";
                        }
                        obj.addMethods(new MethodData(dado_linha[8], begin, end));
                    }
                }
            }
            return listaMetodosClasse1;
        } catch (Exception e) {

        }
        return listaMetodosClasse1;
    }
    
    private void iniciaProcessamento() {
		progress.setValue(0);
		pnlProgress.add(progress);
		
		new Thread() {
			
			@Override
			public void run(){
				progress.setValue(0);
				progressoT.run();
	        try {
	            System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	            Graph graph1 = new MultiGraph("TSVizzEvolution");
	    		//setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePathDefault1.getText())));
	            String linha = null;
	
	            List listaDeLinhasInt = new ArrayList();
	            List listaDeLinhas = new ArrayList();
	            List listaMetodos = new ArrayList();
	            String cabecalho;
	
	            cabecalho = reader.readLine();
	
	            String[] cabecalhoLista = cabecalho.split(VIRGULA);
	            for (int i = 10; i < cabecalhoLista.length; i++) {
	                graph1.addNode(cabecalhoLista[i] + "_1");
	                Node n = graph1.getNode(cabecalhoLista[i] + "_1");
	                n.setAttribute("ui.label", cabecalhoLista[i] + "_1");
	                n.addAttribute("ui.class", "quadradoTS");
	                double x = (Math.random() * ((1000000) + 1));
	                double y = (Math.random() * ((1000000) + 1));
	                n.setAttribute("x", x);
	                n.setAttribute("y", y);
	
	            }
				progress.setValue(55);
	            if (cabecalho != null) {
	                while ((linha = reader.readLine()) != null) {
	                    String[] dados = linha.split(VIRGULA);
	                    listaDeLinhas.add(dados);
	                    int[] valorInteiros = new int[dados.length];
	
	                    for (int i = 0; i < dados.length; i++) {
	                        valorInteiros[i] = converteInteiro(String.valueOf(dados[i]));
	                    }
	                    listaDeLinhasInt.add(valorInteiros);
	                }
	                
	            }
	
	            BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePathDefault2.getText())));
	            String linha2 = null;
	
	            List listaDeLinhasInt2 = new ArrayList();
	            List listaDeLinhas2 = new ArrayList();
	            String cabecalho2;
	
	            cabecalho2 = reader2.readLine();
	
	            String[] cabecalhoLista2 = cabecalho2.split(VIRGULA);
	            for (int i = 10; i < cabecalhoLista2.length; i++) {
	                graph1.addNode(cabecalhoLista2[i] + "_2");
	                Node n = graph1.getNode(cabecalhoLista2[i] + "_2");
	                n.setAttribute("ui.label", cabecalhoLista2[i] + "_2");
	                n.addAttribute("ui.class", "quadradoTS");
	                double x = (Math.random() * ((1000000) + 1));
	                double y = (Math.random() * ((1000000) + 1));
	                n.setAttribute("x", x);
	                n.setAttribute("y", y);
	
	            }
	            if (cabecalho2 != null) {
	                while ((linha2 = reader2.readLine()) != null) {
	                    String[] dados = linha2.split(VIRGULA);
	                    listaDeLinhas2.add(dados);
	                    int[] valorInteiros = new int[dados.length];
	
	                    for (int i = 0; i < dados.length; i++) {
	                        valorInteiros[i] = converteInteiro(String.valueOf(dados[i]));
	                    }
	                    listaDeLinhasInt2.add(valorInteiros);
	                }
	
	            }
	
	            //----------------------------------------------------------------------------------------------------------
	            progress.setValue(75);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            String f = "All Test Classes";
	            String selecionado = (String) cbTimeline.getSelectedItem();///////
	            int coluna = 0;
	            if (selecionado.equals("Project")) {
	                coluna = 5;
	                f = "Project";
	            }else {
	                coluna = 6;
	            }
	            List<ClassMethod> listaMetodosClasse1 = CriaListaDeMetodos(txtFilePathMethod.getText());
               // txtFilePathMethod2.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\all_report_by_testsmells.csv"); //depois tirar essa linha
	            List<ClassMethod> listaMetodosClasse2 = CriaListaDeMetodos(txtFilePathMethod2.getText());
	            List<Data> l1 = retornaDados(txtFilePathDefault1.getText(), f);
	            List<Data> l2 = retornaDados(txtFilePathDefault2.getText(), f);
	            String filtro = "";
	            progress.setValue(78);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            try {
	            	 if (selecionado.equals("Project") || selecionado.equals("All Test Classes")) {
	                    CriaGrafoCompleto(listaDeLinhasInt, listaDeLinhas, cabecalhoLista, graph1, coluna, 1, txtFilePathDefault1.getText(), selecionado, l1);
	                    CriaGrafoCompleto(listaDeLinhasInt2, listaDeLinhas2, cabecalhoLista2, graph1, coluna, 2, txtFilePathDefault2.getText(), selecionado, l2);
	                } else {
	                	
	                }
	            	graph1 = removeVerticesDoisLados(graph1);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	           // String path = System.getProperty("user.dir").replace('\\', '/');
                CriaLegenda(graph1);
	            graph1.addAttribute("ui.stylesheet", "url('tsvizzevolution/Config.css')");
	        	progress.setValue(99);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progress.setValue(100);
				progressoT.interrupt();
	            if (graph1.getNodeCount() == 0){
	                String msg = "";
	                if (selecionado.equals("Author")) {
	                    msg = "<html>The combination Test Smells x Author does not exist!";
	                }
	                if (selecionado.equals("A Specific Test Smells")) {
	                    msg = "<html>The selected Test Smells has no occurrences in the selected csv file!";
	                }
					if (selecionado.equals("Methods")) {
						msg = "<html>The combination Test Class x Test Smells does not exist!";
					}
	                JOptionPane optionPane = new JOptionPane();
	                optionPane.setMessage(msg);
	                optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	                JDialog dialog = optionPane.createDialog(null, "Warning");
	                dialog.setVisible(true);
	                
	            }else {
//	                v.disableAutoLayout();
//					v.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	            }
        } catch (IOException ex) {
            Logger.getLogger(TimelineView.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}.start();
}
    private static Graph CriaLegenda(Graph graph1) {
        SpriteManager sm = new SpriteManager(graph1);
        Sprite legenda = sm.addSprite("legenda");
        legenda.setPosition(StyleConstants.Units.PX, 720, 500, 0);
        graph1.addAttribute("ui.stylesheet" , "" +
                "sprite#legenda  {" +
                "   size: 5px; " +
                "   fill-color: white;  " +
                "   icon-mode: at-left; " +
                "   icon: url('tsvizzevolution/legenda.png');" +
                "}"
        );
        Viewer v = graph1.display();
        v.disableAutoLayout();
        v.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        return graph1;
    }
    
    private static void CriaGrafoCompleto(List listaDeLinhasInt, List listaDeLinhas, String[] cabecalho, Graph graph1, int coluna, int flag, String file, String filtro, List<Data> l) throws IOException {
        String complemento = "";
        if (flag == 1){
            complemento = "_1";
        }else{
            complemento = "_2";
        }
        for (int i = 0; i < listaDeLinhasInt.size(); i++) {
            int[] linhaInt = (int[]) listaDeLinhasInt.get(i);
            String[] linha = (String[]) listaDeLinhas.get(i);
            try {
                graph1.addNode(linha[coluna] + complemento);
            } catch (Exception e) {
            }
            String busca = linha[coluna] + complemento;
            Node n1 = graph1.getNode(linha[coluna] + complemento);
            n1.setAttribute("ui.label", linha[coluna] + complemento);
            if (filtro.equals("Project"))
                n1.addAttribute("ui.class", "projeto");
            double x = (Math.random() * ((1000000) + 1)) + 700000;
            double y = (Math.random() * ((1000000) + 1)) + 700000;
            if (complemento.equals("_1")){
                x = (Math.random() * ((500000) + 1));
                y = (Math.random() * ((500000) + 1));
            }
            n1.setAttribute("x", x);
            n1.setAttribute("y", y);
            for (int j = 10; j < linhaInt.length; j++) {
                if (linhaInt[j] != 0) {
                    try {
                        graph1.addEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento, cabecalho[j] + complemento, linha[coluna] + complemento);
                        Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento);
                        int valor = retornaDadosDoisNos(cabecalho[j], linha[coluna], filtro, l);
                        e.setAttribute("ui.label", valor);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private static void CriaGrafoParcialAutor(List listaDeLinhasInt, List listaDeLinhas, String[] cabecalho, Graph graph1, String nome, String nomeAutor, int coluna, int flag, String file, List<Data> l) throws IOException {
        String complemento = "";
        if (flag == 1){
            complemento = "_1";
        }else{
            complemento = "_2";
        }
        String filtro_autor = nomeAutor;
        int colunaAutor = 1;
        List<Data> l1 = retornaDadosAutores(file);
        if (nomeAutor.equals("All")){
            ArrayList<String> nomeAutores = new ArrayList<>();
            for (int i = 0; i < listaDeLinhas.size(); i++){
                String[] linha = (String[]) listaDeLinhas.get(i);
                if (!nomeAutores.contains(linha[1])){
                    nomeAutores.add(linha[1]);
                }
            }
            for (int x = 0; x < nomeAutores.size(); x++){
                nomeAutor = nomeAutores.get(x);
                graph1.addNode(nomeAutor + complemento);
                Node autor = graph1.getNode(nomeAutor + complemento);
                autor.setAttribute("ui.label", nomeAutor + complemento);
                autor.addAttribute("ui.class", "boneco");
                double x1 = (Math.random() * ((1000000) + 1)) + 700000;
                double y = (Math.random() * ((1000000) + 1)) + 700000;
                if (complemento.equals("_1")){
                    x1 = (Math.random() * ((500000) + 1));
                    y = (Math.random() * ((500000) + 1));
                }
                autor.setAttribute("x", x1);
                autor.setAttribute("y", y);

                for (int i = 0; i < listaDeLinhasInt.size(); i++) {
                    int[] linhaInt = (int[]) listaDeLinhasInt.get(i);
                    String[] linha = (String[]) listaDeLinhas.get(i);
                    try {
                        graph1.addNode(linha[coluna] + complemento);
                    } catch (Exception e) {
                    }
                    Node n1 = graph1.getNode(linha[coluna] + complemento);
                    n1.setAttribute("ui.label", linha[coluna] + complemento);
                    x1 = (Math.random() * ((1000000) + 1)) + 700000;
                    y = (Math.random() * ((1000000) + 1)) + 700000;
                    if (complemento.equals("_1")){
                        x1 = (Math.random() * ((500000) + 1));
                        y = (Math.random() * ((500000) + 1));
                    }
                    n1.setAttribute("x", x1);
                    n1.setAttribute("y", y);
                    for (int j = 10; j < linhaInt.length; j++) {
                        if (linhaInt[j] != 0) {
                            if (nomeAutor.equals(linha[colunaAutor]) || nome.equals(cabecalho[j])) {
                                if (nome.equals(cabecalho[j])) {
                                    try {
                                        graph1.addEdge(cabecalho[j] + complemento + " " + linha[colunaAutor] + complemento, cabecalho[j] + complemento, linha[colunaAutor] + complemento);
                                        Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[colunaAutor] + complemento);
                                        int valor = retornaDadosAutorMetodo(linha[colunaAutor], cabecalho[j], file, l1);
                                        e.setAttribute("ui.label", valor);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }

                }
                if (complemento.equals("_2")) {
                    boolean stop = false;
                    while (!stop) {
                        boolean Flag = false;
                        for (int i = 0; i < graph1.getNodeCount(); i++) {
                            Node n1 = graph1.getNode(i);
                            if (n1.getDegree() == 0) {
                                if (!(n1.getId().equals(nome + "_1") || n1.getId().equals(nome + "_2"))) {
                                    Flag = true;
                                    graph1.removeNode(n1);
                                    break;
                                }
                            }
                        }
                        if (!Flag) {
                            stop = true;
                        }
                    }
                }
            }
        }else {
            graph1.addNode(nomeAutor + complemento);
            Node autor = graph1.getNode(nomeAutor + complemento);
            autor.setAttribute("ui.label", nomeAutor + complemento);
            autor.addAttribute("ui.class", "boneco");
            double x = (Math.random() * ((1000000) + 1)) + 700000;
            double y = (Math.random() * ((1000000) + 1)) + 700000;
            if (complemento.equals("_1")){
                x = (Math.random() * ((500000) + 1));
                y = (Math.random() * ((500000) + 1));
            }
            autor.setAttribute("x", x);
            autor.setAttribute("y", y);
            int soma_valor = 0;
            for (int i = 0; i < listaDeLinhasInt.size(); i++) {
                int[] linhaInt = (int[]) listaDeLinhasInt.get(i);
                String[] linha = (String[]) listaDeLinhas.get(i);
                try {
                    graph1.addNode(linha[coluna] + complemento);
                } catch (Exception e) {
                }
                Node n1 = graph1.getNode(linha[coluna] + complemento);
                n1.setAttribute("ui.label", linha[coluna] + complemento);

                double x1 = (Math.random() * ((1000000) + 1)) + 700000;
                y = (Math.random() * ((1000000) + 1)) + 700000;
                if (complemento.equals("_1")){
                    x = (Math.random() * ((500000) + 1));
                    y = (Math.random() * ((500000) + 1));
                }
                n1.setAttribute("x", x1);
                n1.setAttribute("y", y);
                for (int j = 10; j < linhaInt.length; j++) {
                    if (linhaInt[j] != 0) {
                        if (nome.equals(linha[coluna]) || nome.equals(cabecalho[j]) && linha[colunaAutor].equals(nomeAutor)) {
                            try {
                                graph1.addEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento, cabecalho[j] + complemento, linha[coluna] + complemento);
                                Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento);
                                int valor = retornaDadosDoisNos(cabecalho[j], linha[coluna], "All Test Classes", l);
                                e.setAttribute("ui.label", valor);
                                soma_valor += valor;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                for (int j = 10; j < linhaInt.length; j++) {
                    if (linhaInt[j] != 0) {
                        if (nomeAutor.equals(linha[colunaAutor]) || nome.equals(cabecalho[j])) {
                            if (nome.equals(cabecalho[j])) {
                                try {
                                    graph1.addEdge(cabecalho[j] + complemento + " " + linha[colunaAutor] + complemento, cabecalho[j] + complemento, linha[colunaAutor] + complemento);
                                    Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[colunaAutor] + complemento);
                                    e.setAttribute("ui.label", soma_valor);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }

            }
            if (complemento.equals("_2")) {
                boolean stop = false;
                while (!stop) {
                    boolean Flag = false;
                    for (int i = 0; i < graph1.getNodeCount(); i++) {
                        Node n1 = graph1.getNode(i);
                        if (n1.getDegree() == 0) {
                            if (!(n1.getId().equals(nome + "_1") || n1.getId().equals(nome + "_2"))) {
                                Flag = true;
                                graph1.removeNode(n1);
                                break;
                            }
                        }
                    }
                    if (!Flag) {
                        stop = true;
                    }
                }
            }
        }
    }

    private static void CriaGrafoParcial(List listaDeLinhasInt, List listaDeLinhas, String[] cabecalho, Graph graph1, String nome, int coluna, int flag, String file, List<Data> l) throws IOException {
        String complemento = "";
        if (flag == 1){
            complemento = "_1";
        }else{
            complemento = "_2";
        }
        for (int i = 0; i < listaDeLinhasInt.size(); i++) {
            int[] linhaInt = (int[]) listaDeLinhasInt.get(i);
            String[] linha = (String[]) listaDeLinhas.get(i);
            try {
                graph1.addNode(linha[coluna] + complemento);
            } catch (Exception e) {
            }
            Node n1 = graph1.getNode(linha[coluna] + complemento);
            n1.setAttribute("ui.label", linha[coluna] + complemento);
            double x = (Math.random() * ((1000000) + 1)) + 700000;
            double y = (Math.random() * ((1000000) + 1)) + 700000;
            if (complemento.equals("_1")){
                x = (Math.random() * ((500000) + 1));
                y = (Math.random() * ((500000) + 1));
            }
            n1.setAttribute("x", x);
            n1.setAttribute("y", y);
            for (int j = 10; j < linhaInt.length; j++) {
                if (linhaInt[j] != 0) {
                    if (nome.equals(linha[coluna]) || nome.equals(cabecalho[j])) {
                        try {
                            graph1.addEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento, cabecalho[j] + complemento, linha[coluna] + complemento);
                            Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento);
                            int valor = retornaDadosDoisNos(cabecalho[j], linha[coluna], "All Test Classes", l);
                            e.setAttribute("ui.label", valor);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

        if (complemento.equals("_2")) {
            boolean stop = false;
            while (!stop) {
                boolean Flag = false;
                for (int i = 0; i < graph1.getNodeCount(); i++) {
                    Node n1 = graph1.getNode(i);
                    if (n1.getDegree() == 0) {
                        if (!(n1.getId().equals(nome + "_1") || n1.getId().equals(nome + "_2"))) {
                            Flag = true;
                            graph1.removeNode(n1);
                            break;
                        }
                    }
                }
                if (!Flag) {
                    stop = true;
                }
            }
        }
    }

    private static void CriaGrafoMetodos(List listaClassesInt, List listaClasses, String[] cabecalho, Graph graph1, String nome, String classe, int coluna, int flag, String file, List<Data> l, List<ClassMethod> listaMetodosClasse, String metodoFiltro) throws IOException {
        String complemento = "";
        if (flag == 1){
            complemento = "_1";
        }else{
            complemento = "_2";
        }
        for (int i = 0; i < listaClassesInt.size(); i++) {
            int[] linhaInt = (int[]) listaClassesInt.get(i);
            String[] linha = (String[]) listaClasses.get(i);
            try {
                graph1.addNode(linha[coluna] + complemento);
            } catch (Exception e) {
            }
            Node n1 = graph1.getNode(linha[coluna] + complemento);
            n1.setAttribute("ui.label", linha[coluna] + complemento);
            double x = (Math.random() * ((1000000) + 1));
            double y = (Math.random() * ((1000000) + 1));
            n1.setAttribute("x", x);
            n1.setAttribute("y", y);
            // n1.setAttribute("layout.weight", 10);
            n1.setAttribute("edges","layout.weight:4");
            for (int j = 10; j < linhaInt.length; j++) {
                if (linhaInt[j] != 0) {
                    if (classe.equals(linha[coluna]) && nome.equals(cabecalho[j])) {
                        try{
                            graph1.addEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento, cabecalho[j] + complemento, linha[coluna] + complemento);
                            Edge e = graph1.getEdge(cabecalho[j] + complemento + " " + linha[coluna] + complemento);
                            int valor = retornaDadosDoisNos(cabecalho[j], linha[coluna], "All Test Classes", l);
                            e.setAttribute("ui.label", valor);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        if (graph1.getEdgeCount() > 0) {
            for (ClassMethod obj : listaMetodosClasse) {
                for (MethodData metodo : obj.metodos) {
                    if (obj.classe.equals(classe)) {
                        try {
                            graph1.addNode(metodo.metodo + complemento);
                            Node n1 = graph1.getNode(metodo.metodo + complemento);
                            n1.setAttribute("ui.label", metodo.metodo+ complemento + "," + metodo.begin + "-" + metodo.end);
                            n1.addAttribute("ui.class", "metodo");
                            double x = (Math.random() * ((1000000) + 1) + 1000000);
                            double y = (Math.random() * ((1000000) + 1) + 1000000);
                            n1.setAttribute("x", x);
                            n1.setAttribute("y", y);
                            if (metodo.metodo.equals(metodoFiltro))
                                graph1.addEdge(metodo.metodo + complemento, obj.classe + complemento, metodo.metodo + complemento);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        if (complemento.equals("_2")) {
            boolean stop = false;
            while (!stop) {
                boolean Flag = false;
                for (int i = 0; i < graph1.getNodeCount(); i++) {
                    Node n1 = graph1.getNode(i);
                    if (n1.getDegree() == 0) {
                        if (!(n1.getId().equals(nome + "_1") || n1.getId().equals(nome + "_2"))) {
                            Flag = true;
                            graph1.removeNode(n1);
                            break;
                        }
                    }
                }
                if (!Flag) {
                    stop = true;
                }
            }
        }
    }

    private void cbTimelineActionPerformed(ActionEvent evt) {
    }

    private void btnGerarUploadActionPerformed(ActionEvent evt) throws IOException {
        String[] a = null;
        String[] b = null;
        String[] c = null;
        String[] a2 = null;
        String[] b2 = null;
        String[] c2 = null;
       // txtFilePathDefault1.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\commons-io_testsmesll_2_1.csv");
     //   txtFilePathMethod.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\all_report_by_testsmells.csv");
        a2 = carrega_lista_linhas(txtFilePathDefault1.getText());
        b2 = carrega_lista_cabecalho(txtFilePathDefault1.getText());
        c2 = carrega_lista_autor(txtFilePathDefault1.getText());

      //  txtFilePathDefault2.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\commons-io_testsmesll_2_6.csv");
       // txtFilePathMethod2.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\all_report_by_testsmells.csv");
        a = carrega_lista_linhas(txtFilePathDefault2.getText());
        b = carrega_lista_cabecalho(txtFilePathDefault2.getText());
        c = carrega_lista_autor(txtFilePathDefault2.getText());

        a = concatena(a, a2);
        b = concatena(b, b2);
        c = concatena_autor(c, c2);

        
		btnVisualizeTimeline.setEnabled(true);

	}
    public static int retornaDadosDoisNos(String a, String b, String filtro, List<Data> l){
        if (filtro.equals("Project")) {
            for (int i = 0; i < l.size(); i++) {
                Data d = l.get(i);
                if (d.nome.equals(a) && d.projeto.equals(b)) {
                    return d.valor;
                }
            }
            return 0;
        }else{
            for (int i = 0; i < l.size(); i++) {
                Data d = l.get(i);
                if (d.nome.equals(a) && d.classe.equals(b)) {
                    return d.valor;
                }
            }
            return 0;
        }
    }

    public static int retornaDadosAutorMetodo(String autor, String metodo, String file, List<Data> l){
        for (int i = 0; i < l.size(); i++) {
            Data d = l.get(i);
            if (d.autor.equals(autor) && d.nome.equals(metodo)) {
                return d.valor;
            }
        }
        return 0;
    }

    public static List<Data> retornaDadosAutores(String file){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String linha = null;

            List listaDeLinhasInt = new ArrayList();
            List listaDeLinhas = new ArrayList();
            String cabecalho;

            cabecalho = reader.readLine();
            String[] cabecalhoLista = cabecalho.split(VIRGULA);
            if (cabecalho != null) {
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(VIRGULA);
                    listaDeLinhas.add(dados);
                    int[] valorInteiros = new int[dados.length];

                    for (int i = 0; i < dados.length; i++) {
                        valorInteiros[i] = converteInteiro(String.valueOf(dados[i]));
                    }
                    listaDeLinhasInt.add(valorInteiros);
                }

            }
            List<Data> resultado = new ArrayList<>();
            List<String> autores = new ArrayList<>();
            int coluna = 1;
            for (int i = 0; i < listaDeLinhas.size(); i++){
                String[] linha_analisada = (String[]) listaDeLinhas.get(i);
                String autor = linha_analisada[coluna];
                boolean flag = false;
                for (int j = 0; j < autores.size(); j++){
                    if (autores.get(j).equals(autor)){
                        flag = true;
                    }
                }
                if (!flag){
                    autores.add(autor);
                }
            }
            for (int i = 0; i < autores.size(); i ++){
                String autor = autores.get(i);
                coluna = 10;
                for (int j = 10; j < cabecalhoLista.length; j++) {
                    int soma = 0;
                    for (int k = 0; k < listaDeLinhas.size(); k++) {
                        String[] linha_analisada = (String[]) listaDeLinhas.get(k);
                        int[] linha_int = (int[]) listaDeLinhasInt.get(k);
                        if (linha_analisada[1].equals(autor)){
                            soma += linha_int[coluna];
                        }
                    }
                    resultado.add(new Data(cabecalhoLista[j], soma, autor));
                    coluna += 1;
                }
            }
            return resultado;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Graph removeVertices(Graph graph1, String filtro){
        boolean stop = false;
        while (!stop) {
            boolean Flag = false;
            for (int i = 0; i < graph1.getNodeCount(); i++) {
                Node n1 = graph1.getNode(i);
                if (n1.getDegree() == 0) {
                    if (!(n1.getId().equals(filtro + "_1") || n1.getId().equals(filtro + "_2"))) {
                        Flag = true;
                        graph1.removeNode(n1);
                        break;
                    }
                }
            }
            if (!Flag) {
                stop = true;
            }
        }
        return graph1;
    }

    public static Graph removeVerticesDoisLados(Graph graph1){
        for (int i = 0; i < graph1.getNodeCount(); i++) {
            Node n1 = graph1.getNode(i);
            String complemento = n1.getId().substring(n1.getId().length()-2, n1.getId().length());
            if (complemento.equals("_1")){
                Node n2 = graph1.getNode(n1.getId().substring(0, n1.getId().length()-2) + "_2");
                if (n2 != null){
                    if(n2.getDegree() == 0 && n1.getDegree() == 0){
                        graph1.removeNode(n2);
                        graph1.removeNode(n1);
                        i --;
                    }
                }
            }else{
                Node n2 = graph1.getNode(n1.getId().substring(0, n1.getId().length()-2) + "_1");
                if (n2 != null){
                    if(n2.getDegree() == 0 && n1.getDegree() == 0){
                        graph1.removeNode(n2);
                        graph1.removeNode(n1);
                        i --;
                    }
                }
            }
        }
        return graph1;
    }


    public static Graph criaNosInexistentes(Graph graph, List listaDeLinhas){
        List<String> todasClasses = retornaTodasClasses(listaDeLinhas);
        for (int i = 0; i < graph.getNodeCount(); i++) {
            Node n1 = graph.getNode(i);
            if (in(n1.getId().substring(0, n1.getId().length()-2), todasClasses)) {
                char complemento = n1.getId().charAt(n1.getId().length() - 1);
                char adicional = ' ';
                if (complemento == '1') {
                    adicional = '2';
                } else {
                    adicional = '1';
                }
                String nomeAlterado = n1.getId();
                nomeAlterado = nomeAlterado.substring(0, nomeAlterado.length() - 1) + adicional;
                boolean achou = false;
                for (int j = 0; j < graph.getNodeCount(); j++) {
                    Node no = graph.getNode(j);
                    if (no.getId().equals(nomeAlterado)) {
                        achou = true;
                    }
                }
                if (!achou) {
                    graph.addNode(nomeAlterado);
                    Node n = graph.getNode(nomeAlterado);
                    n.setAttribute("ui.label", nomeAlterado);
                    n.addAttribute("ui.class", "x");
                    n.setAttribute("x", -1000);
                    n.setAttribute("y", 0);
                }
            }
        }

        return graph;
    }

    public static List<Data> OrdenaPeloNumeroOcorrencias(List<Data> l){
        Data[] v = new Data[l.size()];
        for (int i = 0; i < v.length; i++){
            v[i] = l.get(i);
        }

        for(int i = 0; i < v.length - 1; i++) {
            for(int j = 0; j < v.length - 1 - i; j++) {
                if(v[j].valor < v[j + 1].valor) {
                    Data aux = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = aux;
                }
            }
        }
        l = new ArrayList<Data>();
        for (int i = 0; i < v.length; i++){
            l.add(v[i]);
        }
        return l;
    }

    public static void arrumaDados(List<Data> dados1, List<Data> dados2){
        for (int i = 0; i < dados1.size(); i++){
            if (dados1.get(i).valor == 0){
                boolean achou = false;
                int j_achado = -1;
                for (int j = 0; j < dados2.size(); j++){
                    if (dados2.get(j).nome.equals(dados1.get(i).nome) && dados2.get(j).classe.equals(dados1.get(i).classe)){
                        achou = true;
                        j_achado = j;
                        break;
                    }
                }
                if (achou){
                    if (dados2.get(j_achado).valor == 0){
                        dados2.remove(dados2.get(j_achado));
                        dados1.remove(dados1.get(i));
                        i--;
                    }
                }else{
                    dados1.remove(dados1.get(i));
                    i--;
                }
            }
        }
        for (int i = 0; i < dados2.size(); i++){
            if (dados2.get(i).valor == 0){
                boolean achou = false;
                int j_achado = -1;
                for (int j = 0; j < dados1.size(); j++){
                    if (dados1.get(j).nome.equals(dados2.get(i).nome) && dados1.get(j).classe.equals(dados2.get(i).classe)){
                        achou = true;
                        j_achado = j;
                        break;
                    }
                }
                if (achou){
                    if (dados1.get(j_achado).valor == 0){
                        dados1.remove(dados1.get(j_achado));
                        dados2.remove(dados2.get(i));
                        i--;
                    }
                }else{
                    dados2.remove(dados2.get(i));
                    i--;
                }
            }
        }
        for (int i =0; i < dados1.size(); i++){
            boolean tem = false;
            for (int j = 0; j < dados2.size(); j++){
                if (dados1.get(i).nome.equals(dados2.get(j).nome) && dados1.get(i).classe.equals(dados2.get(j).classe)){
                    tem = true;
                    if (dados1.get(i).valor > 0){
                        dados1.get(i).cor = Color.green;
                    }else{
                        dados1.get(i).cor = Color.gray;
                    }
                    if (dados1.get(i).valor > 0) {
                        dados2.get(j).cor = Color.YELLOW;
                        if (dados1.get(i).valor > dados2.get(j).valor) {
                            if(dados2.get(j).valor > 0){
                                dados2.get(j).cor = Color.BLUE;
                            }else{
                                dados2.get(j).cor = Color.gray;
                            }
                        }
                        if (dados1.get(i).valor < dados2.get(j).valor) {
                            if(dados2.get(j).valor > 0){
                                dados2.get(j).cor = Color.red;
                            }else{
                                dados2.get(j).cor = Color.gray;
                            }
                        }
                    }else{
                        dados2.get(j).cor = Color.GREEN;
                    }
                }
            }
            if (!tem){
                dados2.add(new Data(dados1.get(i).nome, 0, dados1.get(i).classe, dados1.get(i).projeto, Color.GRAY));
                dados1.get(i).cor = Color.green;
            }
        }
        for (int i =0; i < dados2.size(); i++){
            boolean tem = false;
            for (int j = 0; j < dados1.size(); j++){
                if (dados2.get(i).nome.equals(dados1.get(j).nome) && dados2.get(i).classe.equals(dados1.get(j).classe)){
                    tem = true;
                }
            }
            if (!tem){
                dados1.add(new Data(dados2.get(i).nome, 0, dados2.get(i).classe, dados1.get(i).projeto, Color.GRAY));
                dados2.get(i).cor = Color.green;
            }
        }
        for (int i = 0; i < dados1.size(); i++){
          dados1.get(i).nome = dados1.get(i).nome + "_1";
          if (dados1.get(i).classe != null){
              dados1.get(i).classe = dados1.get(i).classe + "_1";
          }
        }
        for (int i = 0; i < dados2.size(); i++){
            dados2.get(i).nome = dados2.get(i).nome + "_2";
            if (dados2.get(i).classe != null) {
                dados2.get(i).classe = dados2.get(i).classe + "_2";
            }
        }
        Collections.sort(dados1);
        Collections.sort(dados2);
    }

    public static List<Data> retornaDados(String file, String filtro){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String linha = null;

            List listaDeLinhasInt = new ArrayList();
            List listaDeLinhas = new ArrayList();
            String cabecalho;

            cabecalho = reader.readLine();
            String[] cabecalhoLista = cabecalho.split(VIRGULA);
            if (cabecalho != null) {
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(VIRGULA);
                    listaDeLinhas.add(dados);
                    int[] valorInteiros = new int[dados.length];

                    for (int i = 0; i < dados.length; i++) {
                        valorInteiros[i] = converteInteiro(String.valueOf(dados[i]));
                    }
                    listaDeLinhasInt.add(valorInteiros);
                }

            }
            List<Data> resultado_final = new ArrayList<>();
            if (filtro.equals("Project")) {

                int coluna = 10;

                for (int i = 10; i < cabecalhoLista.length; i++) {
                    int soma = 0;
                    String nome_projeto = "";
                    for (int j = 0; j < listaDeLinhas.size(); j++) {
                        int[] linha_int = (int[]) listaDeLinhasInt.get(j);
                        String[] linha_analisada = (String[]) listaDeLinhas.get(j);
                        soma += linha_int[coluna];
                        nome_projeto = linha_analisada[5];
                    }
                    resultado_final.add(new Data(cabecalhoLista[i], soma, "", nome_projeto));
                    coluna += 1;

                }
            }
            if (filtro.equals("All Test Classes") || filtro.equals("Methods")){
                List<String> classes = new ArrayList<>();
                int coluna = 6;
                for (int i = 0; i < listaDeLinhas.size(); i++){
                    String[] linha_analisada = (String[]) listaDeLinhas.get(i);
                    String classe = linha_analisada[coluna];
                    boolean flag = false;
                    for (int j = 0; j < classes.size(); j++){
                        if (classes.get(j).equals(classe)){
                            flag = true;
                        }
                    }
                    if (!flag){
                        classes.add(classe);
                    }
                }
                for (int i = 0; i < classes.size(); i ++){
                    String classe_analisada = classes.get(i);
                    coluna = 10;
                    for (int j = 10; j < cabecalhoLista.length; j++) {
                        int soma = 0;
                        String nome_projeto = "";
                        for (int k = 0; k < listaDeLinhas.size(); k++) {
                            String[] linha_analisada = (String[]) listaDeLinhas.get(k);
                            int[] linha_int = (int[]) listaDeLinhasInt.get(k);
                            if (linha_analisada[6].equals(classe_analisada)){
                                soma += linha_int[coluna];
                                nome_projeto = linha_analisada[5];
                            }
                        }
                        resultado_final.add(new Data(cabecalhoLista[j], soma, classe_analisada, nome_projeto));
                        coluna += 1;
                    }
                }
            }
            return resultado_final;
        }catch (Exception e){
            return null;
        }
    }

    public static void main(String args[]) throws IOException {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TimelineView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TimelineView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TimelineView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TimelineView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					new TimelineView().setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    
    public static String[] carrega_lista_linhas(String path) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String linha = null;
        List resposta = new ArrayList();
        while ((linha = reader.readLine()) != null) {
            String[] dados = linha.split(VIRGULA);
            String classe = dados[6];
            boolean flag = false;
            for(int i = 0; i < resposta.size(); i++){
            	if(resposta.get(i).equals(classe)){
            		flag = true;
            	}
            }
            if (flag == false){
            	resposta.add(classe);
            }
        }
        String[] resposta_final = new String[resposta.size() - 1];
        for(int i = 0; i < resposta.size() - 1; i++){
        	resposta_final[i] = (String) resposta.get(i+1);
        }
		return resposta_final;
    }
    
    public static String[] carrega_lista_cabecalho(String path) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String cabecalho_combo;
        cabecalho_combo = reader.readLine();
        String[] cabecalhoTest = cabecalho_combo.split(VIRGULA);
        String[] resultado = new String[cabecalhoTest.length - 10];
        for (int i = 10; i < cabecalhoTest.length; i++){
        	resultado[i - 10] = cabecalhoTest[i];
        }
		return resultado;	
    }
   
    public static String[] carrega_lista_autor(String path) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String linha = null;
        List resposta = new ArrayList();
        while ((linha = reader.readLine()) != null) {
            String[] dados = linha.split(VIRGULA);
            String classe = dados[1];
            boolean flag = false;
            for(int i = 0; i < resposta.size(); i++){
            	if(resposta.get(i).equals(classe)){
            		flag = true;
            	}
            }
            if (flag == false){
            	resposta.add(classe);
            }
        }
        resposta.remove(0);
        Collections.sort(resposta);
        resposta.remove(1);
        String[] resposta_final = new String[resposta.size()];
        for (int i = 0; i < resposta.size(); i++) {
             resposta_final[i] = (String) resposta.get(i);
        }
        return resposta_final;
    }
    @SuppressWarnings("unchecked")

    private String[] concatena_autor(String[] lista1, String[] lista2){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < lista1.length; i++){
            lista.add(lista1[i]);
        }
        for (int i = 0; i < lista2.length; i++){
            boolean achou = false;
            for (int j = 0; j < lista.size(); j++){
                if (lista.get(j).equals(lista2[i])){
                    achou = true;
                }
            }
            if (!achou){
                lista.add(lista2[i]);
            }
        }
        Collections.sort(lista);
        lista.add(0, "All");
        String[] resultado = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++){
            resultado[i] = lista.get(i);
        }
        return resultado;
    }

    private String[] concatena(String[] lista1, String[] lista2){
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < lista1.length; i++){
            lista.add(lista1[i]);
        }
        for (int i = 0; i < lista2.length; i++){
            boolean achou = false;
            for (int j = 0; j < lista.size(); j++){
                if (lista.get(j).equals(lista2[i])){
                    achou = true;
                }
            }
            if (!achou){
                lista.add(lista2[i]);
            }
        }
        Collections.sort(lista);
        String[] resultado = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++){
            resultado[i] = lista.get(i);
        }
        return resultado;
    }

    public void carregaComponentes(){
        try {
            String[] a = null;
            String[] b = null;
            String[] c = null;
            String[] a2 = null;
            String[] b2 = null;
            String[] c2 = null;

            a2 = carrega_lista_linhas(txtFilePathDefault1.getText());
            b2 = carrega_lista_cabecalho(txtFilePathDefault1.getText());
            c2 = carrega_lista_autor(txtFilePathDefault1.getText());

            a = carrega_lista_linhas(txtFilePathDefault2.getText());
            b = carrega_lista_cabecalho(txtFilePathDefault2.getText());
            c = carrega_lista_autor(txtFilePathDefault2.getText());

            a = concatena(a, a2);
            b = concatena(b, b2);
            c = concatena_autor(c, c2);
           }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List concatenaLista(List lista1, List lista2){
        List lista = new ArrayList<>();
        for (int i = 0; i < lista1.size(); i++){
            lista.add(lista1.get(i));
        }
        for (int i = 0; i < lista2.size(); i++){
            boolean achou = false;
            for (int j = 0; j < lista.size(); j++){
                if (lista.get(j).equals(lista2.get(i))){
                    achou = true;
                }
            }
            if (!achou){
                lista.add(lista2.get(i));
            }
        }
        return lista;
    }

    public static List<String> retornaTodasClasses(List listaDeLinhas){
        List<String> listaClasses = new ArrayList<>();
        for (int i = 0; i < listaDeLinhas.size(); i++){
            String[] linha = (String[]) listaDeLinhas.get(i);
            Boolean encontrou = false;
            for (int j = 0; j < listaClasses.size(); j++){
                if (listaClasses.get(j).equals(linha[6])){
                    encontrou = true;
                }
            }
            if (!encontrou){
                listaClasses.add(linha[6]);
            }
        }
        return listaClasses;
    }

    public static boolean in(String x, List<String> l){
        for (int i = 0; i < l.size(); i++){
            if (x.equals(l.get(i))){
                return true;
            }
        }
        return false;
    }

    public static String[] carrega_lista_autor_test(String path, String autor) throws IOException {
        List<String> resposta_final_array = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String[] cabecalho = reader.readLine().split(VIRGULA);
        String linha = null;
        while ((linha = reader.readLine()) != null) {
            String[] dados = linha.split(VIRGULA);
            if(autor.equals(dados[1])){
                for(int i=10; i<dados.length; i++){
                    if(!dados[i].equals("")){
                        if(!dados[i].equals("0")){
                            if(!resposta_final_array.contains(cabecalho[i]))
                                resposta_final_array.add(cabecalho[i]);
                        }
                    }
                }
            }
        }
        Collections.sort(resposta_final_array);
        String[] resposta_final = new String[resposta_final_array.size()];
        for (int i = 0; i < resposta_final_array.size(); i++) {
            resposta_final[i] = (String) resposta_final_array.get(i);
        }
        return resposta_final;
    }

    private List<ClassMethod> retorna_lista_classe_metodo() throws IOException {
        List<ClassMethod> listaMetodosClasse = new ArrayList<>();
        List listaMetodos = new ArrayList();
        String linha = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePathMethod.getText())));
        while ((linha = reader.readLine()) != null) {
            String[] dados = linha.split(VIRGULA);
            listaMetodos.add(dados);
        }

        for (int i = 0; i < listaMetodos.size(); i++) {
            boolean tem = false;
            String[] dado_linha = (String[]) listaMetodos.get(i);
            for (ClassMethod obj : listaMetodosClasse) {
                if (dado_linha[1].equals(obj.classe)) {
                    tem = true;
                }
            }
            if (tem == false) {
                listaMetodosClasse.add(new ClassMethod(dado_linha[1]));
            }
        }
        for (ClassMethod obj : listaMetodosClasse) {
            for (int i = 0; i < listaMetodos.size(); i++) {
                String[] dado_linha = (String[]) listaMetodos.get(i);
                if (obj.classe.equals(dado_linha[1])) {
                    String begin;
                    String end;
                        try {
                            begin = dado_linha[9];
                        }catch (Exception e){
                            begin = "0";
                        }
                    try {
                        end = dado_linha[10];
                    }catch (Exception e){
                        end = "0";
                    }
                    obj.addMethods(new MethodData(dado_linha[8], begin, end));
                }
            }
        }
        return listaMetodosClasse;
    }

    private void initComponents() throws IOException {
        pnlGraph = new JPanel();
		pnlTimeline = new JPanel();
	    pnlMethod = new JPanel();
	    txtFilePathDefault1 = new JTextField();
        txtFilePathDefault2 = new JTextField();
        btnSearchMethod = new JButton();
		progress = new JProgressBar(0, 100);
		pnlSelectMethod = new JPanel();
		
		pnlSelectMethod.setVisible(false);

		progress.setStringPainted(true);
		progress.setValue(0);
		progress.setSize(new Dimension(100, 23));
        pnlTimeline.setVisible(true);
        pnlMethod.setVisible(false);
                     /*if (event.getItem().equals("Graph View")) {
                         pnlMethod.setVisible(false);
                         pnlSelectMethod.setVisible(false);
                         pnlTimeline.setVisible(false);
                         btnVisualizeTimeline.setVisible(false);
 						 lblVisualizeTimeline.setVisible(false);}*/
                        /* pnlTimeline.setVisible(true);
                         btnVisualizeTimeline.setVisible(true);
                         pnlMethod.setVisible(false);
 						 lblVisualizeTimeline.setVisible(true);
                         pnlSelectMethod.setVisible(false);*/
                                         
        cbTimeline = new JComboBox<>();
        cbTimeline.setModel(new DefaultComboBoxModel<>(new String[] { "Project",  "All Test Classes", "Methods" }));
        cbTimeline.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		cbTimelineActionPerformed(evt);
            }
        });

        btnChooseFileSearch2 = new JButton();
        btnChooseFileSearch2.setText("Search ...");
        btnChooseFileSearch2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
					btnChooseFileSearch2ActionPerformed(evt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnChooseFileSearch1 = new JButton();
        btnChooseFileSearch1.setText("Search ...");
        btnChooseFileSearch1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChooseFileSearch1ActionPerformed(evt);
            }
        });
       
        btnSearchMethod.setText("Search ...");
    		btnSearchMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchMethodActionPerformed(evt);
            }
        });

        lblTimeline = new JLabel();
        lblTimeline.setText("Select the level of granularity:");

        lblVisualization = new JLabel();
        lblVisualization.setText("View type: Timeline View");

        lblCsv2 = new JLabel();
        lblCsv2.setText("Select the second .csv File (resultado_evolution1.csv):");
        lblCsv1 = new JLabel();

        lblCsv1.setText("Select the first .csv File (resultado_evolution1.csv):");
        lblCsv1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        pnlVisualization = new JPanel();

 		lblSelectTheCsvMethod = new JLabel();
 		lblSelectTheCsvMethod.setText("Select the first .csv File (''project''_result_byclasstest_testsmells.csv) :");
 		
 		txtFilePathMethod = new JTextField();
 	// txtFilePathMethod.setText("C:\\Users\\T-GAMER\\IdeaProjects\\GraphTwoVersions\\src\\tsvizzevolution\\commons-io_testsmesll_2_6.csv");
 		
 		lblSelectTheSecond = new JLabel();
 		lblSelectTheSecond.setText("Select the second .csv File (''project''_result_byclasstest_testsmells.csv) :");
 		   		
 		txtFilePathMethod2 = new JTextField();
 				
 		btnSearchMethod2 = new JButton();
 		btnSearchMethod2.setText("Search ...");
 	    btnSearchMethod2.addActionListener(new java.awt.event.ActionListener() {
 	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
 	    		try {
					btnSearchMethod2ActionPerformed(evt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	        }
 	    });
  				
		lblSelectMethod = new JLabel();
		lblSelectMethod.setText("Select a Method:");

        cbSelectMethod = new JComboBox<String>();

		pnlSelectMethod.setVisible(false);

  		lblSelectMethod.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbSelectMethod.setFont(new Font("Tahoma", Font.PLAIN, 16));
 		btnSearchMethod2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnChooseFileSearch1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnChooseFileSearch2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        cbTimeline.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTimeline.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblCsv1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblCsv2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblVisualization.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtFilePathDefault1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFilePathDefault2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFilePathMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
  		lblSelectTheCsvMethod.setFont(new Font("Tahoma", Font.PLAIN, 16));
  		btnSearchMethod.setFont(new Font("Tahoma", Font.PLAIN, 16));
  		lblSelectTheSecond.setFont(new Font("Tahoma", Font.PLAIN, 16));
 		txtFilePathMethod2.setFont(new Font("Tahoma", Font.PLAIN, 14));
 		
lblVisualizeTimeline = new JLabel();
		
		lblVisualizeTimeline.setVisible(true);
		lblVisualizeTimeline.setText("Click here to generate the visualization :");
		lblVisualizeTimeline.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlProgress = new JPanel();
		pnlProgress.setVisible(false);
		
		        btnVisualizeTimeline = new JButton();
		        btnVisualizeTimeline.setText("Generate Timeline View");
		        btnVisualizeTimeline.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent evt) {
		                btnGerarTimelineActionPerformed(evt);
		            }


		        });
		        btnVisualizeTimeline.setVisible(true);
		        btnVisualizeTimeline.setFont(new Font("Tahoma", Font.PLAIN, 16));
		   		
		   		
        GroupLayout gl_pnlSelectMethod = new GroupLayout(pnlSelectMethod);
		gl_pnlSelectMethod.setHorizontalGroup(
			gl_pnlSelectMethod.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSelectMethod.createSequentialGroup()
					.addComponent(lblSelectMethod, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbSelectMethod, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)
					.addGap(71))
		);
		gl_pnlSelectMethod.setVerticalGroup(
			gl_pnlSelectMethod.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSelectMethod.createSequentialGroup()
					.addGap(3)
					.addGroup(gl_pnlSelectMethod.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectMethod)
						.addComponent(cbSelectMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		pnlSelectMethod.setLayout(gl_pnlSelectMethod);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(pnlGraph, GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(pnlGraph, GroupLayout.PREFERRED_SIZE, 454, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(22, Short.MAX_VALUE))
		);
		   		
		GroupLayout gl_pnlGraph = new GroupLayout(pnlGraph);
		gl_pnlGraph.setHorizontalGroup(
			gl_pnlGraph.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGraph.createSequentialGroup()
					.addGroup(gl_pnlGraph.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlGraph.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_pnlGraph.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCsv1)
								.addGroup(gl_pnlGraph.createSequentialGroup()
									.addComponent(txtFilePathDefault1, GroupLayout.PREFERRED_SIZE, 536, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btnChooseFileSearch1))
								.addComponent(lblCsv2)
								.addGroup(gl_pnlGraph.createSequentialGroup()
									.addComponent(txtFilePathDefault2, GroupLayout.PREFERRED_SIZE, 536, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(btnChooseFileSearch2))
								.addComponent(pnlVisualization, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
								.addComponent(pnlSelectMethod, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_pnlGraph.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlGraph.createParallelGroup(Alignment.LEADING)
								.addComponent(pnlMethod, 0, 0, Short.MAX_VALUE)
								.addGroup(gl_pnlGraph.createSequentialGroup()
									.addComponent(pnlTimeline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(23))))
						.addGroup(gl_pnlGraph.createSequentialGroup()
							.addContainerGap()
							.addComponent(pnlProgress, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlGraph.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblVisualizeTimeline, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVisualizeTimeline, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_pnlGraph.setVerticalGroup(
			gl_pnlGraph.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlGraph.createSequentialGroup()
					.addComponent(lblCsv1)
					.addGap(6)
					.addGroup(gl_pnlGraph.createParallelGroup(Alignment.LEADING)
						.addComponent(txtFilePathDefault1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnChooseFileSearch1))
					.addGap(13)
					.addComponent(lblCsv2)
					.addGroup(gl_pnlGraph.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlGraph.createSequentialGroup()
							.addGap(6)
							.addComponent(txtFilePathDefault2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnChooseFileSearch2))
					.addGap(6)
					.addComponent(pnlVisualization, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlTimeline, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlMethod, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlGraph.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVisualizeTimeline)
						.addComponent(btnVisualizeTimeline))
					.addGap(18)
					.addComponent(pnlProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(199)
					.addComponent(pnlSelectMethod, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(75))
		);
		pnlGraph.setLayout(gl_pnlGraph);
		contentPane.setLayout(gl_contentPane);
   		
   		GroupLayout gl_pnlTimeline = new GroupLayout(pnlTimeline);
   		gl_pnlTimeline.setHorizontalGroup(
   			gl_pnlTimeline.createParallelGroup(Alignment.LEADING)
   				.addGroup(Alignment.TRAILING, gl_pnlTimeline.createSequentialGroup()
   					.addComponent(lblTimeline, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
   					.addPreferredGap(ComponentPlacement.RELATED)
   					.addComponent(cbTimeline, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
   					.addGap(32))
   		);
   		gl_pnlTimeline.setVerticalGroup(
   			gl_pnlTimeline.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_pnlTimeline.createSequentialGroup()
   					.addGroup(gl_pnlTimeline.createParallelGroup(Alignment.BASELINE)
   						.addComponent(lblTimeline)
   						.addComponent(cbTimeline, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
   					.addContainerGap(23, Short.MAX_VALUE))
   		);
 		 pnlTimeline.setLayout(gl_pnlTimeline);
   		
   		GroupLayout gl_pnlMethod = new GroupLayout(pnlMethod);
   		gl_pnlMethod.setHorizontalGroup(
   			gl_pnlMethod.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_pnlMethod.createSequentialGroup()
   					.addGroup(gl_pnlMethod.createParallelGroup(Alignment.LEADING)
   						.addComponent(lblSelectTheCsvMethod)
   						.addGroup(gl_pnlMethod.createSequentialGroup()
   							.addGap(2)
   							.addComponent(txtFilePathMethod, GroupLayout.PREFERRED_SIZE, 534, GroupLayout.PREFERRED_SIZE)
   							.addPreferredGap(ComponentPlacement.UNRELATED)
   							.addComponent(btnSearchMethod, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
   						.addComponent(lblSelectTheSecond, GroupLayout.PREFERRED_SIZE, 456, GroupLayout.PREFERRED_SIZE)
   						.addGroup(gl_pnlMethod.createSequentialGroup()
   							.addGap(2)
   							.addComponent(txtFilePathMethod2, GroupLayout.PREFERRED_SIZE, 534, GroupLayout.PREFERRED_SIZE)
   							.addPreferredGap(ComponentPlacement.UNRELATED)
   							.addComponent(btnSearchMethod2, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)))
   					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
   		);
   		gl_pnlMethod.setVerticalGroup(
   			gl_pnlMethod.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_pnlMethod.createSequentialGroup()
   					.addGap(9)
   					.addComponent(lblSelectTheCsvMethod)
   					.addPreferredGap(ComponentPlacement.RELATED)
   					.addGroup(gl_pnlMethod.createParallelGroup(Alignment.BASELINE)
   						.addComponent(btnSearchMethod, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
   						.addComponent(txtFilePathMethod, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
   					.addPreferredGap(ComponentPlacement.RELATED)
   					.addComponent(lblSelectTheSecond, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
   					.addPreferredGap(ComponentPlacement.RELATED)
   					.addGroup(gl_pnlMethod.createParallelGroup(Alignment.BASELINE)
   						.addComponent(btnSearchMethod2, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
   						.addComponent(txtFilePathMethod2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
   					.addContainerGap(41, Short.MAX_VALUE))
   		);
   		pnlMethod.setLayout(gl_pnlMethod);
   		
   		GroupLayout gl_pnlVisualization = new GroupLayout(pnlVisualization);
   		gl_pnlVisualization.setHorizontalGroup(
   			gl_pnlVisualization.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_pnlVisualization.createSequentialGroup()
   					.addComponent(lblVisualization)
   					.addContainerGap(280, Short.MAX_VALUE))
   		);
   		gl_pnlVisualization.setVerticalGroup(
   			gl_pnlVisualization.createParallelGroup(Alignment.LEADING)
   				.addGroup(gl_pnlVisualization.createSequentialGroup()
   					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
   					.addComponent(lblVisualization)
   					.addContainerGap())
   		);
   		pnlVisualization.setLayout(gl_pnlVisualization);
	}
}