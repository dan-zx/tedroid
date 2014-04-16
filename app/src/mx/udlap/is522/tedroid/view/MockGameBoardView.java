package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tablero del juego de pruebas. NO LLAMAR A ESTA CLASE DIRECTAMENTE pues solo es para pruebas
 * unitarias.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MockGameBoardView extends GameBoardView {

    private Queue<Tetromino> expectedTetrominos;

    /**
     * Construye un tablero de juego mediante un context.
     * 
     * @see android.view.View#View(Context)
     */
    public MockGameBoardView(Context context) {
        super(context);
    }

    /**
     * Construye un tablero de juego mediante XML
     * 
     * @see android.view.View#View(Context, AttributeSet)
     */
    public MockGameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construye un tablero de juego mediante XML y aplicando un estilo.
     * 
     * @see android.view.View#View(Context, AttributeSet, int)
     */
    public MockGameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Además inicializar el tablero normalmente, crea una secuencia definida de tetrominos para
     * pruebas y usa valores diferentes para el tablero.
     */
    @Override
    protected void setUp() {
        super.setUp();
        setCustomDimensions(10, 5);
        setInitialLevel(4);
        expectedTetrominos = new LinkedList<Tetromino>();
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.Z).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.J).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.T).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.O).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.O).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.O).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.L).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.J).build());
        expectedTetrominos.add(new Tetromino.Builder(this).use(TetrominoShape.I).build());
    }

    /** @return saca uno de los tetrominos definidos no aleatorios. */
    @Override
    protected Tetromino randomTetromino() {
        return expectedTetrominos.poll();
    }
}