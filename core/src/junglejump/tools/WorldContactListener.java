package junglejump.tools;

import com.badlogic.gdx.physics.box2d.*;
import junglejump.sprite.Coin;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() == "player" || fixtureB.getUserData() == "player") {
            Fixture player = fixtureA.getUserData() == "player" ? fixtureA : fixtureB;
            Fixture object = player == fixtureA ? fixtureB : fixtureA;

            if (object.getUserData() != null && Coin.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Coin) object.getUserData()).onPlayerCollision();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
