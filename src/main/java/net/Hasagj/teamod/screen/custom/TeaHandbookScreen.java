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
    private int currentPage = 0;


    private final Component text1 = Component.translatable("text.item.teamod.text1");
    private final Component text2 = Component.translatable("text.item.teamod.text2");
    private final Component text3 = Component.translatable("text.item.teamod.text3");
    private final Component text4 = Component.translatable("text.item.teamod.text4");
    private final Component text5 = Component.translatable("text.item.teamod.text5");
    private final Component text6 = Component.translatable("text.item.teamod.text6");
    private final Component text7 = Component.translatable("text.item.teamod.text7");
    private final Component text8 = Component.translatable("text.item.teamod.text8");
    private final Component text9 = Component.translatable("text.item.teamod.text9");
    private final Component text10 = Component.translatable("text.item.teamod.text10");
    private final Component text11 = Component.translatable("text.item.teamod.text11");
    private final Component text12 = Component.translatable("text.item.teamod.text12");
    private final Component text13 = Component.translatable("text.item.teamod.text13");
    private final Component text14 = Component.translatable("text.item.teamod.text14");
    private final Component text15 = Component.translatable("text.item.teamod.text15");
    private final Component text16 = Component.translatable("text.item.teamod.text16");
    private final Component text17 = Component.translatable("text.item.teamod.text17");
    private final Component text18 = Component.translatable("text.item.teamod.text18");
    private final Component text19 = Component.translatable("text.item.teamod.text19");
    private final Component text20 = Component.translatable("text.item.teamod.text20");
    private final Component text21 = Component.translatable("text.item.teamod.text21");
    private final Component text22 = Component.translatable("text.item.teamod.text22");
    private final Component text23 = Component.translatable("text.item.teamod.text23");
    private final Component text24 = Component.translatable("text.item.teamod.text24");


    private static final ResourceLocation PAGE1 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page1.png");
    private static final ResourceLocation PAGE2 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page2.png");
    private static final ResourceLocation PAGE3 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page3.png");
    private static final ResourceLocation PAGE4 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page4.png");
    private static final ResourceLocation PAGE5 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page5.png");
    private static final ResourceLocation PAGE6 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page6.png");
    private static final ResourceLocation PAGE7 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page7.png");
    private static final ResourceLocation PAGE8 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page8.png");
    private static final ResourceLocation PAGE9 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page9.png");
    private static final ResourceLocation PAGE10 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page10.png");
    private static final ResourceLocation PAGE11 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page11.png");
    private static final ResourceLocation PAGE12 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page12.png");
    private static final ResourceLocation PAGE13 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page13.png");
    private static final ResourceLocation PAGE14 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page14.png");
    private static final ResourceLocation PAGE15 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page15.png");
    private static final ResourceLocation PAGE16 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page16.png");
    private static final ResourceLocation PAGE17 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page17.png");
    private static final ResourceLocation PAGE18 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page18.png");
    private static final ResourceLocation PAGE19 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page19.png");
    private static final ResourceLocation PAGE20 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page20.png");
    private static final ResourceLocation PAGE21 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page21.png");
    private static final ResourceLocation PAGE22 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page22.png");
    private static final ResourceLocation PAGE23 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page23.png");
    private static final ResourceLocation PAGE24 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page24.png");
    private static final ResourceLocation PAGE25 =
            ResourceLocation.fromNamespaceAndPath(TeaMod.MOD_ID,"textures/gui/tea_handbook/tea_handbook_page25.png");

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
        this.currentText = this.currentPage;
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
        this.backButton.visible = this.currentPage > 0 && this.currentText == 0;
        this.forwardButton.visible = this.currentPage < 24 && this.currentText == 0;
        this.readButton.visible = this.currentText == 0 && this.currentPage > 0;
        this.hideTextButton.visible = this.currentText > 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.currentText == 0) {
            switch (this.currentPage) {
                case 0 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE1, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 1 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE2, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 2 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE3, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 3 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE4, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 4 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE5, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 5 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE6, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 6 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE7, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 7 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE8, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 8 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE9, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 9 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE10, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 10 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE11, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 11 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE12, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 12 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE13, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 13 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE14, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 14 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE15, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 15 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE16, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 16 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE17, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 17 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE18, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 18 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE19, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 19 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE20, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 20 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE21, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 21 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE22, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 22 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE23, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 23 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE24, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);
                case 24 ->
                        guiGraphics.blit(RenderType.GUI_TEXTURED, PAGE25, this.width / 2 - 72, this.height / 2 - 89, 0, 0, 144, 178, 144, 178);




            }
        } else {
            int y = this.height / 2 - 40;
            switch (this.currentText) {
                case 1 -> {
                    for (FormattedCharSequence line : this.font.split(text1, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 2 -> {
                    for (FormattedCharSequence line : this.font.split(text2, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 3 -> {
                    for (FormattedCharSequence line : this.font.split(text3, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 4 -> {
                    for (FormattedCharSequence line : this.font.split(text4, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 5 -> {
                    for (FormattedCharSequence line : this.font.split(text5, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 6 -> {
                    for (FormattedCharSequence line : this.font.split(text6, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 7 -> {
                    for (FormattedCharSequence line : this.font.split(text7, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 8 -> {
                    for (FormattedCharSequence line : this.font.split(text8, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 9 -> {
                    for (FormattedCharSequence line : this.font.split(text9, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 10 -> {
                    for (FormattedCharSequence line : this.font.split(text10, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 11 -> {
                    for (FormattedCharSequence line : this.font.split(text11, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 12 -> {
                    for (FormattedCharSequence line : this.font.split(text12, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 13 -> {
                    for (FormattedCharSequence line : this.font.split(text13, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 14 -> {
                    for (FormattedCharSequence line : this.font.split(text14, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 15 -> {
                    for (FormattedCharSequence line : this.font.split(text15, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 16 -> {
                    for (FormattedCharSequence line : this.font.split(text16, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 17 -> {
                    for (FormattedCharSequence line : this.font.split(text17, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 18 -> {
                    for (FormattedCharSequence line : this.font.split(text18, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 19 -> {
                    for (FormattedCharSequence line : this.font.split(text19, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 20 -> {
                    for (FormattedCharSequence line : this.font.split(text20, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 21 -> {
                    for (FormattedCharSequence line : this.font.split(text21, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 22 -> {
                    for (FormattedCharSequence line : this.font.split(text22, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 23 -> {
                    for (FormattedCharSequence line : this.font.split(text23, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
                case 24 -> {
                    for (FormattedCharSequence line : this.font.split(text24, this.width / 2)) {
                        guiGraphics.drawCenteredString(this.font, line, this.width / 2, y, 0xFFFFFF);
                        y += this.font.lineHeight + 2;
                    }
                }
            }
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // чтобы не ставило игру на паузу
    }
}
