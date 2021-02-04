package com.creon.engine.test.game.entity;

import java.util.ArrayList;

import com.creon.engine.test.game.entity.component.Component;
import com.creon.engine.test.game.entity.component.RenderComponent;

import org.joml.Vector2f;

public class Entity {
    private Vector2f position;

    private ArrayList<Component> components;
    private ArrayList<Entity> entities;

    public void update() {
        for (Component c : components) {
            c.update();
        }

        for (Entity e : entities) {
            e.update();
        }
    }

    public void render() {
        for (Component c : components) {
            if (c instanceof RenderComponent) {
                ((RenderComponent) c).render();
            }
        }
        for (Entity e : entities) {
            e.render();
        }
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
}
