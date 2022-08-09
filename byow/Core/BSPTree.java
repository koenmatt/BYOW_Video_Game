package byow.Core;

import java.util.ArrayList;
import java.util.Random;

public class BSPTree {

    Random r;
    Node root;
    ArrayList<Room> corridors;

    public BSPTree(Random r) {
        this.r = r;
        corridors = new ArrayList<>();
        root = new Node(new Room(90, 44, 0, 0, r, 0));

    }

    class Node {

        Room self; //a Room object to store the dimensions of the space
        Room nestedRoom; //a Room object to store the dimensions of the actual room
        Node left; //left child
        Node right; //right child
        int horVert;
        Node parent;

        public Node(Room self) {
            this.self = self;
            this.nestedRoom = null;
            this.left = null;
            this.right = null;
            this.parent = root;

        }

        /** Splits the current Node and creates two new children */
        public boolean split() {
            int splitPoint;
            horVert = r.nextInt(0, 2);

            int side = horVert == 0 ? this.self.width : this.self.height;

            if (side < 8) {
                return false;
            }

            //simple case, lets start by splitting in half
            splitPoint = side / 2;

            if (horVert == 0) {
                this.left = new Node(new Room(splitPoint, this.self.height, this.self.startX, this.self.startY, r, 0));
                this.left.nestedRoom = buildSubRoom(this.left);
                this.left.parent = this;

                this.right = new Node(new Room(this.self.width - splitPoint, this.self.height, this.self.startX + splitPoint - 1, this.self.startY, r, 1));
                this.right.nestedRoom = buildSubRoom(this.right);
                this.right.parent = this;
            }
            else {
                this.left = new Node(new Room(this.self.width, splitPoint, this.self.startX, this.self.startY, r, 2));
                this.left.nestedRoom = buildSubRoom(this.left);
                this.left.parent = this;

                this.right = new Node(new Room(this.self.width, this.self.height - splitPoint, this.self.startX, this.self.startY + splitPoint - 1, r, 3));
                this.right.nestedRoom = buildSubRoom(this.right);
                this.right.parent = this;

            }

            return true;

        }


        public Room buildSubRoom(Node leaf) {
            int newHeight = r.nextInt(3, leaf.self.height - 1);
            int newWidth = r.nextInt(3, leaf.self.width - 1);

            int newStartX = leaf.self.startX + 2;
            int newStartY = leaf.self.startY + 2;

            return new Room(newWidth, newHeight, newStartX, newStartY, r, leaf.self.side);
        }

    }

    public void buildTree(int numRooms) {
        buildTree(root, numRooms);
    }
    public void buildTree(Node t, int numRooms) {
        if (numRooms == 0) {
            return;
        }

        if (t != null) {
            t.split();
            buildTree(t.left, numRooms - 1);
            buildTree(t.right, numRooms - 1);
        }

        }


    /** Connecting rooms */






}
