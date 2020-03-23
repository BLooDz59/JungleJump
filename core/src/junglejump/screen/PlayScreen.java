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

public class PlayScreen implements Screen {

    private final static float GRAVITY = 9.81f;

    private final static int WORLD_WIDTH = 100;
    private final static int WORLD_HEIGHT = 60;

    public final static float PPM = 16f;

    private JungleJump game;

    private OrthogonalTiledMapRenderer renderer;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;

    private Background background;

    private World world;

    private Player player;
    private TextureAtlas playerAtlas;

    private boolean activeDebug = false;

    public PlayScreen(JungleJump game) {
        this.game = game;
        playerAtlas = new TextureAtlas("player.atlas");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(WORLD_WIDTH / 2f,WORLD_HEIGHT * (h / w));

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        TmxMapLoader loader = new TmxMapLoader();
        TiledMap map = loader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        world = new World(Vector2.Y.scl(-GRAVITY),true);
        debugRenderer = new Box2DDebugRenderer();

        background = new Background(0);

        player = new Player(world, playerAtlas);

        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(1).getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.x / PPM + (rect.getWidth() / PPM) / 2, rect.y / PPM + (rect.getHeight() / PPM) / 2);

                body = world.createBody(bodyDef);

                polygonShape.setAsBox((rect.getWidth() / PPM) / 2, (rect.getHeight() / PPM) / 2);
                fixtureDef.shape = polygonShape;
                fixtureDef.friction = 1f;
                body.createFixture(fixtureDef);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        game.spriteBatch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    private void update(float dt) {
        handleInput(dt);
        world.step(1/60f, 6,2);
        player.update(dt);
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(1.5f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-1.5f, 0), player.body.getWorldCenter(), true);
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
