package net.hasagj.teamod.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;


public class NautilusLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    private final HumanoidModel<PlayerRenderState> innerModel;
    private final HumanoidModel<PlayerRenderState> outerModel;
    private static final ResourceLocation OUTER = ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/armor/nautilus/nautilus_outer.png");
    private static final ResourceLocation INNER = ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/armor/nautilus/nautilus_inner.png");

    public NautilusLayer(PlayerRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.innerModel = new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.outerModel = new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
    }


    @Override
        public void render(PoseStack stack, MultiBufferSource buffers, int packedLight, PlayerRenderState state, float yRot, float xRot) {

        Player player = Minecraft.getInstance().player;
        if (player != null && player.hasEffect(ModEffects.SEAS_BLESSING_EFFECT)) {
            this.getParentModel().copyPropertiesTo(this.innerModel);
            this.getParentModel().copyPropertiesTo(this.outerModel);

            this.innerModel.setupAnim(state);
            this.outerModel.setupAnim(state);

            stack.pushPose();

            this.innerModel.setAllVisible(false);
            this.innerModel.head.visible = true;
            this.innerModel.body.visible = true;
            this.innerModel.leftArm.visible = true;
            this.innerModel.rightArm.visible = true;
            this.innerModel.leftLeg.visible = true;
            this.innerModel.rightLeg.visible = true;

            VertexConsumer inner = buffers.getBuffer(RenderType.armorCutoutNoCull(INNER));
            this.innerModel.renderToBuffer(stack, inner, packedLight, OverlayTexture.NO_OVERLAY);

            this.outerModel.setAllVisible(false);
            this.outerModel.head.visible = true;
            this.outerModel.body.visible = true;
            this.outerModel.leftArm.visible = true;
            this.outerModel.rightArm.visible = true;
            this.outerModel.leftLeg.visible = true;
            this.outerModel.rightLeg.visible = true;

            VertexConsumer outer = buffers.getBuffer(RenderType.armorCutoutNoCull(OUTER));
            this.outerModel.renderToBuffer(stack, outer, packedLight, OverlayTexture.NO_OVERLAY);

            stack.popPose();
        }
    }


}

