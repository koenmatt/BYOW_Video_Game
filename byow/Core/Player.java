package byow.Core;

public class Player {
    int x;
    int y;
    boolean sticky = false;
    int numMoves = 0;
    int health;


    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.numMoves += 1;
        this.health -= 1;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


}
