package junglejump.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import junglejump.ui.GameHud;


public class Coin extends Sprite implements Disposable {

    private Animation<TextureRegion> animation;
    private Body body;

    private float radius;

    float stateTimer;

    public Coin(float x, float y, World world) {
        super(new Texture(Gdx.files.internal("Coin.png")));
        stateTimer = 0;
        radius = 0.4f;

        //Create animation
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 10, 0, 10,10));
        }
        animation = new Animation<TextureRegion>(0.2f, frames);

        setBounds(x,y, 1,1);

        BodyDef def = new BodyDef();
        def.position.set(x + radius, y + radius);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float dt) {
        stateTimer += dt;
        setRegion(animation.getKeyFrame(stateTimer, true));
    }

    public void onPlayerCollision() {
        GameHud.increaseScore(1);
    }

    @Override
    public void dispose() {
        getTexture().dispose();
    }
}
