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
    
	/*
    private static void crear() throws Exception {
        
		try {
            // metadatos del archivo de datos y del archivo ’ndice
            File datos = new File( "Archivo.Datos" );
            File denso = new File( "Indice.Denso" );
            
            // handlers para manipular el contenido de los archivos
			RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
			RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
            // archivo indexado usando una clave de bœsqueda de 20 bytes
			Archivo archivo = new Archivo( archivoRaF, indiceRaF );
			Registro registro;
            
            for( int num = 1, i = 1; i <= 999; i++ ) {
                for( int j = 1; j <= 9; j++ ) {
                    //for( int k = 1; k <= 1; k++, num++ ) {
                        
                    String suc = "Sucursal " + String.format( "%3d", i );
                    String nom = "Cliente " + j;
                        
                    double salMin = 100.0, salMax = 30000.6;
                    double sal = Math.random() * (salMax - salMin) + salMin;
                        
                    archivo.insertar( new Registro( suc, num, nom, sal ) );
                    
                    num++;
                    
                    //}
                }
            }
            
            for( int i = 0; i < 2; i ++ )
                archivo.borrar( "Sucursal   8" );
            
            for( int i = 0; i < 2; i ++ )
                archivo.borrar( "Sucursal   6" );

			archivo.cerrar();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
*/

    private static void crearDisperso() throws IOException {
        
        // metadatos del archivo de datos y del archivo ⮤ice
        File datos = new File( "ArchivoDisperso.Datos" );
        File denso = new File( "Indice.Disperso" );
            
        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
        // archivo indexado usando una clave de b쳱ueda de 20 bytes
        Disperso archivo = new Disperso( archivoRaF, indiceRaF );
        Registro registro;
                        
        //-----------------------------------------------------
        //Sparse index section---------------------------------
        for( int num = 1, i = 1; i <= MAX_SUC; i++ ) {
            for( int j = 1; j <= MAX_CLNT; j++ ) {
				
                String suc = "Sucursal " + String.format( "%" + SUC_DIGITS +  "d", i );
                String nom = "Cliente " + String.format( "%" + CLNT_DIGITS + "d", j );
                     
                double sal = Math.random() * (SAL_MAX - SAL_MIN) + SAL_MIN;
                        
                archivo.insertDisperso( new Registro( suc, num++, nom, sal ) );
            }
        }
        //-----------------------------------------------------
            
        /*
        //Test:
        archivo.probar();
        */
            
        /*
        for( int i = 0; i < 1000; i ++ )
            file.delete( "Sucursal 100" );
            
        for( int i = 0; i < 1000; i ++ )
            file.delete( "Sucursal  95" );
        */
        archivo.cerrar();
            
    }

	private static void mostrar() {
        
		try {
            // metadatos del archivo de datos y del archivo ’ndice
            File datos = new File( "ArchivoDisperso.Datos" );
            File disperso = new File( "Indice.Disperso" );
            
            // handlers para manipular el contenido de los archivos
			RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
			RandomAccessFile indiceRaF = new RandomAccessFile( disperso, "rw" );
            
            // archivo indexado usando una clave de bœsqueda de 20 bytes
			Disperso archivo = new Disperso( archivoRaF, indiceRaF );
            
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
    
	public static void main( String[] args ) throws Exception {
        //double denseIndexTime = 0;
        double sparseIndexTime = 0;
        double initTime = System.nanoTime();
		//crear();
		//mostrar();
        double endTime = System.nanoTime();
        //denseIndexTime = (endTime - initTime) / 1000000000;
        crearDisperso();
        mostrar();
        endTime = System.nanoTime();
        sparseIndexTime = (endTime - initTime) / TIME_UNIT;
		
        //System.out.println("Tiempo Indice Denso: " + denseIndexTime);
        System.out.println("Tiempo Indice Disperso: " + sparseIndexTime);
	}
}
