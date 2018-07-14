package com.spiretos.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.spiretos.mariobros.MarioBros;
import com.spiretos.mariobros.Scenes.HUD;
import com.spiretos.mariobros.Sprites.Enemies.Enemy;
import com.spiretos.mariobros.Sprites.Items.Item;
import com.spiretos.mariobros.Sprites.Items.ItemDef;
import com.spiretos.mariobros.Sprites.Items.Mushroom;
import com.spiretos.mariobros.Sprites.Mario;
import com.spiretos.mariobros.Tools.B2WorldCreator;
import com.spiretos.mariobros.Tools.WorldContactListener;



import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen, InputProcessor {
    private MarioBros game;
    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private HUD hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Mario player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    boolean available;
    private float accelX;
    private float accelY;
    private float accelZ;



    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        hud = new HUD(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();


        Gdx.input.setInputProcessor(this);
        available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();

    }


    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }


    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }



    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    //When the player does an action, then the character[Mario] will move.
    //May need to get rid of this method or edit to apply the wear/wearemote
    //as the function of moving the character instead of using phone/keyboard.
    public void handleInput(float dt) {
        if(player.currentState != Mario.State.DEAD) {

            if ((Gdx.input.getAccelerometerY() >=1) && (player.b2Body.getLinearVelocity().x <= 2)) {
                player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0f), player.b2Body.getWorldCenter(), true);
            }
            if ((Gdx.input.getAccelerometerY() <= -1) && (player.b2Body.getLinearVelocity().x >= -2)) {
                player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0f), player.b2Body.getWorldCenter(), true);
            }
        }
    }


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
        player.jump();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();
        setMarioSpeedY(dt);
        //marioMovement();

        world.step(1/60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / MarioBros.PPM){
                enemy.b2Body.setActive(true);
            }
        }

        for(Item item : items) {
            item.update(dt);
        }

        hud.update(dt);

        //attach our gamecam to our players.x coordinate
        if(player.currentState != Mario.State.DEAD) {
            gameCam.position.x = player.b2Body.getPosition().x;
        }

        gameCam.position.x = player.b2Body.getPosition().x;


        /*if (accelY >= 1 && accelY <= 9 && player.b2Body.getLinearVelocity().x <= 2) {
            player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0f), player.b2Body.getWorldCenter(), true);
        }
        if (accelY >= -10 && accelY <= -1 && player.b2Body.getLinearVelocity().x >= -2) {
            player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0f), player.b2Body.getWorldCenter(), true);
        }*/
        //System.out.println(Gdx.input.getAccelerometerY());

        gameCam.update();
        renderer.setView(gameCam);



    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }
        for (Item item : items) {
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }




    }

    public void marioJump(boolean jump) {
        if(jump == true) {
            player.jump();
        }
    }

    public void setMarioSpeedY(float y) {

        if(player.currentState != Mario.State.DEAD) {
            if (player.setSpeed(y) == 0) {
                player.b2Body.applyLinearImpulse(new Vector2(0, 0), player.b2Body.getWorldCenter(), true);
            }
            if ((player.setSpeed(y) >= 1) && (player.b2Body.getLinearVelocity().x <= 2)) {
                player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
                setMarioSpeedY(y);
            }
            if ((player.setSpeed(y) <= -1) && (player.b2Body.getLinearVelocity().x >= -2)) {
                player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
                setMarioSpeedY(y);
            }
        }

        //player.setSpeed(y * 130f);
    }

   /* public void marioMovement() {

        float y = accelY;

        if(y == 0) {
            player.b2Body.applyLinearImpulse(new Vector2(0, 0), player.b2Body.getWorldCenter(), true);

        }
        if ((y >= 1) && (player.b2Body.getLinearVelocity().x <= 2)) {
            player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0f), player.b2Body.getWorldCenter(), true);
        }
        if ((y <= -1) && (player.b2Body.getLinearVelocity().x >= -2)) {
            player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0f), player.b2Body.getWorldCenter(), true);
        }
    }*/


    public boolean gameOver(){
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

}
