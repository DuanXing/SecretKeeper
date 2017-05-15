package duanxing.swpu.com.secretkeeper.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import duanxing.swpu.com.secretkeeper.Adapter.NoteItemAdapter;
import duanxing.swpu.com.secretkeeper.Adapter.NoteItemAdapter.ViewHolder;
import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.NoteItem;

public class NoteListActivity extends BaseActivity {
    private Button btn_add;
    private Button btn_delete;
    private ListView lst_notes;

    private NoteItemAdapter noteItemAdapter = null;
    private List<NoteItem> noteItems = new ArrayList<>();

    private HashMap<Integer, Boolean> checkedIdsMap = new HashMap<>();

    private static final String TAG = "NoteListActivity";

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_note_list);
    }

    @Override
    protected void initSubView() {
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_add = (Button) findViewById(R.id.btn_add);
        lst_notes = (ListView) findViewById(R.id.list);

        initAdapter();
    }

    @Override
    protected void setListener() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterActivity(NoteEditActivity.class);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = checkedIdsMap.size();
                if(0 == size) {
                    return;
                }

                for (Object o : checkedIdsMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Integer i = (Integer) entry.getKey();
                    Boolean b = (Boolean) entry.getValue();
                    if (b) {
                        // TODO delete database value
                        Log.i(TAG, "delete " + i);
                    }
                }

                refreshListview();
            }
        });

        lst_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(NoteItemAdapter.showCheckbox) {
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    viewHolder.checkBox.toggle();
                    checkedIdsMap.put(viewHolder.id, viewHolder.checkBox.isChecked());
                }
                else {
                    // TODO enter NoteEditActivity
                }
            }
        });

        lst_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!NoteItemAdapter.showCheckbox) {
                    NoteItemAdapter.showCheckbox = true;
                    noteItemAdapter.notifyDataSetChanged();
                    
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    viewHolder.checkBox.toggle();
                    checkedIdsMap.put(viewHolder.id, viewHolder.checkBox.isChecked());

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void processLogic() {

    }

    private void enterActivity(Class<?> cls) {
        Intent intent = new Intent(NoteListActivity.this, cls);
        startActivity(intent);
    }

    private void initAdapter() {
        if(null == noteItemAdapter) {
            noteItemAdapter = new NoteItemAdapter(NoteListActivity.this);
        }

        // TODO get notes from database
        noteItems.add(new NoteItem(1, "标题", "文本"));
        noteItems.add(new NoteItem(2, "标题", "文本"));
        noteItems.add(new NoteItem(3, "标题", "文本"));

        noteItemAdapter.setItems(noteItems);
        lst_notes.setAdapter(noteItemAdapter);
    }

    private void refreshListview() {
        NoteItemAdapter.showCheckbox = false;
        noteItemAdapter.notifyDataSetChanged();
        checkedIdsMap.clear();
        noteItems.clear();

        // TODO get notes from database
        noteItems.add(new NoteItem(1, "标题", "文本"));
        noteItems.add(new NoteItem(2, "标题", "文本"));
        noteItems.add(new NoteItem(3, "标题", "文本"));

        noteItemAdapter.setItems(noteItems);
        lst_notes.setAdapter(noteItemAdapter);
    }

    @Override
    public void onBackPressed() {
        if(NoteItemAdapter.showCheckbox) {
            NoteItemAdapter.showCheckbox = false;
            noteItemAdapter.notifyDataSetChanged();
            checkedIdsMap.clear();
        }
        else {
            super.onBackPressed();
        }
    }
}
