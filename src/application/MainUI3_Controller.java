package application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class MainUI3_Controller implements Initializable {

	@FXML
	private Pane root;

	@FXML
	private Label timerRecord;

	@FXML
	private Label lblLevel;

	@FXML
	private Label scoreRecord;

	Player player;

	ImageView gunImageView;

	private double timeInterval = 0.0;

	private boolean gameRunning = true;

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int x = 300;
	private static final int y = 300;

	private ImageView explosion;

	private MediaPlayer backgroundMusicPlayer;
	private AudioClip explosionSound;
	private AudioClip laserSound;

	public LocalDateTime start;
	public LocalDateTime end;
	public static String formattedTime;
	public static String level;

	public static int score;

	private static final Random RNG = new Random();

	public void playingLevel() {
		System.out.println("in the playing" + level);

		Player mainEnemy = new Player(WIDTH / 2 - 50, -50, 100.0, 100.0, 10, "enemy", "/images/flyingBoss.gif", 5);
        root.getChildren().add(mainEnemy);
        flyInFromTop(mainEnemy); 
        
        Timeline bulletTimeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> fireBullet(root)));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
	}

	private void flyInFromTop(Player enemy) {
		Timeline timeline = new Timeline(
				new KeyFrame(javafx.util.Duration.ZERO, new KeyValue(enemy.translateYProperty(), -30.0)),
				new KeyFrame(javafx.util.Duration.millis(1000),
						new KeyValue(enemy.translateYProperty(), 150.0 + enemy.getTranslateY())));
		timeline.play();
	}

	public List<Player> getAllActors() {
//		return root.getChildren().stream().map(a -> (Player) a).toList();
		return root.getChildren().stream().filter(Player.class::isInstance).map(Player.class::cast).toList();
	}

	public void update() {
		if (!gameRunning) {
			return;
		}
		if (level.equalsIgnoreCase("Level1")) {
			timeInterval += 0.016;
		} else {
			timeInterval += 0.05;
		}

		// Create a list to collect actors to be removed
		List<Player> actorsToRemove = new ArrayList<>();

		getAllActors().forEach(a -> {
			switch (a.type) {

			case "enemybullet":
				a.moveDown();

				if (a.getBoundsInParent().intersects(player.getBoundsInParent())) {
					player.dead = true;
					a.dead = true;
				}
				break;

			case "playerbullet":
				a.moveUpNoLimit();

				getAllActors().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
					if (a.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
						System.out.println("Player bullet hit enemy!");
						enemy.dead = true;
						a.dead = true;
						showExplosion(enemy.getTranslateX(), enemy.getTranslateY(), enemy.getWidth(),
								enemy.getHeight());
						score += 5;
						calculateScore(score);
					}
				});

				break;

			case "enemy":
				if (timeInterval > 2) {
					if (Math.random() < 0.3) {
						fireBullet(root);
						a.moveDown();
					}
				}
				break;
			}

			// Collect actors to be removed
			if (a.dead) {
				actorsToRemove.add(a);
			}
		});

		// Remove dead actors from the root pane
		root.getChildren().removeAll(actorsToRemove);

		if (player.dead) {
			stopGame();
			showUI("GameEndUI.fxml", "GAME OVER");

		}

		if (getAllActors().stream().filter(a -> a.type.equals("enemy")).allMatch(e -> e.dead)) {

			stopGame();

			showUI("LevelComplete.fxml", "LEVEL FINISH");

		}

		if (timeInterval > 2) {
			timeInterval = 0;

		}

		end = LocalDateTime.now();
		timeDuration(start, end);

	}

	private void calculateScore(int score2) {
		// TODO Auto-generated method stub
		scoreRecord.setText("Score: " + score);

	}

	private void timeDuration(LocalDateTime start2, LocalDateTime end2) {
		// TODO Auto-generated method stub

		long countSec = 0;
		Duration d = Duration.between(start, end);
		countSec = d.getSeconds();
		long countMinute = countSec / 60;
		countSec = countSec % 60;
		long countHr = countMinute / 60;
		countMinute = countMinute % 60;
		formattedTime = String.format("%02d:%02d:%02d", countHr, countMinute, countSec);
		timerRecord.setText("Time: " + formattedTime);

	}

	private void showUI(String UIPath, String Title) {
		Stage stage = (Stage) (root.getScene().getWindow());
		stage.hide();
		backgroundMusicPlayer.stop();
		Pane gameEnd = null;
		try {
			gameEnd = (Pane) FXMLLoader.load(getClass().getResource(UIPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene = new Scene(gameEnd);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle(Title);
		stage.show();

	}

	private void stopGame() {
		gameRunning = false;
	}

//	public void shot(Player who) {
//		String bulletImage;
//
//		if (who.type.equals("player")) {
//			bulletImage = "/images/bullet-shot.png";
//		} else if (who.type.equals("enemy")) {
//			bulletImage = "/images/red-light.png";
//		} else {
//			return;
//		}
//		Player bullet = new Player(who.getTranslateX() + 10, who.getTranslateY(), 25, 28, 0, who.type + "bullet",
//				bulletImage, 5);
//		root.getChildren().add(bullet);
//
//		bullet.setOnMouseClicked(event -> {
//			// Handle collision with enemy
//			getAllActors().stream().filter(e -> e.type.equals("enemy"))
//					.filter(enemy -> bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())).findFirst()
//					.ifPresent(enemy -> {
//						root.getChildren().removeAll(bullet, enemy);
//						showExplosion(enemy.getTranslateX(), enemy.getTranslateY(), enemy.getWidth(),
//								enemy.getHeight());
//					});
//		});
//	}

	private void fireBullet(final Pane root) { 
        int numberOfBullets = 5; // Set the number of bullets to shoot in each round

        for (int i = 0; i < numberOfBullets; i++) {
            final Shape bullet = new Circle(3, Color.RED);
            root.getChildren().add(bullet);

            double gunCenterX = gunImageView.getLayoutX() + (gunImageView.getFitWidth() / 2);
            double gunTopY = (gunImageView.getLayoutY() + (gunImageView.getFitHeight() / 2));

            final TranslateTransition bulletAnimation = new TranslateTransition(javafx.util.Duration.seconds(2), bullet);
            final double bulletTargetX = RNG.nextInt(WIDTH);
            bulletAnimation.setFromX(gunCenterX);
            bulletAnimation.setFromY(gunTopY);
            bulletAnimation.setToX(bulletTargetX);
            bulletAnimation.setToY(HEIGHT);

            bulletAnimation.setOnFinished(event -> root.getChildren().remove(bullet));
            bulletAnimation.play();
        }
    }

	private void showExplosion(double x, double y, double enemyWidth, double enemyHeight) {
		System.out.println("Explosion at: " + x + ", " + y);

		explosion.setTranslateX(x);
		explosion.setTranslateY(y);
		explosion.setFitWidth(enemyWidth); // Set explosion size same as enemy
		explosion.setFitHeight(enemyHeight);
		explosion.setVisible(true);
		explosionSound.play();

		// Create a TimeLine for the explosion animation
		Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(500), e -> {
			explosion.setVisible(false);
			System.out.println("Explosion hidden");
		}));
		timeline.play();
	}

	private long lastShotTime = 0;
	private static final long SHOT_COOLDOWN = 300;

	Set<KeyCode> pressedKeys = new HashSet<>();

	// It can move simultaneously and also shoot bullets at the same time
	private void updatePlayerMovement() {

		long currentTime = System.currentTimeMillis();
		if (pressedKeys.contains(KeyCode.W)) {
			player.moveUp();
		}

		if (pressedKeys.contains(KeyCode.A)) {
			player.moveLeft();
		}

		if (pressedKeys.contains(KeyCode.D)) {
			player.moveRight(root.getPrefWidth());
		}

		if (pressedKeys.contains(KeyCode.S)) {
			player.moveDown(root.getPrefHeight());
		}

		if (pressedKeys.contains(KeyCode.SPACE)) {
			if (currentTime - lastShotTime >= SHOT_COOLDOWN) {
				//shot(player);
				laserSound.play();
				lastShotTime = currentTime;
			}
		}

		if (pressedKeys.isEmpty()) {
			player.moveStop();
		}
	}

	public void movePlayer(Pane root) {

		root.getScene().setOnKeyPressed(e -> {
			pressedKeys.add(e.getCode());
			updatePlayerMovement();
		});

		root.getScene().setOnKeyReleased(e -> {
			pressedKeys.remove(e.getCode());
			updatePlayerMovement();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		level = lblLevel.getText();
		System.out.println(level);

		player = new Player((root.getPrefWidth() / 2.0) - 20.0, root.getPrefHeight() - 70, 50.0, 65.0, 5, "player",
				"/images/player.png", 10);

		root.getChildren().add(player);

		Platform.runLater(() -> movePlayer(root));

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				update();
			}

		};

		explosion = new ImageView(new Image(getClass().getResourceAsStream("/images/explotion-explode.gif")));
		explosion.setFitWidth(45);
		explosion.setFitHeight(65);
		explosion.setVisible(false);
		root.getChildren().add(explosion);
		
		//Level3
        gunImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/gun_level3.png")));
        gunImageView.setFitWidth(50);
        gunImageView.setFitHeight(50);
        gunImageView.setPreserveRatio(true);
        gunImageView.setLayoutX(WIDTH / 2 - 25);
        gunImageView.setLayoutY(HEIGHT - 60);
        root.getChildren().add(gunImageView);

		String backgroundMusicFile = "/audio/background_music.mp3";
		Media backgroundMusic = new Media(getClass().getResource(backgroundMusicFile).toExternalForm());
		backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		// explosion sound
		String explosionSoundFile = "/audio/falling-hit.wav";
		explosionSound = new AudioClip(getClass().getResource(explosionSoundFile).toExternalForm());

		// laser shot
		String laserShotFile = "/audio/laserShot.mp3";
		laserSound = new AudioClip(getClass().getResource(laserShotFile).toExternalForm());

		timer.start();

		playingLevel();

		backgroundMusicPlayer.play();

		start = LocalDateTime.now();

	}

}
