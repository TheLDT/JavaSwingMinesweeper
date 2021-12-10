package minesweepergui;

public class Cell {

    private int neighbours;
    private boolean bomb;
    private boolean flagged;
    private boolean pressed;
    private char visual;

    public Cell() {
        this.neighbours = 0;
        this.bomb = false;
        this.flagged = false;
        this.pressed = false;
        this.visual = ' ';
    }

    public void makeBomb() {
        this.bomb = true;
        this.neighbours = -1000;
    }

    public boolean isEmpty() {
        return this.neighbours == 0;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public boolean isBomb() {
        return bomb;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void incrementNeighbours() {
        this.neighbours++;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void setVisual(char visual) {
        this.visual = visual;
    }

    public char getVisual() {
        return visual;
    }

    public char press() {
        this.pressed = true;
        if (this.bomb) {
            this.visual = '*';
            return '*';
        } else if (this.isEmpty()) {
            this.visual = '0';
            return '0';
        } else {
            this.visual = (char) (this.neighbours + '0');
            return (char) (this.neighbours + '0');
        }
    }
}
