/******************************************************************
 /  clase: Prueba
 /
 /  autor: Dr. JosŽ Luis Zechinelli Martini
 /******************************************************************/

import java.io.*;

public class Prueba implements Constants {

    private static double initTime;
    private static double currentTime;
    private static double endTime;
    
    /*-----------------------------------------------------------------
     / mŽtodos de prueba
     /-----------------------------------------------------------------*/

    private static void crear() throws IOException {

        empezarTemporizador();

        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File denso = new File( "Indice.Disperso" );
            
        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
        // archivo indexado usando una clave de b쳱ueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaF, indiceRaF );

        // Se borra el archivo para garantizar una prueba limpita
        archivo.borrar();

        Registro registro;

        //Sparse index section---------------------------------
        for( int num = 1, i = 1; i <= MAX_SUC; i++ ) {
            for( int j = 1; j <= MAX_CLNT; j++ ) {
				
                String suc = "Sucursal " + String.format( "%" + SUC_DIGITS +  "d", i );
                String nom = "Cliente " + String.format( "%" + CLNT_DIGITS + "d", j );
                     
                double sal = Math.random() * (SAL_MAX - SAL_MIN) + SAL_MIN;
                        
                archivo.insertar( new Registro( suc, num++, nom, sal ) );
            }
            //System.out.println( "Se crearon " + MAX_CLNT  + " clientes en la Sucursal "
            //        + String.format( "%" + SUC_DIGITS +  "d", i )) ;
            //vueltaTemporizador();
        }


        archivo.cerrar();

        pararTemporizador();
    }

	private static void mostrar() throws IOException {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaF = new RandomAccessFile( disperso, "rw" );

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaF, indiceRaF );

        // imprime los registros del indice y los del archivo
        archivo.mostrar();

        archivo.mostrarArbol();

        archivo.cerrar();
	}

    private static void busqueda( int sucursalNum ) throws IOException {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaf =  new RandomAccessFile (datos, "rw");
        RandomAccessFile indiceRaf =  new RandomAccessFile (disperso, "rw");

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaf, indiceRaf );

        // Busqueda de un registro por sucursal
        String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", sucursalNum );
        archivo.buscar( suc );

        archivo.cerrar();

        vueltaTemporizador();
    }

    private static void busquedaIntervalo( int sucursalNum ) throws IOException {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaf =  new RandomAccessFile (datos, "rw");
        RandomAccessFile indiceRaf =  new RandomAccessFile (disperso, "rw");

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaf, indiceRaf );

        // Busqueda de un registro por sucursal
        String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", sucursalNum );
        archivo.buscarGrupo( suc );

        archivo.cerrar();
    
        vueltaTemporizador();
    }


    private static void eliminacion( int sucursalNum ) throws IOException  {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaf = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaf = new RandomAccessFile( disperso, "rw" );

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaf, indiceRaf );

        // Eliminacion de de un registro utlizanod sucursal como referencia
        String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", sucursalNum );
        archivo.borrar(suc);

        archivo.cerrar();
        
        vueltaTemporizador();
    }

    /*-----------------------------------------------------------------
    / metodo principal
    /-----------------------------------------------------------------*/
	public static void main( String[] args ) throws Exception {

        crear();

        //empezarTemporizador();

        mostrar();
    
        /*
        for (int i = 1; i <= MAX_SUC; i++) {
            busqueda( i );
        }
        */

        /*
        for (int j = MAX_CLNT; j >= 1; j--)
            for (int i = 0; i < MAX_CLNT; i++)
                eliminacion( j );
        */

        /*
        for (int i = 1; i <= MAX_SUC; i++) {
            busquedaIntervalo( i );
        }
        */

        //pararTemporizador();

    }

	private static void empezarTemporizador() {

        initTime = System.nanoTime();
        currentTime = System.nanoTime();
    }

    private static void vueltaTemporizador() {

        endTime = System.nanoTime();
        final double lap  = (endTime - currentTime) / TIME_UNIT;
        final double time  = (endTime - initTime) / TIME_UNIT;

        System.out.println("Vuelta: " + lap + " segundos;\tTiempo: " + time + "\n");
        currentTime = System.nanoTime();
    }

    private static void pararTemporizador() {

        final double time  = (endTime - initTime) / TIME_UNIT;
        System.out.println("Tiempo Total: " + time + " segundos\n");
    }
}
