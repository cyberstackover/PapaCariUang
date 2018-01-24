package com.mygdx.buildthetower.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.buildthetower.BuildTheTower;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by musial321 on 4/8/2016.
 */
public class LoadingScreen implements Screen {
    BuildTheTower app;
    private ShapeRenderer shapeRenderer;
    private float progress;

    private Stage stage;
    private Image splashImage;

    public LoadingScreen(BuildTheTower app)
    {
        setupStage();
        this.app = app;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
    }


    @Override
    public void show() {
        progress = 0f;

        splashImage = new Image(new Texture("splash.png"));
        splashImage.setOrigin(splashImage.getWidth()/2,splashImage.getHeight()/2);
        splashImage.scaleBy(2);
        splashImage.setPosition(stage.getWidth()/2 - splashImage.getWidth()/2, stage.getHeight()/2 +32);

        stage.addActor(splashImage);



        splashImage.addAction(sequence(alpha(0f),scaleTo(.1f,.1f),
                parallel(fadeIn(1.5f, Interpolation.pow2),scaleTo(3f,3f,2f,Interpolation.pow5),moveTo(stage.getWidth()/2-splashImage.getWidth()/2,stage.getHeight()/2, 2f,Interpolation.swing))

        ));

        queueAssets();
    }

    private void setupStage()
    {
        stage = new Stage(new FitViewport(app.VIRTUAL_WIDTH, app.VIRTUAL_HEIGHT,app.camera));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.329412f,.329412f,.329412f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, app.camera.viewportHeight/2 - 8-200,progress*(app.VIRTUAL_WIDTH - 64),16);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
        app.actualWidth = width;
        app.actualHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void queueAssets()
    {
        app.assets.load("cinderblock.png",Texture.class);
        app.assets.load("crate2.png",Texture.class);
        app.assets.load("star.png",Texture.class);
        app.assets.load("floor.png",Texture.class);
        app.assets.load("background.png",Texture.class);
        app.assets.load("background2.png",Texture.class);
        app.assets.load("diamond.png",Texture.class);
        app.assets.load("diamond2.png",Texture.class);
        app.assets.load("diamond3.png",Texture.class);
        app.assets.load("diamond4.png",Texture.class);
        app.assets.load("diamond5.png",Texture.class);
        app.assets.load("mainmenu.png",Texture.class);
        app.assets.load("restart.png",Texture.class);
        app.assets.load("levels.png",Texture.class);
        app.assets.load("cinderblock.png",Texture.class);
        app.assets.load("help.png",Texture.class);
        app.assets.load("back.png",Texture.class);
        app.assets.load("rotate.png",Texture.class);
        app.assets.load("pause.png", Texture.class);
        app.assets.load("pausescreen.png", Texture.class);
        app.assets.load("ff.png", Texture.class);
        app.assets.load("nextlevel.png", Texture.class);
        app.assets.load("steelbeam.png", Texture.class);
        app.assets.load("cone.png", Texture.class);
        app.assets.load("levelcover.png", Texture.class);
        app.assets.load("gem.wav", Sound.class);
    }

    private void update(float delta)
    {


        stage.act(delta);

        progress = MathUtils.lerp(progress, app.assets.getProgress(), .01f);

        if(app.assets.update() && progress >= app.assets.getProgress() - .1)
        {
            app.setScreen(new MainMenu(app));
        }
    }
}
