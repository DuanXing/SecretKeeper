package duanxing.swpu.com.secretkeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import duanxing.swpu.com.secretkeeper.Adapter.NoteItemAdapter;
import duanxing.swpu.com.secretkeeper.Adapter.NoteItemAdapter.ViewHolder;
import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.NoteItem;
import duanxing.swpu.com.secretkeeper.utils.DatabaseCipher;
import duanxing.swpu.com.secretkeeper.utils.DatabaseHelper;

public class NoteListActivity extends BaseActivity {
    private Button btn_add;
    private Button btn_delete;
    private ListView lst_notes;

    private NoteItemAdapter noteItemAdapter = null;
    private List<NoteItem> noteItems = new ArrayList<>();

    private HashMap<Integer, Boolean> checkedIdsMap = new HashMap<>();

    private static final String TAG = "NoteListActivity";
    public static int NOTE_EDIT_RESULT = 9;

    private String strTitleSelect;
    private String strContentSelect;
    private int iIdSelect;

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
                enterActivity(NoteEditActivity.class, true);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = checkedIdsMap.size();
                if(0 == size) {
                    return;
                }

                DatabaseHelper helper = new DatabaseHelper(NoteListActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                for (Object o : checkedIdsMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Integer i = (Integer) entry.getKey();
                    Boolean b = (Boolean) entry.getValue();
                    if (b) {
                        db.delete(DatabaseHelper.TB_NOTE, DatabaseHelper.NOTE_KEY_ID + "=?", new String[] {String.valueOf(i)});
                        Log.i(TAG, "delete " + i);
                    }
                }

                refreshListView();
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
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    iIdSelect = viewHolder.id;
                    strTitleSelect = viewHolder.txt_title.getText().toString();
                    strContentSelect = viewHolder.txt_content.getText().toString();
                    enterActivity(NoteEditActivity.class, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(resultCode == NOTE_EDIT_RESULT) {
            refreshListView();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void processLogic() {

    }

    private void enterActivity(Class<?> cls, boolean isAdd) {
        Intent intent = new Intent(NoteListActivity.this, cls);
        if(isAdd) {
            intent.putExtra("isNew", true);
        }
        else {
            intent.putExtra("isNew", false);
            intent.putExtra("title", strTitleSelect);
            intent.putExtra("content", strContentSelect);
            intent.putExtra("id", iIdSelect);
        }
        startActivityForResult(intent, 1);
    }

    private void initAdapter() {
        if(null == noteItemAdapter) {
            noteItemAdapter = new NoteItemAdapter(NoteListActivity.this);
        }

        getNotesFromDb();

        noteItemAdapter.setItems(noteItems);
        lst_notes.setAdapter(noteItemAdapter);
    }

    private void refreshListView() {
        NoteItemAdapter.showCheckbox = false;
        checkedIdsMap.clear();
        noteItems.clear();

        getNotesFromDb();

        noteItemAdapter.setItems(noteItems);
        noteItemAdapter.notifyDataSetChanged();
    }

    private void getNotesFromDb() {
        DatabaseCipher cipher = new DatabaseCipher(Cipher.DECRYPT_MODE);
        DatabaseHelper helper = new DatabaseHelper(NoteListActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TB_NOTE, new String[] {DatabaseHelper.NOTE_KEY_ID, DatabaseHelper.NOTE_KEY_TITLE, DatabaseHelper.NOTE_KEY_CONTENT},
                null, null, null, null, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int i = cursor.getInt(0);
                String strT = cursor.getString(1);
                String strC = cursor.getString(2);
                noteItems.add(new NoteItem(i, cipher.doFinal(strT), cipher.doFinal(strC)));
            }while(cursor.moveToNext());
        }

        db.close();
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
