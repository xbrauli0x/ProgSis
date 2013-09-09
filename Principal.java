import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;


public class Principal extends JFrame {

	private JPanel contentPane;
	
	Vector<Vector<String>> vtok=new Vector<Vector<String>>();
	class Error
	{
		String error="";
		String linea="";
	}

	Vector<Error> elista=new Vector<Error>();
	Vector<Linea> vfinal=new Vector<Linea>();
	Vector<JTextField> vtxt=new Vector<JTextField>();
	
	int contlinea=0, banend=0;
	String cad="";
	private JScrollPane scrollPane;
	private JPanel panel_2;
	private JPanel panel;
	private JToolBar toolBar;
	private JPanel panel_3;
	private JPanel panel_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnAbrir = new JButton("Abrir");
		toolBar.add(btnAbrir);
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Abrir();
			}
		});
		btnAbrir.setBounds(43, 130, 89, 23);
		
		JButton btnSalir = new JButton("Salir");
		toolBar.add(btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setBounds(159, 130, 89, 23);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setVisible(false);
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		panel_3 = new JPanel();
		scrollPane.setViewportView(panel_3);
		panel_3.setLayout(new GridLayout(0, 4, 0, 0));
		
		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 5));
		
		JLabel lblLinea = new JLabel("L\u00EDnea");
		panel_1.add(lblLinea);
		
		JLabel lblEtiq = new JLabel("Etiqueta");
		panel_1.add(lblEtiq);
		
		JLabel lblCodop = new JLabel("C\u00F3digo de Operaci\u00F3n");
		panel_1.add(lblCodop);
		
		JLabel lblOperador = new JLabel("Operador");
		panel_1.add(lblOperador);
	}
	
	public void Abrir()
	{
		JFileChooser jfc=new JFileChooser();
		int opc=jfc.showOpenDialog(null);
		if(opc==JFileChooser.APPROVE_OPTION)
		{
			String arch=jfc.getSelectedFile().getAbsolutePath();
			if(arch.substring(arch.length()-4, arch.length()).equalsIgnoreCase(".asm")==true)
			{
				try
				{
					FileReader fr=new FileReader(arch);
					BufferedReader br=new BufferedReader(fr);
					cad="";
					vtok.clear();
					contlinea=1;
					String com="";
					
					vfinal.clear();
					elista.clear();
					vtxt.clear();
					
					banend=0;
					Vector<String> tkn=new Vector<String>();
					while(br.ready())
					{
						tkn.clear();
						cad=br.readLine();
						StringTokenizer st=new StringTokenizer(cad);
						com="";
						while(st.hasMoreTokens())
						{
							com=st.nextToken();
							if(com.equals(";")==true || com.charAt(0)==';')
								break;
							else
							{
								String str[]=com.split(";",2);
								if(str.length>1)
								{
									tkn.add(str[0]);
										
								}
								else
									tkn.add(com);
							}
						}
						
						if(tkn.size()>0)
								Analizar(tkn.size(),tkn);
						
						if(vfinal.size()>0)
						  if(ValidarEnd())
						    	break;
						contlinea++;
					}
					fr.close();
					br.close();
					
					if(!ValidarEnd())
					   Errores(4,0,"");
					else
					{
						if(vfinal.lastElement().etq.equalsIgnoreCase("NULL"))
							vfinal.lastElement().etq="NULL";
						vfinal.lastElement().oper="NULL";
					}
					
					if(vfinal.size()>0)
					{
						panel_3.removeAll();
						for(int i=0;i<4*vfinal.size();i++)
						{
							JTextField txt=new JTextField();
							vtxt.add(txt);
						}
						
						int i=0;
						
						try
						{
							FileWriter fi=new FileWriter(arch.substring(0,arch.length()-4)+".INST");
							PrintWriter pw=new PrintWriter(fi);
							for(Linea l: vfinal)
							{
								panel_3.add(vtxt.get(4*i));
								panel_3.add(vtxt.get((4*i)+1));
								panel_3.add(vtxt.get((4*i)+2));
								panel_3.add(vtxt.get((4*i)+3));
								
								vtxt.get(4*i).setText(l.linea);
								vtxt.get((4*i)+1).setText(l.etq);
								vtxt.get((4*i)+2).setText(l.codop);
								vtxt.get((4*i)+3).setText(l.oper);
								
								pw.write(l.linea+"\t"+l.etq+"\t"+l.codop+"\t"+l.oper+"\r\n");
								
								i++;
							}
							fi.close();
							pw.close();
						}
						catch(IOException ioe)
						{
							
						}
						panel_3.revalidate();
						panel_3.repaint();
					}
					
					if(elista.size()>0)
					{
						FileWriter fw=new FileWriter(arch.substring(0,arch.length()-4)+".ERR");
						PrintWriter pw=new PrintWriter(fw);
						
						for(Error e: elista)
						   pw.print("Linea "+e.linea+" - "+e.error+"\r\n");
						
						fw.close();
						pw.close();
					}
					panel.setVisible(true);
				}
				catch(IOException ioe)
				{
					JOptionPane.showMessageDialog(null, "Error con el archivo.");
				}
			}
			else
				JOptionPane.showMessageDialog(null, "La extensión del archivo no es ASM");
		}
	}
	
	public void Analizar(int opc, Vector<String> tkn)
	{
		switch(opc)
		{
			case 1:
				if(cad.charAt(0)==' ' || cad.charAt(0)=='\t')
				{
					if(AnalizarCodop(tkn.get(0)))
					{
						Linea l=new Linea();
						l.codop=tkn.get(0);
						l.linea=Integer.toString(contlinea);
						vfinal.add(l);
					}
				}
				else
					Errores(5,0,cad);
				break;
				
			case 2:
				
				if(cad.charAt(0)==' ' || cad.charAt(0)=='\t')
				{
					if(AnalizarCodop(tkn.get(0)))
					{
						Linea l=new Linea();
						l.codop=tkn.get(0);
						l.oper=tkn.get(1);
						l.linea=Integer.toString(contlinea);
						vfinal.add(l);
					}
				}
				else
				{
					if(AnalizarEtq(tkn.get(0)) && AnalizarCodop(tkn.get(1)))
					{
						Linea l=new Linea();
						l.codop=tkn.get(1);
						l.etq=tkn.get(0);
						l.linea=Integer.toString(contlinea);
						vfinal.add(l);
					}
				}
				break;
				
			case 3:
				if(cad.charAt(0)!=' ' & cad.charAt(0)!='\t')
				{
					if(AnalizarEtq(tkn.get(0)) && AnalizarCodop(tkn.get(1)))
					{
						Linea l=new Linea();
						l.codop=tkn.get(1);
						l.etq=tkn.get(0);
						l.oper=tkn.get(2);
						l.linea=Integer.toString(contlinea);
						vfinal.add(l);
					}
				}
				else	
					Errores(3,tkn.size(),"");
				
				
				break;
				
			default:
				Errores(3,opc,"");
				break;
		}
		
	}
	
	public boolean AnalizarCodop(String codop)
	{
		int punto=0,ban=1,i=0;
		if(Character.isLetter(codop.charAt(0)) && codop.length()<=5)
		{
			ban=0;
			for(i=0;i<codop.length();i++)
			{
				if(codop.charAt(i)=='.')
					punto++;
				if((!Character.isLetter(codop.charAt(i)) && codop.charAt(i)!='.') || punto>1 )
				{
					ban=1;
					break;
				}
			}
		}
		if(ban==1)
		{
			Errores(2,i,codop);
			return false;
		}
		else
			return true;
		
	}
	
	public boolean AnalizarEtq(String etq)
	{
		int ban=1,i=0;
		if(Character.isLetter(etq.charAt(0)) && etq.length()<=8)
		{
			ban=0;
			for(i=1;i<etq.length();i++)
				if(!Character.isLetter(etq.charAt(i)) && !Character.isDigit(etq.charAt(i)) && etq.charAt(i)!='_')
				{
					ban=1;
					break;
				}
		}
		
		if(ban==1)
		{
			Errores(1,i,etq);
			return false;
		}
		else
			return true;
	}
	
	public void Errores(int opc,int pos,String cad)
	{
		Error e=new Error();
		switch(opc)
		{
		    case 1:
		    	if(cad.length()>8)
		    	{
		    		e.linea=Integer.toString(contlinea);
			    	e.error="Etiqueta inválida: "+cad+". Supera el límite de caracteres válidos: 8.";
			    	elista.add(e);
		    	}
		    	else
		    	{
		    		e.linea=Integer.toString(contlinea);
			    	e.error="Etiqueta inválida: "+cad+". Cáracter inválido: \" "+cad.charAt(pos)+" \"";
			    	elista.add(e);
		    	}
		    	break;
		    case 2:
		    	if(cad.length()>5)
		    	{
		    		e.linea=Integer.toString(contlinea);
			    	e.error="Código de operación inválido: "+cad+". Supera el límite de caracteres válidos: 5.";
			    	elista.add(e);
		    	}
		    	else
		    	{
		    		e.linea=Integer.toString(contlinea);
			    	e.error="Código de operación inválido: "+cad+". Cáracter inválido: \" "+cad.charAt(pos)+" \"";
			    	elista.add(e);
		    	}
		    	break;
		    	
		    case 3: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="Excede los 3 argumentos permitidos. Hay "+pos+" argumentos en la línea.";
		    	elista.add(e);
		    	break;
		    case 4: 
		    	e.linea="X";
		    	e.error="Falta la directiva END.";
		    	elista.add(e);
		    	break;
		    case 5: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="No es un código de operación válido: "+cad;
		    	elista.add(e);
		    	break;
		    	
		}
	}
	
	public boolean ValidarEnd()
	{

		if(vfinal.lastElement().codop.equalsIgnoreCase("END"))
		   return true;
		
		return false;
	}
}
