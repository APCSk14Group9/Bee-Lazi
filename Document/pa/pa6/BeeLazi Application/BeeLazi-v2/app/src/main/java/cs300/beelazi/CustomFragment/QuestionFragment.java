package cs300.beelazi.CustomFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cs300.beelazi.R;

public class QuestionFragment extends Fragment {

    Context mContext;
    ImageView ivFirstPic, ivSecondPic;
    Button yesButton, noButton;

    public QuestionFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        mContext = rootView.getContext();
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View rootView) {
        ivFirstPic = (ImageView) rootView.findViewById(R.id.firstPic);
        ivSecondPic = (ImageView) rootView.findViewById(R.id.secondPic);
        yesButton = (Button) rootView.findViewById(R.id.yesButton);
        noButton = (Button) rootView.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Picasso.with(mContext).load("https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&gadget=a&no_expand=1&refresh=604800&url=http://animehay.com/uploads/images/2017/01/amaenaide-yo-katsu-thumbnail.jpg").fit().into(ivFirstPic);
        Picasso.with(mContext).load("https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&gadget=a&no_expand=1&refresh=604800&url=http://animehay.com/uploads/images/2017/01/amaenaide-yo-thumbnail.jpg").fit().into(ivSecondPic);

    }



}
