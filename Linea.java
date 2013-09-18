import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Linea extends Principal{
	String etq,codop,oper, linea,modos;
	int clinea=0;
	Vector<Error> ev;
	
	public Linea()
	{
		etq="NULL";
		codop="NULL";
		oper="NULL";
		linea="NULL";
		modos="NULL";
	}
	
	public Linea(Vector<Error> v, int cl)
	{
		etq="NULL";
		codop="NULL";
		oper="NULL";
		linea="NULL";
		modos="NULL";
		ev=v;
		clinea=cl;
	}
	
	public boolean AnalizarEtq(String etq)
	{
		if(etq.length()<=8)
		{
			Pattern pat = Pattern.compile("^[a-zA-Z].*");
		    Matcher mat = pat.matcher(etq);
		    
		    if(mat.matches())
		    {
		    	pat = Pattern.compile("^[a-zA-Z]\\w+");
		    	mat=pat.matcher(etq);
		    	if(mat.matches())
		    		return true;
		    	else
		    	{
		    		Errores(3,etq);
		    		return false;
		    	}
		    }
		    else
		    {
		    	Errores(2,etq);
		    	return false;
		    }
		}
		else
		{
			Errores(1,etq);
			return false;
		}
	}
	

	public boolean AnalizarCodop(String codop)
	{
		if(codop.length()<=5)
		{
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
		    	
		}
	}

}
