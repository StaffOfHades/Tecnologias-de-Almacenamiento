

public interface Constants {

    double SAL_MIN = 100.0;
    double SAL_MAX = 30000.6;
    
    int STR_SIZE = 20;      // tamaño maximo de las cadenas
    int SIN_ASIGNAR = -1;   // registro no encontrado

    int MAX_CLNT = 1;
    int MAX_SUC = 100;

    int CLNT_DIGITS = Integer.toString(MAX_CLNT).length();
    int SUC_DIGITS = Integer.toString(MAX_SUC).length();
    int NUM_DIGITS = Integer.toString(MAX_CLNT * MAX_SUC).length();

    int SAL_DECIMAL_DIGITS = 4;
    int SAL_DIGITS = 5;

    int N = 10;
    
    int TIME_UNIT = 1000000000; // Nano seconds

}
