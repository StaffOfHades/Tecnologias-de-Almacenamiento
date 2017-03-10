/******************************************************************
/  clase Registro
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;
import java.lang.*;

// Quitar serializacion -> move a convertir los valores a byte de manera local
// Queda como parte d; Punto extra de bloque


public class Registro {

    private int tamañoSucursal;
    private int tamañoNombre;    
	private byte[] sucursal;
	private int numero = 0;
	private byte[] nombre;
	private double saldo = 0;
    //private byte borrar;   
 
    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Registro() {
        tamañoSucursal = 0;
        tamañoNombre = 0;
        sucursal = new byte[0];
        nombre = new byte[0];
    }
    
	public Registro( String nomSucursal, int numCuenta,
                        String nomCliente, double deposito )
	{
	    set( nomSucursal, numCuenta, nomCliente, deposito );
    }
   
    private void set( Registro copia ) {
        set( copia.getSucursal(), copia.getNumero(),
             copia.getNombre(), copia.getSaldo() );
    }
 
	private void set( String nomSucursal, int numCuenta,
                        String nomCliente, double deposito )
	{
        tamañoSucursal = nomSucursal.length();
        tamañoNombre = nomCliente.length();
 
        sucursal = nomSucursal.getBytes();
        
		numero = numCuenta;
        
		nombre = nomCliente.getBytes();
        
		saldo = deposito;

        //borrar = 0;
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
        //return borrar == 1;
        return false;
    }
 
    /*-----------------------------------------------------------------
    / metodos setter
    /-----------------------------------------------------------------*/ 
    
    public void setSucursalSize(int size) {
        this.tamañoSucursal = size;
        sucursal = new byte[size];
    }

    public void setNombreSize(int size) {
        this.tamañoNombre = size;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setParaBorrar() {
        //borrar = 1;
    }
    
    /*-----------------------------------------------------------------
    / longitud en bytes de un registro
    /-----------------------------------------------------------------*/
    
	public int length() {   
		return Integer.SIZE / 8 +
               Integer.SIZE / 8 +
               tamañoSucursal +
               Integer.SIZE / 8 +
               tamañoNombre +
               Double.SIZE / 8; // +
               //1; // Un byte (la bandera de borrar);
	}
    
    /*-----------------------------------------------------------------
    / metodos para escribir y leer un registro
    /-----------------------------------------------------------------*/
    
    private byte[] read() {
    
    }

   	private void write(byte[] bytes) {
    
    }
 
    @Deprecated
    public void read( RandomAccessFile raf ) throws IOException {
         
		tamañoSucursal = raf.readInt();
        setSucursalSize( tamañoSucursal );
        tamañoNombre = raf.readInt();
        setNombreSize( tamañoNombre );
        raf.read( sucursal );
		numero = raf.readInt();
		raf.read( nombre );
		saldo = raf.readDouble();
        borrar = raf.readByte();
	}
   
    @Deprecated
	public void write( RandomAccessFile raf ) throws IOException { 
		raf.writeInt( tamañoSucursal );
        raf.writeInt( tamañoNombre );
        raf.write( sucursal );
		raf.writeInt( numero );
		raf.write( nombre );
		raf.writeDouble( saldo );
	    raf.writeByte( borrar );
   }

    public byte[] read() {
        ByteArrayOutputStream array = null;
        ObjectOutputStream output = null;
        try {
            array = new ByteArrayOutputStream();
            output = new ObjectOutputStream(array);
            output.writeObject(this);
            return array.toByteArray();
        } catch (IOException e) {
            System.err.println("No se pudo serializar el archivo");
            return null;
        } finally {
            try {
                if (array != null)
                    array.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes) {
        ByteArrayInputStream array = null;
        ObjectInputStream input = null;
        try {
            array = new ByteArrayInputStream(bytes);
            input = new ObjectInputStream(array);
            set( (Registro) input.readObject() );
        } catch (Exception e) {
            System.err.println("No se pudo serializar el archivo");
        } finally {
            try {
                if (array != null)
                    array.close();
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
    }
	
}
