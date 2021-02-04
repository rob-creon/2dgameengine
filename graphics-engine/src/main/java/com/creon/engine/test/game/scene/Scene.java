package com.creon.engine.test.game.scene;

import java.util.ArrayList;

import com.creon.engine.test.game.entity.Entity;

public abstract class Scene {
    private ArrayList<Entity> entities;
    
    public Scene() {

    }

    private void update() {
        for(Entity e : entities){
            e.update();
        }
    }

    private void render() {
        for(Entity e : entities){
            e.render();
        }
    }
}
