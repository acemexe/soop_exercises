//this file presents two basic examples of the creation and usage of complex data types
//until not told otherwise remove the code below before creating your solutions

import static java.lang.IO.*;  //including package IO to be able to use simple print()
import static java.lang.IO.println;
import static term.term.*;     //includes package term (createElements() functions were moved there)
import java.util.Random;
import java.security.SecureRandom;

int findMin(int[][] m) {
    int min = m[0][0];

    for (int i = 0; i < m.length; i++) {
        for (int j = 0; j < m[i].length; j++) {
            if (m[i][j] < min) {
                min = m[i][j];
            }
        }
    }
    return min;
}

int findMax(int[][] m) {
    int max = m[0][0];

    for (int i = 0; i < m.length; i++) {
        for (int j = 0; j < m[i].length; j++) {
            if (m[i][j] > max) {
                max = m[i][j];
            }
        }
    }
    return max;
}

void drawScreen(int[][] matrix, int matrix_rows, int matrix_cols, int drawfreq, int counter, int[][] oldmatrix, boolean skipsim, int loops) {
    if (counter % drawfreq == 0  || skipsim) {
        if (skipsim && counter != loops) {
            return;
        }
        int hist_min = findMin(matrix);
        int hist_max = findMax(matrix);
        int old_hist_min = findMin(oldmatrix);
        int old_hist_max = findMax(oldmatrix);

        for (int i = 0; i < matrix_rows; i++) {

            for (int j = 0; j < matrix_cols; j++) {
                if (!skipsim && matrix[i][j] == oldmatrix[i][j] && hist_min == old_hist_min && hist_max == old_hist_max) {
                    continue;
                }
                gotoxy(j + 1, i + 1);
                int green = (int) (255.0 * ((matrix[i][j] - hist_min) / (double) (hist_max - hist_min)));
                setfgcolor_rgb(10, green, 10);
                print('█');
            }
        }
    }
}


int[][] randompickMatrix(int[][] matrix, int matrix_rows, int matrix_cols, long seed_col, long seed_row, boolean secure, java.util.Random rand) {

    rand.setSeed(seed_row);
    int rows = rand.nextInt(matrix_rows);
    rand.setSeed(seed_col);
    int cols = rand.nextInt(matrix_cols);
    matrix[rows][cols] += 1;
    return matrix;
}

int[][] initMatrix(int matrix_rows, int matrix_cols) {
    int[][] matrix = new int[matrix_rows][matrix_cols];
    for (int i = 0; i < matrix_rows; i++) {
        for (int j = 0; j < matrix_cols; j++) {
            matrix[i][j] = 0;
        }
    }
    return matrix;
}


java.util.Random createRandom(boolean secure) {
    java.util.Random rand;
    if (secure) {
        java.security.SecureRandom s = new java.security.SecureRandom();

        rand = s;
    } else {
        rand = new java.util.Random();
    }
    return rand;
}

void randomVisualiser(int loops, int matrix_rows, int matrix_columns, long seed_col, long seed_row, int delay, boolean secure, boolean changeseed, int drawfreq, boolean skipsim) {
    cursor_hide();
    clrscr();
    setfgcolor(white);
    java.util.Random rand = createRandom(secure);

    int[][] matrix = initMatrix(matrix_rows, matrix_columns);
    int[][] oldmatrix = initMatrix(matrix_rows, matrix_columns);

    int counter = 0;
    while (counter < loops) {
        counter++;
        if (changeseed) {
            seed_col++;
            seed_row++;
        }

        drawScreen(matrix, matrix_rows, matrix_columns, drawfreq, counter, oldmatrix, skipsim, loops);

        for (int i = 0; i < matrix_rows; i++) {
            for (int j = 0; j < matrix_columns; j++) {
                oldmatrix[i][j] = matrix[i][j];
            }
        }

        matrix = randompickMatrix(matrix,  matrix_rows, matrix_columns, seed_col, seed_row, secure, rand);
        delay(delay);
    }
    cursor_show();
    setfgcolor(white);
    gotoxy(1, matrix_rows);

}

void main() {

    randomVisualiser(10000, 30, 100, 5000, 5001, 0, true, true, 1, true);

}
