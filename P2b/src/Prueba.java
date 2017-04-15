/******************************************************************
 /  clase: Prueba
 /
 /  autor: Dr. JosŽ Luis Zechinelli Martini
 /******************************************************************/

import java.io.*;

public class Prueba implements Constants {
    
    /*-----------------------------------------------------------------
     / mŽtodos de prueba
     /-----------------------------------------------------------------*/
    
	private static void crear() throws Exception {
        
		try {
            // metadatos del archivo de datos y del archivo ’ndice
            File datos = new File( "Archivo.Datos" );
            File disperso = new File( "Indice.Disperso" );
            
            // handlers para manipular el contenido de los archivos
			RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
			RandomAccessFile indiceRaF = new RandomAccessFile( disperso, "rw" );
            
            // archivo indexado usando una clave de bœsqueda de 20 bytes
			Archivo archivo = new Archivo( archivoRaF, indiceRaF );
			Registro registro;
                     
            for( int num = 1, i = 1; i <= MAX_SUC; i++ ) {
                for( int j = 1; j <= MAX_CLNT; j++ ) {
                        
                    String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", i );
                    String nom = "Cliente " + String.format( "%" + CLNT_DIGITS + "d", j );
                        
                    double sal = Math.random() * (SAL_MAX - SAL_MIN) + SAL_MIN;
                        
                    archivo.insertar( new Registro( suc, num++, nom, sal ) );
                    
                }
            }
            
			archivo.cerrar();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
	private static void mostrar() {
        
		try {
            // metadatos del archivo de datos y del archivo ’ndice
            File datos = new File( "Archivo.Datos" );
            File disperso = new File( "Indice.Disperso" );
            
            // handlers para manipular el contenido de los archivos
			RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
			RandomAccessFile indiceRaF = new RandomAccessFile( disperso, "rw" );
            
            // archivo indexado usando una clave de bœsqueda de 20 bytes
			Archivo archivo = new Archivo( archivoRaF, indiceRaF );
            
            // imprime los registros del indice y los del archivo
            archivo.mostrar();

            //archivo.mostrar( "Sucursal   1" );
            archivo.cerrar();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
    /*-----------------------------------------------------------------
    / metodo principal
    /-----------------------------------------------------------------*/
    
	public static void main( String[] args ) throws Exception{
        
		crear();
		mostrar();
	}
}
