package com.eternalisland.infinitearmada.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.eternalisland.infinitearmada.actors.Enemy;
import com.eternalisland.infinitearmada.actors.EnemyFactory;
import com.eternalisland.infinitearmada.actors.Player;

public class World extends Actor {

	Stage stage;
	Player player;
	EnemyFactory ef;
	String enemyType;
	int numEnemies, index;
	int delay = 0;
	long lastSpawnTime = 0;
	String[] currWave, waveDat;
	private boolean endOfWave;

	public World(Stage stage, Player player) {
		this.stage = stage;
		this.player = player;
		this.index = 0;

		FileHandle file = Gdx.files.internal("data/wave1");
		currWave = file.readString().split("\n");

		ef = new EnemyFactory(player);
	}

	public void spawnEnemies(String enemyType, int numEnemies) {
		for (int i = 0; i < numEnemies; i++) {
			Enemy enemy = ef.makeEnemy(enemyType, (float) MathUtils.random(0,
					Gdx.graphics.getWidth()), (float) MathUtils.random(
					Gdx.graphics.getHeight(), Gdx.graphics.getHeight() + 200));

			stage.addActor(enemy);
		}
		lastSpawnTime = TimeUtils.millis();
	}

	@Override
	public void act(float delta) {
		if (index < currWave.length
				&& ((TimeUtils.millis() - lastSpawnTime) > delay)) {
			this.waveDat = currWave[index].split(" ");
			this.enemyType = waveDat[0].trim();
			this.numEnemies = Integer.parseInt(waveDat[1].trim());

			spawnEnemies(enemyType, numEnemies);

			this.delay = Integer.parseInt(waveDat[2].trim());

			index++;
		}
		if (index > currWave.length && stage.getActors().size <= 2) {
			endOfWave = true;
		}
		
		if(endOfWave){
			System.out.println("Wave over");
		}
	}
}
