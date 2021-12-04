package me.catmi.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.nio.file.AtomicMoveNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {
    private static final float SPEED = 0.1f;
    private List<Particle> particleList = new ArrayList<>();
    private boolean mouse;
    private boolean rainbow;
    private int dist;

    public ParticleSystem(int initAmount, boolean mouse, boolean rainbow, int dist) {

        addParticles(initAmount);
        this.mouse = mouse;
        this.dist = dist;
        this.rainbow = rainbow;

    }

    public void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            particleList.add(Particle.generateParticle());
        }
    }


    public void tick(int delta) {
        if (Mouse.isButtonDown(0)) addParticles(1);
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    public void render() {
        for (Particle particle : particleList) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, particle.getAlpha() / 255.0f);
            GL11.glPointSize(particle.getSize());
            GL11.glBegin(GL11.GL_POINTS);

            GL11.glVertex2f(particle.getX(), particle.getY());
            GL11.glEnd();

            if (mouse) {

                java.awt.Color c = null;
                if (rainbow) {

                    c = Color2.rainbow(50.0f, 0.0f);

                }

                float distance = (float) MathUtil2.distance(particle.getX(), particle.getY(), Mouse.getX(), Display.getHeight() - Mouse.getY());
                if (distance < dist) {
                    float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - distance / dist));
                    drawLine(particle.getX(),
                            particle.getY(),
                            Mouse.getX(),
                            Display.getHeight() - Mouse.getY(),
                            rainbow ? c.getRed() / 255.0f : 1,
                            rainbow ? c.getGreen() / 255.0f : 1,
                            rainbow ? c.getBlue() / 255.0f : 1,
                            alpha);
                }

            } else {

                float nearestDistance = 0;
                Particle nearestParticle = null;

                for (Particle particle1 : particleList) {
                    float distance = particle.getDistanceTo(particle1);
                    if (distance <= dist
                            && (MathUtil2.distance(Mouse.getX(), Display.getHeight() - Mouse.getY(), particle.getX(), particle.getY()) <= dist
                            || MathUtil2.distance(Mouse.getX(), Display.getHeight() - Mouse.getY(), particle1.getX(), particle1.getY()) <= dist)
                            && (nearestDistance <= 0 || distance <= nearestDistance)) {

                        nearestDistance = distance;
                        nearestParticle = particle1;

                    }
                }

                if (nearestParticle != null) {

                    java.awt.Color c = null;
                    if (rainbow) {

                        c = Color2.rainbow(50.0f, 0.0f);

                    }
                    float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));
                    drawLine(particle.getX(),
                            particle.getY(),
                            nearestParticle.getX(),
                            nearestParticle.getY(),
                            rainbow ? c.getRed() / 255.0f : 1,
                            rainbow ? c.getGreen() / 255.0f : 1,
                            rainbow ? c.getBlue() / 255.0f : 1,
                            alpha);
                }

            }
        }
    }

    private void drawLine(float x, float y, float x1, float y1, float r, float g, float b, float alpha) {

        GL11.glColor4f(r, g, b, alpha);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(GL11.GL_LINES);

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();

    }

}