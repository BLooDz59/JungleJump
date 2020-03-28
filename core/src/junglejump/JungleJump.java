package junglejump;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import junglejump.screen.MenuScreen;
import junglejump.screen.PlayScreen;
import junglejump.screen.SettingScreen;

public class JungleJump extends Game {

	public SpriteBatch spriteBatch;

	public MenuScreen MENU_SCREEN;
	public PlayScreen PLAY_SCREEN;
	public SettingScreen SETTING_SCREEN;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();

		MENU_SCREEN = new MenuScreen(this);
		PLAY_SCREEN = new PlayScreen(this);
		SETTING_SCREEN = new SettingScreen(this);

		setScreen(MENU_SCREEN);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
