/******************************************************************
/
/  clase Test
/
/  autor Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class Prueba {
    
	private static int longuitudLlave = 20;
    
    /*-----------------------------------------------------------------
     / Test functions
     /-----------------------------------------------------------------*/
	private static void crearArchivo() {
        
		try {
            
			File file = new File( "Depositos.Info" );
			RandomAccessFile raf = new RandomAccessFile( file, "rw" );
			Archivo archivo = new Archivo( raf );
            
			Registro registro;
            
			registro = new Registro( "Sucursal 3", 3, "Cliente 3", 300.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 2", 2, "Cliente 2", 200.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 1", 5, "Cliente 1", 100.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 0", 0, "Cliente 0", 0.0 );
			archivo.insertarBinaria( registro );
            registro = new Registro( "Sucursal 3", 6, "Cliente 3", 300.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 2", 7, "Cliente 2", 200.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 1", 1, "Cliente 1", 100.0 );
			archivo.insertarBinaria( registro );
			registro = new Registro( "Sucursal 0", 8, "Cliente 0", 0.0 );
			archivo.insertarBinaria( registro ); 

			archivo.borrarBinaria(0);
			archivo.borrarBinaria(7);
			archivo.borrarBinaria(8);
			
			registro = new Registro( "Sucursal 0", 0, "Cliente 0", 0.0 );
			archivo.insertarBinaria( registro );
			
			raf.close();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
	
	private static void mostrarArchivo() {
        
		try {
            
			File file = new File( "Depositos.Info" );
			RandomAccessFile raf = new RandomAccessFile( file, "rw" );
            
			Archivo archivo = new Archivo( raf );
			archivo.imprimirRegistros();
			raf.close();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
	
	private static void ordenArchivo() {
		
		try {
			
			File file = new File( "Depositos.Info" );
			RandomAccessFile raf = new RandomAccessFile( file, "rw" );
            
			Archivo archivo = new Archivo( raf );
			archivo.comprobarOrden();
			raf.close();
		
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
	
    /*-----------------------------------------------------------------
    / metodo main
    /-----------------------------------------------------------------*/
    
	public static void main( String[] args ) {
			
		crearArchivo();
		mostrarArchivo();
		//ordenArchivo();
		mostrarArchivo();
	}
}
