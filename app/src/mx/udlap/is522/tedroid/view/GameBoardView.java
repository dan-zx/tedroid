package mx.udlap.is522.tedroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import mx.udlap.is522.tedroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Tablero del juego donde los tetrominos se acumlan.
 * 
 * @author Daniel Pedraza-Arcega, Andrés Peña-Peralta, Wassim Lima Saad
 * @since 1.0
 */
public class GameBoardView extends View {

    public static final int DEFAULT_LEVEL = 0;
    public static final int MAX_LEVEL = 9;

    private static final long DEFAULT_SPEED = 1000L;
    private static final int HEIGHT_ASPECT = 2;
    private static final int SPEED_FACTOR = 6;
    private static final int DEFAULT_COLUMNS = 10;
    private static final int DEFAULT_ROWS = 20;
    private static final int DROP_SOUND = 0;
    private static final int GAME_OVER_SOUND = 1;
    private static final int LEVEL_UP_SOUND = 2;
    private static final int LINE_CLEAR_SOUND = 3;
    private static final int PAUSE_SOUND = 4;
    private static final int ROTATE_SOUND = 5;

    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private int[][] boardMatrix;
    private int initialLevel;
    private int repeatedTetromino;
    private long currentSpeed;
    private float boardColumnWidth;
    private float boardRowHeight;
    private boolean isPaused;
    private boolean isGameOver;
    private boolean isGameStarted;
    private GestureListener gestureListener;
    private GestureDetector gestureDetector;
    private MoveDownCurrentTetrominoTask moveDownCurrentTetrominoTask;
    private Paint tetrominoBorder;
    private Paint tetrominoForeground;
    private Paint gridBackground;
    private OnCommingNextTetrominoListener commingNextTetrominoListener;
    private OnPointsAwardedListener pointsAwardedListener;
    private OnGameOverListener gameOverListener;
    private Random random;
    private SoundPool soundPool;
    private SparseIntArray soundPoolMap;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > 0 && heightMeasureSpec > 0) super.onMeasure(widthMeasureSpec, widthMeasureSpec*HEIGHT_ASPECT); 
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        drawBackgroundGrid(canvas);

        if (!isInEditMode()) {
            currentTetromino.drawOn(canvas);
            drawBoardMatrix(canvas);
        } else {
            currentTetromino = randomTetromino();
            currentTetromino.centerOnGameBoardView();
            currentTetromino.drawOn(canvas);
        }
    }

    /** Pinta el fondo de la cuadrilla del tablero. */
    private void drawBackgroundGrid(Canvas canvas) {
        for (int i = 0; i < boardMatrix[0].length; i++) canvas.drawLine(i * boardColumnWidth, 0, i * boardColumnWidth, getHeight(), gridBackground);
        for (int i = 0; i < boardMatrix.length; i++) canvas.drawLine(0, i * boardRowHeight, getWidth(), i * boardRowHeight, gridBackground);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /** Inicializa el layout de este tablero y las variables con su valor por default. */
    protected void setUp() {
        setUpDefaultValues();
        setUpBoardMatrix();
        setUpSounds();
        setUpGestures();
        setUpStyle();
    }

    /** Inicializa las variables con su valor por default. */
    private void setUpDefaultValues() {
        currentSpeed = DEFAULT_SPEED;
        initialLevel = DEFAULT_LEVEL;
    }

    /** Inicializa la matriz y la llena de color transparente. */
    private void setUpBoardMatrix() {
        boardMatrix = new int[DEFAULT_ROWS][DEFAULT_COLUMNS];
        for (int[] row : boardMatrix) Arrays.fill(row, android.R.color.transparent);
    }

    /** Inicializa los objetos encargados de manejar los gestos de esta vista. */
    private void setUpGestures() {
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    /** Inicializa los sonidos a reproducir. */
    private void setUpSounds() {
        if (isSoundEnabled()) {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
            soundPoolMap = new SparseIntArray(6);
            soundPoolMap.put(DROP_SOUND, soundPool.load(getContext(), R.raw.on_drop, 1));
            soundPoolMap.put(GAME_OVER_SOUND, soundPool.load(getContext(), R.raw.on_game_over, 1));
            soundPoolMap.put(LEVEL_UP_SOUND, soundPool.load(getContext(), R.raw.on_level_up, 1));
            soundPoolMap.put(LINE_CLEAR_SOUND, soundPool.load(getContext(), R.raw.on_line_clear, 1));
            soundPoolMap.put(PAUSE_SOUND, soundPool.load(getContext(), R.raw.on_pause, 1));
            soundPoolMap.put(ROTATE_SOUND, soundPool.load(getContext(), R.raw.on_rotate, 1));
        }
    }

    /** Inicializa el estilo para pintar. */
    private void setUpStyle() {
        tetrominoForeground = new Paint();
        tetrominoForeground.setStyle(Paint.Style.FILL); // El color se toma de la matriz
        tetrominoBorder = new Paint();
        tetrominoBorder.setStyle(Paint.Style.STROKE);
        tetrominoBorder.setColor(getContext().getResources().getColor(android.R.color.white));
        gridBackground = new Paint();
        gridBackground.setStyle(Paint.Style.STROKE);
        gridBackground.setColor(getContext().getResources().getColor(android.R.color.black));
    }

    /**
     * Inicializa el tetromino actual, lo centra en el esta vista e inicializa el próximo tetromino
     * en cola. Si no puede centrar el tetromino actual, el juego habrá terminado.
     */
    protected void setUpNewTetrominos() {
        if (nextTetromino == null) currentTetromino = randomTetromino();
        else currentTetromino = nextTetromino;
        do nextTetromino = randomTetromino(); while (shouldGetAnotherRandomTetromino());
        if (commingNextTetrominoListener != null) commingNextTetrominoListener.onCommingNextTetromino(nextTetromino);
        if (!currentTetromino.centerOnGameBoardView()) onGameOver();
    }

    /** @return tetromino escogiendo al azar una de las figuras predefinadas. */
    protected Tetromino randomTetromino() {
        if (random == null) random = new Random();
        int randomIndex = random.nextInt(TetrominoShape.values().length);
        TetrominoShape randomShape = TetrominoShape.values()[randomIndex];
        return new Tetromino.Builder(this).use(randomShape).build();
    }

    /**
     * Revisa que no haya más de 3 tetrominos repetidos uno tras otro.
     * 
     * @return si ya se repitio 3 veces o más.
     */
    private boolean shouldGetAnotherRandomTetromino() {
        if (currentTetromino.equals(nextTetromino)) {
            if (repeatedTetromino == 0) repeatedTetromino = 2;
            else repeatedTetromino++;
        } else repeatedTetromino = 0;

        return repeatedTetromino >= 3;
    }

    /**
     * Detiene el juego, reproduce el sonido de game over y ejecuta
     * {@link OnGameOverListener#onGameOver()}.
     */
    private void onGameOver() {
        stopDropingTaskIfNeeded();
        invalidate();
        isGameOver = true;
        isGameStarted = false;
        play(GAME_OVER_SOUND);
        if (gameOverListener != null) gameOverListener.onGameOver();
    }

    /** Actualiza la matriz del tablero con los valores del tetromino actual. */
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
     * @return la lista con los indicies de las filas completas o una lista vacia.
     */
    private ArrayList<Integer> lookForCompletedLines() {
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

    /** Limpia las lineas completas y baja las lineas arriba de las lineas completas. */
    private void clearAnyCompletedLines() {
        ArrayList<Integer> rowsToClear = lookForCompletedLines();
        if (!rowsToClear.isEmpty()) {
            for (int rowToClear : rowsToClear) {
                for (int row = rowToClear; row >= 0; row--) {
                    boardMatrix[row] = new int[boardMatrix[row].length];
                    if (row == 0) Arrays.fill(boardMatrix[0], android.R.color.transparent);
                    else System.arraycopy(boardMatrix[row - 1], 0, boardMatrix[row], 0, boardMatrix[row].length);
                }
            }

            play(LINE_CLEAR_SOUND);
            if (pointsAwardedListener != null) pointsAwardedListener.onClearedLines(rowsToClear.size());
        }
    }

    /** Detiene la caida del tetromino actual si esta callendo. */
    private void stopDropingTaskIfNeeded() {
        if (moveDownCurrentTetrominoTask != null && 
                moveDownCurrentTetrominoTask.getStatus() == AsyncTask.Status.RUNNING) {
            moveDownCurrentTetrominoTask.cancel(true);
            moveDownCurrentTetrominoTask = null;
        }
    }

    /** Inicia la caida del tetromino actual. */
    private void startDropingTask(long speed) {
        moveDownCurrentTetrominoTask = new MoveDownCurrentTetrominoTask();
        moveDownCurrentTetrominoTask.execute(speed);
    }

    /** @return si los sonidos estan habilitados o no la configuración. */
    private boolean isSoundEnabled() {
        if (!isInEditMode()) {
            return PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getBoolean(
                            getContext().getString(R.string.sounds_switch_key), 
                            getContext().getResources().getBoolean(R.bool.default_sounds_switch_value));
        }

        return false;
    }

    /**
     * Reproduce el sonido asociado con el id del sonido proporcionado.
     * 
     * @param sound el id del sonido a reproducir.
     */
    private void play(int sound) {
        if (isSoundEnabled()) soundPool.play(soundPoolMap.get(sound), 1f, 1f, 1, 0, 1f);
    }

    /** @return la matriz del tablero. */
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /** @return la altura de las filas del tablero. */
    public float getBoardRowHeight() {
        return boardRowHeight;
    }

    /** @return la anchura de las columnas del tablero. */
    public float getBoardColumnWidth() {
        return boardColumnWidth;
    }

    /** @return si el juego esta pausado o no. */
    public boolean isPaused() {
        return isPaused;
    }

    /** @return si el juego esta detenido o no. */
    public boolean isStopped() {
        return moveDownCurrentTetrominoTask == null || 
                moveDownCurrentTetrominoTask.getStatus() != AsyncTask.Status.RUNNING;
    }

    /** @return si el juego termino finalizo o no. */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Inicia el juego.
     * 
     * @throws IllegalStateException cuando el juego ya ha sido iniciado o cuando el juego ha
     *         termindo.
     */
    public void startGame() {
        if (isGameStarted) throw new IllegalStateException("Game is already started!!");
        if (isGameOver) throw new IllegalStateException("Call GameBoardView.restartGame()!!");
        setUpNewTetrominos();
        startDropingTask(currentSpeed);
        isGameStarted = true;
        invalidate();
    }

    /** Pausa el juego. */
    public void pauseGame() {
        if (!isGameOver && !isPaused) {
            isPaused = true;
            play(PAUSE_SOUND);
        }
    }

    /** Reanuda el juego. */
    public void resumeGame() {
        if (!isGameOver && isPaused) {
            isPaused = false;
            play(PAUSE_SOUND);
        }
    }

    /** Detiene el juego y debe llamarse a {@link #restartGame()} para reiniciar el juego. */
    public void stopGame() {
        stopDropingTaskIfNeeded();
        isGameStarted = false;
    }

    /** Reinicia el juego. */
    public void restartGame() {
        isPaused = false;
        isGameOver = false;
        isGameStarted = false;
        stopDropingTaskIfNeeded();
        setInitialLevel(initialLevel);
        setUpBoardMatrix();
        startGame();
    }

    /** @return el tetromino en juego. */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /** @return el siguiente tetromino en caer. */
    public Tetromino getNextTetromino() {
        return nextTetromino;
    }

    /**
     * Cambia el nivel inicial del juego.
     * 
     * @param level {@link #DEFAULT_LEVEL} <= level <= {@link #MAX_LEVEL}.
     */
    public void setInitialLevel(int level) {
        initialLevel = level <= DEFAULT_LEVEL ? DEFAULT_LEVEL : level >= MAX_LEVEL ? MAX_LEVEL : level;
        currentSpeed = DEFAULT_SPEED;
        for (int i = 0; i < initialLevel; i++) currentSpeed -= currentSpeed / SPEED_FACTOR;
    }

    /** @return el nivel inicial del juego. */
    public int getInitialLevel() {
        return initialLevel;
    }

    /** Aumenta la velocidad y el nivel de juego. */
    public void levelUp() {
        currentSpeed -= currentSpeed / SPEED_FACTOR;
        play(LEVEL_UP_SOUND);
        stopDropingTaskIfNeeded();
        startDropingTask(currentSpeed);
    }

    /** @return la velocidad del juego actual. */
    public long getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * @param commingNextTetrominoListener el listener que escuchará cuando haya un nuevo tetromino
     *        listo.
     */
    public void setOnCommingNextTetrominoListener(OnCommingNextTetrominoListener commingNextTetrominoListener) {
        this.commingNextTetrominoListener = commingNextTetrominoListener;
    }

    /**
     * @param pointsAwardedListener listener que escuchará cuando caiga un tetromino y cuando haya
     *        lineas completas.
     */
    public void setOnPointsAwardedListener(OnPointsAwardedListener pointsAwardedListener) {
        this.pointsAwardedListener = pointsAwardedListener;
    }

    /** @param gameOverListener listener que escuchará cuando se acabe el juego. */
    public void setOnGameOverListener(OnGameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
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
     * Listener que escuchará cuando haya eventos que otorgen puntos.
     * 
     * @author Daniel Pedraza-Arcega
     * @since versión 1.0
     */
    public static interface OnPointsAwardedListener {

        /**
         * Ejecuta este método cuando hay lineas que se completaron.
         * 
         * @param linesCleared el numero de lineas completas.
         */
        void onClearedLines(int linesCleared);

        /**
         * Ejecuta este método cuando el tetromino actual cae de golpe al piso.
         * 
         * @param gridSpaces los espacios recorridos al caer.
         */
        void onHardDropped(int gridSpaces);

        /**
         * Ejecuta este método cuando el tetromino actual cae al piso más rápido que la velocidad
         * del nivel.
         * 
         * @param gridSpaces los espacios recorridos al caer.
         */
        void onSoftDropped(int gridSpaces);
    }

    /**
     * Listener que escuchará cuando se acabe el juego.
     * 
     * @author Daniel Pedraza-Arcega
     * @since versión 1.0
     */
    public static interface OnGameOverListener {

        /** Ejecuta este método cuando se termina el juego. */
        void onGameOver();
    }

    /**
     * Cancela gestos, actualiza la matriz, limpia las lineas completas y reproduce
     * {@link #DROP_SOUND}.
     */
    private void onCurrentTetrominoOnFloor() {
        gestureListener.shouldStopScrollEvent = true;
        updateBoardMatrix();
        clearAnyCompletedLines();
        invalidate();
        play(DROP_SOUND);
    }

    /** Move el tetromino hasta el suelo del tablero en un tiempo. */
    private void hardDropCurrentTetromino() {
        int gridSpaces;
        for (gridSpaces = 0; currentTetromino.moveTo(Tetromino.Direction.DOWN); gridSpaces++);
        if (pointsAwardedListener != null) pointsAwardedListener.onHardDropped(gridSpaces);
        onCurrentTetrominoOnFloor();
        setUpNewTetrominos();
    }

    /**
     * Tarea que lleva la cuenta de la velocidad de caida del tetromino en juego.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    private class MoveDownCurrentTetrominoTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(params[0]);
                    publishProgress();
                } catch (InterruptedException e) {
                    // Se ignora y si no se ha cancelado esta tarea volverá a
                    // intentar dormir.
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (!isPaused && !isGameOver) {
                if (!currentTetromino.moveTo(Tetromino.Direction.DOWN)) {
                    onCurrentTetrominoOnFloor();
                    setUpNewTetrominos();
                } else invalidate();
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
        private int softDropGridSpaces;
        private float totalDistanceX;
        private float totalDistanceY;

        @Override // TODO: Reducir la complejidad ciclomática de este método
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isGameStarted && !isPaused && !isGameOver && !shouldStopScrollEvent) {
                // Scroll a los lados
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    totalDistanceX += distanceX;
                    // Derecha
                    if (distanceX < 0) {
                        if (Math.abs(totalDistanceX) >= boardColumnWidth) {
                            totalDistanceX = 0;
                            if (currentTetromino.moveTo(Tetromino.Direction.RIGHT)) invalidate();
                        }
                        // Izquierda
                    } else {
                        if (Math.abs(totalDistanceX) >= boardColumnWidth) {
                            totalDistanceX = 0;
                            if (currentTetromino.moveTo(Tetromino.Direction.LEFT)) invalidate();
                        }
                    }
                    // Scroll hacia abajo
                } else if (distanceY < 0) {
                    totalDistanceY += distanceY;
                    if (Math.abs(totalDistanceY) >= boardRowHeight) {
                        totalDistanceY = 0;
                        // Si no puede continuar abajo significa que bajo en modo SoftDrop
                        if (!currentTetromino.moveTo(Tetromino.Direction.DOWN)) {
                            onCurrentTetrominoOnFloor();
                            setUpNewTetrominos();
                            if (pointsAwardedListener != null) pointsAwardedListener.onSoftDropped(softDropGridSpaces);
                        } else {
                            invalidate();
                            softDropGridSpaces++;
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isGameStarted && !isPaused && !isGameOver) {
                if (currentTetromino.rotate()) {
                    invalidate();
                    play(ROTATE_SOUND);
                }
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isGameStarted && !isPaused && !isGameOver) hardDropCurrentTetromino();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            shouldStopScrollEvent = false;
            softDropGridSpaces = 0;
            return true;
        }
    }
}