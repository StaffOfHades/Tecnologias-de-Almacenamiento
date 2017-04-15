/******************************************************************
/  clase: Registro
/
/  autor: Dr. JosŽ Luis Zechinelli Martini
/******************************************************************/

import java.io.*;
import java.lang.*;

public class Registro implements Constants {
   
    private boolean borrado = false;
	private byte[] sucursal = new byte[STR_SIZE];
	private int numero = 0;
	private byte[] nombre = new byte[STR_SIZE];
	private double saldo = 0;
    
    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Registro() {}
    
	public Registro( String nomSucursal, int numCuenta,
                     String nomCliente, double deposito )
	{
 
		if( nomSucursal.length() > STR_SIZE || nomCliente.length() > STR_SIZE ) {
            
			System.out.println( "ATENCION: Sucursal o nombre con mas de " + STR_SIZE + " caracteres" );
        }
        
		for( int i = 0; i < STR_SIZE && i < nomSucursal.getBytes().length; i++ )
			sucursal[i] = nomSucursal.getBytes()[i];
        
		numero = numCuenta;
        
		for( int i = 0; i < STR_SIZE && i < nomCliente.getBytes().length; i++ )
			nombre[i] = nomCliente.getBytes()[i];
        
		saldo = deposito;
	}
    
    /*-----------------------------------------------------------------
    / mŽtodos getters
	/ Se implementa el comando .replace() para que se eliminen los valores nulos de las cadenas creadas
    /-----------------------------------------------------------------*/
    public boolean borrado()    { return borrado; }
	
	public String getSucursal() { return new String( sucursal ).replace("\0",""); }
 
	public int getNumero()      { return numero; }
    
	public String getNombre()   { return new String( nombre ).replace("\0",""); }
    
	public double getSaldo()    { return saldo; }
	
	/*-----------------------------------------------------------------
    / mŽtodos setters
	/ Se implementa el comando .replace() para que se eliminen los valores nulos de las cadenas creadas
    /-----------------------------------------------------------------*/
    public void setBorrado(boolean estado) {
		borrado = estado;
	}
	
	public void setSucursal (String suc) {
		
		sucursal = new byte [sucursal.length];
		
		if( suc.length() > sucursal.length ) {
			System.out.println( "ATENCION: Sucursal con mas de " + STR_SIZE + " caracteres" );
        }
        
		for( int i = 0; i < sucursal.length && i < suc.getBytes().length; i++ )
			sucursal[i] = suc.getBytes()[i];
	}
    /*-----------------------------------------------------------------
    / longitud en bytes de un registro
    /-----------------------------------------------------------------*/
    
	public int length() {
        
		return sucursal.length +
               Integer.SIZE / 8 +
               nombre.length +
               Double.SIZE / 8 +
			   1;
	}
    
    /*-----------------------------------------------------------------
    / mŽtodos para escribir y leer un registro
    /-----------------------------------------------------------------*/
    
	public void read( RandomAccessFile raf ) throws IOException {
        borrado= raf.readBoolean();
		raf.read( sucursal );
		numero = raf.readInt();
		raf.read( nombre );
		saldo = raf.readDouble();
	}
    
	public void write( RandomAccessFile raf ) throws IOException {
        raf.writeBoolean (borrado);
		raf.write( sucursal );
		raf.writeInt( numero );
		raf.write( nombre );
		raf.writeDouble( saldo );
	}	

}
