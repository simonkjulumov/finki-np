package com.company;

import java.util.*;

class ObjectCanNotBeMovedException extends Exception{

}

class MovableObjectNotFittableException extends Exception {

}

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable {
    void moveUp();
    void moveDown();
    void moveRight();
    void moveLeft();
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
}

class MovablePoint implements Movable {
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private TYPE type;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.type = TYPE.POINT;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }

    @Override
    public void moveUp() {
        y = y + ySpeed;
    }

    @Override
    public void moveDown() {
        y = y - ySpeed;
    }

    @Override
    public void moveRight() {
        x = x + xSpeed;
    }

    @Override
    public void moveLeft() {
        x = x - xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return type;
    }
}

class MovableCircle implements Movable {
    private int radius;
    private MovablePoint  movablePoint; //center for movingCircle
    private TYPE type;

    public MovableCircle(int radius, MovablePoint movablePoint) {
        this.radius = radius;
        this.movablePoint = movablePoint;
        this.type = TYPE.CIRCLE;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d", movablePoint.getCurrentXPosition(), movablePoint.getCurrentYPosition(), radius);
    }

    @Override
    public void moveUp() {
        movablePoint.moveUp();
    }

    @Override
    public void moveDown() {
        movablePoint.moveDown();
    }

    @Override
    public void moveRight() {
        movablePoint.moveRight();
    }

    @Override
    public void moveLeft() {
        movablePoint.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return movablePoint.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return movablePoint.getCurrentXPosition();
    }

    @Override
    public TYPE getType() {
        return type;
    }
}

class MovablesCollection {
    private Movable[] movables;
    private static int MAX_X;
    private static int MAX_Y;

    public MovablesCollection(int x_MAX, int y_MAX) {
        MAX_X = x_MAX;
        MAX_Y = y_MAX;
        movables = new Movable[0];
    }

    public static void setxMax(int xMax) {
        MAX_X = xMax;
    }

    public static void setyMax(int yMax) {
        MAX_Y = yMax;
    }

    public void addMovableObject(Movable m) {
        if(m.getCurrentXPosition() < 0 || m.getCurrentXPosition() > MAX_X) {
            if(m instanceof MovablePoint) {
            }
            else {

            }
        }
        if(m.getCurrentYPosition() < 0 || m.getCurrentYPosition() > MAX_X) {
            if(m instanceof MovableCircle) {

            }
            else {

            }
        }


        Movable[] movablesCopy = Arrays.copyOf(movables, movables.length + 1);
        movablesCopy[movables.length] = m;
        movables = movablesCopy;
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for(Movable movable : movables) {
            if(movable.getType() == TYPE.POINT) {
                MovablePoint movablePoint = (MovablePoint)movable;
                switch (direction) {
                    case UP:
                        movablePoint.moveUp();
                        break;
                    case DOWN:
                        movablePoint.moveDown();
                        break;
                    case LEFT:
                        movablePoint.moveLeft();
                        break;
                    case RIGHT:
                        movablePoint.moveRight();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + direction);
                }
            }
            else {
                MovableCircle movableCircle = (MovableCircle)movable;
                switch (direction) {
                    case UP:
                        movableCircle.moveUp();
                        break;
                    case DOWN:
                        movableCircle.moveDown();
                        break;
                    case LEFT:
                        movableCircle.moveLeft();
                        break;
                    case RIGHT:
                        movableCircle.moveRight();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + direction);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for(Movable movable : movables) {
            stringJoiner.add(movable.toString());
        }

        return String.format("Collection of movable objects with size %d:\n", movables.length) +
                stringJoiner.toString();
    }
}
public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
