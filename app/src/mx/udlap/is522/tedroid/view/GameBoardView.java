package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import mx.udlap.is522.tedroid.view.model.DefaultShape;
import mx.udlap.is522.tedroid.view.model.Direction;
import mx.udlap.is522.tedroid.view.model.Tetromino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Tablero del juego donde los tetrominos se acumlan.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta, Wassim Lima Saad
 * @since 1.0
 */
public class GameBoardView extends View {

    public static final int DEFAULT_LEVEL = 0;

    private static final long DEFAULT_SPEED = 1000l;
    private static final int DEFAULT_COLUMNS = 10;
    private static final int DEFAULT_ROWS = 20;
    private static final String TAG = GameBoardView.class.getSimpleName();

    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private int[][] boardMatrix;
    private int rows;
    private int columns;
    private int level;
    private int tetrominoDownMoves;
    private int repeatedTetromino;
    private long currentSpeed;
    private float boardColumnWidth;
    private float boardRowHeight;
    private boolean startDropingTetrominos;
    private boolean isPaused;
    private boolean isGameOver;
    private GestureListener gestureListener;
    private GestureDetector gestureDetector;
    private MoveDownCurrentTetrominoTask moveDownCurrentTetrominoTask;
    private Paint tetrominoBorder;
    private Paint tetrominoForeground;
    private Paint background;
    private OnCommingNextTetrominoListener onCommingNextTetrominoListener;
    private OnPointsGainedListener onPointsGainedListener;
    private OnGameOverListener onGameOverListener;
    private Random random;

    /**
     * Construye un tablero de juego mediante un context.
     * 
     * @see android.view.View#View(Context)
     */
    public GameBoardView(Context context) {
        super(context);
        setUp();
    }

    /**
     * Construye un tablero de juego mediante XML
     * 
     * @see android.view.View#View(Context, AttributeSet)
     */
    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    /**
     * Construye un tablero de juego mediante XML y aplicando un estilo.
     * 
     * @see android.view.View#View(Context, AttributeSet, int)
     */
    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardColumnWidth = w / ((float) boardMatrix[0].length);
        boardRowHeight = h / ((float) boardMatrix.length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!startDropingTetrominos) {
            startDropingTetrominos = true;
            stopDropingTaskIfNeeded();
            setUpCurrentAndNextTetrominos();
            setAnotherRandomTetrominoIfNeeded();
            if (onCommingNextTetrominoListener != null) onCommingNextTetrominoListener.onCommingNextTetromino(nextTetromino);
            startDropingTask(currentSpeed);
        }

        drawBackground(canvas);
        currentTetromino.drawOn(canvas);
        drawBoardMatrix(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * Inicializa el layout de este tablero.
     */
    protected void setUp() {
        if (rows == 0 || columns == 0) {
            rows = DEFAULT_ROWS;
            columns = DEFAULT_COLUMNS;
        }

        buildBoardMatrix();
        setLevel(DEFAULT_LEVEL);
        random = new Random(System.nanoTime());
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        isPaused = false;
        isGameOver = false;
        tetrominoBorder = new Paint();
        tetrominoBorder.setStyle(Paint.Style.STROKE);
        tetrominoBorder.setColor(getContext().getResources().getColor(android.R.color.black));
        tetrominoForeground = new Paint();
        tetrominoForeground.setStyle(Paint.Style.FILL);
        background = new Paint();
        background.setStyle(Paint.Style.STROKE);
        background.setStrokeWidth(2);
        background.setColor(getContext().getResources().getColor(android.R.color.black));
    }

    protected void setUpCurrentAndNextTetrominos() {
        if (nextTetromino == null) currentTetromino = getRandomTetromino();
        else currentTetromino = nextTetromino;
        nextTetromino = getRandomTetromino();
        currentTetromino.centerOnGameBoardView();
    }

    /**
     * Saca otro tetromino al azar para el siguiente en caer si es que se repite
     * más de 2 veces seguidas.
     */
    private void setAnotherRandomTetrominoIfNeeded() {
        while (shouldGetAnotherRandomTetromino()) {
            Log.w(TAG, "Should get another random tetromino because Java randoms are dumb");
            nextTetromino = getRandomTetromino();
        }
    }

    /**
     * Construye la matriz y la llena de color transparente.
     */
    private void buildBoardMatrix() {
        boardMatrix = new int[rows][columns];
        for (int[] row: boardMatrix) Arrays.fill(row, android.R.color.transparent);
    }

    /**
     * Dibuja el fondo del tablero.
     * 
     * @param canvas un canvas para dibujar.
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
    }

    /**
     * Actualiza la matriz del tablero con los valores del tetromino actual.
     */
    private void updateBoardMatrix() {
        int[][] shapeMatrix = currentTetromino.getShapeMatrix();
        for (int row = 0; row < shapeMatrix.length; row++) {
            for (int column = 0; column < shapeMatrix[0].length; column++) {
                if (shapeMatrix[row][column] != android.R.color.transparent) {
                    int boardMatrixRow = currentTetromino.getPosition().getBoardMatrixRow() + row;
                    int boardMatrixColumn = currentTetromino.getPosition().getBoardMatrixColumn() + column;
                    boardMatrix[boardMatrixRow][boardMatrixColumn] = shapeMatrix[row][column];
                }
            }
        }
    }

    /**
     * Dibuja los tetrominos acumlados en el tablero.
     * 
     * @param canvas un Canvas donde dibujar.
     */
    private void drawBoardMatrix(Canvas canvas) {
        for (int row = 0; row < boardMatrix.length; row++) {
            for (int column = 0; column < boardMatrix[0].length; column++) {
                if (boardMatrix[row][column] != android.R.color.transparent) {
                    float x0 = column * boardColumnWidth;
                    float y0 = row * boardRowHeight;
                    float x1 = (column + 1) * boardColumnWidth;
                    float y1 = (row + 1) * boardRowHeight;
                    tetrominoForeground.setColor(getContext().getResources().getColor(boardMatrix[row][column]));
                    canvas.drawRect(x0, y0, x1, y1, tetrominoForeground);
                    canvas.drawRect(x0, y0, x1, y1, tetrominoBorder);
                }
            }
        }
    }

    /**
     * Checa si hay lineas completas para borrar.
     * 
     * @return la lista con los indicies de las filas completas o una lista
     *         vacia.
     */
    private List<Integer> lookForCompletedLines() {
        ArrayList<Integer> rowsToClear = new ArrayList<Integer>(4);
        for (int row = 0; row < boardMatrix.length; row++) {
            boolean isComplete = true;
            for (int color : boardMatrix[row]) {
                if (color == android.R.color.transparent) {
                    isComplete = false;
                    break;
                }
            }

            if (isComplete) rowsToClear.add(row);
        }

        return rowsToClear;
    }

    /**
     * Limpia las lineas completas y baja las lineas arriba de las lineas
     * completas.
     * 
     * @param rowsToClear los indicies de las filas que hay que limpiar.
     */
    private void clearCompletedLines(List<Integer> rowsToClear) {
        for (int rowToClear : rowsToClear) {
            for (int row = rowToClear; row >= 0; row--) {
                boardMatrix[row] = new int[boardMatrix[row].length];
                if (row == 0) Arrays.fill(boardMatrix[0], android.R.color.transparent);
                else System.arraycopy(boardMatrix[row - 1], 0, boardMatrix[row], 0, boardMatrix[row].length);
            }
        }
    }

    /**
     * Detiene la caida del tetromino actual si esta callendo.
     */
    private void stopDropingTaskIfNeeded() {
        if (moveDownCurrentTetrominoTask != null && moveDownCurrentTetrominoTask.getStatus() == AsyncTask.Status.RUNNING) {
            moveDownCurrentTetrominoTask.cancel(true);
            moveDownCurrentTetrominoTask = null;
        }
    }

    /**
     * Inicia la caida del tetromino actual.
     */
    private void startDropingTask(long speed) {
        moveDownCurrentTetrominoTask = new MoveDownCurrentTetrominoTask();
        moveDownCurrentTetrominoTask.execute(speed);
    }

    /**
     * @return tetromino escogiendo al azar una de las figuras predefinadas.
     */
    protected Tetromino getRandomTetromino() {
        int randomIndex = random.nextInt(DefaultShape.values().length);
        DefaultShape randomShape = DefaultShape.values()[randomIndex];
        return new Tetromino.Builder(this)
            .use(randomShape)
            .build();
    }

    /**
     * Revisa que no haya más de 3 tetrominos repetidos uno tras otro.
     * 
     * @return si ya se repitio 3 veces o más. 
     */
    protected boolean shouldGetAnotherRandomTetromino() {
        if (currentTetromino.equals(nextTetromino)) {
            if (repeatedTetromino == 0) repeatedTetromino = 2;
            else repeatedTetromino++;
        } else {
            repeatedTetromino = 0;
        }

        return repeatedTetromino >= 3;
    }

    /**
     * Reinicia el juego.
     */
    public void restartGame() {
        stopDropingTaskIfNeeded();
        buildBoardMatrix();
        startDropingTetrominos = false;
        isPaused = false;
        isGameOver = false;
        invalidate();
    }

    /**
     * @return la matriz del tablero.
     */
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * @return la altura de las filas del tablero.
     */
    public float getBoardRowHeight() {
        return boardRowHeight;
    }

    /**
     * @return la anchura de las columnas del tablero.
     */
    public float getBoardColumnWidth() {
        return boardColumnWidth;
    }

    /**
     * @return si el juego esta pausado o no.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * @return si el juego esta detenido o no.
     */
    public boolean isStopped() {
        return moveDownCurrentTetrominoTask == null || moveDownCurrentTetrominoTask.getStatus() != AsyncTask.Status.RUNNING;
    }

    /**
     * @return si el juego termino finalizo o no.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Pausa el juego.
     */
    public void pauseGame() {
        isPaused = true;
    }

    /**
     * Reanuda el juego.
     */
    public void resumeGame() {
        isPaused = false;
    }

    /**
     * Detiene el juego y no se podrá reinciar más
     */
    public void stopGame() {
        stopDropingTaskIfNeeded();
    }

    /**
     * @return el tetromino en juego.
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * @return el siguiente tetromino en caer.
     */
    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    /**
     * Inicializa las dimensiones del tablero de juego.
     * 
     * @param rows cuantas filas.
     * @param columns cuantas columnas.
     */
    public void setCustomDimensions(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        buildBoardMatrix();
    }

    /**
     * Cambia el nivel del juego.
     * 
     * @param level 0+.
     */
    public void setLevel(int level) {
        this.level = level < DEFAULT_LEVEL ? DEFAULT_LEVEL : level;
        currentSpeed = DEFAULT_SPEED / (this.level + 1);
    }

    /**
     * Aumenta la velocidad y el nivel de juego.
     */
    public void levelUp() {
        currentSpeed = DEFAULT_SPEED / (++level + 1);
    }

    /**
     * @param onCommingNextTetrominoListener el listener que escuchará cuando
     *        haya un nuevo tetromino listo.
     */
    public void setOnCommingNextTetrominoListener(OnCommingNextTetrominoListener onCommingNextTetrominoListener) {
        this.onCommingNextTetrominoListener = onCommingNextTetrominoListener;
    }

    /**
     * @param onPointsGainedListener listener que escuchará cuando caiga un
     *        tetromino y cuando haya lineas completas.
     */
    public void setOnPointsGainedListener(OnPointsGainedListener onPointsGainedListener) {
        this.onPointsGainedListener = onPointsGainedListener;
    }

    /**
     * @param onGameOverListener listener que escuchará cuando se acabe el
     *        juego.
     */
    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        this.onGameOverListener = onGameOverListener;
    }

    /**
     * Listener que que escuchará cuando haya un nuevo tetromino listo.
     * 
     * @author Daniel Pedraza-Arcega
     * @since versión 1.0
     */
    public static interface OnCommingNextTetrominoListener {

        /**
         * Ejecuta este método cuando se genera el siguente tetromino.
         * 
         * @param nextTetromino el siguente tetromino en caer.
         */
        void onCommingNextTetromino(Tetromino nextTetromino);
    }

    /**
     * Listener que escuchará cuando caiga un tetromino y cuando haya 
     * lineas completas.
     * 
     * @author Daniel Pedraza-Arcega
     * @since versión 1.0
     */
    public static interface OnPointsGainedListener {

        /**
         * Ejecuta este método cuando hay lineas que se completaron.
         * 
         * @param linesCleared el numero de lineas completas.
         */
        void onClearedLines(int linesCleared);
    }

    /**
     * Listener que escuchará cuando se acabe el juego.
     * @author Daniel Pedraza-Arcega
     * @since versión 1.0
     */
    public static interface OnGameOverListener {

        /**
         * Ejecuta este método cuando se termina el juego.
         */
        void onGameOver();
    }

    /**
     * Maneja el evento cuando el tetromino cae.
     */
    private void handleTetrominoDown() {
        if (!currentTetromino.moveTo(Direction.DOWN)) {
            gestureListener.shouldStopScrollEvent = true;
            if (tetrominoDownMoves == 0) { // TODO: esta mal esta condición para el Game over
                Log.i(TAG, "Game over");
                stopGame();
                isGameOver = true;
                if (onGameOverListener != null) onGameOverListener.onGameOver();
            } else {
                tetrominoDownMoves = 0;
                updateBoardMatrix();
                List<Integer> rowsToClear = lookForCompletedLines();
                if (!rowsToClear.isEmpty()) {
                    clearCompletedLines(rowsToClear);
                    if (onPointsGainedListener != null) onPointsGainedListener.onClearedLines(rowsToClear.size());
                }
                setUpCurrentAndNextTetrominos();
                setAnotherRandomTetrominoIfNeeded();
                if (onCommingNextTetrominoListener != null) onCommingNextTetrominoListener.onCommingNextTetromino(nextTetromino);
                invalidate();
            }
        } else {
            tetrominoDownMoves++;
            Log.d(TAG, "Move down tetromino");
            invalidate();
        }
    }

    /**
     * Tarea que lleva la cuenta de la velocidad de caida del tetromino en
     * juego.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class MoveDownCurrentTetrominoTask extends AsyncTask<Long, Void, Void> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected Void doInBackground(Long... params) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(params[0]);
                    publishProgress();
                } catch (InterruptedException e) { }
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            if (!isPaused && !isGameOver) {
                handleTetrominoDown();
            }
        }
    }

    /**
     * Escucha los eventos del tablero para mover el tetromino en juego.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private boolean shouldStopScrollEvent;
        private float totalDistanceX;
        private float totalDistanceY;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isPaused && !isGameOver && !shouldStopScrollEvent) {
                if (Math.abs(distanceX) > Math.abs(distanceY)) { // Scroll a los lados pero se pudo haber ido chueco
                    totalDistanceX += distanceX;
                    if (distanceX < 0) { // Derecha
                        if (Math.abs(totalDistanceX) >= boardColumnWidth) {
                            totalDistanceX = 0;
                            if (currentTetromino.moveTo(Direction.RIGHT)) {
                                Log.d(TAG, "Move tetromino to the right");
                                invalidate();
                            }
                        }
                    } else { // Izquierda
                        if (Math.abs(totalDistanceX) >= boardColumnWidth) {
                            totalDistanceX = 0;
                            if (currentTetromino.moveTo(Direction.LEFT)) {
                                Log.d(TAG, "Move tetromino to the left");
                                invalidate();
                            }
                        }
                    }
                } else if (distanceY < 0) { // Scroll hacia abajo pero se pudo haber ido chueco
                    totalDistanceY += distanceY;
                    if (Math.abs(totalDistanceY) >= boardRowHeight) {
                        totalDistanceY = 0;
                        handleTetrominoDown();
                    }
                }
            }

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!isPaused && !isGameOver) {
                if (currentTetromino.rotate()) {
                    Log.d(TAG, "Rotate tetromino");
                    invalidate();
                }
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onDown(MotionEvent e) {
            shouldStopScrollEvent = false;
            return true;
        }
    }
}