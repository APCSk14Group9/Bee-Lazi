package cs300.beelazi.CardUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.squareup.picasso.Picasso;

import cs300.beelazi.R;

@SuppressLint("NewApi")
public class CardItemView extends FrameLayout {
    private Spring springX, springY;
    public ImageView imageView;
    public View maskView;
    private TextView userNameTv;
    private CardSlidePanel parentView;
    private View topLayout, bottomLayout;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_item, this);
        imageView = (ImageView) findViewById(R.id.card_image_view);
        maskView = findViewById(R.id.maskView);
        userNameTv = (TextView) findViewById(R.id.card_user_name);

        topLayout = findViewById(R.id.card_top_layout);
        bottomLayout = findViewById(R.id.card_bottom_layout);
        initSpring();
    }

    private void initSpring() {
        SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(15, 20);
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring().setSpringConfig(springConfig);
        springY = mSpringSystem.createSpring().setSpringConfig(springConfig);

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });
    }

    public void fillData(CardDataItem itemData) {
//        ImageLoader.getInstance().displayImage(itemData.imagePath, imageView);
        Picasso.with(getContext()).load(itemData.imagePath).fit().into(imageView);
        userNameTv.setText(itemData.userName);

    }


    public void animTo(int xPos, int yPos) {
        setCurrentSpringPos(getLeft(), getTop());
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }


    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    public void setParentView(CardSlidePanel parentView) {
        this.parentView = parentView;
    }

    public void onStartDragging() {
        springX.setAtRest();
        springY.setAtRest();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            boolean shouldCapture = shouldCapture((int) ev.getX(), (int) ev.getY());
            if (shouldCapture) {
                parentView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean shouldCapture(int x, int y) {
        int captureLeft = topLayout.getLeft() + topLayout.getPaddingLeft();
        int captureTop = topLayout.getTop() + topLayout.getPaddingTop();
        int captureRight = bottomLayout.getRight() - bottomLayout.getPaddingRight();
        int captureBottom = bottomLayout.getBottom() - bottomLayout.getPaddingBottom();

        if (x > captureLeft && x < captureRight && y > captureTop && y < captureBottom) {
            return true;
        }
        return false;
    }
}
