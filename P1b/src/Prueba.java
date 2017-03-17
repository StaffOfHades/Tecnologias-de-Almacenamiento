/******************************************************************
/
/  clase Test
/
/  autor Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class Prueba { 
    
    /*-----------------------------------------------------------------
     / Test functions
     /-----------------------------------------------------------------*/
    
	private static void crearArchivo() {
        
		try {
            
			File file = new File( "Depositos.Info" );
			RandomAccessFile raf = new RandomAccessFile( file, "rw" );
			Archivo archivo = new Archivo( raf, 10) ;
            
			Registro registro;
            
			registro = new Registro( "Sucursal 3", 3, "Cliente 3", 300.0 );
			archivo.insertarI( registro );
			registro = new Registro( "Sucursal 2", 2, "Cliente 2", 200.0 );
			archivo.insertarI( registro );
			registro = new Registro( "Sucursal 1", 1, "Cliente 1", 100.0 );
			archivo.insertarI( registro );
			registro = new Registro( "Sucursal 0", 0, "Cliente 0", 0.0 );
			archivo.insertarI( registro );
                        
			raf.close();
            
		} catch( IOException e ) {
            
			System.err.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
	private static void mostrarArchivo() {
        
		try {
            
			File file = new File( "Depositos.Info" );
			RandomAccessFile raf = new RandomAccessFile( file, "rw" );
            
			Archivo archivo = new Archivo( raf, 10 );
			archivo.imprimirRegistros();
            
            archivo.imprimir(1); 
            archivo.cambiar(1, 20.5);
            archivo.imprimir(1);            
       
            archivo.borrarEn(0);
            
            archivo.imprimirRegistros();
         
            archivo.cambiarTamaño(30);

            Registro registro = new Registro( "Sucursal 0", 0, "Maria del Rosario Alvarez Figu", 300.0 );
			archivo.insertarF( registro ); 
            
            archivo.imprimirRegistros();
 
			raf.close();
            
		} catch( IOException e ) {
            
			System.err.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
    /*-----------------------------------------------------------------
    / metodo main
    /-----------------------------------------------------------------*/
    
	public static void main( String[] args ) {
        
		crearArchivo();
		mostrarArchivo();

	}
}
