package junglejump.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import junglejump.JungleJump;
import junglejump.sprite.Background;
import junglejump.sprite.Coin;
import junglejump.sprite.Player;
import junglejump.tools.WorldConst;
import junglejump.tools.WorldContactListener;
import junglejump.tools.WorldCreator;

public class PlayScreen implements Screen {

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
    private TextureAtlas playerAtlas;
    private Player player;
    private Background background;

    //Debug fields
    private boolean activeDebug = false;

    public PlayScreen(JungleJump game) {
        this.game = game;

        //Initialize Camera
        float aspectRatio = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera();

        //Initialize TiledMap
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / WorldConst.PPM);

        //Initialize Box2D World
        world = new World(Vector2.Y.scl(-WorldConst.GRAVITY),true);
        world.setContactListener(new WorldContactListener());
        debugRenderer = new Box2DDebugRenderer();
        WorldCreator creator = new WorldCreator(world, map);
        creator.init();

        //Initialize Sprites
        playerAtlas = new TextureAtlas("player.atlas");
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
        background.draw(game.spriteBatch, camera.viewportHeight * (16/9f), camera.viewportHeight);
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
        handleInput();
        world.step(WorldConst.TIME_STEP, WorldConst.VELOCITY_ITERATIONS, WorldConst.POSITIONS_ITERATIONS);
        player.update(dt);
        camera.position.set(player.body.getPosition().x, camera.position.y, 0);
        camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth  / 2f,  100 - camera.viewportWidth  / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight / 2f, 100 - camera.viewportHeight / 2f);
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.move(Player.Direction.RIGHT);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.move(Player.Direction.LEFT);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.body.getLinearVelocity().y == 0) {
            player.body.applyLinearImpulse(new Vector2(0f, 10f), player.body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            activeDebug = !activeDebug;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / (2*WorldConst.PPM);
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
        map.dispose();
        world.dispose();
        renderer.dispose();
        debugRenderer.dispose();
        playerAtlas.dispose();
        player.dispose();
    }
}
