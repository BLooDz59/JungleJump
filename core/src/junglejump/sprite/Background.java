package junglejump.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Background {

    Array<Texture> textures;
    Vector2 position;

    public Background(float x) {
        textures = new Array<>();
        for(int i = 1; i <= 5; i++) {
            textures.add(new Texture("backgrounds/plx-" + i + ".png"));
        }
        position = new Vector2(x, 0);
    }

    public Array<Texture> getTextures() {
        return textures;
    }

    public Vector2 getPosition() {
        return position;
    }
}
