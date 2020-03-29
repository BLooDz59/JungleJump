package junglejump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import junglejump.screen.PlayScreen;

public class GameHud implements Disposable {

    private Stage stage;
    private Table table;

    private static Label scoreLbl;
    private static int score;

    private PlayScreen playScreen;

    public boolean debug;

    public GameHud(PlayScreen screen) {
        debug = false;

        playScreen = screen;

        stage = new Stage();
        table = new Table();

        table.setFillParent(true);
        table.center().top();

        score = 0;
        scoreLbl = new Label("Score: " + score, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(scoreLbl);

        stage.addActor(table);
    }

    public static void increaseScore(int value) {
        score += value;
        scoreLbl.setText("Score: " + score);
    }

    public void draw() {
        table.setDebug(debug);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
