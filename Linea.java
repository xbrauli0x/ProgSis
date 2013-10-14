import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Linea extends Principal{
	String etq,codop,oper, linea,modos;
	int clinea=0,num=0,banetiq=0;
	Vector<Error> ev;
	Vector<Tabop> vtb;
	
	String numtemp="";
	
	public Linea()
	{
		etq="NULL";
		codop="NULL";
		oper="NULL";
		linea="NULL";
		modos="NULL";
	}
	
	public Linea(Vector<Error> v, int cl, Vector<Tabop> vt)
	{
		etq="NULL";
		codop="NULL";
		oper="NULL";
		linea="NULL";
		modos="NULL";
		ev=v;
		clinea=cl;
		vtb=vt;
	}
	
	public boolean AnalizarEtq(String etq)
	{
		if(etq.length()<=8)
		{
			Pattern pat = Pattern.compile("^[a-zA-Z].*");
		    Matcher mat = pat.matcher(etq);
		    
		    if(mat.matches())
		    {
		    	pat = Pattern.compile("^[a-zA-Z]\\w*");
		    	mat=pat.matcher(etq);
		    	if(mat.matches())
		    		return true;
		    	else
		    	{
		    		if(banetiq==0)
		    			Errores(3,etq);
		    		return false;
		    	}
		    }
		    else
		    {
		    	if(banetiq==0)
		    		Errores(2,etq);
		    	return false;
		    }
		}
		else
		{
			if(banetiq==0)
				Errores(1,etq);
			return false;
		}
	}
	
	public boolean AnalizarOper(String opr, String codop)
	{
		Pattern pat = Pattern.compile("^\\[.*\\]$");
		Matcher mat = pat.matcher(opr);
		if(mat.matches())
		{
	    	//IDX[]
	    	pat = Pattern.compile("^\\[D,(X|x|Y|y|SP|Sp|sP|sp|PC|Pc|pC|pc)\\]$");
	    	mat = pat.matcher(opr);
	    	if(mat.matches())
	    	{
	    		if(ValidarDIDX(codop))
				{
					modos="[D,IDX]";
					return true;
				}
	    		else
	    			return false;
	    	}
	    	else
	    	{
	    		pat = Pattern.compile("^\\[[-$@%]?[A-Fa-f0-9]+,(X|x|Y|y|SP|Sp|sP|sp|PC|Pc|pC|pc)\\]$");
		    	mat = pat.matcher(opr);
		    	String spl[]=opr.split(",",2);
		    	if(mat.matches())
		    	{
		    		if(RegularizarBase(spl[0].substring(1,spl[0].length())))
		    		{
		    			if(num>=0 && num<=65535)
		    			{
		    				if(ValidarIDXX2(codop))
		    				{
		    					modos="[IDX2]";
		    					return true;
		    				}
		    			}
		    			else
	    				{
	    					modos="[IDX2]";
	    					Errores(9,opr);
	    					return false;
	    				}
		    		}
		    	}
		    	else
		    	{
		    		Errores(15,opr);
		    		return false;
		    	}
	    	}
	    }
	    System.out.print("Operador: "+opr+"\t");
	    
		if(opr.charAt(0)=='[' && opr.charAt(opr.length()-1)!=']')
		{
			Errores(13,opr);
			return false;
		}
		else
		{
			if(opr.charAt(0)!='[' && opr.charAt(opr.length()-1)==']')
			{
				Errores(13,opr);
				return false;
			}
		}
		
		pat = Pattern.compile("^[$#%0-9@,ABCD+-].*");
	    mat = pat.matcher(opr);
		if(mat.matches() || opr.charAt(0)==' ')
		{
			System.out.println("Operador válido: "+opr);
			pat = Pattern.compile("[^,]*,[^,]*");
		    mat = pat.matcher(opr);
		    if(mat.matches())
		    {
		    	String spl[]=opr.split(",",2);
		    	pat = Pattern.compile("^[+-](PC|Pc|cP|cp)");
		    	mat = pat.matcher(spl[1]);
		    	if(mat.matches())
		    	{
		    		modos="IDX Pre/Post";
		    		Errores(18,opr);
		    		return false;
		    	}
		    	else
		    	{
		    		pat = Pattern.compile("^(PC|Pc|pC|pc)[+-]");
		    		mat = pat.matcher(spl[1]);
		    		if(mat.matches())
		    		{
		    			modos="IDX Pre/Post";
		    			Errores(18,opr);
			    		return false;
		    		}
		    	}
		    	
		    	pat = Pattern.compile("^[AaBbDd],(X|x|Y|y|SP|Sp|sP|sp|PC|Pc|pC|pc)");
		    	mat = pat.matcher(opr);
		    	if(mat.matches())
		    	{
		    		if(ValidarIDX(codop))
    				{
    					modos="IDX";
    					return true;
    				}
		    	}
		    	else
		    	{
		    		pat = Pattern.compile("^[+-](X|x|Y|y|SP|Sp|sP|sp)");
			    	mat = pat.matcher(spl[1]);
		    		if(mat.matches())
		    		{
		    			if(RegularizarBase(spl[0]))
		    			{
		    				if(num>=1 && num<=8)
		    				{
		    					if(ValidarIDX(codop))
			    				{
			    					modos="IDX";
			    					return true;
			    				}
			    				else
			    				{
			    					modos="IDX Pre/Post";
			    					Errores(9,opr);
			    					return false;
			    				}
		    				}
		    				else
		    				{
		    					modos="IDX";
		    					Errores(9,opr);
		    					return false;
		    				}
		    			}
		    		}
		    		else
		    		{
		    			pat = Pattern.compile("^(X|x|Y|y|SP|Sp|sP|sp)[+-]");
				    	mat = pat.matcher(spl[1]);
			    		if(mat.matches())
			    		{
			    			if(RegularizarBase(spl[0]))
			    			{
			    				if(num>=1 && num<=8)
			    				{
			    					if(ValidarIDX(codop))
				    				{
				    					modos="IDX";
				    					return true;
				    				}
				    				else
				    				{
				    					modos="IDX Pre/Post";
				    					Errores(9,opr);
				    					return false;
				    				}
			    				}
			    				else
			    				{
			    					modos="IDX Pre/Post";
			    					Errores(9,opr);
			    					return false;
			    				}
			    			}
			    		}
			    		else
			    		{
			    			pat = Pattern.compile("^[-$@%]?[A-Fa-f0-9]*,(X|x|Y|y|SP|Sp|sP|sp|PC|Pc|pC|pc)");
					    	mat = pat.matcher(opr);
					    	if(mat.matches() || opr.charAt(0)==' ')
					    	{
					    		
					    		if(!spl[0].equals(""))
					    		{
					    			
					    			if(RegularizarBase(spl[0]) || opr.charAt(0)==' ')
						    		{
					    				if(num>=-16 && num<=15)
						    			{
						    				if(ValidarIDX(codop))
						    				{
						    					modos="IDX";
						    					return true;
						    				}
						    				else
						    				{
						    					modos="IDX";
						    					Errores(9,opr);
						    					return false;
						    				}
						    			}
						    			else
						    			{
						    				if(num>=-256 && num<=255)
						    				{
						    					if(ValidarIDX1(codop))
							    				{
							    					modos="IDX1";
							    					return true;
							    				}
							    				else
							    				{
							    					modos="IDX1";
							    					Errores(9,opr);
							    					return false;
							    				}
						    				}
						    				else
						    				{
						    					if(num>=0 && num<=65535)
							    				{
							    					if(ValidarIDX2(codop))
								    				{
								    					modos="IDX2";
								    					return true;
								    				}
								    				else
								    				{
								    					modos="IDX2";
								    					Errores(9,opr);
								    					return false;
								    				}
							    				}
							    				else
								    			{
							    					modos="IDX2";
								    				Errores(9,opr);
								    				return false;
								    			}
						    				}
						    			}
						    		}
					    		}
					    		else
					    		{
					    			if(ValidarIDX(codop))
				    				{
				    					modos="IDX";
				    					return true;
				    				}
				    				else
				    					return false;
					    		}
					    	}
					    	else
					    	{
					    		Errores(15,opr);
					    		return false;
					    	}
			    		}
		    		}
		    	}
		    }
		    else
		    {
		    	pat = Pattern.compile("([^,]*,[^,]*)*");
			    mat = pat.matcher(opr);
			    if(mat.matches())
			    {
			    	Errores(15,opr);
			    	return false;
			    }
		    	System.out.print("No entro: "+opr+"\t");
		    	
		    	switch(opr.charAt(0))
				{
				    case '#':
				    		if(RegularizarBase(opr.substring(1,opr.length())))
				    		{
				    			if(num>=-256 && num<=255)
				    			{
				    				if(ValidarIMM8(codop))
				    				{
				    					modos="IMM8";
				    					return true;
				    				}
				    				else
				    				{
				    					if(ValidarIMM16(codop))
					    				{
					    					modos="IMM16";
					    					return true;
					    				}
				    					else
				    					{
				    						Errores(7,codop);
				    						return false;
				    					}
				    				}
				    			}
				    			else
				    			{
				    				if(num>=-32768 && num<=65535)
				    				{
				    					if(ValidarIMM16(codop))
				    					{
				    						modos="IMM16";
				    						return true;
				    					}
					    				else
					    				{
					    					Errores(10,codop);
					    					return false;
					    				}
				    				}
				    				else
				    				{
				    					modos="IMM16";
				    					Errores(9,opr);
				    					return false;
				    				}
				    			}
				    		}
				    		
				    	break;
				    default: 
				    	int bane=0;
				    	banetiq=1;
				    	if(AnalizarEtq(opr))
				    	{
				    		bane=1;
				    		if(ValidarREL8(codop))
				    		{
				    			modos="REL8";
				    			return true;
				    		}
				    		else
				    		{
				    			if(ValidarREL9(codop))
				    			{
				    				modos="REL9";
					    			return true;
				    			}
				    			else
				    			{
				    				if(ValidarREL16(codop))
						    		{
						    			modos="REL16";
						    			return true;
						    		}
						    		else
						    		{
						    			banetiq=0;
						    			return false;
						    		}
				    			}
				    		}
				    	}
				    	else
				    		banetiq=0;
				    	
				    	if(RegularizarBase(opr) && bane==0)
				    	{	
				    		if(num>=0 && num<=65535)
				    		{
				    			if(num<=127)
				    			{
				    				if(ValidarREL8(codop))
				    				{
				    					modos="REL8";
				    					return true;
				    				}
				    				else
				    				{
				    					if(ValidarDIR(codop))
					    				{
					    					modos="DIR";
					    					return true;
					    				}
				    					else
				    					{
				    						if(ValidarEXT(codop))
				    						{
				    							modos="EXT";
				    							return true;
				    						}
				    						else
				    						{
				    							if(ValidarREL16(codop))
			    								{
			    									modos="REL16";
			    									return true;
			    								}
				    						}
				    					}
				    				}
				    			}
				    			else
				    			{
				    				if(num<=255)
				    				{
				    					if(ValidarREL9(codop))
					    				{
					    					modos="REL9";
					    					return true;
					    				}
					    				else
					    				{
					    					if(ValidarDIR(codop))
						    				{
						    					modos="DIR";
						    					return true;
						    				}
					    				}
				    				}
				    				else
				    				{
				    					if(ValidarREL16(codop))
				    					{
				    						modos="REL16";
				    						return true;
				    					}
				    					else
				    					{
				    						if(ValidarEXT(codop))
					    					{
					    						modos="EXT";
						    					return true;
					    					}
				    					}
				    				}
				    			}
				    		}
				    		else
				    		{
				    			if(num<0 && num>=-32768)
				    			{
				    				if(num>=-127)
				    				{
				    					if(ValidarREL8(codop))
				    					{
				    						modos="REL8";
				    						return true;
				    					}
				    					else
				    					{
				    						if(ValidarEXT(codop))
					    					{
					    						modos="EXT";
					    						return true;
					    					}
				    					}
				    				}
				    				else
				    				{
				    					if(num>=-256)
				    					{
				    						if(ValidarREL9(codop))
					    					{
					    						modos="REL9";
					    						return true;
					    					}
				    					}
				    					else
				    					{
				    						if(ValidarREL16(codop))
					    					{
					    						modos="REL16";
					    						return true;
					    					}
				    						else
				    						{
				    							if(ValidarEXT(codop))
						    					{
						    						modos="EXT";
						    						return true;
						    					}
				    						}
				    					}
				    				}
				    			}
				    		}
				    		
				    		if(ValidarREL8(codop))
				    		{
				    			modos="REL8";
				    			Errores(9,opr);
				    			return false;
				    		}
				    		else
				    		{
				    			if(ValidarREL9(codop))
					    		{
					    			modos="REL9";
					    			Errores(9,opr);
					    			return false;
					    		}
				    			else
				    			{
				    				if(ValidarREL16(codop))
						    		{
						    			modos="REL16";
						    			Errores(9,opr);
						    			return false;
						    		}
				    			}
				    			
				    			if(ValidarDIR(codop))
				    			{
				    				modos="DIR";
				    				Errores(11,codop);
				    				return false;
				    			}
				    			else
				    			{
				    				if(ValidarEXT(codop))
					    			{
					    				modos="EXT";
					    				Errores(12,codop);
					    				return false;
					    			}
				    			}
				    		}
				    	}
				    	break;
				}
		    } 
		}
		else
		{
			pat = Pattern.compile("[^,]*,[^,]*");
		    mat = pat.matcher(opr);
		    if(mat.matches())
		    {
		    	Errores(15,opr);
		    	return false;
		    }
		    
			banetiq=1;
			System.out.println("Etiqueta: "+opr);
			if(AnalizarEtq(opr))
	    	{
	    		if(ValidarREL8(codop))
	    		{
	    			modos="REL8";
	    			return true;
	    		}
	    		else
	    		{
	    			if(ValidarREL9(codop))
	    			{
	    				modos="REL9";
		    			return true;
	    			}
	    			else
	    			{
	    				if(ValidarREL16(codop))
			    		{
			    			modos="REL16";
			    			return true;
			    		}
			    		else
			    		{
			    			banetiq=0;
			    			return false;
			    		}
	    			}
	    		}
	    	}
	    	else
	    	{
	    		banetiq=0;
	    		modos="";
	    		Errores(22,opr);
	    	}
		}
		
		return false;
	}
	
	public boolean AnalizarCodop(String codop)
	{
		if(codop.length()<=5)
		{
			if(codop.equalsIgnoreCase("END"))
				return true;
			Pattern pat = Pattern.compile("^[a-zA-Z].*");
		    Matcher mat = pat.matcher(codop);
		    if(mat.matches())
		    {
		    	pat = Pattern.compile("[a-zA-Z]*[\\.]?[a-zA-Z]*");
		    	mat=pat.matcher(codop);
		    	mat.toString();
		    	if(mat.matches())
		    		return true;
		    	else
		    	{
		    		Errores(6,codop);
		    		return false;
		    	}
		    }
		    else
		    {
		    	Errores(5,codop);
		    	return false;
		    }
		}
		else
		{
			Errores(4,codop);
			return false;
		}
	}
	
	public boolean Complemento(String num,int opc)
	{
		String numbin="",zeros="";
		
		switch(opc)
		{
		   case 1:
			   try
			   {
				   numbin=Integer.toBinaryString(Integer.parseInt(num, 16));
				   for(int i=0;i<(4*num.length())-numbin.length();i++)
					   zeros+="0";
				   numbin=zeros+numbin;
			   }
			   catch(Exception e)
			   {
		          return false;
		       } 
			 
			   numtemp="";
			   for(int i=0;i<numbin.length();i++)
			   {
				   if(numbin.charAt(i)=='0')
					   numtemp+="1";
				   else
					   numtemp+="0";
			   }
			   
			   this.num=(Integer.parseInt(numtemp, 2)+1)*-1;
			   
			   return true;
			   
		   case 2:
			   numbin=num;
			   numtemp="";
			   for(int i=0;i<numbin.length();i++)
			   {
				   if(numbin.charAt(i)=='0')
					   numtemp+="1";
				   else
					   numtemp+="0";
			   }
			   
			   this.num=(Integer.parseInt(numtemp, 2)+1)*-1;

			   System.out.println("Num BINAAAA: "+num+"\t"+this.num);
			   return true;
			   
		   case 3:
			   try
			   {
				   numbin=Integer.toBinaryString(Integer.parseInt(num, 8));
				   for(int i=0;i<(3*num.length())-numbin.length();i++)
					   zeros+="0";
				   numbin=zeros+numbin;
			   }
			   catch(Exception e)
			   {
		          return false;
		       } 
			 
			   numtemp="";
			   for(int i=0;i<numbin.length();i++)
			   {
				   if(numbin.charAt(i)=='0')
					   numtemp+="1";
				   else
					   numtemp+="0";
			   }
			   
			   this.num=(Integer.parseInt(numtemp, 2)+1)*-1;
			   
			   return true;
		}
		
		return false;
	}
	
	public boolean RegularizarBase(String opr)
	{
		int ban=0;
		Pattern pat;
		Matcher mat;
		if(opr.length()>0)
		{
			switch(opr.charAt(0))
			{
			    case '$':
			    	pat = Pattern.compile("[0-9A-Fa-f]*");
					mat = pat.matcher(opr.substring(1,opr.length()));
					if(mat.matches())
					{
						if(opr.charAt(1)=='F' || opr.charAt(1)=='f')
			    		{
			    			for(int i=1;i<opr.length();i++)
			    			{ 
			    				if(opr.charAt(i)!='F' && opr.charAt(i)!='f')
			    				{	
			    					ban=1;
			    					if(Complemento(opr.substring(i,opr.length()),1))
			    						return true;
			    					else
			    					{
			    						oper="Hexadecimal";
			    			        	Errores(6,opr);
			    			        	return false;
			    					}
			    				}
			    			}
			    			
			    			if(ban==0)
			    			{
			    				if(Complemento(opr.substring(opr.length()-2,opr.length()),1))
		    						return true;
			    			}
			    			oper="Hexadecimal";
    			        	Errores(6,opr);
    			        	return false;
			    		}
			    		
			    		numtemp=opr.substring(1,opr.length());
			    		num=Integer.parseInt(numtemp,16);
			    		return true;
					}
					else
					{
						oper="Hexadecimal";
			        	Errores(6,opr);
			        	return false;
					}
			    	
			   
			    case '%': 
			    	pat = Pattern.compile("[01]*");
					mat = pat.matcher(opr.substring(1,opr.length()));
			    	if(mat.matches())
			    	{
			    		if(opr.charAt(1)=='1')
			    		{
			    			for(int i=1;i<opr.length();i++)
			    			{ 
			    				if(opr.charAt(i)!='1')
			    				{	
			    					ban=1;
			    					if(Complemento(opr.substring(i,opr.length()),2))
			    						return true;
			    					else
			    					{
			    						oper="Binario";
			    			        	Errores(6,opr);
			    			        	return false;
			    					}
			    				}
			    			}
			    			if(ban==0)
			    			{
			    				if(Complemento(opr.substring(opr.length()-2,opr.length()),2))
		    						return true;
			    			}
			    			oper="Binario";
				        	Errores(6,opr);
				        	return false;
			    		}
			    		
			    		numtemp=opr.substring(1,opr.length());
			    		num=Integer.parseInt(numtemp,2);
			    		return true;
			    	}
			    	else
			    	{
			    		oper="Binario";
			        	Errores(6,opr);
			        	return false;
			    	}
			   
			    case '@': 
			    	pat = Pattern.compile("[0-7]*");
					mat = pat.matcher(opr.substring(1,opr.length()));
					if(mat.matches())
					{
						if(opr.charAt(1)=='7')
			    		{
			    			for(int i=2;i<opr.length();i++)
			    			{ 
			    				if(opr.charAt(i)!='7')
			    				{	
			    					if(Complemento(opr.substring(i,opr.length()),3))
			    						return true;
			    					else
			    					{
			    						oper="Octal";
			    			        	Errores(6,opr);
			    			        	return false;
			    					}
			    				}
			    			}
			    			if(ban==0)
			    			{
			    				if(Complemento(opr.substring(opr.length()-2,opr.length()),3))
		    						return true;
			    			}
			    			oper="Octal";
				        	Errores(6,opr);
				        	return false;
			    		}
			    		
			    		numtemp=opr.substring(1,opr.length());
			    		System.out.println("Numtemp: "+numtemp);
			    		num=Integer.parseInt(numtemp,8);
			    		return true;
					}
					else
			        {
			        	oper="Octal";
			        	Errores(6,opr);
			        	return false;
			        }
			    	
			    default: 
			    	try{
			    		num=Integer.parseInt(opr);
			    		return true;
			    	}
			        catch(Exception e)
			        {
			        	oper="Decimal";
			        	Errores(6,opr);
			        	return false;
			        }
			}
		}
		return false;
	}
	
	public boolean ValidarIMM8(String codop)
	{
		for(Tabop tb: imm8)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarIMM16(String codop)
	{
		for(Tabop tb: imm16)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarDIR(String codop)
	{
		for(Tabop tb: dir)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarEXT(String codop)
	{
		for(Tabop tb: ext)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarIDX(String codop)
	{
		int ban=0;
		for(Tabop tb: idx)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				ban=1;
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		if(ban==0)
			Errores(14,codop);
		
		return false;
	}
	
	public boolean ValidarIDX1(String codop)
	{
		int ban=0;
		for(Tabop tb: idx1)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				ban=1;
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		if(ban==0)
			Errores(16,codop);
		
		return false;
	}
	
	public boolean ValidarIDX2(String codop)
	{
		int ban=0;
		for(Tabop tb: idx2)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				ban=1;
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		if(ban==0)
			Errores(17,codop);
		
		return false;
	}
	
	public boolean ValidarDIDX(String codop)
	{
		int ban=0;
		for(Tabop tb: didx)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				ban=1;
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		if(ban==0)
			Errores(19,codop);
		
		return false;
	}
	
	public boolean ValidarIDXX2(String codop)
	{
		int ban=0;
		for(Tabop tb: idxx2)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				ban=1;
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		if(ban==0)
			Errores(20,codop);
		
		return false;
	}
	
	public boolean ValidarREL8(String codop)
	{
		for(Tabop tb: rel8)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarREL9(String codop)
	{
		for(Tabop tb: rel9)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public boolean ValidarREL16(String codop)
	{
		for(Tabop tb: rel16)
		{
			if(tb.ins.equalsIgnoreCase(codop))
			{
				if(tb.byxc==0)
				{
					Errores(8,codop);
					return false;
				}
				else
					return true;
			}
		}
		
		return false;
	}
	
	public void Errores(int opc,String cadena)
	{
		Error e=new Error();
		switch(opc)
		{
		    case 1: 
		    	e.linea=Integer.toString(clinea);
		    	e.error="Etiqueta inválida: "+cadena+". Supera el límite de carácteres válidos: 8.";
		    	ev.add(e);
			   break;
		    case 2:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Etiqueta inválida: "+cadena+". Debe comenzar por una letra mayúscula o minúscula.";
		    	ev.add(e);
		    	break;
		    case 3:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Etiqueta inválida: "+cadena+". Contiene carácteres inválidos.";
		    	ev.add(e);
		    	break;
		    case 4:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación inválido: "+cadena+". Supera el límite de carácteres válidos: 5.";
		    	ev.add(e);
		    	break;
		    case 5:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación inválido: "+cadena+". Debe comenzar por una letra mayúscula o minúscula.";
		    	ev.add(e);
		    	break;
		    case 6:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Operando inválido: "+cadena+". No es un número "+oper+" válido.";
		    	ev.add(e);
		    	break;
		    case 7:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento IMM8.";
		    	ev.add(e);
		    	break;
		    case 8:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No necesita operando.";
		    	ev.add(e);
		    	break;
		    case 9:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Operando: "+cadena+". Fuera de rango para direccionamiento: "+modos+".";
		    	ev.add(e);
		    	break;
		    case 10:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento IMM16.";
		    	ev.add(e);
		    	break;
		    case 11:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento DIR.";
		    	ev.add(e);
		    	break;
		    case 12:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento EXT.";
		    	ev.add(e);
		    	break;
		    case 13:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Operando: "+cadena+". Se esperaba token de apertura/cerradura: [n,r]";
		    	ev.add(e);
		    	break;
		    case 14:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento IDX.";
		    	ev.add(e);
		    	break;
		    case 15:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Operando "+cadena+". Formato inválido para operando de direccionamiento Indizado.";
		    	ev.add(e);
		    	break;
		    case 16:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento IDX1.";
		    	ev.add(e);
		    	break;
		    case 17:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento IDX2.";
		    	ev.add(e);
		    	break;
		    case 18:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Registro: "+cadena+". No válido para direccionamiento: "+modos+".";
		    	ev.add(e);
		    	break;
		    case 19:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento [D,IDX].";
		    	ev.add(e);
		    	break;
		    case 20:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento [IDX2].";
		    	ev.add(e);
		    	break;
		    case 21:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Código de operación: "+cadena+". No posee direccionamiento REL8.";
		    	ev.add(e);
		    	break;
		    case 22:
		    	e.linea=Integer.toString(clinea);
		    	e.error="Operando inválido: "+cadena+". No es una etiqueta válida.";
		    	ev.add(e);
		    	break;
		}
	}
}
