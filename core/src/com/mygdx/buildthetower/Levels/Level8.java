package com.mygdx.buildthetower.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.buildthetower.Assets;
import com.mygdx.buildthetower.BuildTheTower;
import com.mygdx.buildthetower.Screens.MainMenu;


/**
 * Created by musial321 on 4/8/2016
 */
public class Level8 implements Screen {

    private static final String LEVEL = "8";

    int counter;

    private Stage stage;
    private boolean crateTouched;
    private boolean cinderTouched;
    private boolean beamTouched;

    private BuildTheTower app;
    private Actor crate;
    private World world;
    private ShapeRenderer renderer;
    private float startLocationX=0;
    private float startLocationY=0;
    private Box2DDebugRenderer b2dr;
    private int diamondScale = 6;
    private float PPM = 100;

    private int count;

    //Bounding boxes.
    private Rectangle restartBounds;
    private Rectangle rotateBounds;
    private Rectangle pauseBounds;

    //Stores locations of the stars
    Vector2[] starLocations = new Vector2[5];

    //Background and floor
    private Image background;
    private Image floor;

    //Stores bodies and actors for the stage.
    private Body[] bodies;
    private Body[] stars;
    private Image[] realBox;

    private int[] starsTouched = new int[5];

    //A test.
    boolean test = true;

    //Checks if stage is complete.
    boolean checking;
    private long checkTime;

    //Stores location that an item is dropped.
    private Vector2 dropLocation;

    private Image restart;
    private Image rotate;
    private Image pause;
    private Image pauseScreen;
    private Image forward;

    private State state;

    private int numCrate;
    private Rectangle resumeBounds;
    private Rectangle menuBounds;
    private Rectangle forwardBounds;
    private Image nextLevel;
    private Rectangle nextLevelBounds;
    private Rectangle restartBounds2;
    private Rectangle homeBounds;
    private Image cinder;
    private int numCinder;
    private int numBeam;
    private boolean checkNum;
    private Image beam;
    private boolean rotated = false;


    enum State
    {
        play,pause,nextlevel
    }

    public Level8(BuildTheTower app) {
        this.app = app;
    }

    @Override
    public void show() {
        numCrate = 2;
        numCinder = 4;
        numBeam = 1;

        rotated = false;

        checkNum = true;
        counter = 0;

        crateTouched = false;
        cinderTouched = false;
        beamTouched = false;

        checking = false;

        count = 0;
        realBox = new Image[20];
        stars = new Body[20];
        bodies = new Body[20];

        setTouched();



        state = State.play;

        b2dr = new Box2DDebugRenderer();

        setupStage();
        addBackgroundFloorToStage();

        setupRenderer();
        world = new World(new Vector2(0,-10f),false);
        setUpListener(world);
        setupItems();
        setupFloorAndWalls();
        setupStars();
        setupRestart();
        setupRotate();
        setupPause();
        setupCount();
        setupFF();
        stageListener();
        Gdx.input.setInputProcessor(stage);

        app.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);

    }

    private void setupCount() {

        if(!crate.hasActions()) {
            app.batch.begin();
            app.font2.draw(app.batch, "x " + numCrate, 74, 775);
            app.batch.end();
        }
        if(!cinder.hasActions())
        {
            app.batch.begin();
            app.font2.draw(app.batch, "x " + numCinder, 74, 700);
            app.batch.end();
        }
        if(!beam.hasActions())
        {
            app.batch.begin();
            app.font2.draw(app.batch, "x " + numBeam, 78, 630);
            app.batch.end();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.62f,.72f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1/60f,6,2);


        //Delete?
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.end();

        drawStars();
        updateStage();
        stage.act(delta);
        stage.draw();


        if(state == State.play) {
            checkComplete();
        }

        setupCount();


        if(app.debug);
        //b2dr.render(world,stage.getCamera().combined.cpy().scl(PPM));


    }

    private void setupRestart() {

        restart = new Image(Assets.restart);


        restart.setPosition(app.VIRTUAL_WIDTH-Assets.restart.getWidth()-5
                ,app.VIRTUAL_HEIGHT-Assets.restart.getHeight()-5);


        restart.setOrigin(Assets.restart.getWidth()/2,Assets.restart.getHeight()/2);

        restart.setSize(restart.getWidth(),restart.getHeight());

        restartBounds = new Rectangle(0,0,restart.getWidth(),restart.getHeight());

        restart.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(x,y));
                unprojected.y = 800-unprojected.y;

                if(state == State.play) {
                    if (restartBounds.contains(x, y)) {
                        if(MathUtils.random(3) == 0) {
                            if (app.adsController.isWifiConnected()) {
                                app.adsController.showInterstitialAd(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("Interstitial app closed");
                                    }
                                });
                            } else {
                                //No wifi... :(
                            }
                        }


                        stage.clear();
                        app.getScreen().show();


                        restart.addAction(Actions.rotateBy(360, .7f));
                    }
                }
            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {


                return true;
            }
        });



        stage.addActor(restart);

    }

    private void setupFF() {

        forward = new Image(Assets.forward);


        forward.setPosition(app.VIRTUAL_WIDTH-Assets.forward.getWidth()-5
                ,app.VIRTUAL_HEIGHT-Assets.forward.getHeight()-135);


        forward.setOrigin(Assets.forward.getWidth()/2,Assets.forward.getHeight()/2);

        forward.setSize(forward.getWidth(),forward.getHeight());

        forwardBounds = new Rectangle(0,0,forward.getWidth(),forward.getHeight());

        forward.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(x,y));
                unprojected.y = 800-unprojected.y;
                if(state == State.play) {
                    if (forwardBounds.contains(x, y)) {
                        if (starsTouched[0] > 0 || starsTouched[1] > 0 || starsTouched[2] > 0 || starsTouched[3] > 0 || starsTouched[4] > 0) {
                            checking = true;
                            if (!forward.hasActions()) {
                                forward.addAction(Actions.sequence(Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f), Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f), Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f), Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f), Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f),
                                        Actions.scaleTo(1, 1), Actions.scaleTo(1.2f, 1.2f, .8f), Actions.scaleTo(1, 1)));
                            }
                        }
                    }
                }
            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {


                return true;
            }
        });



        stage.addActor(forward);

    }

    private void setupRotate() {

        rotate = new Image(Assets.rotate);


        rotate.setPosition(app.VIRTUAL_WIDTH-Assets.rotate.getWidth()-250
                ,app.VIRTUAL_HEIGHT-Assets.rotate.getHeight()-5);


        rotate.setOrigin(Assets.rotate.getWidth()/2,Assets.rotate.getHeight()/2);

        rotate.setSize(rotate.getWidth(),rotate.getHeight());

        rotateBounds = new Rectangle(0,0,rotate.getWidth(),rotate.getHeight());

        rotate.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                if(state == State.play) {

                    crate.addAction(Actions.rotateBy(-90,.3f, Interpolation.pow3));
                    cinder.addAction(Actions.rotateBy(-90,.3f, Interpolation.pow3));
                    beam.addAction(Actions.rotateBy(-90,.3f, Interpolation.pow3));

                    if(rotated)
                    {
                        rotated = false;
                    }
                    else if(!rotated)
                    {
                        rotated = true;
                    }

                }
            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {


                return true;
            }
        });





        stage.addActor(rotate);
    }


    private void setupNext() {
        nextLevel = new Image(Assets.nextLevel);
        nextLevel.setPosition(app.VIRTUAL_WIDTH/2-Assets.nextLevel.getWidth()/2
                ,app.VIRTUAL_HEIGHT/2-Assets.nextLevel.getHeight()/2);
        nextLevel.setOrigin(0,0);
        nextLevel.setSize(nextLevel.getWidth(),nextLevel.getHeight());

        nextLevelBounds = new Rectangle(260,19,93,63);
        restartBounds2 = new Rectangle(17,15,55,55);
        homeBounds = new Rectangle(83,15,55,55);

        nextLevel.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                if(nextLevelBounds.contains(x,y))
                {
                    app.setScreen(new Level9(app));
                }
                if(restartBounds2.contains(x,y))
                {
                    app.getScreen().show();
                    restart.addAction(Actions.rotateBy(360,.7f));
                }
                if(homeBounds.contains(x,y))
                {
                    app.setScreen(new MainMenu(app));
                }
            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {


                return true;
            }
        });
        stage.addActor(nextLevel);

        int counter = 0;
        for(Integer i: starsTouched)
        {
            if(i > 0)
            {
                counter++;
            }
        }

        if(app.prefs.getInteger("level" + LEVEL) < counter) {
            app.prefs.putInteger("level" + LEVEL, counter);
            app.prefs.flush();
        }


        Image star1 = new Image(Assets.diamond);

        star1.setPosition(app.VIRTUAL_WIDTH/2-Assets.diamond.getWidth()/2
                ,app.VIRTUAL_HEIGHT/2-Assets.diamond.getHeight()/2+15);
        star1.setOrigin(star1.getWidth()/2,star1.getHeight()/2);
        star1.setSize(star1.getWidth(),star1.getHeight());

        star1.addAction(Actions.parallel(Actions.scaleTo(.2f,.2f,.3f),Actions.moveBy(-70,0,.3f),Actions.rotateBy(20,.3f)));
        stage.addActor(star1);

        if(counter > 1) {
            Image star2 = new Image(Assets.diamond);

            star2.setPosition(app.VIRTUAL_WIDTH / 2 - Assets.diamond.getWidth() / 2
                    , app.VIRTUAL_HEIGHT / 2 - Assets.diamond.getHeight() / 2 + 23);
            star2.setOrigin(star1.getWidth() / 2, star1.getHeight() / 2);
            star2.setSize(star1.getWidth(), star1.getHeight());

            star2.addAction(Actions.sequence(Actions.alpha(0f), Actions.delay(.4f), Actions.alpha(1), Actions.parallel(Actions.scaleTo(.2f, .2f, .3f), Actions.moveBy(-35, 0, .3f),Actions.rotateBy(10,.3f))));
            stage.addActor(star2);
        }

        //3rd star
        if(counter > 2) {
            Image star2 = new Image(Assets.diamond);

            star2.setPosition(app.VIRTUAL_WIDTH / 2 - Assets.diamond.getWidth() / 2
                    , app.VIRTUAL_HEIGHT / 2 - Assets.diamond.getHeight() / 2 + 27);
            star2.setOrigin(star1.getWidth() / 2, star1.getHeight() / 2);
            star2.setSize(star1.getWidth(), star1.getHeight());

            star2.addAction(Actions.sequence(Actions.alpha(0f), Actions.delay(.8f), Actions.alpha(1), Actions.parallel(Actions.scaleTo(.2f, .2f, .3f), Actions.moveBy(0, 0, .3f))));
            stage.addActor(star2);
        }

        if(counter > 3) {
            Image star3 = new Image(Assets.diamond);

            star3.setPosition(app.VIRTUAL_WIDTH / 2 - Assets.diamond.getWidth() / 2
                    , app.VIRTUAL_HEIGHT / 2 - Assets.diamond.getHeight() / 2 + 23);
            star3.setOrigin(star1.getWidth() / 2, star1.getHeight() / 2);
            star3.setSize(star1.getWidth(), star1.getHeight());

            star3.addAction(Actions.sequence(Actions.alpha(0f), Actions.delay(1.2f), Actions.alpha(1), Actions.parallel(Actions.scaleTo(.2f, .2f, .3f), Actions.moveBy(35, 0, .3f), Actions.rotateBy(-10))));
            stage.addActor(star3);
        }

        if(counter > 4) {
            Image star3 = new Image(Assets.diamond);

            star3.setPosition(app.VIRTUAL_WIDTH / 2 - Assets.diamond.getWidth() / 2
                    , app.VIRTUAL_HEIGHT / 2 - Assets.diamond.getHeight() / 2 + 15);
            star3.setOrigin(star1.getWidth() / 2, star1.getHeight() / 2);
            star3.setSize(star1.getWidth(), star1.getHeight());

            star3.addAction(Actions.sequence(Actions.alpha(0f), Actions.delay(1.6f), Actions.alpha(1), Actions.parallel(Actions.scaleTo(.2f, .2f, .3f), Actions.moveBy(70, 0, .3f), Actions.rotateBy(-20))));
            stage.addActor(star3);
        }



    }

    private void setupPause()
    {
        pause = new Image(Assets.pause);


        pause.setPosition(app.VIRTUAL_WIDTH-Assets.pause.getWidth()-5
                ,app.VIRTUAL_HEIGHT-Assets.pause.getHeight()-70);


        pause.setOrigin(Assets.pause.getWidth()/2,Assets.pause.getHeight()/2);

        pause.setSize(pause.getWidth(),pause.getHeight());

        pauseBounds = new Rectangle(0,0,pause.getWidth(),pause.getHeight());

        pause.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(x,y));
                unprojected.y = 800-unprojected.y;

                if(state == State.play) {
                    if (pauseBounds.contains(x, y)) {
                        stage.addActor(pauseScreen);
                        checking = false;
                        state = State.pause;
                    }
                }



            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {

                return true;
            }
        });




        stage.addActor(pause);


        pauseScreen = new Image(Assets.pauseScreen);


        pauseScreen.setOrigin(0,0);
        pauseScreen.setPosition(app.VIRTUAL_WIDTH/2-Assets.pauseScreen.getWidth()/2
                ,app.VIRTUAL_HEIGHT-Assets.pauseScreen.getHeight()-250);



        pauseScreen.setSize(pauseScreen.getWidth(),pauseScreen.getHeight());
        resumeBounds = new Rectangle(30,113,322,64);
        menuBounds = new Rectangle(30,34,322,64);


        pauseScreen.addListener(new InputListener()
        {

            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                Vector2 unprojected = stage.getViewport().unproject(new Vector2(x,y));
                unprojected.y = 800-unprojected.y;

                System.out.println(x + " " + y);

                if(resumeBounds.contains(x,y))
                {

                    state = State.play;
                    checkTime = System.currentTimeMillis();
                    pauseScreen.remove();
                }
                if(menuBounds.contains(x,y))
                {
                    app.setScreen(new MainMenu(app));
                }


            }


            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {


                return true;
            }
        });


    }



    private void checkComplete() {
        if(!checking)
        {
            checkTime = System.currentTimeMillis();
            if(numCrate == 0 && numCinder == 0 && numBeam == 0)
            {
                checking = true;
            }


        }
        if(checking)
        {

            if(checkNum)
            {
                for(Integer i: starsTouched)
                {
                    if(i > 0)
                    {
                        counter++;
                    }
                }
                checkNum = false;
            }

            int temp = 0;
            for(Integer i: starsTouched)
            {
                if(i > 0)
                {
                    temp++;
                }
            }
            if(temp!=counter)
            {
                checking = false;
                counter = 0;
                checkNum = true;
            }



            if(starsTouched[0]<=0 && starsTouched[1]<=0 && starsTouched[2]<=0 && starsTouched[3]<=0 && starsTouched[4]<=0)
            {
                checking = false;
                forward.clearActions();
                forward.addAction(Actions.scaleTo(1,1));
            }

            if(System.currentTimeMillis()-checkTime<2000)
            {
                //Do nothing
            }
            else if(System.currentTimeMillis()-checkTime<3500)
            {
                app.batch.begin();
                app.font.draw(app.batch,"3",app.VIRTUAL_WIDTH/2,app.VIRTUAL_HEIGHT/2);
                app.batch.end();
            }
            else if(System.currentTimeMillis()-checkTime<5000)
            {
                app.batch.begin();
                app.font.draw(app.batch,"2",app.VIRTUAL_WIDTH/2,app.VIRTUAL_HEIGHT/2);
                app.batch.end();
            }
            else if(System.currentTimeMillis()-checkTime<6500)
            {
                app.batch.begin();
                app.font.draw(app.batch,"1",app.VIRTUAL_WIDTH/2,app.VIRTUAL_HEIGHT/2);
                app.batch.end();
            }
            else if(System.currentTimeMillis()-checkTime>7500)
            {
                //Complete
                forward.clearActions();
                forward.addAction(Actions.scaleTo(1,1));

                if(app.levelCount%2 == 0) {
                    if (app.adsController.isWifiConnected()) {
                        app.adsController.showInterstitialAd(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Interstitial app closed");
                            }
                        });
                    } else {
                        //No wifi... :(
                    }
                }
                app.levelCount++;

                setupNext();
                checking = false;
                state = State.nextlevel;
                //app.setScreen(new Level2(app));
            }
        }
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

    private void setTouched() {
        starsTouched[0] = 0;
        starsTouched[1] = 0;
        starsTouched[2] = 0;
        starsTouched[3] = 0;
        starsTouched[4] = 0;
    }

    private void setUpListener(World world) {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                for(int i = 0; i < 5; i++)
                {
                    if(contact.getFixtureA().getBody() == stars[i] || contact.getFixtureB().getBody() == stars[i])
                    {
                        starsTouched[i]++;
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                for(int i = 0; i < 5; i++)
                {
                    if(contact.getFixtureA().getBody() == stars[i] || contact.getFixtureB().getBody() == stars[i])
                    {
                        starsTouched[i]--;
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private void setupStars() {
        stars[0]=addStar(world,81,185,0);
        stars[1]=addStar(world,165,277,1);
        stars[2]=addStar(world,233,212,2);
        stars[3]=addStar(world,398,378,3);
        stars[4]=addStar(world,320,319,4);
    }

    private void setupFloorAndWalls() {
        //Create floor;
        addBox(world,0,97,480,1,true,3);
        addBox(world,-1,-1,1,800,true,3);
        addBox(world,481,-1,1,800,true,3);
    }




    private void addBackgroundFloorToStage() {
        background = new Image(Assets.background);
        background.setPosition(-40,0);
        stage.addActor(background);

        floor = new Image(Assets.floor);
        floor.setPosition(0,0);
        stage.addActor(floor);
    }

    private void drawStars() {

        for(Vector2 location:starLocations)
        {
            if(location!=null) {
                app.batch.begin();
                app.batch.draw(Assets.diamond, location.x, location.y,Assets.diamond.getWidth()/diamondScale,
                        Assets.diamond.getHeight()/diamondScale);
                app.batch.end();
            }
        }
    }

    private void updateStage() {
        if(crateTouched)
        {
            Vector2 unprojected = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

            crate.setPosition(crate.getX()+unprojected.x-startLocationX, crate.getY() + unprojected.y - startLocationY);

            startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),0f)).x;
            startLocationY = stage.getViewport().unproject(new Vector2(0f,Gdx.input.getY())).y;
        }

        if(cinderTouched)
        {
            Vector2 unprojected = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

            cinder.setPosition(cinder.getX()+unprojected.x-startLocationX, cinder.getY() + unprojected.y - startLocationY);

            startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),0f)).x;
            startLocationY = stage.getViewport().unproject(new Vector2(0f,Gdx.input.getY())).y;
        }

        if(beamTouched)
        {
            Vector2 unprojected = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

            beam.setPosition(beam.getX()+unprojected.x-startLocationX, beam.getY() + unprojected.y - startLocationY);

            startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),0f)).x;
            startLocationY = stage.getViewport().unproject(new Vector2(0f,Gdx.input.getY())).y;
        }

        //Update all objects
        for(int i = 0; i < realBox.length; i++)
        {

            if(realBox[i] != null)
            {
                realBox[i].setPosition(bodies[i].getPosition().x*PPM-realBox[i].getWidth()/2,bodies[i].getPosition().y*PPM-realBox[i].getHeight()/2);
                realBox[i].setRotation((bodies[i].getAngle() * 180 / (float) Math.PI ));
            }

        }

    }

    private void makeCinder() {
        float scale = .5f;
        dropLocation = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

        cinder.addAction(Actions.alpha(1f));

        if(!(dropLocation.x < 80 && dropLocation.y > 665) && dropLocation.y > 100) {

            numCinder--;

            bodies[count] = addBox(world,cinder.getX()+cinder.getWidth()/2,cinder.getY()+cinder.getHeight()/2,cinder.getWidth()*scale,cinder.getHeight()*scale,false,3);

            //Checks if bodies are placed outside of screen.
            if(bodies[count].getPosition().x*PPM - cinder.getWidth()*scale < 0)
            {
                bodies[count].setTransform(0,bodies[count].getPosition().y,0);
            }
            if(bodies[count].getPosition().x*PPM > 480 - cinder.getWidth()*scale)
            {
                bodies[count].setTransform(480/PPM,bodies[count].getPosition().y,0);
            }

            bodies[count].setTransform(bodies[count].getPosition().x, bodies[count].getPosition().y, (float) (cinder.getRotation() * (Math.PI / 180)));

            realBox[count] = new Image(Assets.cinder);
            realBox[count].setScale(scale);
            realBox[count].setOrigin(Assets.cinder.getWidth() / 2, Assets.cinder.getHeight() / 2);
            realBox[count].setPosition(bodies[count].getPosition().x * PPM - cinder.getWidth(), bodies[count].getPosition().y * PPM - cinder.getHeight());

            stage.addActor(realBox[count]);


            if (numCinder == 0) {
                cinder.clearListeners();
            }

            count++;


        }

        cinder.setPosition(15,675);
        cinder.setOrigin(Assets.cinder.getWidth()/4,Assets.cinder.getHeight()/4);
        cinder.setSize(Assets.cinder.getWidth()/2,Assets.cinder.getHeight()/2);
        cinder.setScale(1);
        cinder.setTouchable(Touchable.disabled);
        cinderTouched = false;
    }

    private void makeBeam()
    {
        float scale = .8f;
        dropLocation = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));

        beam.addAction(Actions.alpha(1f));

        if(!(dropLocation.x < 80 && dropLocation.y > 560) && dropLocation.y > 100) {

            numBeam--;

            bodies[count] = addBox(world,beam.getX()+beam.getWidth()/2,beam.getY()+beam.getHeight()/2,beam.getWidth()*scale,beam.getHeight()*scale,false,9);

            //Checks if bodies are placed outside of screen.

            if(!rotated) {
                if (bodies[count].getPosition().x * PPM - beam.getWidth() * scale < 0) {
                    bodies[count].setTransform(.85f, bodies[count].getPosition().y, 0);
                }
                if (bodies[count].getPosition().x * PPM > 480 - beam.getWidth() * scale) {
                    bodies[count].setTransform(480 / PPM, bodies[count].getPosition().y, 0);
                }
            }

            bodies[count].setTransform(bodies[count].getPosition().x, bodies[count].getPosition().y, (float) (beam.getRotation() * (Math.PI / 180)));

            realBox[count] = new Image(Assets.beam);
            realBox[count].setScale(scale);
            realBox[count].setOrigin(Assets.beam.getWidth() / 2, Assets.beam.getHeight() / 2);
            realBox[count].setPosition(bodies[count].getPosition().x * PPM - beam.getWidth(), bodies[count].getPosition().y * PPM - beam.getHeight());

            stage.addActor(realBox[count]);


            if (numBeam == 0) {
                beam.clearListeners();
            }

            count++;


        }

        beam.setPosition(-20,605);

        beam.setOrigin(Assets.beam.getWidth()/4,Assets.beam.getHeight()/4);
        beam.setSize(Assets.beam.getWidth()/2,Assets.beam.getHeight()/2);
        beam.setScale(.6f);
        beam.setTouchable(Touchable.disabled);
        beamTouched = false;
    }

    private void stageListener()
    {
        stage.addListener(new InputListener()
        {



            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {


                if(beamTouched)
                {
                    makeBeam();
                }
                else if(cinderTouched)
                {
                    makeCinder();
                }
            }

            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {


                if(x > 0 && x < 87 && y > 573 && y < 655 && numBeam > 0) {
                    float scale = .8f;
                    beamTouched = true;
                    beam.addAction(Actions.scaleTo(2 * scale, 2 * scale));
                    beam.addAction(Actions.alpha(.4f));
                    startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(), 0f)).x;
                    startLocationY = stage.getViewport().unproject(new Vector2(0f, Gdx.input.getY())).y;

                }

                if(x > 0 && x < 87 && y > 658 && y < 723 && numCinder > 0) {
                    float scale = .5f;
                    cinderTouched=true;
                    cinder.addAction(Actions.scaleTo(2*scale,2*scale));
                    cinder.addAction(Actions.alpha(.4f));
                    startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(),0f)).x;
                    startLocationY = stage.getViewport().unproject(new Vector2(0f,Gdx.input.getY())).y;

                }

                return true;
            }
        });
    }



    private void setupItems()
    {

        setupCrate();
        setupCinder();
        setupBeam();
    }

    private void setupBeam()
    {
        beam = new Image(Assets.beam);



        beam.setPosition(-20,1000);
        beam.setOrigin(Assets.beam.getWidth()/4,Assets.beam.getHeight()/4);
        beam.setSize(Assets.beam.getWidth()/2,Assets.beam.getHeight()/2);
        stage.addActor(beam);

        beam.addAction(Actions.scaleTo(0,0,0));

        beam.addAction(Actions.sequence(Actions.delay(1f),Actions.parallel(Actions.scaleTo(.6f,.6f,.5f), Actions.moveTo(-20,605,.5f))));
    }

    private void setupCinder() {
        cinder = new Image(Assets.cinder);



        cinder.setPosition(15,1000);
        cinder.setOrigin(Assets.cinder.getWidth()/4,Assets.cinder.getHeight()/4);
        cinder.setSize(Assets.cinder.getWidth()/2,Assets.cinder.getHeight()/2);

        stage.addActor(cinder);

        cinder.addAction(Actions.scaleTo(0,0,0));
        cinder.addAction(Actions.sequence(Actions.delay(.5f),Actions.parallel(Actions.scaleTo(1,1,.5f), Actions.moveTo(15,675,.5f))));
    }

    private void setupCrate() {


        crate = new Image(Assets.crate);

        crate.addListener(new InputListener()
        {
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if(state == State.play && !crate.hasActions())
                {
                    dropLocation = stage.getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                    crate.addAction(Actions.alpha(1f));

                    if (!(dropLocation.x < 80 && dropLocation.y > 665) && dropLocation.y > 100) {

                        numCrate--;

                        bodies[count] = addBox(world, crate.getX() + crate.getWidth() / 2, crate.getY() + crate.getHeight() / 2, crate.getWidth(), crate.getHeight(), false,1);
                        bodies[count].setTransform(bodies[count].getPosition().x, bodies[count].getPosition().y, (float) (crate.getRotation() * (Math.PI / 180)));

                        //Checks if bodies are placed outside of screen.
                        if (bodies[count].getPosition().x * PPM - 50 < 0) {
                            bodies[count].setTransform(0, bodies[count].getPosition().y, 0);
                        }
                        if (bodies[count].getPosition().x * PPM > 430) {
                            bodies[count].setTransform(480 / PPM, bodies[count].getPosition().y, 0);
                        }

                        realBox[count] = new Image(Assets.crate);


                        realBox[count].setOrigin(Assets.crate.getWidth() / 2, Assets.crate.getHeight() / 2);
                        realBox[count].setPosition(bodies[count].getPosition().x * PPM - crate.getWidth(), bodies[count].getPosition().y * PPM - crate.getHeight());

                        realBox[count].setRotation(crate.getRotation());
                        stage.addActor(realBox[count]);


                        count++;


                    }


                    crate.setPosition(15, 735);
                    crate.setOrigin(Assets.crate.getWidth() / 4, Assets.crate.getHeight() / 4);
                    crate.setSize(Assets.crate.getWidth() / 2, Assets.crate.getHeight() / 2);
                    crate.setScale(1);

                    if (numCrate == 0) {
                        crate.clearListeners();
                    }

                    crateTouched = false;
                }
            }

            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                if(state ==  State.play && !crate.hasActions()) {
                    crateTouched = true;
                    crate.addAction(Actions.scaleTo(2, 2));
                    crate.addAction(Actions.alpha(.4f));
                    startLocationX = stage.getViewport().unproject(new Vector2(Gdx.input.getX(), 0f)).x;
                    startLocationY = stage.getViewport().unproject(new Vector2(0f, Gdx.input.getY())).y;
                    return true;
                }
                return false;
            }
        });

        if (numCrate == 0) {
            crate.clearListeners();
        }

        crate.setPosition(15,1000);
        crate.setOrigin(Assets.crate.getWidth()/4,Assets.crate.getHeight()/4);
        crate.setSize(Assets.crate.getWidth()/2,Assets.crate.getHeight()/2);

        stage.addActor(crate);

        crate.addAction(Actions.scaleTo(0,0,0));
        crate.addAction(Actions.sequence(Actions.parallel(Actions.scaleTo(1,1,.5f), Actions.moveTo(15,735,.5f))));
    }

    private Body addBox(World world, float x, float y, float width, float height, boolean isStatic,float density) {
        Body body;
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/PPM,y/PPM);
        bdef.linearDamping = 1f;
        bdef.type = isStatic ? BodyDef.BodyType.StaticBody: BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PPM,height/PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = density;
        fdef.friction = .8f;
        body.createFixture(fdef);
        shape.dispose();

        return body;
    }



    private Body addStar(World world, float x, float y,int count) {
        starLocations[count] = new Vector2(x,y);

        Body body;
        BodyDef bdef = new BodyDef();
        bdef.position.set(x/PPM,y/PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();

        Vector2[] v2 = new Vector2[5];

        v2[0] = new Vector2(106/PPM/diamondScale,0);
        v2[1] = new Vector2(211/PPM/diamondScale,128/PPM/diamondScale);
        v2[2] = new Vector2(168/PPM/diamondScale,170/PPM/diamondScale);
        v2[3] = new Vector2(43/PPM/diamondScale,170/PPM/diamondScale);
        v2[4] = new Vector2(0,128/PPM/diamondScale);
        shape.set(v2);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.maskBits = 1;
        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef);


        shape.dispose();

        body.setActive(true);

        return body;
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


    private void setupRenderer() {
        renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(stage.getCamera().combined);
    }
}
