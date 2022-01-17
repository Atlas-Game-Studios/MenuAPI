package com.ags.menuapi.decoration;

import java.util.ArrayList;
import java.util.List;

import com.ags.menuapi.Menu.MenuSize;

public enum Scheme {

	TOP, BOTTOM, RIGHT, LEFT, BOX, LEFTRIGHT, TOPBOTTOM, SIDES, FILL, SPLIT;
	
	public List<Integer> getSlotsArray(MenuSize size) {
		int s = size.toNumber();
		int r = size.toRows();
		int c = size.toColumns();
		List<Integer> slots = new ArrayList<Integer>();
		switch(this) {
		case BOTTOM:
			setBottom(slots,s,c);
		case BOX:
			setBottom(slots,s,c);
			setTop(slots,c);
			setLeft(slots,r,c);
			setRight(slots,r,c);
			break;
		case FILL:
			setAll(slots,s);
			break;
		case LEFT:
			setLeft(slots,r,c);
			break;
		case LEFTRIGHT:
			setLeft(slots,r,c);
			setRight(slots,r,c);
			break;
		case RIGHT:
			setRight(slots,r,c);
			break;
		case TOP:
			setTop(slots,c);
			break;
		case TOPBOTTOM:
			setBottom(slots,s,c);
			setTop(slots,c);
			break;
		case SPLIT:
			setRight(slots,r,5); // Makes a row in the middle by manipulating the perceived 
		default:
			break;
		}
		return slots;
	}
	
	private void setVertical(List<Integer> slots, int r, int c, int l) {
		for(int i = 0; i < r*c; i=i+9) {
			slots.add(i+l);
		}
		for(int i = 0; i < 3*9; i=i+9) {
			slots.add(i+4);
		}
	}

	private void setBottom(List<Integer> slots, int s, int c) {
		for(int i = 0; i < c; i++) {
			slots.add((s-1)-i);
		}
	}
	
	private void setTop(List<Integer> slots, int c) {
		for(int i = 0; i < c; i++) {
			slots.add(i);
		}
	}
	
	private void setLeft(List<Integer> slots, int r, int c) {
		for(int i = 0; i < r; i++) {
			slots.add(i*c);
		}
	}
	
	private void setRight(List<Integer> slots, int r, int c) {
		//setVertical(slots,r,c,(c-1));
		for(int i = 1; i <= r; i++) {
			slots.add(i*c-1);
		}
	}
	
	private void setAll(List<Integer> slots, int s) {
		for(int i = 0; i < s; i++) {
			slots.add(i);
		}
	}
	
	
}
