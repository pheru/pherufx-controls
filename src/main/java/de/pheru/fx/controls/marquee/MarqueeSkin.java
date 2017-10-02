package de.pheru.fx.controls.marquee;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MarqueeSkin extends SkinBase<Marquee> {

    private Text textNode = new Text();
    private TranslateTransition transition = new TranslateTransition();
    private Rectangle clip = new Rectangle();

    protected MarqueeSkin(final Marquee control) {
        super(control);
        init();
    }

    private void init() {
        initTextNode();
        initClip();
        initTransition();
        initChangeListener();

        getChildren().addAll(textNode);
    }

    private void initTextNode() {
        textNode.setTextOrigin(VPos.TOP);
        textNode.textProperty().bind(getSkinnable().textProperty());
    }

    private void initClip() {
        clip.widthProperty().bind(getSkinnable().widthProperty());
        clip.heightProperty().bind(getSkinnable().heightProperty());
        clip.translateXProperty().bind(textNode.translateXProperty().negate());
        textNode.setClip(clip);
    }

    private void initTransition() {
        transition.setOnFinished(event -> {
            final Thread t = new Thread(() -> {
                try {
                    final long endDelay = (long) getSkinnable().getAnimationEndDelay().toMillis();
                    Thread.sleep(endDelay);
                } catch (final InterruptedException e) {
                    // nothing to do
                }
                textNode.setTranslateX(0);
                transition.playFromStart();
            });
            t.setDaemon(true);
            t.start();
        });
        transition.delayProperty().bind(getSkinnable().animationStartDelayProperty());
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setNode(textNode);
    }

    private void initChangeListener() {
        final ChangeListener<Object> changeListener = createChangeListener();
        getSkinnable().textProperty().addListener(changeListener);
        getSkinnable().widthProperty().addListener(changeListener);
    }

    private ChangeListener<Object> createChangeListener() {
        return (observable, oldValue, newValue) -> {
            textNode.setTranslateX(0);
            final double textWidth = textNode.getLayoutBounds().getWidth();
            if (textWidth < getSkinnable().getWidth()) {
                transition.stop();
            } else {
                final double animationWidth = textWidth - getSkinnable().getWidth();
                final double animationDuration = animationWidth * 125 / getSkinnable().getAnimationSpeed();
                transition.stop();
                transition.setByX(-animationWidth);
                transition.setDuration(Duration.millis(animationDuration));
                transition.play();
            }
        };
    }

    @Override
    protected double computePrefWidth(final double height, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return textNode.getLayoutBounds().getWidth();
    }

    @Override
    protected double computeMinWidth(final double height, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return 0.0;
    }

    @Override
    protected double computeMaxWidth(final double height, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return getSkinnable().prefWidth(height);
    }

    @Override
    protected double computePrefHeight(final double width, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset); //TODO
    }

    @Override
    protected double computeMinHeight(final double width, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return textNode.getLayoutBounds().getHeight();
    }

    @Override
    protected double computeMaxHeight(final double width, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return getSkinnable().prefHeight(width);
    }

    @Override
    protected void layoutChildren(final double contentX, final double contentY, final double contentWidth, final double contentHeight) {
        // nothing (keep text-layout at 0)
    }

    @Override
    public void dispose() {
        super.dispose();
        textNode = null;
        transition.stop();
        transition = null;
        clip = null;
    }
}
