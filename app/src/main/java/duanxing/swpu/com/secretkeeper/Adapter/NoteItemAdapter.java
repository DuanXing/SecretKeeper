package duanxing.swpu.com.secretkeeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.NoteItem;

/**
 * Created by duanxing on 14/05/2017.
 */

public class NoteItemAdapter extends BaseAdapter {
    private Context context;

    private List<NoteItem> items;

    public static boolean showCheckbox = false;

    public NoteItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<NoteItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note_list, parent, false);
            holder = new ViewHolder();
            holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            if(showCheckbox) {
                holder.checkBox.setVisibility(CheckBox.VISIBLE);
            }
            else {
                holder.checkBox.setVisibility(CheckBox.INVISIBLE);
                holder.checkBox.setChecked(false);
            }
        }
        holder.id = items.get(position).getId();
        holder.txt_title.setText(items.get(position).getTitle());
        holder.txt_content.setText(items.get(position).getContent());

        return convertView;
    }

    public final class ViewHolder {
        public int id;
        public TextView txt_title;
        public TextView txt_content;
        public CheckBox checkBox;
    }
}
