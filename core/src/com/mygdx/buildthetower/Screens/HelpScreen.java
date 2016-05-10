package com.mygdx.buildthetower.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.buildthetower.Assets;
import com.mygdx.buildthetower.BuildTheTower;


/**
 * Created by musial321 on 4/8/2016.
 */
public class HelpScreen implements Screen {
    private Stage stage;
    private BuildTheTower app;

    //Touch boxes for buttons
    private Rectangle backButton;

    private Actor background;


    public HelpScreen(BuildTheTower app) {
        this.app = app;
    }

    @Override
    public void show() {
        setupStage();

        backButton = new Rectangle(44,116,98,48);



        background = new Image(Assets.help);
        background.setPosition(0,0);
        stage.addActor(background);

        Gdx.input.setInputProcessor(stage);

        handleInput();

        stage.addAction(Actions.alpha(0));

        stage.addAction(Actions.sequence(Actions.delay(.2f),Actions.alpha(1,.4f)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.62f,.72f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);






        stage.act(delta);
        stage.draw();



    }

    private void handleInput() {
        InputMultiplexer multi = new InputMultiplexer();

        multi.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }
        }));

        multi.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(screenX,screenY));


                if(backButton.contains(unprojected.x,unprojected.y))
                {
                    app.setScreen(new MainMenu(app));
                }


                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });


        Gdx.input.setInputProcessor(multi);
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


    private void setupStage()
    {
        if(Math.abs((float)Gdx.graphics.getHeight()/Gdx.graphics.getWidth() - 1.66667) > .4)
        {
            stage = new Stage(new FitViewport(app.VIRTUAL_WIDTH, app.VIRTUAL_HEIGHT,app.camera));
        }
        else
        {
            stage = new Stage(new StretchViewport(app.VIRTUAL_WIDTH, app.VIRTUAL_HEIGHT,app.camera));
        }
    }
}
