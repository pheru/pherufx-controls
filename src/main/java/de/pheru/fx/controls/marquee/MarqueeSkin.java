package de.pheru.fx.controls.marquee;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MarqueeSkin extends SkinBase<Marquee> {

    private static final int ANIMATION_DURATION_ADJUSTMENT = 50;
    private Text textNode = new Text();
    private Rectangle clip = new Rectangle();
    private TranslateTransition finiteTransition;
    private TranslateTransition infiniteTransition;
    private Text infiniteScrollTextNode;
    private Rectangle infiniteScrollClip = new Rectangle();

    protected MarqueeSkin(final Marquee control) {
        super(control);
        init();
    }

    private void init() {
        initTextNode();
        initClip();
        if (getSkinnable().isInfiniteScroll()) {
            initInfiniteScroll();
        } else {
            initFiniteScroll();
        }
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

    private void initInfiniteScroll() {
        initInfiniteTransition();
        initInfiniteScrollTextNode();
        initInfiniteScrollClip();

        disposeFiniteTransition();
    }


    private void initInfiniteTransition() {
        infiniteTransition = new TranslateTransition();
        infiniteTransition.setCycleCount(Animation.INDEFINITE);
        infiniteTransition.delayProperty().bind(getSkinnable().animationStartDelayProperty());
        infiniteTransition.setInterpolator(Interpolator.LINEAR);
        infiniteTransition.setNode(textNode);
    }

    private void initInfiniteScrollTextNode() {
        infiniteScrollTextNode = new Text();
        infiniteScrollTextNode.setTextOrigin(VPos.TOP);
        infiniteScrollTextNode.textProperty().bind(
                getSkinnable().infiniteScrollSeparatorProperty()
                        .concat(getSkinnable().textProperty()));
        infiniteScrollTextNode.translateXProperty().bind(textNode.translateXProperty());
        getChildren().add(infiniteScrollTextNode);
    }

    private void initInfiniteScrollClip() {
        infiniteScrollClip = new Rectangle();
        updateinfiniteScrollClipTranslateBinding();
        infiniteScrollClip.setWidth(textNode.getLayoutBounds().getWidth());
        infiniteScrollClip.setHeight(textNode.getLayoutBounds().getHeight());
        textNode.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            infiniteScrollClip.setWidth(textNode.getLayoutBounds().getWidth());
            infiniteScrollClip.setHeight(textNode.getLayoutBounds().getHeight());
            updateinfiniteScrollClipTranslateBinding();
        });
        updateinfiniteScrollClipTranslateBinding();
        infiniteScrollTextNode.setClip(infiniteScrollClip);
    }

    private void updateinfiniteScrollClipTranslateBinding() {
        infiniteScrollClip.translateXProperty().bind(
                textNode.translateXProperty().negate()
                        .subtract(textNode.getLayoutBounds().getWidth()));
    }

    private void initFiniteScroll() {
        initFiniteTransition();

        disposeInfiniteTransition();
        getChildren().remove(infiniteScrollTextNode);
        infiniteScrollTextNode = null;
        infiniteScrollClip = null;
    }

    private void initFiniteTransition() {
        finiteTransition = new TranslateTransition();
        finiteTransition.setOnFinished(event -> {
            final Thread t = new Thread(() -> {
                try {
                    final long endDelay = (long) getSkinnable().getAnimationEndDelay().toMillis();
                    Thread.sleep(endDelay);
                } catch (final InterruptedException e) {
                    // nothing to do
                }
                textNode.setTranslateX(0);
                finiteTransition.playFromStart();
            });
            t.setDaemon(true);
            t.start();
        });
        finiteTransition.delayProperty().bind(getSkinnable().animationStartDelayProperty());
        finiteTransition.setInterpolator(Interpolator.LINEAR);
        finiteTransition.setNode(textNode);
    }

    private void initChangeListener() {
        final ChangeListener<Object> textOrWidthChangeListener = createTextOrWidthChangeListener();
        getSkinnable().textProperty().addListener(textOrWidthChangeListener);
        getSkinnable().widthProperty().addListener(textOrWidthChangeListener);
        getSkinnable().infiniteScrollProperty().addListener(createInfiniteScrollChangeListener());
    }

    private ChangeListener<Object> createTextOrWidthChangeListener() {
        return (observable, oldValue, newValue) -> {
            textNode.setTranslateX(0);
            getCurrentTransition().stop();
            if (textNode.getLayoutBounds().getWidth() > getSkinnable().getWidth()) {
                computeAndPlayTransition();
            }
        };
    }

    private ChangeListener<Boolean> createInfiniteScrollChangeListener() {
        return (observable, oldValue, newValue) -> {
            if (newValue) {
                initInfiniteScroll();
            } else {
                initFiniteScroll();
            }
            getCurrentTransition().play();
        };
    }

    private TranslateTransition getCurrentTransition() {
        return getSkinnable().isInfiniteScroll() ? infiniteTransition : finiteTransition;
    }

    private void computeAndPlayTransition() {
        final double animationWidth = getSkinnable().isInfiniteScroll() ?
                infiniteScrollTextNode.getLayoutBounds().getWidth()
                : textNode.getLayoutBounds().getWidth() - getSkinnable().getWidth();
        final double animationDuration = animationWidth * ANIMATION_DURATION_ADJUSTMENT / getSkinnable().getAnimationSpeed();
        playTransition(animationWidth, animationDuration);
    }

    private void playTransition(final double animationWidth, final double animationDuration) {
        final TranslateTransition transition = getCurrentTransition();
        transition.setByX(-animationWidth);
        transition.setDuration(Duration.millis(animationDuration));
        transition.play();
    }

    @Override
    protected double computeMinWidth(final double height, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return 0.0;
    }

    @Override
    protected double computePrefWidth(final double height, final double topInset, final double rightInset, final double bottomInset, final double leftInset) {
        return textNode.getLayoutBounds().getWidth();
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
        if (infiniteScrollTextNode != null) {
            layoutInArea(infiniteScrollTextNode, contentX + textNode.getLayoutBounds().getWidth(),
                    contentY, contentWidth, contentHeight, -1, HPos.LEFT, VPos.TOP);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        textNode = null;
        clip = null;
        infiniteScrollTextNode = null;
        infiniteScrollClip = null;
        disposeFiniteTransition();
        disposeInfiniteTransition();
    }

    private void disposeFiniteTransition() {
        if (finiteTransition != null) {
            finiteTransition.stop();
            finiteTransition = null;
        }
    }

    private void disposeInfiniteTransition() {
        if (infiniteTransition != null) {
            infiniteTransition.stop();
            infiniteTransition = null;
        }
    }
}
