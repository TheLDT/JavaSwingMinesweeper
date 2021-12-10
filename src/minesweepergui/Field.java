package minesweepergui;

public class Field {

    private Cell[][] minefield;
    private int pressed_cells = 0;
    private int size, total_cells, bomb_count;
    private int x, y;

    public Field(int size) {
        this.size = size;
        this.total_cells = size * size;
        this.bomb_count = size * 2;
        this.minefield = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.minefield[i][j] = new Cell();
            }
        }
    }

    public Field(int size, int bombs) {
        this.size = size;
        this.total_cells = size * size;
        this.bomb_count = bombs;
        this.minefield = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.minefield[i][j] = new Cell();
            }
        }
    }

    public Cell[][] getMinefield() {
        return minefield;
    }

    public void setMinefield(Cell[][] minefield) {
        this.minefield = minefield;
    }

    public int getPressed_cells() {
        return pressed_cells;
    }

    public void setPressed_cells(int pressed_cells) {
        this.pressed_cells = pressed_cells;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal_cells() {
        return total_cells;
    }

    public void setTotal_cells(int total_cells) {
        this.total_cells = total_cells;
    }

    public int getBomb_count() {
        return bomb_count;
    }

    public void setBomb_count(int bomb_count) {
        this.bomb_count = bomb_count;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setup_field() {
        int max = this.size;
        int range = max - 1;

        int bombX, bombY, bomb_count_temp = 0;
        while (bomb_count_temp < this.bomb_count) {
            bombX = (int) (Math.random() * range);
            bombY = (int) (Math.random() * range);
            if (this.minefield[bombX][bombY].isBomb()) {
                continue;
            }
            this.minefield[bombX][bombY].makeBomb();
            increase_neighbours(bombX, bombY, 0);
            bomb_count_temp++;
        }
    }

    public void increase_neighbours(int bombX, int bombY, int cell) {
        if (cell > 8) {
            return;
        }
        String[] xy;
        if (check_cell(bombX, bombY, cell)) {
            this.minefield[x][y].incrementNeighbours();
        }
        increase_neighbours(bombX, bombY, cell + 1);
    }

    /**
     * Searches for other empty cells.
     *
     * @param bombX
     * @param bombY
     * @param cell
     */
    public void find_other_empty(int bombX, int bombY, int cell) {
        if (cell > 8) {
            return;
        }
        if (check_cell(bombX, bombY, cell)) {
            if (this.minefield[x][y].getNeighbours() < 0) {
                return;
                //it is a bomb, don't press.
            } else {
                if (!this.minefield[x][y].isPressed()) {
                    this.minefield[x][y].press();
                    this.pressed_cells++;
                    if (this.minefield[x][y].isEmpty()) {
                        find_other_empty(x, y, 0);
                    }
                }
            }
        }
        find_other_empty(bombX, bombY, cell + 1);
    }

    /**
     * Gets the row and column and depending on the **cell** position tests if
     * the cell at the given relative position exists. If it does, it stores the
     * row and column at the global variables x and y and returns true, else
     * returns false.
     *
     * @param bombX row
     * @param bombY column
     * @param cell position relative to the cell whose coordinates are bombX and
     * bombY
     * @return true if the cell is valid, false otherwise.
     */
    public boolean check_cell(int bombX, int bombY, int cell) {
        switch (cell) {
            case 0://upleft
                x = bombX - 1;
                y = bombY - 1;
                break;
            case 1://up
                x = bombX - 1;
                y = bombY;
                break;
            case 2://upright
                x = bombX - 1;
                y = bombY + 1;
                break;
            case 3://left
                x = bombX;
                y = bombY - 1;
                break;
            case 4://this
                x = bombX;
                y = bombY;
                break;
            case 5://right
                x = bombX;
                y = bombY + 1;
                break;
            case 6://downleft
                x = bombX + 1;
                y = bombY - 1;
                break;
            case 7://down
                x = bombX + 1;
                y = bombY;
                break;
            case 8://downright
                x = bombX + 1;
                y = bombY + 1;
                break;
            default:
                x = -1;
                y = -1;
        }

        return (x >= 0 && y >= 0 && x < this.size && y < this.size);
    }

    /**
     * For the CLI, not used in any way in the GUI. Prints the minefield.
     *
     * @param show_bombs true for the end screen to print the field with the
     * bombs showing.
     */
    public void print_field(boolean show_bombs) {
        String spaces = "|      |";
        for (int i = 0; i < this.size; i++) {
            if (i == 0) {
                System.out.print("  |");
                for (int z = 0; z < this.size; z++) {
                    System.out.print((i + z) + spaces);
                }
                System.out.println();
            }
            for (int j = 0; j < this.size; j++) {
                if (j == 0) {
                    System.out.print(" |");
                }
                //What the cell should show
                if (show_bombs) {
                    if (this.minefield[i][j].isBomb()) {
                        System.out.print("*" + spaces);
                    } else {
                        System.out.print(this.minefield[i][j].getVisual() + spaces);
                    }
                } else {
                    System.out.print(this.minefield[i][j].getVisual() + spaces);
                }
            }
            System.out.println();
        }
    }

    /**
     * For the CLI, not used in any way in the GUI. Checks the input to be in
     * bounds.
     *
     * @param row the row input.
     * @param column the column input.
     * @param field_size the size of the field.
     * @return true if valid input., false otherwise.
     */
    public boolean not_in_limits(int row, int column, int field_size) {
        if (row < 0 || column < 0 || row >= field_size || column >= field_size) {
            System.out.println(row + " and " + column + " do not exist in the field! Enter another combination:\n");
            return true;
        }
        return false;
    }

    public String getBombPercentage() {
        double percentage = (double) this.bomb_count / (this.size * this.size);
        percentage *= 100;
        return Double.toString(percentage);
    }
}
