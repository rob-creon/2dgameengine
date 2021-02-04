package com.creon.engine.test.game.entity;

import java.util.ArrayList;

import com.creon.engine.test.game.entity.component.EntityComponent;

import org.joml.Vector2f;

public class Entity {
    private Vector2f position;
    
    private ArrayList<EntityComponent> components;
    private ArrayList<Entity> entities;
}
