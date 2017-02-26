package cs300.beelazi.CustomFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

import cs300.beelazi.AsynchronousActivity;
import cs300.beelazi.LoginActivity;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.Model.UserInfomation;
import cs300.beelazi.PersonalEditActivity;
import cs300.beelazi.R;


public class PersonalFragment extends Fragment{

    ImageView editSchedule, signoutButton, editPersonalInfo;
    public static TextView tvName;
    Context mContext;
    private ImageView userCoverView;
    private ImageView userProfilePicture;

    public PersonalFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.personal_fragment, container, false);
        mContext = rootView.getContext();
        initLayouts(rootView);
        setClickEvents(rootView);
        return rootView;
    }

    private void setClickEvents(final View rootView) {
        editPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalEditActivity.class);
                intent.putExtra("realName", tvName.getText().toString());
                startActivity(intent);
            }
        });
        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), AsynchronousActivity.class);
                startActivity(intent);
            }
        });
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginWithFacebook()) {
                    LoginManager.getInstance().logOut();
                }
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private String decodeUnicode(String string){
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-16BE");
            string = new String(utf8, "UTF-16BE");
            Log.d("fuckfuck", string);
            return string;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    private void initLayouts(View rootView) {
        editPersonalInfo = (ImageView) rootView.findViewById(R.id.editProfile);
        userCoverView = (ImageView) rootView.findViewById(R.id.user_cover);
        editSchedule = (ImageView) rootView.findViewById(R.id.editSchedule);
        signoutButton = (ImageView) rootView.findViewById(R.id.signoutButton);
        tvName = (TextView) rootView.findViewById(R.id.name);
        userProfilePicture = (ImageView) rootView.findViewById(R.id.avatar);

        if (isLoginWithFacebook()){

            Picasso.with(mContext).load(StaticData.user.getImageProfile()).fit().into(userProfilePicture);
            tvName.setText(decodeUnicode(StaticData.user.getRealname()));
        }
        else{
            Picasso.with(mContext).load(StaticData.user.getImageProfile()).fit().into(userProfilePicture);
            UserInfomation user = StaticData.user;
            tvName.setText(decodeUnicode(user.getRealname()));
        }
        Picasso.with(rootView.getContext()).load(R.drawable.user_cover).into(userCoverView);

    }

    public boolean isLoginWithFacebook(){
        return AccessToken.getCurrentAccessToken() != null;
    }


}