import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTests {

    /* Tests that identity matrix row reduces to identity matrix */
	@Test
	public void testRREF1() {
		double[][] m = {{1, 0}, {0, 1}};
		Matrix M = new Matrix(m);
		Matrix Mrref = M.rref();
		assertTrue(M.equals(Mrref));

		double[][] n = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		Matrix N = new Matrix(n);
		Matrix Nrref = N.rref();
		assertTrue(N.equals(Nrref));
		
		double[][] p = {{1, 0, 0, 0}, {0, 1, 0, 0}, 
				{0, 0, 1, 0}, {0, 0, 0, 1}};
		Matrix P = new Matrix(n);
		Matrix Prref = P.rref();
		assertTrue(P.equals(Prref));

	}
	
	@Test
	public void testRREF2() {
		double[][] m = {{0, 0, 5}, {2, 2, 4}, {3, 1, 0}};
		double[][] ch = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		Matrix M = new Matrix(m);
		Matrix Mrref = M.rref();
	
		Matrix check = new Matrix(ch);
		assertTrue(Mrref.equals(check));
	}
	
	@Test
	public void testRREF3() {
		double[][] m = {{1, 2, -2, 7, 3}, {3, 6, 1, 7, 0}, {1, 2, 1, 1, -1},
				{7, 14, 3, 15, -2}};
		double[][] ch = {{1, 2, 0, 3, 0}, {0, 0, 1, -2, 0}, {0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0}};
		
		Matrix M = new Matrix(m);
		Matrix Mrref = M.rref();
		Matrix check = new Matrix(ch);
		assertTrue(Mrref.equals(check));
		
	}
	
	@Test
	public void testRREF4() {
		double[][] m = {{1, -1, 2, -2}, {4, 4, -2, 1}, {-2, 2, -4, 0}};
		double[][] ch = {{1, 0, .75, 0}, {0, 1, -1.25, 0}, {0, 0, 0, 1}};
		Matrix M = new Matrix(m);
		Matrix Mrref = M.rref();
		Matrix check = new Matrix(ch);
		System.out.println(Mrref);
		assertTrue(Mrref.equals(check));
	}
	
	@Test
	public void testRREF5() {
		double[][] m = {{1, 2, 3, 4, 5}, {2, 4, 6, 8, 10}, {3, 6, 9, 10, 15},
				{6, 22, 18, 37, 5}, {5, 10, 15, 5, 50}};
		
		Matrix M = new Matrix(m);
		Matrix Mrref = M.rref();
		System.out.println(Mrref);
	}

}
