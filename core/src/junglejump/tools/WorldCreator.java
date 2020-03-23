package junglejump.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class WorldCreator {

    private World world;
    private TiledMap map;
    private MapProperties properties;

    public WorldCreator(World world, TiledMap map) {
        this.world = world;
        this.map = map;
        properties = map.getProperties();
    }

    public void init() {
        float PPM = WorldConst.PPM;
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        int groundHitboxLayerIndex = (Integer) properties.get("GroundHitboxLayer");

        for (MapObject object : map.getLayers().get(groundHitboxLayerIndex).getObjects()) {
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
}
