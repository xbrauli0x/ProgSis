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

	class Tabop
	{
		String ins="",modo="",codmaq="";
		int noper=0,byc=0,byxc=0,sby=0;
	}
	
	public Vector<Error> elista=new Vector<Error>();
	public Vector<Linea> vfinal=new Vector<Linea>();
	public Vector<JTextField> vtxt=new Vector<JTextField>();
	public Vector<Tabop> vtab=new Vector<Tabop>();
	
	public Vector<Tabop> inh=new Vector<Tabop>();
	public Vector<Tabop> imm8=new Vector<Tabop>();
	public Vector<Tabop> imm16=new Vector<Tabop>();
	public Vector<Tabop> dir=new Vector<Tabop>();
	public Vector<Tabop> ext=new Vector<Tabop>();
	public Vector<Tabop> idx=new Vector<Tabop>();
	public Vector<Tabop> idx1=new Vector<Tabop>();
	public Vector<Tabop> idx2=new Vector<Tabop>();
	public Vector<Tabop> idxx2=new Vector<Tabop>();
	public Vector<Tabop> idxpp=new Vector<Tabop>();
	public Vector<Tabop> idxac=new Vector<Tabop>();
	public Vector<Tabop> didx=new Vector<Tabop>();
	public Vector<Tabop> rel8=new Vector<Tabop>();
	public Vector<Tabop> rel9=new Vector<Tabop>();
	public Vector<Tabop> rel16=new Vector<Tabop>();
	
	int contlinea=0, banend=0;
	String cad="";
	private JScrollPane scrollPane;
	private JPanel panel_2;
	private JPanel panel;
	private JToolBar toolBar;
	private JPanel panel_3;
	private JPanel panel_1;
	private JLabel lblModo;

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

		String num="ffff";
		int comp=(int) Math.pow(16, num.length()) -1;
		System.out.println(comp);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 304);
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
		panel_3.setLayout(new GridLayout(0, 5, 0, 0));
		
		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 45, 5));
		
		JLabel lblLinea = new JLabel("L\u00EDnea");
		panel_1.add(lblLinea);
		
		JLabel lblEtiq = new JLabel("Etiqueta");
		panel_1.add(lblEtiq);
		
		JLabel lblCodop = new JLabel("CodOP");
		panel_1.add(lblCodop);
		
		JLabel lblOperador = new JLabel("Operador");
		panel_1.add(lblOperador);
		
		lblModo = new JLabel("Modo");
		panel_1.add(lblModo);
		
		try
		{
			String linea="";
			FileReader fr=new FileReader("TABOP.txt");
			BufferedReader br=new BufferedReader(fr);
			while(br.ready())
			{
				linea=br.readLine();
				String arr[]=linea.split("\t");
				if(arr.length>1)
				{
					Tabop t=new Tabop();
					t.ins=arr[0];
					t.modo=arr[1];
					t.codmaq=arr[2];
					String spl[]=arr[2].split(" ");
					t.byc=spl.length;
					t.byxc=Integer.parseInt(arr[3]);
					t.sby=Integer.parseInt(arr[4]);
					vtab.add(t);
					
					if(t.modo.equalsIgnoreCase("INH"))
						inh.add(t);
					else
						if(t.modo.equalsIgnoreCase("IMM8"))
							imm8.add(t);
						else
							if(t.modo.equalsIgnoreCase("IMM16"))
								imm16.add(t);
							else
								if(t.modo.equalsIgnoreCase("DIR"))
									dir.add(t);
								else
									if(t.modo.equalsIgnoreCase("EXT"))
										ext.add(t);
									else
										if(t.modo.equalsIgnoreCase("IDX"))
											idx.add(t);
										else
											if(t.modo.equalsIgnoreCase("IDX1"))
												idx1.add(t);
											else
												if(t.modo.equalsIgnoreCase("IDX2"))
													idx2.add(t);
												else
													if(t.modo.equalsIgnoreCase("[IDX2]"))
														idxx2.add(t);
													else
														if(t.modo.equalsIgnoreCase("[D,IDX]"))
															didx.add(t);
														else
															if(t.modo.equalsIgnoreCase("REL8"))
																rel8.add(t);
															else
																if(t.modo.equalsIgnoreCase("REL9"))
																	rel9.add(t);
																else
																	if(t.modo.equalsIgnoreCase("REL16"))
																		rel16.add(t);
				}
			}
			
			fr.close();
			br.close();
		}
		catch(IOException ioe)
		{
			JOptionPane.showMessageDialog(null, "Error con el archivo TABOP.TXT");
		}
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
									tkn.add(str[0]);
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
					
					if(vfinal.size()>0)
					{
						panel_3.removeAll();
						for(int i=0;i<5*vfinal.size();i++)
						{
							JTextField txt=new JTextField();
							txt.setColumns(5);
							vtxt.add(txt);
						}
						
						int i=0;
						
						try
						{
							FileWriter fi=new FileWriter(arch.substring(0,arch.length()-4)+".INST");
							PrintWriter pw=new PrintWriter(fi);
							for(Linea l: vfinal)
							{
								panel_3.add(vtxt.get(5*i));
								panel_3.add(vtxt.get((5*i)+1));
								panel_3.add(vtxt.get((5*i)+2));
								panel_3.add(vtxt.get((5*i)+3));
								panel_3.add(vtxt.get((5*i)+4));
								
								vtxt.get(5*i).setText(l.linea);
								vtxt.get((5*i)+1).setText(l.etq);
								vtxt.get((5*i)+2).setText(l.codop);
								vtxt.get((5*i)+3).setText(l.oper);
								vtxt.get((5*i)+4).setText(l.modos);
								
								pw.write(l.linea+"\t"+l.etq+"\t"+l.codop+"\t"+l.oper+"\t"+l.modos+"\r\n");
								
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
		int banm=0,banopr=0;
		
		Linea l=new Linea(elista,contlinea,vtab);
		switch(opc)
		{
			case 1:
				if(cad.charAt(0)==' ' || cad.charAt(0)=='\t')
				{
					if(l.AnalizarCodop(tkn.get(0)))
					{
						if(tkn.get(0).equalsIgnoreCase("ORG"))
						{
							Errores(9,0,"");
							break;
						}
						else
						{
							if(tkn.get(0).equalsIgnoreCase("END"))
							{
								l.codop=tkn.get(0);
								l.linea=Integer.toString(contlinea);
								vfinal.add(l);
								break;
							}
						}
						
						for(Tabop tb: vtab)
						{
							if(tb.ins.equalsIgnoreCase(tkn.get(0)))
							{
								if(tb.byxc>0)
								{
									banopr=1;
									break;
								}
								else
								{
									l.codop=tkn.get(0);
									l.linea=Integer.toString(contlinea);
									l.modos=tb.modo;
									vfinal.add(l);
									banm=1;
									break;
								}
							}
						}
						
						if(banopr==1)
						{
							Errores(8,0,tkn.get(0));
							break;
						}
						
						if(banm==0)
							Errores(6,0,tkn.get(0));
					}
				}
				else
					Errores(5,0,tkn.get(0));
				break;
				
			case 2:
				if(cad.charAt(0)==' ' || cad.charAt(0)=='\t')
				{
					if(l.AnalizarCodop(tkn.get(0)))
					{
						if(tkn.get(0).equalsIgnoreCase("ORG"))
						{
							if(l.RegularizarBase(tkn.get(1)))
							{
								l.codop=tkn.get(0);
								l.oper=tkn.get(1);
								l.linea=Integer.toString(contlinea);
								vfinal.add(l);
								banm=1;
							}
						}
						else
						{
							if(tkn.get(0).equalsIgnoreCase("END"))
							{
								l.codop=tkn.get(0);
								l.linea=Integer.toString(contlinea);
								vfinal.add(l);
								break;
							}
							else
							{
								for(Tabop tb: vtab)
								{
									if(tb.ins.equalsIgnoreCase(tkn.get(0)))
									{
										if(tb.byxc==0)
											Errores(7,0,tkn.get(0));
										
										if(l.AnalizarOper(tkn.get(1),tkn.get(0)))
										{
											l.codop=tkn.get(0);
											l.oper=tkn.get(1);
											l.linea=Integer.toString(contlinea);
											vfinal.add(l);
										}
										banm=1;
										break;
									}
								}
							}
						}
						
						if(banm==0)
						  Errores(6,0,tkn.get(0));
					}
				}
				else
				{
					if(l.AnalizarEtq(tkn.get(0)) && l.AnalizarCodop(tkn.get(1)))
					{
						if(tkn.get(1).equalsIgnoreCase("ORG"))
						{
							Errores(9,0,"");
							break;
						}
						else
						{
							if(tkn.get(1).equalsIgnoreCase("END"))
							{
								l.codop=tkn.get(1);
								l.etq=tkn.get(0);
								l.linea=Integer.toString(contlinea);
								vfinal.add(l);
								break;
							}
						}
						
						for(Tabop tb: vtab)
						{
							if(tb.ins.equalsIgnoreCase(tkn.get(1)))
							{
								if(tb.byxc>=1)
								{
									banopr=1;
									break;
								}
								
								if(banm==0)
								{
									l.codop=tkn.get(1);
									l.etq=tkn.get(0);
									if(tb.byxc==0)
										l.modos=tb.modo;
									l.linea=Integer.toString(contlinea);
									banm=1;
								}
								
								break;
							}
								
						}
						
						if(banopr==1)
						{
							Errores(8,0,tkn.get(1));
							break;
						}
						
						if(banm==1)
							vfinal.add(l);
						else
							Errores(6,0,tkn.get(1));
					}
				}
				break;
				
			case 3:
				if(cad.charAt(0)!=' ' & cad.charAt(0)!='\t')
				{
					if(l.AnalizarEtq(tkn.get(0)) && l.AnalizarCodop(tkn.get(1)))
					{
						if(tkn.get(1).equalsIgnoreCase("ORG"))
						{
							if(l.RegularizarBase(tkn.get(2)))
							{
								l.etq=tkn.get(0);
								l.codop=tkn.get(1);
								l.oper=tkn.get(2);
								l.linea=Integer.toString(contlinea);
								vfinal.add(l);
								banm=1;
								vfinal.add(l);
							}
						}
						else
						{
							if(tkn.get(1).equalsIgnoreCase("END"))
							{
								Errores(6,0,tkn.get(1));
								break;
							}
							
							for(Tabop tb: vtab)
							{
								if(tb.ins.equalsIgnoreCase(tkn.get(1)))
								{
									if(tb.byxc==0)
									{
										banopr=1;
										break;
									}
									
									if(l.AnalizarOper(tkn.get(2), tkn.get(1)))
									{
										l.codop=tkn.get(1);
										l.etq=tkn.get(0);
										l.oper=tkn.get(2);
										l.linea=Integer.toString(contlinea);
										vfinal.add(l);
									}
									banm=1;
									break;
								}
							}
						}
						
						if(banopr==1)
						{
							Errores(7,0,tkn.get(1));
							break;
						}
						
						if(banm==0)
							Errores(6,0,tkn.get(1));
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
	
	public void Errores(int opc,int pos,String cad)
	{
		Error e=new Error();
		switch(opc)
		{
		    case 3: 
	     	    e.linea=Integer.toString(contlinea);
	    	    e.error="Excede los 3 argumentos permitidos.";
	    	    elista.add(e);
	    	break;
		    case 4: 
	    	    e.linea="X";
	    	    e.error="Falta la directiva END";
	    	    elista.add(e);
	    	break;
		    case 5: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="Necesita tokens de tabulación/espacio para ser una línea válida: "+cad;
		    	elista.add(e);
		    	break;
		    case 6: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="No coincide el CODOP: "+cad+" con el TABOP.";
		    	elista.add(e);
		    	break;
		    case 7: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="Este modo de direccionamiento: "+cad+" no permite operandos.";
		    	elista.add(e);
		    	break;
		    case 8: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="Este modo de direccionamiento: "+cad+" necesita operandos.";
		    	elista.add(e);
		    	break;
		    case 9: 
		    	e.linea=Integer.toString(contlinea);
		    	e.error="Operando ORG necesita un operando.";
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
