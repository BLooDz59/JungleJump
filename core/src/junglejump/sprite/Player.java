package junglejump.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import junglejump.screen.PlayScreen;

public class Player extends Sprite {

    public enum State { IDLE, JUMPING, FALLING, RUNNING, MID_AIR }

    private State currentState;
    private State previousState;

    private World world;
    public Body body;

    private TextureRegion idle;
    private TextureRegion jumpTexture;
    private TextureRegion landingTexture;
    private Animation idleAnimation;
    private Animation runningAnimation;
    private Animation midAirAnimation;

    private float stateTimer;
    private boolean runningRight;

    public Player(World world, TextureAtlas atlas) {
        super(atlas.findRegion("idle"));
        this.world = world;

        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(getTexture(), 2 + i * 21,0,21,39));
        }
        idleAnimation = new Animation(0.1f, frames);
        frames.clear();
        for (int i = 13; i < 19; i++) {
            frames.add(new TextureRegion(getTexture(), 2 + i * 23,0,23,39));
        }
        runningAnimation = new Animation(0.1f, frames);
        frames.clear();
        for (int i = 20; i < 22; i++) {
            frames.add(new TextureRegion(getTexture(), 1 + i * 22,0,23,39));
        }
        midAirAnimation = new Animation(0.7f, frames);


        idle = new TextureRegion(getTexture(),0,0,21,39);
        jumpTexture = atlas.findRegion("jump outline");
        landingTexture = atlas.findRegion("landing outline");


        createPlayer();
        setBounds(0,0,21 / PlayScreen.PPM,39 / PlayScreen.PPM);
        setRegion(idle);
    }

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth(), body.getPosition().y - getHeight());
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currentState = getCurrentState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = (TextureRegion) runningAnimation.getKeyFrame(stateTimer,true);
                break;
            case JUMPING:
                region = jumpTexture;
                break;
            case FALLING:
                region = landingTexture;
                break;
            case MID_AIR:
                region = (TextureRegion) midAirAnimation.getKeyFrame(stateTimer);
                break;
            default:
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTimer, true);
                break;

        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    private State getCurrentState() {
        if((body.getLinearVelocity().y < 2.7f && body.getLinearVelocity().y > -2.7f) && body.getLinearVelocity().y != 0) {
            return State.MID_AIR;
        }
        if(body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        }
        else if(body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if(body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.IDLE;
        }
    }

    public void createPlayer() {
        BodyDef def = new BodyDef();
        def.position.set(5, 10);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        Vector2 hitboxDimension = new Vector2(idle.getRegionWidth(), idle.getRegionHeight());
        hitboxDimension.scl(1 / (2 * PlayScreen.PPM));
        polygonShape.setAsBox(hitboxDimension.x,hitboxDimension.y, new Vector2(-hitboxDimension.x,-hitboxDimension.y),0);
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);

    }
}
