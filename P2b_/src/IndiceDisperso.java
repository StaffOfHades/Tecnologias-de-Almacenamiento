/******************************************************************
/  clase: IndiceDenso
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class IndiceDisperso implements Constants { 

	private RegIndice registro = null;
	private RandomAccessFile raf = null;
    
    /*-----------------------------------------------------------------
    / constructor
    /-----------------------------------------------------------------*/
    
	public IndiceDisperso( RandomAccessFile indice, int longitud ) {
        
		raf = indice;
		registro = new RegIndice( longitud );
	}
    
    /*-----------------------------------------------------------------
    / consulta del numero total de entradas, de la
    / posicion de un registro y del contenido de su liga
    /-----------------------------------------------------------------*/
    
    public int size() throws IOException {
        
        return (int) raf.length() / registro.length();
    }
    
	public int find( String clave ) throws IOException {
        
		if( size() == 0 )
			return SIN_ASIGNAR;
		else
			return busquedaBinaria( clave, 0, size() - 1 );
	}
    
    public int getLiga( int posicion ) throws IOException {
        
		raf.seek( posicion * registro.length() );
		registro.read( raf );
        
		return registro.getLiga();
	}

    public boolean getTieneEspacio( int posicion ) throws IOException {
        raf.seek( posicion * registro.length() );
		registro.read( raf );
        
		return registro.tieneEspacio;
    }
    
    public int length() {
    	
        return registro.length();
    }
    /*-----------------------------------------------------------------
    / mŽtodos de inserci—n y actualizaci—n
    /-----------------------------------------------------------------*/
    
	public int getPosicion( String clave ) throws IOException {
        
		if( size() == 0 )
			return insertarEn( 0, clave );
		else
			return buscarInsertar( clave, 0, size() - 1 );
	}
    
	public void updateLiga( int posicion, int liga ) throws IOException {
        
		raf.seek( posicion * registro.length() );   // lee el registro
		registro.read( raf );
        
		registro.setLiga( liga );                   // actualiza la liga
        
		raf.seek( posicion * registro.length() );   // guarda el registro
		registro.write( raf );
	}

    //Metodo de busqueda lineal
    public int busquedaLineal (String claveBusq) throws IOException{
    	
        int size = (int) raf.length() / registro.length();
        boolean found = false;
        int i = 0;
        
        while ( !found && i < size) {
    		raf.seek( i * registro.length() );
    		registro.read( raf );
            
            found = registro.getClave().equals(claveBusq);
            if (!found) i++;
        } 
        return found ? i : SIN_ASIGNAR;
    }
    
    /*-----------------------------------------------------------------
    / busca un registro en el ’ndice y regresa su posici—n
    /-----------------------------------------------------------------*/
    
	private int busquedaBinaria( String clave, int izq, int der )
            throws IOException
    {
        int mitad;
		while( izq <= der ) {
            
			mitad = izq + ( der - izq ) / 2;
            
			raf.seek( mitad * registro.length() );
			registro.read( raf );
            
			if( registro.getClave().compareTo( clave ) < 0 )
				izq = mitad + 1;
			else if( registro.getClave().compareTo( clave ) > 0 )
				der = mitad - 1;
			else
				return mitad;
		}    

		return SIN_ASIGNAR;
	}
 
    /*-----------------------------------------------------------------
    / busca un registro y si no lo encuentra lo crea
    /-----------------------------------------------------------------*/
    
    private int buscarInsertar( String clave, int izq, int der )
        throws IOException
    {
		while( izq <= der ) {
            
			int mitad = izq + ( der - izq ) / 2;
            
			raf.seek( mitad * registro.length() );
			registro.read( raf );           
 
			if( registro.getClave().compareTo( clave ) > 0 ) {
             
				if( izq == der || ( mitad - 1 ) < 0)
					return insertarEn( mitad, clave );
				else
					der = mitad;
                
			} else if( registro.getClave().compareTo( clave ) < 0 ) {
                
				if( izq == der )
					return insertarEn( mitad + 1, clave );
				else
					izq = mitad + 1;
                
			} else {
                
				return mitad;
			}
		}
        
		throw new IOException( "Archivo inconsistente" );
	}

    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en el indice
    /-----------------------------------------------------------------*/
    
	private int insertarEn( int posicion, String clave ) throws IOException {
        
        System.out.println("Posicion: " + posicion);
       
        if ( size() > 0) {
            raf.seek( posicion  * registro.length() );
            registro.read( raf );
        
            if ( !registro.tieneEspacio() ) {

		        for( int i = size() - 1; i >= posicion; i-- ) {
            
			        raf.seek( i * registro.length() );
			        registro.read( raf );
            
			        raf.seek( (i + 1) * registro.length() );
			        registro.write( raf );
		        }
        
		        raf.seek( posicion * registro.length() );
		        registro.setClave( clave );
                registro.setLiga( SIN_ASIGNAR ); 
            }

        } else {
		    
            registro.setClave( clave );
            registro.setLiga( SIN_ASIGNAR ); 
        }

        registro.agregar();
		registro.write( raf );
        
		return posicion;
	}
    /*-----------------------------------------------------------------
    / borra una entrada del indice compactando el archivo
    /-----------------------------------------------------------------*/
	
	public void borrarEntrada (int posicion) throws Exception {
		
		if (posicion >= 0 && posicion <= size() - 1) {
			
			for (int i = posicion + 1; i < size(); i++) {
				
				//Posicionamiento de la posicion que se va a mantener
				raf.seek( i * registro.length() );
				registro.read( raf );
				
                //Posicionamiento en la posicion del elemento que se desea borrar
				raf.seek( (i - 1) * registro.length() );
				registro.write( raf );
				
			}

			raf.setLength( raf.length() - registro.length() );
		} else {
			throw new Exception ("Posicion invalida");
		}	
	}
    /*-----------------------------------------------------------------
    / presenta los registros del ’ndice
    /-----------------------------------------------------------------*/
    
	public void mostrar() throws IOException {
        
		System.out.println( "Numero de entradas: " + size() );
		raf.seek( 0 );
        
		for( int i = 0; i < size(); i++ ) {
            
			registro.read( raf );
            
			System.out.println( "( " + registro.getClave() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getLiga() )+ " )" );
		}
	}

    public void mostrar( String nomSuc ) throws IOException {
        
        int indice = busquedaBinaria( nomSuc, 0, size() - 1 );

        if (indice != SIN_ASIGNAR ) {
            System.out.println( "( " + registro.getClave() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getLiga() )+ " )" );
        } else {
            System.err.println( "No se encontro nigun indice bajo\"" + nomSuc + "\"" );
        }
    }
    
    /*-----------------------------------------------------------------
    / cierra el archivo indice
    /-----------------------------------------------------------------*/
    
    public void cerrar() throws IOException { raf.close(); }

}
