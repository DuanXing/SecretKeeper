package duanxing.swpu.com.secretkeeper.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import duanxing.swpu.com.secretkeeper.Adapter.SecretKeeperIconAdapter;
import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.MyIcon;

public class MainActivity extends BaseActivity {

    private GridView grid_secret_keeper;
    private List<MyIcon> icons = new ArrayList<>();
    private SecretKeeperIconAdapter secretKeeperIconAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initSubView() {
        grid_secret_keeper = (GridView) findViewById(R.id.grid_secret_keeper);
        initAdapter();
    }

    @Override
    protected void setListener() {
        grid_secret_keeper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positon, long id) {
                switch (icons.get(positon).getiId()) {
                    case R.mipmap.iv_icon_1:
                        enterActivity(FileEncryptionActivity.class);
                        break;
                    case R.mipmap.iv_icon_2:
                        enterActivity(FileDecryptionActivity.class);
                        break;
                    case R.mipmap.iv_icon_3:
                        break;
                    case R.mipmap.iv_icon_4:
                        break;
                    case R.mipmap.iv_icon_5:
                        break;
                    case R.mipmap.iv_icon_6:
                        break;

                }
            }
        });


    }

    private void enterActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void processLogic() {

    }

    private void initAdapter() {
        secretKeeperIconAdapter = new SecretKeeperIconAdapter(MainActivity.this);
        icons.add(new MyIcon(R.mipmap.iv_icon_1, "Encrypt"));
        icons.add(new MyIcon(R.mipmap.iv_icon_2, "Decrypt"));
        icons.add(new MyIcon(R.mipmap.iv_icon_3, "Note"));
        icons.add(new MyIcon(R.mipmap.iv_icon_4, "Icon4"));
        icons.add(new MyIcon(R.mipmap.iv_icon_5, "Icon5"));
        icons.add(new MyIcon(R.mipmap.iv_icon_6, "Icon6"));
        secretKeeperIconAdapter.setIcons(icons);
        grid_secret_keeper.setAdapter(secretKeeperIconAdapter);
    }
}
