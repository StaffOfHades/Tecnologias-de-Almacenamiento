/******************************************************************
/  clase Registro
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;
import java.lang.*;

public class Registro {
    
	private byte[] sucursal = new byte[20];
	private int numero = 0;
	private byte[] nombre = new byte[20];
	private double saldo = 0;
    private byte borrar;   
 
    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Registro() { }
    
	public Registro( String nomSucursal, int numCuenta,
                     String nomCliente, double deposito )
	{
        
		if( nomSucursal.length() > 20 || nomCliente.length() > 20 ) {
            
			System.out.println( "ATENCION: Sucursal o nombre con mas de 20 caracteres" );
        }
        
		for( int i = 0; i < 20 && i < nomSucursal.getBytes().length; i++ )
			sucursal[i] = nomSucursal.getBytes()[i];
        
		numero = numCuenta;
        
		for( int i = 0; i < 20 && i < nomCliente.getBytes().length; i++ )
			nombre[i] = nomCliente.getBytes()[i];
        
		saldo = deposito;

        borrar = 0;
	}
    
    /*-----------------------------------------------------------------
    / metodos getters
    /-----------------------------------------------------------------*/
	public String getSucursal() { 
        return new String( sucursal );
     }
    
	public int getNumero() {
        return numero;
     }
    
	public String getNombre() {
        return new String( nombre ); 
    }
    
	public double getSaldo() {
        return saldo;
    }

    public boolean paraBorrar() {
        return borrar == 1;
    }
    
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setParaBorrar() {
        borrar = 1;
    }
    /*-----------------------------------------------------------------
    / longitud en bytes de un registro
    /-----------------------------------------------------------------*/
    
	public int length() {   
		return sucursal.length +
               Integer.SIZE / 8 +
               nombre.length +
               Double.SIZE / 8 +
               1; // Un byte
	}
    
    /*-----------------------------------------------------------------
    / metodos para escribir y leer un registro
    /-----------------------------------------------------------------*/
    
	public void read( RandomAccessFile raf ) throws IOException {   
		raf.read( sucursal );
		numero = raf.readInt();
		raf.read( nombre );
		saldo = raf.readDouble();
        borrar = raf.readByte();
	}
    
	public void write( RandomAccessFile raf ) throws IOException { 
		raf.write( sucursal );
		raf.writeInt( numero );
		raf.write( nombre );
		raf.writeDouble( saldo );
	    raf.writeByte( borrar );
    }	
}