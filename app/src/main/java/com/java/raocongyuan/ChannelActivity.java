package com.java.raocongyuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.channel.Channel;
import com.cheng.channel.ChannelView;
import com.cheng.channel.ViewHolder;
import com.cheng.channel.adapter.BaseStyleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ChannelActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private ChannelView channelView;
    private LinkedHashMap<String, List<Channel>> data = new LinkedHashMap<>();
    private ImageView button_close;
    private ImageView button_reset;

    private final List<String> proto_channels = Arrays.asList("News", "Paper", "Events");
    private final List<String> proto_other_channels = Arrays.asList("境内疫情", "境外疫情", "政府行动", "疫情", "行业战疫");

    List<String> userChannelList = new ArrayList<>();
    List<String> otherChannelList = new ArrayList<>();

    List<Channel> ch_userChannelList = new ArrayList<>();
    List<Channel> ch_otherChannelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        Intent intent = getIntent();
        channelView = (ChannelView)findViewById(R.id.channel_view_2);
        //close-buttons
        button_close = (ImageView)findViewById(R.id.channel_category_close2);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //reset-button
        button_reset = (ImageView) findViewById(R.id.channel_category_reset2);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("确定重置为初始状态吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userChannelList.clear();
                        ch_userChannelList.clear();
                        userChannelList.addAll(proto_channels);
                        otherChannelList.clear();
                        ch_otherChannelList.clear();
                        otherChannelList.addAll(proto_other_channels);

                        int i = 0;
                        for (String the_channel : userChannelList) {
                            Channel channel = new Channel(the_channel, 2, (Object) i);
                            i++;
                            ch_userChannelList.add(channel);
                        }

                        for (String the_channel : otherChannelList) {
                            Channel channel = new Channel(the_channel);
                            ch_otherChannelList.add(channel);
                        }

                        saveChannel(false);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        setResult(NewsListFragment.CHANNELRESULT, intent);
                        finish();
                    }
                });
            }
        });
        init();
    }

    private void init() {

        //TODO::get userChannelList from history
        userChannelList = null;
        otherChannelList = null;
        if(userChannelList == null || userChannelList.size() == 0) {
            userChannelList = TopMenuChoice.getChoice();
            otherChannelList = TopMenuChoiceOthers.getChoice();
        }

        //add data to the channel list
        int i = 0;
        for (String the_channel : userChannelList) {
            Channel channel = new Channel(the_channel, 2, (Object) i);
            i++;
            ch_userChannelList.add(channel);
        }
        for (String the_channel : otherChannelList) {
            Channel channel = new Channel(the_channel);
            ch_otherChannelList.add(channel);
        }


        //将频道列表添加到界面
        channelView.setChannelFixedCount(1);
        //data.put("selected channels", ch_userChannelList);
        //data.put("other channels", ch_otherChannelList);
        channelView.addPlate("selected channels", ch_userChannelList);
        channelView.addPlate("other channels", ch_otherChannelList);

        channelView.inflateData();
        channelView.setOnChannelItemClickListener(new ChannelView.OnChannelListener() {
            @Override
            public void channelItemClick(int position, Channel channel) {

            }

            @Override
            public void channelEditFinish(List<Channel> channelList) {

            }

            @Override
            public void channelEditStart() {

            }
        });

    }

    private void saveChannel(boolean flag) {
        ArrayList<String> save_user_choices = new ArrayList<>();
        ArrayList<String> save_other_choices = new ArrayList<>();
        if(flag) {
            ch_userChannelList = channelView.getMyChannel();
            ch_otherChannelList = channelView.getOtherChannel().get(0);
        }
        for(Channel channel : ch_userChannelList) {
            save_user_choices.add(channel.getChannelName());
            Log.d("test", "channel name:" + channel.getChannelName());
        }
        int cnt = 0;
        for(Channel channel : ch_otherChannelList) {
            save_other_choices.add(channel.getChannelName());
            cnt++;
            if(cnt >= 16) {
                break;
            }
        }

        TopMenuChoice.setChoice(save_user_choices);
        TopMenuChoiceOthers.setChoice(save_other_choices);
        Log.d("test length:", String.valueOf(TopMenuChoice.getChoice().size())+String.valueOf(TopMenuChoiceOthers.getChoice().size()));

        //TODO::backend::save the channel choices
        //NewsManager.updateTopMenu(username, save_user_choices, save_other_choices);
    }


    @Override
    public void onBackPressed() {
        saveChannel(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        setResult(NewsListFragment.CHANNELRESULT, intent);
        finish();
    }

    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChannelActivity.this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("Save", onClickListener);
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

}
