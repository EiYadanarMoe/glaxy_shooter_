package application;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends Rectangle {

	boolean dead = false;
	final String type;
	int speed;
	int hp;

	public Player(double x, double y, double width, double height, int hp, String type, String imagePath, int speed) {
		super(width, height);
		this.type = type;
		this.speed = speed;
		setTranslateX(x);
		setTranslateY(y);
		System.out.println("player.img" + imagePath);
		setImage(imagePath);

	}

	private void setImage(String imagePath) {
		try {
			Image image = new Image(getClass().getResourceAsStream(imagePath));
			ImagePattern imagePattern = new ImagePattern(image);
			setFill(imagePattern);
		} catch (Exception e) {
			System.out.println("Failed to load image: " + imagePath);
		}
	}

	public void moveLeft() {
		if (getTranslateX() - speed >= 0) {
			setTranslateX(getTranslateX() - speed);
		}
	}

	public void moveLeftNoLimit() {
		setTranslateX(getTranslateX() - speed);
	}

	public void moveRight(double paneWidth) {
		if (getTranslateX() + getWidth() + speed <= paneWidth) {
			setTranslateX(getTranslateX() + speed);
		}
	}

	public void moveRight() {
		setTranslateX(getTranslateX() + speed);
	}

	public void moveUp() {
		if (getTranslateY() - speed >= 0) {
			setTranslateY(getTranslateY() - speed);
		}
	}

	public void moveUpNoLimit() {
		setTranslateY(getTranslateY() - speed);
	}

	public void moveDown(double paneHeight) {
		if (getTranslateY() + getHeight() + speed <= paneHeight) {
			setTranslateY(getTranslateY() + speed);
		}
	}

	public void moveDown() {
		setTranslateY(getTranslateY() + speed);
	}

	public void moveStop() {
		setTranslateY(getTranslateY());
		setTranslateX(getTranslateX());
	}
	
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

}
