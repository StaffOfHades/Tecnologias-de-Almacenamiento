/******************************************************************
/  clase: Archivo
/
/  autor: Dr. JosŽ Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class Archivo implements Constants  {
     
	private RandomAccessFile raf = null;
	private IndiceDenso indiceDenso = null;
    
    /*-----------------------------------------------------------------
    / constructor: ’ndice denso con una clave de bœsqueda de STR_SIZE bytes
    /-----------------------------------------------------------------*/
    
	public Archivo( RandomAccessFile archivo,
                    RandomAccessFile indice )
	{
		raf = archivo;
		indiceDenso = new IndiceDenso( indice, STR_SIZE );
	}
    
    /*-----------------------------------------------------------------
    / File methods
    /-----------------------------------------------------------------*/
    
	public void insertar( Registro registro ) throws IOException {
        
		int posicionIndice = indiceDenso.getPosicion( registro.getSucursal() );
        
		if( posicionIndice == indiceDenso.size()-1 ) {
            
			int posicionArchivo = (int) raf.length() / registro.length();
			insertarEn( posicionArchivo, registro );
            
            if( indiceDenso.getLiga( posicionIndice ) == SIN_ASIGNAR )
				indiceDenso.updateLiga( posicionIndice, posicionArchivo );
            
            } else {
            
			int posicionArchivo = indiceDenso.getLiga( posicionIndice + 1 );
			insertarEn( posicionArchivo, registro );
            
			if( indiceDenso.getLiga( posicionIndice ) == SIN_ASIGNAR )
				indiceDenso.updateLiga( posicionIndice, posicionArchivo );
            
			for( posicionIndice ++;
                 posicionIndice < indiceDenso.size(); posicionIndice ++ )
            {
				posicionArchivo = indiceDenso.getLiga( posicionIndice ) + 1;
				indiceDenso.updateLiga( posicionIndice, posicionArchivo );
			}
		}
	}

    public Registro consultar (String nomSuc) throws IOException {
        int posicionIndice = indiceDenso.find(nomSuc);
		
		if (posicionIndice != SIN_ASIGNAR) {
			//Se crea un registro de datos
			Registro registro = new Registro();
			//Se obtiene la posicion del registro
			int posicion = indiceDenso.getLiga(posicionIndice);
			//Se posiciona el puntero enla posicion del registro y se lee
			raf.seek (posicion*registro.length()); 
			registro.read (raf);

            return registro;
        }

        return null;
    } 
	
	/*-----------------------------------------------------------------
    / borrar un registro del archivo
    /-----------------------------------------------------------------*/
    
	public boolean borrar (String nomSuc) throws Exception {
		
		int posicionIndice = indiceDenso.find(nomSuc);
		
		if (posicionIndice != SIN_ASIGNAR) {
			//Se crea un registro de datos
			Registro registro = new Registro();
			//Se obtiene la posicion del registro
			int posicion = indiceDenso.getLiga(posicionIndice);
			//Se posiciona el puntero enla posicion del registro y se lee
			raf.seek (posicion*registro.length()); 
			registro.read (raf);
			//Borrado logico del registro (En memoria)
			registro.setBorrado (true);
			registro.setSucursal ("@¡Eliminado!@");
			//Borrado en archivo
			raf.seek (posicion*registro.length()); 
			registro.write (raf);
			
			if (raf.getFilePointer() == raf.length()){
				//Con esto se compacta el archivo de Indices si este es el ultimo registro
				indiceDenso.borrarEntrada (posicionIndice);
			} else {
				//Se lee el registro justo debajo del que esta borrado
				registro.read (raf);
				
				if (registro.getSucursal().compareTo(nomSuc) == 0) {
					//Si entra al if significa que existe otro registro con la misma sucursal
					indiceDenso.updateLiga (posicionIndice, posicion+1);
				} else {
					//Se comparcta el archivo Indices
					indiceDenso.borrarEntrada (posicionIndice);
				}
			}

			return true;
		}

        return false;
	}
    
    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en el archivo
    /-----------------------------------------------------------------*/
    
	private void insertarEn( int posicion, Registro registro ) throws IOException {
        
		int n = (int) raf.length() / registro.length();
        
		for( int i = n-1; i >= posicion; i -- ) {
            
			Registro temp = new Registro();
            
			raf.seek( i * temp.length() );
			temp.read( raf );
            
			raf.seek( (i+1) * temp.length() );
			temp.write( raf );
		}
        
		raf.seek( posicion * registro.length() );
		registro.write( raf );
	}
    
    /*-----------------------------------------------------------------
    / presenta los registros tanto del archivo como de su ’ndice
    /-----------------------------------------------------------------*/
    
    public void mostrar() throws IOException {
        
		Registro registro = new Registro();
		int size = (int) raf.length() / registro.length();
        
		indiceDenso.mostrar();
        
		System.out.println( "Nœmero de registros: " + size );
		raf.seek( 0 );
        
		for( int i = 0; i < size; i ++ ) {
            
			registro.read( raf );
            //Se salta los registros que esten borrados
			if (registro.borrado()) continue;
			
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%3d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}
    
    /*-----------------------------------------------------------------
    / cierra el archivo de datos
    /-----------------------------------------------------------------*/
    
    public void cerrar() throws IOException {
        
        raf.close();
        indiceDenso.cerrar();
    }
}
