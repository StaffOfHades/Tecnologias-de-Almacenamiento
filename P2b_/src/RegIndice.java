/******************************************************************
/  clase: Indice
/
/  autor: Dr. JosŽ Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class RegIndice implements Constants {
    
	private byte[] clave;
	private int liga;
    private int ocupacion = 0;
    
    /*-----------------------------------------------------------------
    / constructor
    /-----------------------------------------------------------------*/
    
	public RegIndice( int longitud ) { clave = new byte[ longitud ]; }
    
    /*-----------------------------------------------------------------
    / mŽtodos getters/setters
    /-----------------------------------------------------------------*/
    
	public String getClave() { return new String( clave ).replace("\0",""); }
    
	public void setClave( String valor ) {
        
		byte[] v = valor.getBytes();
        
		for( int i = 0; i < clave.length && i < v.length; i++ )
            clave[i] = v[i];
	}
    
    public int getLiga() { return liga; }
    
	public void setLiga( int posicion ) { liga = posicion; }

    public int getOcupacion() { return ocupacion; }

    public boolean tieneEspacio() { return (ocupacion + 1) < MAX_CAPACITY; }

    public boolean agregar() {
        if ( tieneEspacio() ) {
            ocupacion++;
            return true;
        }
        return false;
    }
    
    /*-----------------------------------------------------------------
    / longitud en bytes y comparaci—n del valor de la clave
    /-----------------------------------------------------------------*/
    
	public int length() {
        return clave.length + Integer.SIZE / 8 + Integer.SIZE / 8;
    }
    
	/*public int compararCon( String valor ) {
        
		byte[] k = valor.getBytes();
		byte[] v = new byte[ clave.length ];
        
		for( int i = 0; i < clave.length && i < k.length; i++ )
             v[i] = k[i];
        
		return getClave().compareTo( new String(v) );
	}*/
    
    /*-----------------------------------------------------------------
    / mŽtodos para escribir y leer una entrada en el ’ndice
    /-----------------------------------------------------------------*/
    
	public void read( RandomAccessFile raf ) throws IOException {
        
		raf.read( clave );
		liga = raf.readInt();
        ocupacion = raf.readInt();
	}
    
	public void write( RandomAccessFile raf ) throws IOException {
        
		raf.write( clave );
		raf.writeInt( liga );
        raf.writeInt( ocupacion );
	}
}
