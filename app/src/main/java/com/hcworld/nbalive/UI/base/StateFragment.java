package com.hcworld.nbalive.UI.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcworld.nbalive.Interface.mainInterface;

import butterknife.ButterKnife;

/**
 * Created by hcw on 2018/12/21.
 * Copyright©hcw.All rights reserved.
 */


public abstract class StateFragment extends Fragment implements mainInterface{
    private static final String TAG = "HomeActivityDemo";

    private Bundle saveState;
    public View parentView;

    protected LayoutInflater inflater;
    public Activity mActivity;

    private String tag = StateFragment.class.getSimpleName();

    public StateFragment(){
        super();
        if(getArguments() == null){
            setArguments(new Bundle());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        if(!restoreStateFromArguments()){
            onFirstTimeLaunched();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        parentView = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, parentView);
        this.inflater = inflater;


        initValue();
        initView();
        initEvent();
        return parentView;
    }



    @Override
    public void initView() {

    }

    @Override
    public void initValue() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // saveStateToArguments();
    }

    /**
     * 第一次加载该类
     * */
    public void onFirstTimeLaunched(){

    }

    private void saveStateToArguments(){
        if(getView() != null){
            saveState = saveState();
        }
        if(saveState != null){
            Bundle b = getArguments();
            if(b!=null){
                b.putBundle("internalSavedViewState8954201239547", saveState);
            }
        }
    }

    private boolean restoreStateFromArguments(){
        Bundle b = getArguments();
        if(b == null) {return false;}
        saveState = b.getBundle("internalSavedViewState8954201239547");
        if(saveState != null){
            restoreState();
            return true;
        }
        return false;
    }

    /**
     * 恢复状态
     * */
    private void restoreState(){
        if(saveState != null){
            onRestoreState(saveState);
        }
    }

    /**
     * 子类重写该方法恢复状态
     * */
    public void onRestoreState(Bundle saveInstanceState){

    }

    /**
     * 保存状态
     * */
    private Bundle saveState(){
        Bundle state = new Bundle();
        //保存数据
        onSaveState(state);
        return state;
    }

    /**
     * 子类重写该方法实现状态保存
     * */
    public void onSaveState(Bundle outState){

    }


    public abstract int getLayoutResID();


}