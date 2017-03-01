/******************************************************************
/  clase Archivo
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class Archivo {
    
	private RandomAccessFile raf = null;
    
    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Archivo( RandomAccessFile raf ) {
        
		this.raf = raf;
	}
    
    /*-----------------------------------------------------------------
    / inserta un registro al inicio del archivo
    /-----------------------------------------------------------------*/
    
	public void insertar( Registro registro ) throws IOException {
        
		insertarEn( 0, registro );
	}
    
    /*-----------------------------------------------------------------
    / presenta los registros del archivo
    /-----------------------------------------------------------------*/
    
	public void imprimirRegistros() throws IOException {
        
		Registro registro = new Registro();
		int length = (int) (raf.length() / registro.length());
        
		System.out.println( "Numero de registros: " + length );
		raf.seek( 0 );
        
		for( int i = 0; i < length; i++ ) {
            
			registro.read( raf );
            
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + registro.getNumero() + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}
    
    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en la posicion p
    /-----------------------------------------------------------------*/
    
	private void insertarEn( int p, Registro registro ) throws IOException {
        
		int n = (int) (raf.length() / registro.length());
        
		for( int i = n-1; i >= p; i -- ) {    // desplazamiento de registros
            
			Registro temp = new Registro();
            
			raf.seek( i * temp.length() );
			temp.read( raf );
            
			raf.seek( (i+1) * temp.length() );
			temp.write( raf );
		}
        
		raf.seek( p * registro.length() );   // inserta el nuevo registro
		registro.write( raf );
	}
}
