package com.devlin_n.dcplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devlin_n.videoplayer.player.BackgroundPlayService;
import com.devlin_n.videoplayer.player.VideoCacheManager;
import com.devlin_n.dcplayer.activity.DanmakuActivity;
import com.devlin_n.dcplayer.activity.FullScreenActivity;
import com.devlin_n.dcplayer.activity.ListViewActivity;
import com.devlin_n.dcplayer.activity.LivePlayerActivity;
import com.devlin_n.dcplayer.activity.PlayerActivity;
import com.devlin_n.dcplayer.activity.RecyclerViewActivity;
import com.devlin_n.dcplayer.activity.VodPlayerActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private boolean isLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.et);

        ((RadioGroup) findViewById(R.id.rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.vod:
                        isLive = false;
                        break;
                    case R.id.live:
                        isLive = true;
                        break;
                }
            }
        });
        //用于将视频缓存到SDCard，如不授权将缓存到/data/data目录
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.close_float_window:
                Intent intent = new Intent(this, BackgroundPlayService.class);
                getApplicationContext().stopService(intent);
                break;
            case R.id.clear_cache:
                if (VideoCacheManager.clearAllCache(this)) {
                    Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void skipToVodPlayer(View view) {
        startActivity(new Intent(this, VodPlayerActivity.class));
    }

    public void skipToLivePlayer(View view) {
        startActivity(new Intent(this, LivePlayerActivity.class));
    }

    public void startFullScreen(View view) {
        startActivity(new Intent(this, FullScreenActivity.class));
    }

    public void recycler(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    public void playOther(View view) {
        String url = editText.getText().toString();
        if (TextUtils.isEmpty(url)) return;
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isLive", isLive);
        startActivity(intent);
    }

    public void clearUrl(View view) {
        editText.setText("");
    }

    public void danmaku(View view) {
        startActivity(new Intent(this, DanmakuActivity.class));
    }

    public void list(View view) {
        startActivity(new Intent(this, ListViewActivity.class));
    }
}
