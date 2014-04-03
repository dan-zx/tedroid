package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.util.AttributeSet;

import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Tablero del juego de pruebas. NO LLAMAR A ESTA CLASE DIRECTAMENTE pues solo
 * es para pruebas unitarias.
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
     * Además inicializar el tablero normalmente, crea una secuencia definida de
     * tetrominos para pruebas y usa valores diferentes para el tablero.
     */
    @Override
    protected void setUp() {
        super.setUp();
        setCustomDimensions(10, 5);
        setLevel(4);
        expectedTetrominos = new LinkedList<Tetromino>();
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.Z)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.J)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.T)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.O)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.O)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.O)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.L)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.J)
            .build());
        expectedTetrominos.add(new Tetromino.Builder(this)
            .use(DefaultShape.I)
            .build());
    }

    /**
     * @return saca uno de los tetrominos definidos no aleatorios.
     */
    @Override
    protected Tetromino getRandomTetromino() {
        return expectedTetrominos.poll();
    }
}