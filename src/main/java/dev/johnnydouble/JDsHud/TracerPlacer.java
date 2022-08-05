package dev.johnnydouble.JDsHud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import scala.Int;
import scala.collection.parallel.ParIterableLike;

import java.awt.*;
import java.text.DecimalFormat;

public class TracerPlacer extends Gui {
    private java.util.List<net.minecraft.entity.player.EntityPlayer> players;
    private java.util.List<net.minecraft.entity.Entity> entities;
    private DecimalFormat numFormat;

    private boolean allSeen;

    TracerPlacer() {
        super();
        numFormat = new DecimalFormat("#.##");
        allSeen = true;
    }

//    @SubscribeEvent
//    public void onResize(Event)

    @SubscribeEvent
    public void renderCustomUI(RenderGameOverlayEvent rgoEV) {
        //ensure one run
        if (rgoEV.type.equals(ElementType.ALL)) {
            allSeen = true;
        }

        if (allSeen && rgoEV.type.equals(ElementType.TEXT)) {
            players = Minecraft.getMinecraft().theWorld.playerEntities;
            entities = Minecraft.getMinecraft().theWorld.loadedEntityList;


            double leastMobDist = Integer.MAX_VALUE;
            double leastPlayerDist = Integer.MAX_VALUE;

            net.minecraft.client.entity.EntityPlayerSP playerSP = Minecraft.getMinecraft().thePlayer;
            Vec3 pos = playerSP.getPositionVector();

            for (net.minecraft.entity.Entity ent : entities) {

                if (ent.isCreatureType(EnumCreatureType.MONSTER, true)) {
                    Vec3 mobPos = ent.getPositionVector();
                    double dist = pos.distanceTo(mobPos);
                    if (dist < leastMobDist && ent.isEntityAlive()) {
                        leastMobDist = dist;
                    }
                }
            }

            for(net.minecraft.entity.player.EntityPlayer p: players) {
                float dist = playerSP.getDistanceToEntity(p);
                if(dist < leastPlayerDist && !p.getUniqueID().equals(playerSP.getUniqueID())){
                    leastPlayerDist = dist;
                }
            }

            String grey = "§7";

            if (leastMobDist < Integer.MAX_VALUE) {
                super.drawString(Minecraft.getMinecraft().fontRendererObj, grey + "Mob: " + numFormat.format(leastMobDist), Minecraft.getMinecraft().displayWidth / 50, Minecraft.getMinecraft().displayHeight / 25, Minecraft.getMinecraft().displayHeight / 50);
            }

            if (leastPlayerDist< Integer.MAX_VALUE) {
                super.drawString(Minecraft.getMinecraft().fontRendererObj, grey + "Plr:  " + numFormat.format(leastPlayerDist), Minecraft.getMinecraft().displayWidth / 50, Minecraft.getMinecraft().displayHeight / 25 + Minecraft.getMinecraft().displayHeight / 200 + 20, Minecraft.getMinecraft().displayHeight / 50);
            }

            allSeen = false;
        }
        Minecraft.getMinecraft().gameSettings.setOptionFloatValue(GameSettings.Options.GAMMA, 2.0F);
    }

}


