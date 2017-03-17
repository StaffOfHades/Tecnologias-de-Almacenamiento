/******************************************************************
/  clase Registro
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;
import java.lang.*;

public class Registro {
    
    private static int tamaño;
	
    private byte[] sucursal;
	private int numero = 0;
	private byte[] nombre;
	private double saldo = 0;
    private byte borrar = 0;

    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Registro( int tamaño ) {
        this.tamaño = tamaño; 
        
        sucursal = new byte[this.tamaño];
        nombre = new byte[this.tamaño];
    }
 
	public Registro( String nomSucursal, int numCuenta,
                     String nomCliente, double deposito )
	{
        sucursal = new byte[this.tamaño];
        nombre = new byte[this.tamaño];
         
		if( nomSucursal.length() > this.tamaño || nomCliente.length() > this.tamaño ) { 
			System.err.println( "ATENCION: Sucursal o nombre con mas de " + this.tamaño + " caracteres" );
			System.err.println( "          Aumente el tamaño asignado a la sucursal y nombre." );

            System.exit(1);
        }
        
		for( int i = 0; i < this.tamaño && i < nomSucursal.getBytes().length; i++ )
			sucursal[i] = nomSucursal.getBytes()[i];
        
		numero = numCuenta;
        
		for( int i = 0; i < this.tamaño && i < nomCliente.getBytes().length; i++ )
			nombre[i] = nomCliente.getBytes()[i];
        
		saldo = deposito;

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

    public void changeSize(int tamaño) {
        final String s = getSucursal();
        final String n = getNombre();

        this.tamaño = tamaño; 
        
        sucursal = new byte[this.tamaño];
        nombre = new byte[this.tamaño];

        for( int i = 0; i < this.tamaño && i < s.getBytes().length; i++ )
			sucursal[i] = s.getBytes()[i];
        
		for( int i = 0; i < this.tamaño && i < n.getBytes().length; i++ )
			nombre[i] = n.getBytes()[i];

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
