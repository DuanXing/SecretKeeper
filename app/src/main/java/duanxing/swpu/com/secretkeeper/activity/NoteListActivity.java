package duanxing.swpu.com.secretkeeper.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import duanxing.swpu.com.secretkeeper.Adapter.NoteItemAdapter;
import duanxing.swpu.com.secretkeeper.R;
import duanxing.swpu.com.secretkeeper.entity.NoteItem;

public class NoteListActivity extends BaseActivity {
    private Button btn_add;
    private Button btn_delete;
    private ListView lst_notes;

    private NoteItemAdapter noteItemAdapter;
    private List<NoteItem> noteItems = new ArrayList<>();

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

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void processLogic() {

    }

    private void initAdapter() {
        noteItemAdapter = new NoteItemAdapter(NoteListActivity.this);

        // TODO: 14/05/2017 get notes from database


        noteItemAdapter.setItems(noteItems);
        lst_notes.setAdapter(noteItemAdapter);
    }
}
