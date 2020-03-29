package junglejump.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Background implements Disposable {

    private Array<Texture> textures;
    private float x;

    public Background(float x) {
        textures = new Array<>();
        for(int i = 1; i <= 5; i++) {
            textures.add(new Texture("backgrounds/plx-" + i + ".png"));
        }
        this.x = x;
    }

    public void draw(SpriteBatch sb, float width, float height) {
        for (Texture texture : textures) {
            sb.draw(texture,x, 0, width, height);
            sb.draw(texture,x + width, 0, width, height);
        }
    }


    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
