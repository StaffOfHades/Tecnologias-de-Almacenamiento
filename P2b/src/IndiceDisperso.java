import java.io.IOException;
import java.io.RandomAccessFile;

public class IndiceDisperso implements Constants {
	
    public RegIndice registro = null;
	public RandomAccessFile raf = null;
    
    public IndiceDisperso (RandomAccessFile index, int length) {
		raf = index;
		registro = new RegIndice (length);
	}
 
	public void pushPointers(int source, int amount) throws IOException
	{
		for(int i = source; i < size(); i++) {
			raf.seek( i * registro.length() );
			registro.read( raf );
			registro.setLiga( registro.getLiga() + amount );
			raf.seek( i * registro.length() );
			registro.write( raf );
		}
	}

	public int linearSearchDisperso(String searchKey) throws IOException
	{
		int length = (int) raf.length() / registro.length();
		if( length < 1 ) {
			return 0;
		}
		int previousData = 0;
		int currentData = 0;
		for( int i = 0; i < length; i++ ) {

			raf.seek( i * registro.length() );
			registro.read( raf );
			currentData = registro.getClave().compareTo( searchKey );
			System.out.println("Current data is " + currentData + " registry " + registro.getClave());

			if(currentData == 0 ||
                (currentData > 0 && i == 0) || 
                (currentData > 0 && previousData < 0) ||
                (currentData < 0 && i == (length - 1) ) )
            {
				return i;
			}

			previousData = currentData;
		}
		return SIN_ASIGNAR;
	}

	public int[] getBoundaries(String target) throws IOException {

		int[] vector = new int[2];
		int puntero = SIN_ASIGNAR;  //No hay limite derecho
			
		puntero = linearSearchDisperso(target);
		//System.out.println("Pointer A is " + pointerA);
		raf.seek( puntero * registro.length() );
		registro.read( raf );
	
		if( registro.getClave().equals(target) ) {
			
            vector[0] = registro.getLiga();
			if( keyOnLast( registro.getClave() ) ) {
				
                vector[1] = SIN_ASIGNAR;
			} else {
				
                raf.seek( ( (puntero + 1) * registro.length() ) );
				registro.read( raf );
				vector[1] = registro.getLiga() - 1;
			}
		} else if( registro.getClave().compareTo( target ) < 0 ) {
			
			vector[0] = registro.getLiga();
			
			if( keyOnLast( registro.getClave() ) ) {
				
				vector[1] = SIN_ASIGNAR;
			} else{
				
				raf.seek( (puntero + 1) * registro.length() );
				registro.read( raf );
				vector[1] = registro.getLiga() - 1;
			}
		} else { //target > 0
				
			vector[1] = registro.getLiga();
				
			String selectorMemory = registro.getClave();
			raf.seek( 0 );
			registro.read( raf );
				
			if( registro.getClave().compareTo(selectorMemory) > 0 ) {
					
				vector[0] = SIN_ASIGNAR;
			} else {
					
				raf.seek( (puntero - 1) * registro.length() );
				registro.read(raf);
				vector[0] = registro.getLiga() + 1;
			}
		}	
		
		return vector;
	}

	public boolean keyOnLast(String currentkey) throws IOException {
		raf.seek( (size() - 1) * registro.length() );
		registro.read( raf );
		return registro.getClave().equals( currentkey );
	}

	public void dumpIndex() throws IOException {

		raf.setLength(0);
	}

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
    
    public int regIndiceSize() {

    	return registro.length();
    }

	public int getPosicion( String clave ) throws IOException {
        
		if( size() == 0 )
			return insertarEn( 0, clave );
		else
			return buscarInsertar( clave, 0, size()-1 );
	}
	
    
	public void updateLiga( int posicion, int liga ) throws IOException {
        
		raf.seek( posicion * registro.length() );   // lee el registro
		registro.read( raf );
        
		registro.setLiga( liga );                   // actualiza la liga
        
		raf.seek( posicion * registro.length() );   // guarda el registro
		registro.write( raf );
	}

	private int busquedaBinaria( String clave, int izq, int der )
    
            throws IOException
	{
		while( izq <= der ) {
            
			int mitad = izq + ( der - izq ) / 2;
            
			raf.seek( mitad * registro.length() );
			registro.read( raf );
            
			if( registro.getClave().compareTo( clave) < 0 )
				izq = mitad + 1;
			else if( registro.getClave().compareTo( clave) > 0 )
				der = mitad - 1;
			else
				return mitad;
		}
        
		return SIN_ASIGNAR;
	}

	public int linearSearch(String searchKey) throws IOException
	{
		int length = (int) raf.length() / registro.length();
		for(int i = 0; i < length; i++)
		{
			raf.seek( i * registro.length() );
			registro.read( raf );
			
			if( registro.getClave().equals(searchKey) ) {
				return i;
			}
		}
		return SIN_ASIGNAR;
	}

	public void averiguarClave() {
		try{
		    raf.seek(0);
            registro.read( raf );
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
		System.out.println( registro.getClave() );
		System.exit(0);
	}

    private int buscarInsertar( String clave, int izq, int der )
    
            throws IOException
    {
		while( izq <= der ) {
            
			int mitad = izq + ( der - izq ) / 2;
            
			raf.seek( mitad * registro.length() );
			registro.read( raf );
            
			if( registro.getClave().compareTo( clave) > 0 ) {
                
				if( izq == der || ( mitad - 1 ) < 0 )
					return insertarEn( mitad, clave );
				else
					der = mitad;
                
			} else if( registro.getClave().compareTo( clave) < 0 ) {
                
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

	public int insertarEn( int posicion, String clave ) throws IOException {
        
		for( int i = size()-1; i >= posicion; i-- ) {
            
			raf.seek( i * registro.length() );
			registro.read( raf );
            
			raf.seek( (i + 1) * registro.length() );
			registro.write( raf );
		}
        
		raf.seek( posicion * registro.length() );
		registro.setClave( clave );
        registro.setLiga( SIN_ASIGNAR );
		registro.write( raf );
        
		return posicion;
	}

	public void mostrar() throws IOException {
        
		System.out.println( "Numero de entradas: " + size() );
		raf.seek( 0 );
        
		for( int i = 0; i < size(); i++ ) {
            
			registro.read( raf );
            
			System.out.println( "( " + registro.getClave() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getLiga() )+ " )" );
		}
	}
    
    /*-----------------------------------------------------------------
    / cierra el archivo indice
    /-----------------------------------------------------------------*/
    
    public void cerrar() throws IOException { 
    	raf.close(); 
    }	
}
