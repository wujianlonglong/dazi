package com.wjl.dazi.model;

import java.awt.*;

public class Letter {
    private String num;
    private Color color;
    private int x;
    private int y;
    private int speed = 1;

    public Letter() {
    }

    public Letter(String num, Color color, int x, int y) {
        super();
        this.num = num;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * 判断字母是否出界
     *
     * @time 2016年6月29日 下午12:27:38
     * @author Lichao
     * @return 出界为true，未出界为false
     */
    public boolean outOfBound() {
        return this.y > LetterGame.HEIGTH;
    }

    public void step() {
        this.y += speed;
    }

    /* getter setter方法 */
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

}
