package com.tummosoft.glide;

import com.tummosoft.glide.Direction;

public abstract class CombineElement<T extends CombineElement> {
    private int x;                          
    private int y;                          
    private boolean center;                 
    private Direction direction = Direction.LeftRight;    
    private float alpha = 1.0f;             
    private boolean repeat;                 
    private int repeatPaddingHorizontal;   
    private int repeatPaddingVertical;     

    public int getX() {
        return x;
    }

    public T setX(int x) {
        this.x = x;
        return (T) this;
    }

    public int getY() {
        return y;
    }

    public T setY(int y) {
        this.y = y;
        return (T) this;
    }

    public boolean isCenter() {
        return center;
    }

    public T setCenter(boolean center) {
        this.center = center;
        return (T) this;
    }

    public Direction getDirection() {
        return direction;
    }

    public T setDirection(Direction direction) {
        this.direction = direction;
        return (T) this;
    }

    public float getAlpha() {
        return alpha;
    }

    public T setAlpha(float alpha) {
        this.alpha = alpha;
        return (T) this;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public T setRepeat(boolean repeat) {
        this.repeat = repeat;
        return (T) this;
    }

    public T setRepeat(boolean repeat, int repeatPadding) {
        this.repeat = repeat;
        this.repeatPaddingHorizontal = repeatPadding;
        this.repeatPaddingVertical = repeatPadding;
        return (T) this;
    }

    public T setRepeat(boolean repeat, int repeatPaddingHorizontal, int repeatPaddingVertical) {
        this.repeat = repeat;
        this.repeatPaddingHorizontal = repeatPaddingHorizontal;
        this.repeatPaddingVertical = repeatPaddingVertical;
        return (T) this;
    }

    public int getRepeatPaddingHorizontal() {
        return repeatPaddingHorizontal;
    }

    public int getRepeatPaddingVertical() {
        return repeatPaddingVertical;
    }
}