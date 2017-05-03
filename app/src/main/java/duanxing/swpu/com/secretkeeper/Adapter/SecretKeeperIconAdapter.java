package duanxing.swpu.com.secretkeeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.MyIcon;

/**
 * Created by DZP on 2017/5/3.
 */

public class SecretKeeperIconAdapter extends BaseAdapter{

    private Context context;

    private List<MyIcon> icons;

    public SecretKeeperIconAdapter(Context context) {
        this.context = context;
    }

    public void setIcons(List<MyIcon> icons) {
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return icons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_icon, parent, false);
            holder = new ViewHolder();
            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.txt_icon = (TextView) convertView.findViewById(R.id.txt_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_icon.setImageResource(icons.get(position).getiId());
        holder.txt_icon.setText(icons.get(position).getiName());
        return convertView;
    }

    static class ViewHolder {
        ImageView img_icon;
        TextView txt_icon;
    }
}
