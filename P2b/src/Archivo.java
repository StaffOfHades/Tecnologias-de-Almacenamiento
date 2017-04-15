/******************************************************************
/  clase: Archivo
/
/  autor: Dr. JosŽ Luis Zechinelli Martini
/******************************************************************/

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

public class Archivo implements Constants  {
     
	private RandomAccessFile raf = null;
	private IndiceDisperso indiceDisperso = null;
	private RandomAccessFile tempIndex;
    
    /*-----------------------------------------------------------------
    / constructor: indice denso con una clave de bœsqueda de STR_SIZE bytes
    /-----------------------------------------------------------------*/
    
	public Archivo( RandomAccessFile archivo,
                    RandomAccessFile indice )
	{
		raf = archivo;
		indiceDisperso = new IndiceDisperso( indice, STR_SIZE );
		tempIndex = indice;
	}
    
    /*-----------------------------------------------------------------
    / File methods
    /-----------------------------------------------------------------*/
    
	public void insertar( Registro registro ) throws IOException {
        
		int posicionIndice = indiceDisperso.getPosicion( registro.getSucursal() );
        
		if( posicionIndice == indiceDisperso.size() - 1 ) {
            
			int posicionArchivo = (int) raf.length() / registro.length();
			insertarEn( posicionArchivo, registro );
            
            if( indiceDisperso.getLiga( posicionIndice ) == SIN_ASIGNAR )
				indiceDisperso.updateLiga( posicionIndice, posicionArchivo );
            
        } else {
            
			int posicionArchivo = indiceDisperso.getLiga( posicionIndice + 1 );
			insertarEn( posicionArchivo, registro );
            
			if( indiceDisperso.getLiga( posicionIndice ) == SIN_ASIGNAR )
				indiceDisperso.updateLiga( posicionIndice, posicionArchivo );
            
			for( posicionIndice++;
                 posicionIndice < indiceDisperso.size(); posicionIndice++ )
            {
				posicionArchivo = indiceDisperso.getLiga( posicionIndice ) + 1;
				indiceDisperso.updateLiga( posicionIndice, posicionArchivo );
			}
		}
	}

    public ListIterator<Registro> consultarGrupo(String nomSuc) throws IOException {
        int posicionIndice = indiceDisperso.find(nomSuc);
		
		if (posicionIndice != SIN_ASIGNAR) {
			
            //Se crea un registro de datos
            final List<Registro> list = new ArrayList<>();

            int i = indiceDisperso.getLiga( posicionIndice );
            int max = indiceDisperso.getLiga( posicionIndice + 1 );    
            
            Registro registro = new Registro();            

            raf.seek( i * registro.length() );

            while (i++ < max) {
			    registro = new Registro();
			    
                registro.read( raf );
                
                if ( registro.borrado() ) continue;

                list.add( registro );
            }

            return list.listIterator();       
        }

        return null;
    }

    public Registro consultar (String nomSuc) throws IOException {
        int posicionIndice = indiceDisperso.find(nomSuc);
		
		if (posicionIndice != SIN_ASIGNAR) {
			
            //Se crea un registro de datos
			Registro registro = new Registro();
			
            //Se obtiene la posicion del registro
			int posicion = indiceDisperso.getLiga(posicionIndice);
			
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
		
		int posicionIndice = indiceDisperso.find(nomSuc);
		
		if (posicionIndice != SIN_ASIGNAR) {
			
            //Se crea un registro de datos
			Registro registro = new Registro();
			
            //Se obtiene la posicion del registro
			int posicion = indiceDisperso.getLiga(posicionIndice);
			
            //Se posiciona el puntero enla posicion del registro y se lee
			raf.seek (posicion*registro.length()); 
			registro.read (raf);
			
            //Borrado logico del registro (En memoria)
			registro.setBorrado (true);
			registro.setSucursal ("@¡Eliminado!@");
			
            //Borrado en archivo
			raf.seek( posicion*registro.length() ); 
			registro.write( raf );
			
			if (raf.getFilePointer() == raf.length()) {

				//Con esto se compacta el archivo de Indices si este es el ultimo registro
				indiceDisperso.borrarEntrada (posicionIndice);
			} else {

				//Se lee el registro justo debajo del que esta borrado
				registro.read (raf);
				
				if (registro.getSucursal().compareTo(nomSuc) == 0) {
					
                    //Si entra al if significa que existe otro registro con la misma sucursal
					indiceDisperso.updateLiga (posicionIndice, posicion+1);
				} else {
					
                    //Se comparcta el archivo Indices
					indiceDisperso.borrarEntrada (posicionIndice);
				}
			}

			return true;
		}

        return false;
	}

	public void indexUpdate() throws IOException {
		
        tempIndex.setLength( 0 );

		String previousTemp = "";
		Registro temp = new Registro();
		int size = (int) raf.length() / temp.length();		

        for(int i = 0; i < size; i++) {
			
            raf.seek( i * temp.length() );
			temp.read( raf );
			
            if ( !temp.getSucursal().equals(previousTemp) ){
				previousTemp = temp.getSucursal();
				int indexPos = indiceDisperso.getPosicion( temp.getSucursal() );
				indiceDisperso.updateLiga(indexPos, i);
			}
		}
	}

	public void compactar() throws IOException {
		
        Registro temp = new Registro();
		int size = (int) raf.length()/temp.length();
		
        for (int i = 0; i < size; i++) {
			
            raf.seek( i * temp.length() );
			temp.read( raf );

			//Se pone para borrar
			if ( temp.borrado() ){
	            compactarDesde(i, size--);
				
				indexUpdate();
				
                return;
			}
		}
	}
    
    public void compactarDesde(int desde, int size) throws IOException {
        
        Registro temp = new Registro();
        for (int i = desde; i < (size - 1); i++) {
		
            raf.seek( (i + 1) * temp.length() );
			temp.read( raf );
					
            raf.seek( i * temp.length() );
			temp.write( raf );
		}

        raf.setLength( raf.length() - temp.length() );
    }

    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en el archivo
    /-----------------------------------------------------------------*/
    
	private void insertarEn( int posicion, Registro registro ) throws IOException {
        
		int n = (int) raf.length() / registro.length();
        
		for( int i = n - 1; i >= posicion; i-- ) {
            
			Registro temp = new Registro();
            
			raf.seek( i * temp.length() );
			temp.read( raf );
            
			raf.seek( (i + 1) * temp.length() );
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
        
		indiceDisperso.mostrar();

		System.out.println( "Numero de registros: " + size );
       
        if (true) return;
		raf.seek( 0 );
        
		for( int i = 0; i < size; i++ ) {
            
			registro.read( raf );

            //Se salta los registros que esten borrados
			if ( registro.borrado() ) continue;
			
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}

    public void mostrar( String nomSuc ) throws IOException {
        
        ListIterator<Registro> it = consultarGrupo( nomSuc );
        if (it == null) {
            System.err.println( "No se encontro ningun registro bajo \"" + nomSuc + "\"" );
            return;
        }
        Registro registro = null;
        indiceDisperso.mostrar( nomSuc );
        int contador = 0;
        for (; it.hasNext() ; contador++, it.next() );
        for (; it.hasPrevious(); it.previous() );

		System.out.println( "Numero de registros: " + contador );

        while ( it.hasNext() ) {

            registro = it.next();

            System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
        }
    }
    
    /*-----------------------------------------------------------------
    / cierra el archivo de datos
    /-----------------------------------------------------------------*/
    
    public void cerrar() throws IOException {
        
        raf.close();
        indiceDisperso.cerrar();
    }
}
