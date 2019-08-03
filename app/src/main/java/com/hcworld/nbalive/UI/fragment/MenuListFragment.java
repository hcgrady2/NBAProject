
package com.hcworld.nbalive.UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.HomeActivity;
import com.hcworld.nbalive.UI.activity.LoginActivity;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.event.LoginEvent;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;
import com.hcworld.nbalive.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * MenuListFragment
 * 左边的菜单栏
 */

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;
    private     TextView menu_header_name;
    TextView view_test_txt;
    private SharedPreferencesUtils helper;
    private boolean isLogin;
    private String UserName= "";

  //  private  boolean isUseWelfare = false;


    public MenuListFragment() {
    }

/*    public static Fragment newInstance(String arg){
        MenuListFragment fragment = new MenuListFragment();
        Bundle bundle = new Bundle();
        bundle.putString( ARG, arg);
        fragment.setArguments(bundle);
        return fragment;
    }
    */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
        isLogin = helper.getBoolean("IsLogin",false);

        //注册 EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


 /*
        <group android:id="@+id/menu_group_other">
        <item
    android:id="@+id/menu_game"
    android:icon="@drawable/ic_game"
    android:title="@string/menu_game" />

        <item
    android:id="@+id/menu_tv"
    android:icon="@drawable/ic_tv"
    android:title="@string/menu_tv" />

    </group>
*/


    //Toast
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "welfare_preference");
        boolean isUseWelfare = helper.getBoolean("welfare",false);



        //找不到控件，要么回退版本，要么先找父控件
        //navigationView 为左侧的导航fragment，通过xml 引用 menu
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);


        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                setContentLayout(menuItem.getItemId());
                return false;
            }
        }) ;
        View headerView = vNavigation.getHeaderView(0);


        Log.i("WelfareDemo", "onCreateView,测试是否有 welfare" + isUseWelfare);

        vNavigation.getMenu().findItem(R.id.menu_welfare).setVisible(isUseWelfare);


        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto_img);
         menu_header_name =(TextView) headerView.findViewById(R.id.menu_header_name);

        //圆形，最好也加一个背景，试一下，todo，glide 配置
        if (ivMenuUserProfilePhoto != null){
   /*         RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                    .skipMemoryCache(true);//不做内存缓存
            */

            String avatar = "https://img5.duitang.com/uploads/item/201410/05/20141005082835_2RTzn.thumb.700_0.jpeg";

            RequestOptions requestOptions =  RequestOptions.circleCropTransform()
                    .override(R.dimen.global_menu_avatar_size,R.dimen.global_menu_avatar_size)
                    .placeholder(R.drawable.nba_default)
                    .error(R.drawable.nba_default)
                    .fallback(R.drawable.nba_default);

            RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                  //  .skipMemoryCache(true)
                    .placeholder(R.drawable.nba_default)
                    .error(R.drawable.nba_default)
                    .fallback(R.drawable.nba_default);

            Glide.with(getContext()).load(R.drawable.ic_launcher).apply(mRequestOptions).into(ivMenuUserProfilePhoto);

            if (isLogin){
                if (UserName != "" || !UserName.equals("")){
                     menu_header_name.setText(UserName.toString());
                }else {
                    UserName = helper.getString("UserName");
                    menu_header_name.setText(UserName.toString());
                }

            }else {
                menu_header_name.setText(R.string.login_hint);
            }


            ivMenuUserProfilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLogin){
                        ToastUtils.showShort(getContext().getApplicationContext(),R.string.had_logined);
                    }else {
                        startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
                    }
                }
            });

        }

        return  view ;
    }




    //这里设置fragment 切换，相当于 fragment 与 fragment 的交互
    private void setContentLayout(int id){
        HomeActivity activity;
        activity = (HomeActivity) getActivity();
        activity.setContentLayout(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    // 处理登录信息的 Event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent loginEvent){
        //   SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
        //   String phoneNum = helper.getString("PhoneNum");
        if (loginEvent.type == 0){
            isLogin = true;
            if (UserName != "" || !UserName.equals("")){
                menu_header_name.setText(UserName.toString());
            }else {
                UserName = helper.getString("UserName");
                menu_header_name.setText(UserName.toString());
            }

        }else if (loginEvent.type == 1){
            menu_header_name.setText(R.string.login_hint);
            isLogin = false;
        }
    }

}
