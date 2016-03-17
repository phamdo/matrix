/* Author: Kaman Phamdo */

/* 
 * This class represents an m x n matrix and provides tools for linear
 * algebra computations on matrices.
 */
public class Matrix {
    private double[][] data;
	private int rows;
	private int cols;

	/** 
     * constructs an m x n Matrix of all zeroes 
     */
	public Matrix(int m, int n) {
		rows = m;
		cols = n;
		data = new double[rows][cols];
	}

    /** 
     * constructs an n x n identity Matrix 
     */
    public Matrix(int n) {
        rows = n;
        cols = n;
        data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j) {
                    data[i][j] = 1;
                }
                else {
                    data[i][j] = 0;
                }
            }
        }
    }

	/** 
     * constructs a Matrix with the same values stored in 2-dim array 'data' 
     */
	public Matrix(double data[][]) {
		rows = data.length;
		cols = data[0].length;
		
		/* looks for row in 2d array with most columns */
		for (int i = 0; i < rows; i++) {
			int len = data[i].length;
			if (len > cols) {
				cols = len;
			}
		}

		/* assigns data to matrix */
		this.data = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				try {
					this.data[i][j] = data[i][j];
				} 
                /* if array is jagged, fill in missing values with 0 */
                catch (ArrayIndexOutOfBoundsException e) {
					this.data[i][j] = 0;
				}		
			}
		}
	}


    /** 
     * constructs a deep copy of the Matrix 'm' 
     */
    public Matrix(Matrix m) {
        this.rows = m.rows;
        this.cols = m.cols;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.data[i][j] = m.data[i][j];
            }
        }
    }	
	
	/** 
     * @return a Matrix that is the row reduced echelon form of current Matrix 
     */
	public Matrix rref() {
		Matrix A = new Matrix(this); /* create copy of current Matrix */

		for (int i = 0; i < rows; i++) {
			boolean pivot = false; /* true if pivot is found */
			for (int j = 0; j < cols && !pivot; j++) {
				if (A.data[i][j] == 0) {
					/* iterate through rows below until non-zero entry found */
					for (int k = i + 1; k < rows; k++) {
						if (A.data[k][j] != 0) {
							A.interchange(i, k);
							pivot = true;
							break;
						}
					}
				} 
				else {
					pivot = true;
				}
				if (pivot) {
					/* divide i-th row to make pivot 1 */
					double ratio = A.data[i][j];
					for (int k = j; k < cols; k++) {
						A.data[i][k] /= ratio;
					}
					
					/* make entries underneath pivot become 0 by subtracting a 
					 * multiple of i-th row */
					for (int k = i + 1; k < rows; k++) {
						double temp = A.data[k][j];
						if (temp != 0) {
							for (int p = j; p < cols; p++) {
								A.data[k][p] -= (A.data[i][p] * temp);
							}
						}					
					}
				}
			}
		}
		/* back substitution */
		for (int i = 1; i < rows; i++) {
			boolean pivot = false;
			/* look for pivot position */
			int j = 0;
			while (j < cols && A.data[i][j] == 0) {
				j++;
			}
			if (j < cols && A.data[i][j] == 1) { /* pivot found */
				for (int k = i - 1; k >= 0; k--) {
					double temp = A.data[k][j];
					for (int p = j; p < cols; p++) {
						A.data[k][p] -= A.data[i][p] * temp;
					}
				}
			}	
		}
		return A;
	}
	

	/** 
     * interchanges the 'a'th row and the 'b'th row in the current Matrix,
	 * assuming the first row of the Matrix has an index of 0. 
     */
	public void interchange(int a, int b) {
		double temp;
		for (int i = 0; i < cols; i++) {
			temp = data[a][i];
			data[a][i] = data[b][i];
			data[b][i] = temp;
		}
	}


	/** 
     * @return the transpose of the current Matrix 
     */
	public Matrix transpose() {
		Matrix t = new Matrix(cols, rows);
		for (int i = 0; i < t.rows; i++) {
			for (int j = 0; j < t.cols; j++) {
				t.data[i][j] = data[j][i];
			}
		}
		return t;
	}
	

    /** 
     * @return the inverse of the current Matrix, or null if the current
     * Matrix is not invertible 
     */
    public Matrix inverse() {
        Matrix inv = null; /* inverse */
        Matrix aug = null; /* augmented matrix */

        /* find inverse of A by row reducing [A | I] ~ [I | inv(A)] */
        if (isInvertible()) {
            /* build augmented matrix [A | I] where A is current Matrix */
            aug = new Matrix(rows, cols * 2);
            
            for (int i = 0; i < aug.rows; i++) {
                for (int j = 0; j < aug.cols; j++) {
                    if (j < cols) {
                        aug.data[i][j] = data[i][j];
                    }
                    else if (j == i + rows) {
                       aug.data[i][j] = 1;
                    } 
                }
            }
            
            /* row reduce augmented matrix to get [I | inv(A)] */
            aug = aug.rref();

            /* assign right half of augmented matrix to inverse */
            inv = new Matrix(rows, cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    inv.data[i][j] = aug.data[i][j + rows];
                }
            }
        }
        return inv;
    }

	/** 
     * @return the sum of the current Matrix and the parameter matrix 
     */
	public Matrix add(Matrix B) {
		Matrix sum = null;
		
		if (cols == B.cols && rows == B.rows) {
			sum = new Matrix(rows, cols);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					sum.data[i][j] = data[i][j] + B.data[i][j];
				}
			}
		}
		else {
			throw new RuntimeException("Matrix dimensions not compatible "
					+ "for addition");
		}
		return sum;
	}
	
	/** 
     * @return the Matrix (A - B) where A is the current Matrix and B is
     * the parameter matrix 
     */
	public Matrix subtract(Matrix B) {
		Matrix result = null;
		
		if (cols == B.cols && rows == B.rows) {
			result = new Matrix(rows, cols);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					result.data[i][j] = data[i][j] - B.data[i][j];
				}
			}
		}
		else {
			throw new RuntimeException("Matrix dimensions not compatible "
					+ "for subtraction");
		}
		return result;
	}

	/** 
     * @return the product of the current Matrix and the parameter matrix B
	 */
	public Matrix multiply(Matrix B) {
		Matrix product = null;
		if (this.cols == B.rows) {
			product = new Matrix(rows, B.cols);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < B.cols; j++) {
					for (int k = 0; k < B.rows; k++) {
						product.data[i][j] += data[i][k] * B.data[k][j];
					}
				}
			}
		}
		else {
			throw new RuntimeException("Matrix dimensions not compatible "
					+ "for multiplication");
		}
		return product;
	}

    /** 
     * @return the rank of the current Matrix 
     */
    public int rank() {
        /* the rank of a matrix equals the number of pivot positions */
        int pivots = 0;
        Matrix rref = this.rref();

        for (int i = 0; i < rref.rows; i++) {
            int j = 0;
            while (j < rref.cols && rref.data[i][j] == 0) {
                j++;
            }
            if (j < rref.cols && rref.data[i][j] == 1) {
                pivots++;
            }
        }
        return pivots;
    }
    
    /**
     *  @return the determinant of the current matrix 
     */
    public double det() {
        double result = 0;
        if (rows == cols) {
            if (rows == 2) {
                result = data[0][0] * data[1][1] - data[0][1] * data[1][0];
            } 
            else {
                for (int i = 0; i < rows; i++) {
                    Matrix a = new Matrix(rows - 1, cols - 1);
                    for (int j = 0; j < a.rows; j++) {
                        for (int k = 0; k < a.cols; k++) {
                            if (j < i) {
                                a.data[j][k] = data[j][k+1];
                            }
                            else {
                                a.data[j][k] = data[j+1][k+1];
                            }
                        }
                    }
                    
                    /* add determinant of submatrix to total */
                    if (i % 2 == 0) {
                        result += (data[i][0] * a.det());
                    }
                    else {
                        result -= (data[i][0] * a.det());
                    }
                }

            }

        }
        else {
            //can't find determinant bc not square
            System.err.println("Error: Determinant cannot be computed because matrix is not square");
        }
        return result;
    }

    /** @return an array containing the eigenvalues of the current matrix */
    public double[] eigenvalues() {
        double[] result = null;
        return result;
    }

    /** @return true if the current Matrix is linearly independent, false
     * if it is linearly dependent */
    public boolean isLinIndep() {
        /* an mxn matrix A is linearly independent if and only if its
         * rank equals n */
        return (rank() == cols);
    }


    /** @return true if the current Matrix is invertible, false otherwise */
    public boolean isInvertible() {
        /* a matrix A is invertible if and only if A is nxn and rank(A) = n */
        return (rows == cols && rank() == cols);
    }


	/** @return true if the current Matrix is symmetric, false otherwise */
	public boolean isSymmetric() {
		// a matrix A is symmetric IFF transpose(A) = A
		return this.equals(transpose());
	}

	/** @return a string representation of the current Matrix's properties */
	public String properties() {
		String str = "Matrix " + " (" + rows + "x" + cols + "):" + "\n";
		str += this.toString();
		return str;
	}
	
	/** 
     * @return a string representation of the current Matrix 
     */
	public String toString() {
		String str = "";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ((int) data[i][j] == data[i][j]) {
					str += (int) data[i][j] + "\t";
				} 
				else {
					str += data[i][j] + "\t";
				}	
			}
			str += "\n";
		}
		return str;
	}
	
	/** 
     * @return true if the current Matrix has the same dimensions and values
	 * as the parameter Matrix, false otherwise
     */
	public boolean equals(Object matrix) {
		boolean result = false;

		if (matrix instanceof Matrix) {
			Matrix m = (Matrix) matrix;
			if (rows == m.rows && cols == m.cols) {
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						if (data[i][j] != m.data[i][j]) {
							return false;
						}
					}
				}
				result = true;
			}
		}	
		return result;
	}
	

	public static void main(String[] args) {
        double m[][] = {{1, 3, 3}, {1, 4, 3}, {1, 3, 4}};
        Matrix M = new Matrix(m);
        System.out.println(M.rref());
        System.out.println("det: " + M.det()); /* 5 */
    }
}
