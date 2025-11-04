package net.hasagj.teamod.screen.custom;

import net.hasagj.teamod.TeaMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;

import java.awt.*;

public class TeaHandbookScreen extends Screen {
    public TeaHandbookScreen() {
        super(Component.literal("TEST"));
    }
    private PageButton forwardButton;
    private PageButton backButton;
    private Button readButton;
    private Button hideTextButton;
    private int currentText = 0;
    private int currentPage = 1;

    @Override
    protected void init() {
        // пример кнопки
        this.readButton =  this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.translatable("text.item.teamod.read_button"), b -> this.setText())
                .bounds(this.width / 2 - 50, this.height / 2 + 95, 100, 20)
                .build());
        this.hideTextButton = this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.translatable("text.item.teamod.hide_text_button"), b -> this.nullifyText())
                .bounds(this.width / 2 - 50, this.height / 2 + 95, 100, 20)
                .build());
        this.forwardButton =  (PageButton) this.addRenderableWidget( new PageButton(this.width / 2 + 49, this.height / 2 + 95, true, b -> this.pageForward(), true));
        this.backButton =  (PageButton) this.addRenderableWidget( new PageButton(this.width / 2 - 73, this.height / 2 + 95, false, b -> this.pageBack(), true));
        this.updateButtonVisibility();
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }
        this.updateButtonVisibility();
        
    }
    private void setText() {
        this.currentText = this.currentPage - 1;
        this.updateButtonVisibility();
    }

    private void  nullifyText() {
        this.currentText = 0;
        this.updateButtonVisibility();
    }

    private void pageForward() {
        ++this.currentPage;
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 1 && this.currentText == 0;
        this.forwardButton.visible = this.currentPage < 34 && this.currentText == 0;
        this.readButton.visible = this.currentText == 0 && this.currentPage > 1;
        this.hideTextButton.visible = this.currentText > 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.currentText == 0) {
            guiGraphics.blit(RenderType.GUI_TEXTURED, ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID, "textures/gui/tea_handbook/tea_handbook_page" + currentPage + ".png"), this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
        } else {
            int y = this.height / 2 - 70;
            for (FormattedCharSequence line : this.font.split(Component.translatable("text.item.teamod.text" + currentText), this.width / 2)) {
                guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                y += this.font.lineHeight + 2;
            }
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // чтобы не ставило игру на паузу
    }
}
