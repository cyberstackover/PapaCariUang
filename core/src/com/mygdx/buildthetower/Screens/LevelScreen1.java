package com.mygdx.buildthetower.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.buildthetower.Assets;
import com.mygdx.buildthetower.BuildTheTower;
import com.mygdx.buildthetower.Levels.*;


/**
 * Created by musial321 on 4/8/2016.
 */
public class LevelScreen1 implements Screen {
    private static final float DIAMOND_SCALE = .2f;
    private Stage stage;
    private BuildTheTower app;

    private Rectangle[] levelBoxes = new Rectangle[12];
    private Rectangle backBox;

    private Actor backButton;

    private Image background;

    private int starsCount[] = new int[100];

    private Vector2 paneVelocity = new Vector2(0,0);

    private Image stars[] = new Image[100];
    private Image cover;
    private Rectangle playBox;

    public LevelScreen1(BuildTheTower app) {
        this.app = app;
    }


    enum State
    {
        page1, page2, moving
    }

    @Override
    public void show() {
        setupStage();

        setupLevelBoxes();
        playBox = new Rectangle(608,13,122,122);




        setupBackground();

        setupBackButton();

        setupStars();

        Gdx.input.setInputProcessor(stage);

        handleInput();

        setupCover();

        stage.addAction(Actions.alpha(0));

        stage.addAction(Actions.sequence(Actions.delay(.2f),Actions.alpha(1,.4f)));


    }

    private void setupBackButton() {
        backBox = new Rectangle(0,0,96,48);
        backButton = new Image(Assets.back);
        backButton.setPosition(50,730);
        backButton.toFront();

        backButton.addListener(new InputListener()
        {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(x,y));
                unprojected.y = 800-unprojected.y;

                if(backBox.contains(x, y))
                {
                    app.setScreen(new MainMenu(app));
                }
            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {

                return true;
            }
        });


        stage.addActor(backButton);

    }

    private void setupCover()
    {
        cover = new Image(Assets.levelcover);

        cover.setPosition(65,225);
        cover.setOrigin(0,0);


        cover.addAction(Actions.scaleTo(0,0));
        cover.addAction(Actions.scaleTo(1,1,.5f,Interpolation.pow5));




        cover.toFront();
        cover.setTouchable(Touchable.disabled);

        stage.addActor(cover);
    }

    private void setupBackground() {
        background = new Image(Assets.levels);

        background.setPosition(54,215);
        background.setOrigin(0,0);


        background.addAction(Actions.scaleTo(0,0));
        background.addAction(Actions.scaleTo(1,1,.5f,Interpolation.pow5));

        background.addListener(new InputListener()
        {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {

                if(playBox.contains(x,y))
                {
                    //Put URL here.
                }

                if(levelBoxes[0].contains(x,y)) app.setScreen(new Level1(app));
                if(levelBoxes[1].contains(x,y)) app.setScreen(new Level2(app));
                if(levelBoxes[2].contains(x,y)) app.setScreen(new Level3(app));
                if(levelBoxes[3].contains(x,y)) app.setScreen(new Level4(app));
                if(levelBoxes[4].contains(x,y)) app.setScreen(new Level5(app));
                if(levelBoxes[5].contains(x,y)) app.setScreen(new Level6(app));
                if(levelBoxes[6].contains(x,y)) app.setScreen(new Level7(app));
                if(levelBoxes[7].contains(x,y)) app.setScreen(new Level8(app));
                if(levelBoxes[8].contains(x,y)) app.setScreen(new Level9(app));
                if(levelBoxes[9].contains(x,y)) app.setScreen(new Level10(app));
                if(levelBoxes[10].contains(x,y)) app.setScreen(new Level11(app));
                if(levelBoxes[11].contains(x,y)) app.setScreen(new Level12(app));




            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {

                return true;
            }
        });

        stage.addActor(background);

    }

    private void setupStars() {
        int count = 0;


        for(String level: app.prefs.get().keySet())
        {
            starsCount[Integer.parseInt(level.substring(5,level.length()))] = app.prefs.getInteger(level);
            count++;
        }

        count = 0;
        for(Integer i: starsCount)
        {
            switch(i)
            {
                case 0:break;

                case 1:
                    stars[count] = new Image(Assets.diamond);
                    stars[count].setPosition(61 + ((count-1)%12%3)*143,588 - ((count-1)%12/3)*120);
                    stars[count].setScale(DIAMOND_SCALE);
                    stars[count].setOrigin(0, 0);
                    stars[count].setTouchable(Touchable.disabled);
                   // stars[count].addAction(Actions.alpha(.7f));
                    stars[count].addAction(Actions.scaleTo(0,0));
                    stars[count].addAction(Actions.scaleTo(DIAMOND_SCALE,DIAMOND_SCALE,.5f,Interpolation.pow5));


                    stage.addActor(stars[count]);
                    break;
                case 2:
                    stars[count] = new Image(Assets.diamond2);
                    stars[count].setPosition(61 + ((count-1)%12%3)*143,588 - ((count-1)%12/3)*120);
                    stars[count].setScale(DIAMOND_SCALE);
                    stars[count].setOrigin(0, 0);
                    //stars[count].addAction(Actions.alpha(.7f));
                    stars[count].setTouchable(Touchable.disabled);
                    stars[count].addAction(Actions.scaleTo(0,0));
                    stars[count].addAction(Actions.scaleTo(DIAMOND_SCALE,DIAMOND_SCALE,.5f,Interpolation.pow5));
                    stage.addActor(stars[count]);

                    break;
                case 3:
                    stars[count] = new Image(Assets.diamond3);
                    stars[count].setPosition(61 + ((count-1)%12%3)*143,588 - ((count-1)%12/3)*120);
                    stars[count].setScale(DIAMOND_SCALE);
                    stars[count].setOrigin(0, 0);
                   // stars[count].addAction(Actions.alpha(.7f));
                    stars[count].setTouchable(Touchable.disabled);
                    stars[count].addAction(Actions.scaleTo(0,0));
                    stars[count].addAction(Actions.scaleTo(DIAMOND_SCALE,DIAMOND_SCALE,.5f,Interpolation.pow5));
                    stage.addActor(stars[count]);
                    break;

                case 4:
                    stars[count] = new Image(Assets.diamond4);
                    stars[count].setPosition(61 + ((count-1)%12%3)*143,588 - ((count-1)%12/3)*120);
                    stars[count].setScale(DIAMOND_SCALE);
                    stars[count].setOrigin(0, 0);
                  //  stars[count].addAction(Actions.alpha(.7f));
                    stars[count].setTouchable(Touchable.disabled);
                    stars[count].addAction(Actions.scaleTo(0,0));
                    stars[count].addAction(Actions.scaleTo(DIAMOND_SCALE,DIAMOND_SCALE,.5f,Interpolation.pow5));
                    stage.addActor(stars[count]);

                    break;
                case 5:
                    stars[count] = new Image(Assets.diamond5);
                    stars[count].setPosition(61 + ((count-1)%12%3)*143,588 - ((count-1)%12/3)*120);
                    stars[count].setScale(DIAMOND_SCALE);
                    stars[count].setOrigin(0, 0);
                   // stars[count].addAction(Actions.alpha(.7f));
                    stars[count].setTouchable(Touchable.disabled);
                    stars[count].addAction(Actions.scaleTo(0,0));
                    stars[count].addAction(Actions.scaleTo(DIAMOND_SCALE,DIAMOND_SCALE,.5f,Interpolation.pow5));
                    stage.addActor(stars[count]);
                    break;
            }
            count++;
        }
    }

    private void setupLevelBoxes() {
        levelBoxes[0] = new Rectangle(0,360,86,86);
        levelBoxes[1] = new Rectangle(143,360,86,86);
        levelBoxes[2] = new Rectangle(285,360,86,86);

        levelBoxes[3] = new Rectangle(0,243,86,86);
        levelBoxes[4] = new Rectangle(143,243,86,86);
        levelBoxes[5] = new Rectangle(285,243,86,86);

        levelBoxes[6] = new Rectangle(0,126,86,86);
        levelBoxes[7] = new Rectangle(143,126,86,86);
        levelBoxes[8] = new Rectangle(285,126,86,86);

        levelBoxes[9] = new Rectangle(0,0,86,86);
        levelBoxes[10] = new Rectangle(143,0,86,86);
        levelBoxes[11] = new Rectangle(285,0,86,86);
    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(.62f,.72f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //move pane.
        background.moveBy(paneVelocity.x,0);
        cover.moveBy(paneVelocity.x,0);
        for(Image star: stars)
        {
            if(star!=null) {
                star.moveBy(paneVelocity.x, 0);
            }
        }
        checkStopBox();








        stage.act(delta);
        stage.draw();

        app.batch.begin();
        //app.batch.draw(Assets.crate,0,0);
        app.batch.end();
    }

    private void checkStopBox() {
        //Subtract 480 for each screen
        if(background.getX() == 54-480)
        {
            paneVelocity.x = 0;
        }

        if(background.getX() == 54)
        {
            paneVelocity.x = 0;
        }

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

                if(velocityX > 0)
                {
                    if(background.getX() < 54)
                    {
                        paneVelocity.x = 15;
                    }
                }
                else
                {
                    if(background.getX() > 54 - 480) //Subtract 480 for each screen
                    paneVelocity.x = -15;
                }
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


        multi.addProcessor(stage);

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
        stage = new Stage(new FitViewport(app.VIRTUAL_WIDTH, app.VIRTUAL_HEIGHT,app.camera));
    }

}
