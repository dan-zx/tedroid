package mx.udlap.is522.tedroid.view.model;

import java.util.Arrays;
import java.util.Random;

import mx.udlap.is522.tedroid.view.GameBoardView;
import android.graphics.Paint;

/**
 * Define el comportamiento de cualquier tetromino o pieza de tetris. 
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class Tetromino {

    private GameBoardView gameBoardView;
    private int[][] shapeMatrix;
    private Position positionOnBoard;
    private boolean hasRotation;
    private Paint foreground;

    /**
     * Constructor. En realidad debe constuirse con un Tetromino.Builder
     *  
     * @param builder objeto para armar un nuevo tetromino.
     */
    private Tetromino(Tetromino.Builder builder) {
	gameBoardView = builder.gameBoardView;
	shapeMatrix = builder.shapeMatrix;
	hasRotation = builder.hasRotation;
	positionOnBoard = new Position();
	foreground = new Paint();
	foreground.setColor(builder.color);
    }

    /**
     * Dibuja este tetromino en el tablero. 
     */
    public void drawOnBoard() {
	// TODO: implementar Tetromino.drawOnBoard()
    }

    /**
     * Mueve este tetromino un lugar tablero usado {@link Direction#LEFT} o 
     * {@link Direction#RIGHT}.
     * 
     * @param direction {@link Direction#LEFT} o {@link Direction#RIGHT} 
     */
    public void moveTo(Tetromino.Direction direction) {
	int[][] boardMatrix = gameBoardView.getBoardMatrix();
	int horizontalSize = positionOnBoard.getX() + shapeMatrix[0].length;
	switch (direction) {
	    case LEFT:
		if (positionOnBoard.getX()-1 >= 0 && 
			boardMatrix[positionOnBoard.getY()][positionOnBoard.getX()-1] == 0) {
		    positionOnBoard.setX(positionOnBoard.getX()-1);
		}
		break;
	    case RIGHT:
		if (horizontalSize < boardMatrix[0].length && 
			boardMatrix[positionOnBoard.getY()][horizontalSize] == 0) {
		    positionOnBoard.setX(positionOnBoard.getX()+1);
		}
		break;
	}
    }

    /**
     * Mueve este tetromino un lugar hacia abajo en el tablero
     */
    public void moveDown() {
	int[][] boardMatrix = gameBoardView.getBoardMatrix();
	int verticalSize = positionOnBoard.getY() + shapeMatrix.length;
	if (verticalSize < boardMatrix.length && 
		boardMatrix[verticalSize][positionOnBoard.getX()] == 0) {
	    positionOnBoard.setY(positionOnBoard.getY()+1);
	}
    }

    /**
     * Rota este tetromino 90° en sentido de las agujas del reloj en el tablero.
     */
    public void rotate() {
	// TODO: rotar solo cuando puede rotar para que no se encime en otras piezas o se salga del tablero 
	if (hasRotation) {
	    int[][] originalShapeMatrix = shapeMatrix;
	    int[][] newShapeMatrix = new int[originalShapeMatrix[0].length][originalShapeMatrix.length];
	    for (int row = 0; row < originalShapeMatrix.length; row++) {
		for (int column = 0; column < originalShapeMatrix[0].length; column++) {
		    newShapeMatrix[column][originalShapeMatrix.length - 1 - row] = originalShapeMatrix[row][column];
		}
	    }

	    shapeMatrix = newShapeMatrix;
	}
    }

    /**
     * @return el tablero asociado a este tetromino
     */
    public GameBoardView getGameBoardView() {
	return gameBoardView;
    }

    /**
     * @return una matriz de 0s y 1s con la forma de este tetromino.
     */
    public int[][] getShapeMatrix() {
	return shapeMatrix;
    }

    /**
     * @return si la figura tiene o no rotación este tetromino.
     */
    public boolean hasRotation() {
	return hasRotation;
    }

    /**
     * @return el color hexadecimal de este tetromino.
     */
    public int getForegroundColor() {
	return foreground.getColor();
    }

    /**
     * @return la posición en el tablero donde se encuentra la esquina superior
     *         izquierda de la matriz de este tetromino
     */
    public Position getPositionOnBoard() {
	return positionOnBoard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + foreground.getColor();
	result = prime * result + positionOnBoard.hashCode();
	result = prime * result + (hasRotation ? 1231 : 1237);
	result = prime * result + Arrays.hashCode(shapeMatrix);
	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Tetromino other = (Tetromino) obj;
	if (foreground.getColor() != other.foreground.getColor())
	    return false;
	if (hasRotation != other.hasRotation)
	    return false;
	if (positionOnBoard != other.positionOnBoard)
	    return false;
	if (!Arrays.deepEquals(shapeMatrix, other.shapeMatrix))
	    return false;
	return true;
    }

    /**
     * Constantes de dirección
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static enum Direction { RIGHT, LEFT }

    /**
     * Contiene las coordenadas X y Y de un tetromino en su tablero.
     * 
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    public static class Position {

	private int x;
	private int y;

	/**
	 * {@inheritDoc}
	 */
	@Override
        public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + x;
	    result = prime * result + y;
	    return result;
        }

	/**
	 * {@inheritDoc}
	 */
	@Override
        public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Position other = (Position) obj;
	    if (x != other.x)
	        return false;
	    if (y != other.y)
	        return false;
	    return true;
        }

	/**
	 * @return la posición en el eje X del tablero donde se encuentra la
	 *         esquina superior izquierda de la matriz de este tetromino.
	 */
	public int getX() {
	    return x;
	}

	/**
	 * @param x la posición en el eje X del tablero donde se encuentra la
	 *        esquina superior izquierda de la matriz de este tetromino.
	 */
	public void setX(int x) {
	    this.x = x;
        }

	/**
	 * @return la posición en el eje Y del tablero donde se encuentra la
	 *          esquina superior izquierda de la matriz de este tetromino.
	 */
	public int getY() {
	    return y;
	}

	/**
	 * @param y la posición en el eje Y del tablero donde se encuentra la
	 *          esquina superior izquierda de la matriz de este tetromino.
	 */
	public void setY(int y) {
	    this.y = y;
        }
    }

    /**
     * Constructor de tetrominos.
     * 
     * @author Daniel Pedraza-Arcega
     */
    public static class Builder {
	
	public static final int DEFAULT_COLOR = 0xffe6e6e6; // Gris
	public static final boolean DEFAULT_HAS_ROATATION = false;
	
	private int[][] shapeMatrix;
	private boolean hasRotation;
	private int color;
	private GameBoardView gameBoardView;

	/**
	 * Constructor que inicializa valores por default.
	 * 
	 * @param gameBoardView el tablero donde crear el nuevo tetromino.
	 */
	public Builder(GameBoardView gameBoardView) {
	    hasRotation = DEFAULT_HAS_ROATATION;
	    color = DEFAULT_COLOR; 
	    this.gameBoardView = gameBoardView;
        }

	/**
	 * Construira un nuevo tetromino usando una de las figuras predefinadas.
	 * 
	 * @param shape un {@link DefaultShape}.
	 * @return este Builder.
	 */
	public Builder use(DefaultShape shape) {
	    shapeMatrix = Arrays.copyOf(shape.getShapeMatrix(), shape.getShapeMatrix().length);
	    color = gameBoardView.getContext().getResources().getColor(shape.getColorId());
	    hasRotation = shape.hasRotation();
	    return this;
	}

	/**
	 * Construira un nuevo tetromino escogiendo al azar una de las figuras
	 * predefinadas.
	 * 
	 * @return este Builder.
	 */
	public Builder useRandomDefaultShape() {
	    int randomIndex = new Random().nextInt(DefaultShape.values().length);
	    DefaultShape randomShape = DefaultShape.values()[randomIndex];
	    return use(randomShape);
	}

	/**
	 * Construira un nuevo tetromino con la forma de una matriz de 1s y 0s.
	 * 
	 * @param shapeMatrix una matriz de 1s y 0s.
	 * @return este Builder.
	 */
	public Builder setShape(int[][] shapeMatrix) {
	    this.shapeMatrix = shapeMatrix;
	    return this;
	}

	/**
	 * Construira un nuevo tetromino con un color hexadecimal.
	 *  
	 * @param hexColor un color hexadecimal.
	 * @return este Builder.
	 */
	public Builder setHexColor(int hexColor) {
	    color = hexColor;
	    return this;
        }

	/**
	 * Construira un nuevo tetromino con un color definido en R.color.
	 * 
	 * @param colorId un color R.color.
	 * @return este Builder.
	 */
	public Builder setColorResId(int colorId) {
	    color = gameBoardView.getContext().getResources().getColor(colorId);
	    return this;
        }

	/**
	 * Construira un nuevo tetromino que podrá rotar.
	 * 
	 * @return este Builder.
	 */
	public Builder hasRotation() {
	    this.hasRotation = true;
	    return this;
        }

	/**
	 * @return un nuevo tetromino.
	 */
	public Tetromino build() {
	    return new Tetromino(this);
	}
    }
}