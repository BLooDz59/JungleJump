package junglejump.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import junglejump.JungleJump;
import junglejump.sprite.Background;
import junglejump.sprite.Player;
import junglejump.tools.WorldCreator;

public class PlayScreen implements Screen {

    private final static float GRAVITY = 9.81f;

    private final static int WORLD_WIDTH = 100;
    private final static int WORLD_HEIGHT = 60;

    public final static float PPM = 16f;

    private JungleJump game;

    //Screen fields
    private OrthographicCamera camera;

    //Tilemap fields
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D fields
    private World world;
    private Box2DDebugRenderer debugRenderer;

    //Assets fields
    private Player player;
    private Background background;

    //Debug fields
    private boolean activeDebug = false;

    public PlayScreen(JungleJump game) {
        this.game = game;

        //Initialize Camera
        float aspectRatio = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera(WORLD_WIDTH / 2f,WORLD_HEIGHT * aspectRatio);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        //Initialize TiledMap
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        //Initialize Box2D World
        world = new World(Vector2.Y.scl(-GRAVITY),true);
        debugRenderer = new Box2DDebugRenderer();
        WorldCreator creator = new WorldCreator(world, map);
        creator.init();

        //Initialize Sprites
        TextureAtlas playerAtlas = new TextureAtlas("player.atlas");
        player = new Player(world, playerAtlas);
        background = new Background(0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        game.spriteBatch.setProjectionMatrix(camera.combined);
        clearScreen();

        game.spriteBatch.begin();
        for (Texture texture : background.getTextures()) {
            game.spriteBatch.draw(texture, background.getPosition().x, background.getPosition().y, camera.viewportWidth, camera.viewportHeight);
        }
        game.spriteBatch.end();

        renderer.render();

        game.spriteBatch.begin();
        player.draw(game.spriteBatch);
        game.spriteBatch.end();

        if (activeDebug) {
            debugRenderer.render(world, camera.combined);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float dt) {
        handleInput(dt);
        world.step(1/60f, 6,2);
        player.update(dt);
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.body.getLinearVelocity().y == 0) {
            player.body.applyLinearImpulse(new Vector2(0f, 10f), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            activeDebug = !activeDebug;
        }


        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth  / 2f, 100 - camera.viewportWidth  / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2f, 100 - camera.viewportHeight / 2f);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width/(WORLD_WIDTH / 2f);
        camera.viewportHeight = camera.viewportWidth * height/width;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

    }
}
