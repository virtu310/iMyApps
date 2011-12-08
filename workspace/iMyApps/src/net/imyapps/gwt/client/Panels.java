package net.imyapps.gwt.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Label;

public class Panels {
	Panel top;
	Panel left;
	Panel right;
	Panel bottom;
	Panel message;
	Panel main;
	
	Label error;
	Label info;
	
	public void clear() {
		top.clear();
		left.clear();
		right.clear();
		bottom.clear();
		clearMessage();
	}
	
	public void clearMessage() {
		error.setText("");
		info.setText("");
	}
	
	public void appendError(String msg) {
		error.setText(error.getText() + "\n" + msg);
	}
	
	public void appendInfo(String msg) {
		info.setText(info.getText() + "\n" + msg);
	}

	public Panel getTop() {
		return top;
	}

	public void setTop(Panel top) {
		this.top = top;
	}

	public Panel getLeft() {
		return left;
	}

	public void setLeft(Panel left) {
		this.left = left;
	}

	public Panel getRight() {
		return right;
	}

	public void setRight(Panel right) {
		this.right = right;
	}

	public Panel getBottom() {
		return bottom;
	}

	public void setBottom(Panel bottom) {
		this.bottom = bottom;
	}

	public Panel getMessage() {
		return message;
	}

	public void setMessage(Panel message) {
		this.message = message;
	}

	public Label getError() {
		return error;
	}

	public void addError(Label error) {
		this.error = error;
		message.add(error);
	}

	public Label getInfo() {
		return info;
	}

	public void addInfo(Label info) {
		this.info = info;
		message.add(info);
	}

	public Panel getMain() {
		if (main == null)
			return left;
		
		return main;
	}
	
	public void setMain(Panel main) {
		this.main = main;
	}
}
